<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, com.royalblades.modelo.*" %>
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
    <title>Gestión de Citas - Royal Blades</title>
    <link href="https://jsdelivr.net" rel="stylesheet">
</head>
<body class="bg-light">
<nav class="navbar navbar-dark bg-dark px-4">
    <a class="navbar-brand fw-bold" href="dashboard.jsp">← Volver al Panel</a>
    <span class="navbar-text text-white">Registro y Cierre de Citas</span>
</nav>

<div class="container py-4">
    <% if (request.getParameter("ok") != null) { %>
        <div class="alert alert-success">Agenda actualizada correctamente.</div>
    <% } else if (request.getParameter("error") != null) { %>
        <div class="alert alert-danger">Error al procesar la cita (verifica que haya barberos en turno).</div>
    <% } %>

    <div class="row g-4">
        <div class="col-md-4">
            <div class="card shadow-sm">
                <div class="card-header bg-dark text-white fw-semibold">Nueva Cita</div>
                <div class="card-body">
                    <form action="citas" method="post">
                        <div class="mb-2">
                            <label class="form-label">Nombre del cliente</label>
                            <input type="text" name="nombreCliente" class="form-control" required>
                        </div>
                        <div class="mb-2">
                            <label class="form-label">Teléfono</label>
                            <input type="text" name="telefonoCliente" class="form-control">
                        </div>
                        <div class="mb-2">
                            <label class="form-label">Correo electrónico</label>
                            <input type="email" name="correoCliente" class="form-control" required>
                        </div>
                        <div class="mb-2">
                            <label class="form-label">Servicio</label>
                            <select name="idServicio" class="form-select" required>
                                <% List<Servicio> servicios = (List<Servicio>) request.getAttribute("servicios");
                                   if (servicios != null) {
                                       for (Servicio s : servicios) { %>
                                    <option value="<%= s.getIdServicio() %>"><%= esc(s.getNombre()) %> ($<%= s.getPrecio() %>)</option>
                                <% } } %>
                            </select>
                        </div>
                        <div class="mb-2">
                            <label class="form-label">Barbero</label>
                            <select name="idBarbero" class="form-select">
                                <option value="">-- Asignación Automática (Menos carga) --</option>
                                <% List<Barbero> barberos = (List<Barbero>) request.getAttribute("barberos");
                                   if (barberos != null) {
                                       for (Barbero b : barberos) { %>
                                    <option value="<%= b.getIdBarbero() %>"><%= esc(b.getNombreCompleto()) %></option>
                                <% } } %>
                            </select>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Fecha y hora</label>
                            <input type="datetime-local" name="fechaHora" class="form-control" required>
                        </div>
                        <button type="submit" class="btn btn-dark w-100">Agendar Cita</button>
                    </form>
                </div>
            </div>
        </div>

        <div class="col-md-8">
            <div class="card shadow-sm">
                <div class="card-header bg-white fw-semibold">Agenda de Citas Recientes</div>
                <div class="card-body p-0">
                    <table class="table table-hover mb-0 align-middle">
                        <thead class="table-light">
                            <tr>
                                <th>Fecha/Hora</th>
                                <th>Cliente</th>
                                <th>Servicio</th>
                                <th>Barbero</th>
                                <th>Estado</th>
                                <th>Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                        <% List<Cita> citas = (List<Cita>) request.getAttribute("citas");
                           if (citas != null && !citas.isEmpty()) {
                               for (Cita c : citas) { %>
                            <tr>
                                <td><%= esc(c.getFechaHora()) %></td>
                                <td><%= esc(c.getNombreCliente()) %></td>
                                <td><%= esc(c.getNombreServicio()) %> ($<%= c.getMontoCobrado() %>)</td>
                                <td class="fw-semibold"><%= esc(c.getNombreBarbero()) %></td>
                                <td>
                                    <% if ("COMPLETADA".equals(c.getEstado())) { %>
                                        <span class="badge bg-success">COMPLETADA</span>
                                    <% } else if ("CANCELADA".equals(c.getEstado())) { %>
                                        <span class="badge bg-danger">CANCELADA</span>
                                    <% } else { %>
                                        <span class="badge bg-warning text-dark">PENDIENTE</span>
                                    <% } %>
                                </td>
                                <td>
                                    <% if ("PENDIENTE".equals(c.getEstado())) { %>
                                        <form action="reportes" method="post" class="d-inline">
                                            <input type="hidden" name="idCita" value="<%= c.getIdCita() %>">
                                            <input type="hidden" name="estado" value="COMPLETADA">
                                            <button type="submit" class="btn btn-sm btn-success">Cobrar</button>
                                        </form>
                                        <form action="reportes" method="post" class="d-inline">
                                            <input type="hidden" name="idCita" value="<%= c.getIdCita() %>">
                                            <input type="hidden" name="estado" value="CANCELADA">
                                            <button type="submit" class="btn btn-sm btn-outline-danger">X</button>
                                        </form>
                                    <% } else { %>
                                        <small class="text-muted">Cerrada</small>
                                    <% } %>
                                </td>
                            </tr>
                        <% } } else { %>
                            <tr><td colspan="6" class="text-center text-muted py-3">No hay citas registradas.</td></tr>
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
