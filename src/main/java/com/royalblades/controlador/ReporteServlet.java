package com.royalblades.controlador;

import com.royalblades.dao.ReporteDAO;
import com.royalblades.modelo.ResumenComision;
import com.royalblades.modelo.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/app/reportes")
public class ReporteServlet extends HttpServlet {

    private final ReporteDAO reporteDAO = new ReporteDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        Usuario u = (Usuario) req.getSession().getAttribute("usuarioLogueado");
        String hoy = LocalDate.now().toString();
        String inicio = req.getParameter("inicio") != null ? req.getParameter("inicio") : hoy;
        String fin = req.getParameter("fin") != null ? req.getParameter("fin") : hoy;

        // Seguridad RBAC: Si es BARBERO, solo consulta sus propias comisiones
        Integer filtroBarbero = "BARBERO".equals(u.getRol()) ? u.getIdUsuario() : null;

        try {
            List<ResumenComision> lista = reporteDAO.obtenerReporteComisiones(inicio, fin, filtroBarbero);
            double totalIngresos = 0;
            double totalComisiones = 0;
            for (ResumenComision r : lista) {
                totalIngresos += r.getTotalGenerado();
                totalComisiones += r.getComisionBarbero();
            }

            req.setAttribute("reporte", lista);
            req.setAttribute("inicio", inicio);
            req.setAttribute("fin", fin);
            req.setAttribute("totalIngresos", totalIngresos);
            req.setAttribute("totalComisiones", totalComisiones);
            req.getRequestDispatcher("/app/reportes.jsp").forward(req, res);
        } catch (SQLException e) {
            throw new ServletException("Error al generar reporte", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        try {
            int idCita = Integer.parseInt(req.getParameter("idCita"));
            String estado = req.getParameter("estado");
            reporteDAO.actualizarEstadoCita(idCita, estado);
            res.sendRedirect(req.getContextPath() + "/app/citas?ok=1");
        } catch (SQLException e) {
            e.printStackTrace();
            res.sendRedirect(req.getContextPath() + "/app/citas?error=1");
        }
    }
}
