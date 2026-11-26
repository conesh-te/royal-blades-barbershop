package com.royalblades.controlador;

import com.royalblades.dao.CitaDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/app/citas")
public class CitaServlet extends HttpServlet {

    private final CitaDAO citaDAO = new CitaDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        try {
            req.setAttribute("servicios", citaDAO.listarServiciosActivos());
            req.setAttribute("barberos", citaDAO.listarBarberosEnTurno());
            req.setAttribute("citas", citaDAO.listarCitasDelDia());
            req.getRequestDispatcher("/app/citas.jsp").forward(req, res);
        } catch (SQLException e) {
            throw new ServletException("Error al cargar datos de citas", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        try {
            String nombre = req.getParameter("nombreCliente");
            String telefono = req.getParameter("telefonoCliente");
            String correo = req.getParameter("correoCliente");
            int idServicio = Integer.parseInt(req.getParameter("idServicio"));
            String barberoParam = req.getParameter("idBarbero");
            Integer idBarbero = (barberoParam != null && !barberoParam.isEmpty())
                    ? Integer.parseInt(barberoParam) : null;
            String fechaHora = req.getParameter("fechaHora");

            citaDAO.registrarCita(nombre, telefono, correo, idServicio, idBarbero, fechaHora);
            res.sendRedirect(req.getContextPath() + "/app/citas?ok=1");
        } catch (SQLException e) {
            e.printStackTrace();
            res.sendRedirect(req.getContextPath() + "/app/citas?error=1");
        }
    }
}
