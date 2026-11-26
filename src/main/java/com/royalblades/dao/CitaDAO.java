package com.royalblades.dao;

import com.royalblades.config.ConexionBD;
import com.royalblades.modelo.Barbero;
import com.royalblades.modelo.Cita;
import com.royalblades.modelo.Servicio;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CitaDAO {

    public List<Servicio> listarServiciosActivos() throws SQLException {
        List<Servicio> lista = new ArrayList<>();
        String sql = "SELECT id_servicio, nombre, precio, duracion_minutos FROM servicios WHERE activo = TRUE";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Servicio s = new Servicio();
                s.setIdServicio(rs.getInt("id_servicio"));
                s.setNombre(rs.getString("nombre"));
                s.setPrecio(rs.getDouble("precio"));
                s.setDuracionMinutos(rs.getInt("duracion_minutos"));
                lista.add(s);
            }
        }
        return lista;
    }

    public List<Barbero> listarBarberosEnTurno() throws SQLException {
        List<Barbero> lista = new ArrayList<>();
        String sql = "SELECT b.id_barbero, u.nombre_completo, b.porcentaje_comision " +
                     "FROM barberos b INNER JOIN usuarios u ON b.id_usuario = u.id_usuario " +
                     "WHERE u.activo = TRUE AND b.en_turno = TRUE";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Barbero b = new Barbero();
                b.setIdBarbero(rs.getInt("id_barbero"));
                b.setNombreCompleto(rs.getString("nombre_completo"));
                b.setPorcentajeComision(rs.getDouble("porcentaje_comision"));
                lista.add(b);
            }
        }
        return lista;
    }

    public int obtenerBarberoConMenosCitasHoy(Connection conn) throws SQLException {
        String sql = "SELECT b.id_barbero, COUNT(c.id_cita) AS total_citas " +
                     "FROM barberos b " +
                     "INNER JOIN usuarios u ON b.id_usuario = u.id_usuario " +
                     "LEFT JOIN citas c ON b.id_barbero = c.id_barbero " +
                     "  AND DATE(c.fecha_hora) = CURDATE() AND c.estado != 'CANCELADA' " +
                     "WHERE u.activo = TRUE AND b.en_turno = TRUE " +
                     "GROUP BY b.id_barbero " +
                     "ORDER BY total_citas ASC, b.id_barbero ASC LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("id_barbero");
            }
        }
        throw new SQLException("No hay barberos disponibles en turno hoy.");
    }

    public boolean registrarCita(String nombreCliente, String telefono, String correo,
                                 int idServicio, Integer idBarberoSolicitado, String fechaHora) throws SQLException {
        String sqlCliente = "INSERT INTO clientes (nombre, telefono, correo) VALUES (?, ?, ?)";
        String sqlPrecio = "SELECT precio FROM servicios WHERE id_servicio = ?";
        String sqlComision = "SELECT porcentaje_comision FROM barberos WHERE id_barbero = ?";
        String sqlCita = "INSERT INTO citas (id_cliente, id_barbero, id_servicio, fecha_hora, asignacion_manual, estado, monto_cobrado, comision_barbero) " +
                         "VALUES (?, ?, ?, ?, ?, 'PENDIENTE', ?, ?)";

        Connection conn = null;
        try {
            conn = ConexionBD.getConnection();
            conn.setAutoCommit(false);

            boolean manual = (idBarberoSolicitado != null && idBarberoSolicitado > 0);
            int idBarberoFinal = manual ? idBarberoSolicitado : obtenerBarberoConMenosCitasHoy(conn);

            // 1. Guardar cliente
            int idCliente = 0;
            try (PreparedStatement psCli = conn.prepareStatement(sqlCliente, Statement.RETURN_GENERATED_KEYS)) {
                psCli.setString(1, nombreCliente.trim());
                psCli.setString(2, telefono != null ? telefono.trim() : "");
                psCli.setString(3, correo.trim().toLowerCase());
                psCli.executeUpdate();
                try (ResultSet rs = psCli.getGeneratedKeys()) {
                    if (rs.next()) idCliente = rs.getInt(1);
                }
            }

            // 2. Obtener precio vigente del servicio
            double precio = 0.0;
            try (PreparedStatement psPre = conn.prepareStatement(sqlPrecio)) {
                psPre.setInt(1, idServicio);
                try (ResultSet rs = psPre.executeQuery()) {
                    if (rs.next()) precio = rs.getDouble("precio");
                }
            }

            // 3. Obtener % de comisión del barbero y calcular monto
            double pctComision = 50.0;
            try (PreparedStatement psCom = conn.prepareStatement(sqlComision)) {
                psCom.setInt(1, idBarberoFinal);
                try (ResultSet rs = psCom.executeQuery()) {
                    if (rs.next()) pctComision = rs.getDouble("porcentaje_comision");
                }
            }
            double montoComision = precio * (pctComision / 100.0);

            // 4. Registrar la cita
            try (PreparedStatement psCita = conn.prepareStatement(sqlCita)) {
                psCita.setInt(1, idCliente);
                psCita.setInt(2, idBarberoFinal);
                psCita.setInt(3, idServicio);
                psCita.setString(4, fechaHora.replace("T", " "));
                psCita.setBoolean(5, manual);
                psCita.setDouble(6, precio);
                psCita.setDouble(7, montoComision);
                psCita.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    public List<Cita> listarCitasDelDia() throws SQLException {
        List<Cita> lista = new ArrayList<>();
        String sql = "SELECT c.id_cita, cl.nombre AS cliente, cl.correo, u.nombre_completo AS barbero, " +
                     "s.nombre AS servicio, c.fecha_hora, c.asignacion_manual, c.estado, c.monto_cobrado, c.comision_barbero " +
                     "FROM citas c " +
                     "INNER JOIN clientes cl ON c.id_cliente = cl.id_cliente " +
                     "INNER JOIN barberos b ON c.id_barbero = b.id_barbero " +
                     "INNER JOIN usuarios u ON b.id_usuario = u.id_usuario " +
                     "INNER JOIN servicios s ON c.id_servicio = s.id_servicio " +
                     "ORDER BY c.fecha_hora DESC LIMIT 50";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Cita c = new Cita();
                c.setIdCita(rs.getInt("id_cita"));
                c.setNombreCliente(rs.getString("cliente"));
                c.setCorreoCliente(rs.getString("correo"));
                c.setNombreBarbero(rs.getString("barbero"));
                c.setNombreServicio(rs.getString("servicio"));
                c.setFechaHora(rs.getString("fecha_hora"));
                c.setAsignacionManual(rs.getBoolean("asignacion_manual"));
                c.setEstado(rs.getString("estado"));
                c.setMontoCobrado(rs.getDouble("monto_cobrado"));
                c.setComisionBarbero(rs.getDouble("comision_barbero"));
                lista.add(c);
            }
        }
        return lista;
    }
}
