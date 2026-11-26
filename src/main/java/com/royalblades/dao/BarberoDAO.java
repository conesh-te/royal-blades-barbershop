package com.royalblades.dao;

import com.royalblades.config.ConexionBD;
import com.royalblades.modelo.Barbero;
import com.royalblades.seguridad.PasswordUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BarberoDAO {

    public List<Barbero> listarBarberos() throws SQLException {
        List<Barbero> lista = new ArrayList<>();
        String sql = "SELECT b.id_barbero, b.id_usuario, u.nombre_completo, u.correo, b.en_turno, b.porcentaje_comision " +
                     "FROM barberos b INNER JOIN usuarios u ON b.id_usuario = u.id_usuario WHERE u.activo = TRUE";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Barbero b = new Barbero();
                b.setIdBarbero(rs.getInt("id_barbero"));
                b.setIdUsuario(rs.getInt("id_usuario"));
                b.setNombreCompleto(rs.getString("nombre_completo"));
                b.setCorreo(rs.getString("correo"));
                b.setEnTurno(rs.getBoolean("en_turno"));
                b.setPorcentajeComision(rs.getDouble("porcentaje_comision"));
                lista.add(b);
            }
        }
        return lista;
    }

    public boolean registrarEmpleado(String nombre, String correo, String passwordPlano, String rol) throws SQLException {
        String sqlUsuario = "INSERT INTO usuarios (nombre_completo, correo, password_hash, rol, activo) VALUES (?, ?, ?, ?, TRUE)";
        String sqlBarbero = "INSERT INTO barberos (id_usuario, en_turno, porcentaje_comision) VALUES (?, TRUE, 50.00)";

        Connection conn = null;
        try {
            conn = ConexionBD.getConnection();
            conn.setAutoCommit(false);

            int idUsuarioGenerado = 0;
            try (PreparedStatement psUser = conn.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS)) {
                psUser.setString(1, nombre.trim());
                psUser.setString(2, correo.trim().toLowerCase());
                psUser.setString(3, PasswordUtil.hashPassword(passwordPlano));
                psUser.setString(4, rol);
                psUser.executeUpdate();

                try (ResultSet keys = psUser.getGeneratedKeys()) {
                    if (keys.next()) {
                        idUsuarioGenerado = keys.getInt(1);
                    }
                }
            }

            if ("BARBERO".equals(rol) && idUsuarioGenerado > 0) {
                try (PreparedStatement psBarb = conn.prepareStatement(sqlBarbero)) {
                    psBarb.setInt(1, idUsuarioGenerado);
                    psBarb.executeUpdate();
                }
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

    public void cambiarEstadoTurno(int idBarbero, boolean enTurno) throws SQLException {
        String sql = "UPDATE barberos SET en_turno = ? WHERE id_barbero = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, enTurno);
            ps.setInt(2, idBarbero);
            ps.executeUpdate();
        }
    }
}
