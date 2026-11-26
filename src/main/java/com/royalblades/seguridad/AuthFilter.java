package com.royalblades.seguridad;

import com.royalblades.modelo.Usuario;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/app/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuarioLogueado") : null;

        if (usuario == null) {
            res.sendRedirect(req.getContextPath() + "/login.jsp?error=sesion");
            return;
        }

        String uri = req.getRequestURI();
        if (uri.contains("/app/admin/") && !"ADMIN".equals(usuario.getRol())) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado: Se requiere rol ADMIN");
            return;
        }

        chain.doFilter(request, response);
    }
}
