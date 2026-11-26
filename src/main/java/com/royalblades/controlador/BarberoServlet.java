package com.royalblades.controlador;

import com.royalblades.dao.BarberoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/app/admin/barberos")
public class BarberoServlet extends HttpServlet {

    private final BarberoDAO barberoDAO = new BarberoDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        try {
            req.setAttribute("listaBarberos", barberoDAO.listarBarberos());
            req.getRequestDispatcher("/app/admin/barberos.jsp").forward(req, res);
        } catch (SQLException e) {
            throw new ServletException("Error al cargar barberos", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String accion = req.getParameter("accion");
        try {
            if ("crear".equals(accion)) {
                String nombre = req.getParameter("nombre");
                String correo = req.getParameter("correo");
                String password = req.getParameter("password");
                String rol = req.getParameter("rol");
                barberoDAO.registrarEmpleado(nombre, correo, password, rol);
            } else if ("turno".equals(accion)) {
                int idBarbero = Integer.parseInt(req.getParameter("idBarbero"));
                boolean enTurno = Boolean.parseBoolean(req.getParameter("enTurno"));
                barberoDAO.cambiarEstadoTurno(idBarbero, enTurno);
            }
            res.sendRedirect(req.getContextPath() + "/app/admin/barberos?ok=1");
        } catch (SQLException e) {
            e.printStackTrace();
            res.sendRedirect(req.getContextPath() + "/app/admin/barberos?error=1");
        }
    }
}
