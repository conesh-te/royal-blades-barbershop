package com.royalblades.dao;

import com.royalblades.config.ConexionBD;
import com.royalblades.modelo.ResumenComision;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReporteDAO {

    public void actualizarEstadoCita(int idCita, String nuevoEstado) throws SQLException {
        if (!"COMPLETADA".equals(nuevoEstado) && !"CANCELADA".equals(nuevoEstado)) {
            throw new IllegalArgumentException("Estado no válido");
        }
        String sql = "UPDATE citas SET estado = ? WHERE id_cita = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idCita);
            ps.executeUpdate();
        }
    }

    public List<ResumenComision> obtenerReporteComisiones(String fechaInicio, String fechaFin, Integer idUsuarioBarbero) throws SQLException {
        List<ResumenComision> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT u.nombre_completo AS barbero, COUNT(c.id_cita) AS total_citas, " +
            "COALESCE(SUM(c.monto_cobrado), 0) AS ingresos, COALESCE(SUM(c.comision_barbero), 0) AS comision " +
            "FROM barberos b " +
            "INNER JOIN usuarios u ON b.id_usuario = u.id_usuario " +
            "LEFT JOIN citas c ON b.id_barbero = c.id_barbero AND c.estado = 'COMPLETADA' " +
            "  AND DATE(c.fecha_hora) BETWEEN ? AND ? " +
            "WHERE u.activo = TRUE "
        );
        if (idUsuarioBarbero != null) {
            sql.append("AND u.id_usuario = ? ");
        }
        sql.append("GROUP BY b.id_barbero, u.nombre_completo ORDER BY ingresos DESC");

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            ps.setString(1, fechaInicio);
            ps.setString(2, fechaFin);
            if (idUsuarioBarbero != null) {
                ps.setInt(3, idUsuarioBarbero);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ResumenComision r = new ResumenComision();
                    r.setNombreBarbero(rs.getString("barbero"));
                    r.setCitasAtendidas(rs.getInt("total_citas"));
                    r.setTotalGenerado(rs.getDouble("ingresos"));
                    r.setComisionBarbero(rs.getDouble("comision"));
                    lista.add(r);
                }
            }
        }
        return lista;
    }
}
