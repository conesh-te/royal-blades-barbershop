package com.royalblades.dao;

import com.royalblades.config.ConexionBD;
import com.royalblades.modelo.Usuario;
import com.royalblades.seguridad.PasswordUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public Usuario autenticar(String correo, String passwordPlano) throws SQLException {
        String sql = "SELECT id_usuario, nombre_completo, correo, password_hash, rol, activo " +
                     "FROM usuarios WHERE correo = ? AND activo = TRUE";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, correo.trim().toLowerCase());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hashGuardado = rs.getString("password_hash");
                    if (PasswordUtil.checkPassword(passwordPlano, hashGuardado)) {
                        Usuario u = new Usuario();
                        u.setIdUsuario(rs.getInt("id_usuario"));
                        u.setNombreCompleto(rs.getString("nombre_completo"));
                        u.setCorreo(rs.getString("correo"));
                        u.setRol(rs.getString("rol"));
                        u.setActivo(rs.getBoolean("activo"));
                        return u;
                    }
                }
            }
        }
        return null;
    }

    public boolean registrarUsuario(Usuario nuevo, String passwordPlano) throws SQLException {
        String sql = "INSERT INTO usuarios (nombre_completo, correo, password_hash, rol, activo) VALUES (?, ?, ?, ?, TRUE)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nuevo.getNombreCompleto().trim());
            ps.setString(2, nuevo.getCorreo().trim().toLowerCase());
            ps.setString(3, PasswordUtil.hashPassword(passwordPlano));
            ps.setString(4, nuevo.getRol());
            return ps.executeUpdate() > 0;
        }
    }

    public void asegurarAdminPorDefecto() {
        String checkSql = "SELECT COUNT(*) FROM usuarios WHERE rol = 'ADMIN'";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(checkSql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next() && rs.getInt(1) == 0) {
                Usuario admin = new Usuario();
                admin.setNombreCompleto("Administrador Royal Blades");
                admin.setCorreo("admin@royalblades.com");
                admin.setRol("ADMIN");
                registrarUsuario(admin, "AdminRoyal2026!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
