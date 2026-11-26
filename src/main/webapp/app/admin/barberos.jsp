<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, com.royalblades.modelo.Barbero" %>
<%!
    public String esc(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Usuarios y Barberos - Royal Blades</title>
    <link href="https://jsdelivr.net" rel="stylesheet">
</head>
<body class="bg-light">
<nav class="navbar navbar-dark bg-dark px-4">
    <a class="navbar-brand fw-bold" href="../dashboard.jsp">← Volver al Panel</a>
    <span class="navbar-text text-white">Administración de Personal</span>
</nav>

<div class="container py-4">
    <% if (request.getParameter("ok") != null) { %>
        <div class="alert alert-success">Operación realizada correctamente.</div>
    <% } else if (request.getParameter("error") != null) { %>
        <div class="alert alert-danger">Error al guardar (verifica que el correo no esté duplicado).</div>
    <% } %>

    <div class="row g-4">
        <!-- Formulario Alta de Personal -->
        <div class="col-md-4">
            <div class="card shadow-sm">
                <div class="card-header bg-dark text-white fw-semibold">Registrar Nuevo Usuario</div>
                <div class="card-body">
                    <form action="barberos" method="post">
                        <input type="hidden" name="accion" value="crear">
                        <div class="mb-3">
                            <label class="form-label">Nombre completo</label>
                            <input type="text" name="nombre" class="form-control" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Correo electrónico</label>
                            <input type="email" name="correo" class="form-control" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Contraseña temporal</label>
                            <input type="password" name="password" class="form-control" minlength="6" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Rol en el sistema</label>
                            <select name="rol" class="form-select">
                                <option value="BARBERO">Barbero (Recibe citas)</option>
                                <option value="RECEPCION">Recepción (Sin privilegios admin)</option>
                                <option value="ADMIN">Administrador</option>
                            </select>
                        </div>
                        <button type="submit" class="btn btn-dark w-100">Guardar Usuario</button>
                    </form>
                </div>
            </div>
        </div>

        <!-- Tabla de Barberos y Turnos -->
        <div class="col-md-8">
            <div class="card shadow-sm">
                <div class="card-header bg-white fw-semibold">Barberos Activos y Disponibilidad en Turno</div>
                <div class="card-body p-0">
                    <table class="table table-hover mb-0 align-middle">
                        <thead class="table-light">
                            <tr>
                                <th>Barbero</th>
                                <th>Correo</th>
                                <th>Comisión</th>
                                <th>Estado</th>
                                <th>Acción</th>
                            </tr>
                        </thead>
                        <tbody>
                        <%
                            List<Barbero> lista = (List<Barbero>) request.getAttribute("listaBarberos");
                            if (lista != null && !lista.isEmpty()) {
                                for (Barbero b : lista) {
                        %>
                            <tr>
                                <td class="fw-semibold"><%= esc(b.getNombreCompleto()) %></td>
                                <td><%= esc(b.getCorreo()) %></td>
                                <td><%= b.getPorcentajeComision() %>%</td>
                                <td>
                                    <% if (b.isEnTurno()) { %>
                                        <span class="badge bg-success">En Turno</span>
                                    <% } else { %>
                                        <span class="badge bg-secondary">Fuera de Turno</span>
                                    <% } %>
                                </td>
                                <td>
                                    <form action="barberos" method="post" class="d-inline">
                                        <input type="hidden" name="accion" value="turno">
                                        <input type="hidden" name="idBarbero" value="<%= b.getIdBarbero() %>">
                                        <input type="hidden" name="enTurno" value="<%= !b.isEnTurno() %>">
                                        <button type="submit" class="btn btn-sm <%= b.isEnTurno() ? "btn-outline-secondary" : "btn-outline-success" %>">
                                            <%= b.isEnTurno() ? "Pausar turno" : "Activar turno" %>
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        <%      }
                            } else { %>
                            <tr><td colspan="5" class="text-center text-muted py-3">Aún no hay barberos registrados.</td></tr>
                        <% } %>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
