package com.royalblades.controlador;

import com.royalblades.dao.UsuarioDAO;
import com.royalblades.modelo.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @Override
    public void init() {
        usuarioDAO.asegurarAdminPorDefecto();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String correo = req.getParameter("correo");
        String password = req.getParameter("password");

        if (correo == null || password == null || correo.trim().isEmpty() || password.isEmpty()) {
            res.sendRedirect("login.jsp?error=campos");
            return;
        }

        try {
            Usuario usuario = usuarioDAO.autenticar(correo, password);
            if (usuario != null) {
                HttpSession session = req.getSession(true);
                req.changeSessionId(); // Prevención de Session Fixation
                session.setAttribute("usuarioLogueado", usuario);
                res.sendRedirect(req.getContextPath() + "/app/dashboard.jsp");
            } else {
                res.sendRedirect("login.jsp?error=credenciales");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            res.sendRedirect("login.jsp?error=servidor");
        }
    }
}
