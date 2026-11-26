<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.royalblades.modelo.Usuario" %>
<%
    Usuario u = (Usuario) session.getAttribute("usuarioLogueado");
    String nombreSeguro = u.getNombreCompleto().replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Panel Principal - Royal Blades</title>
    <link href="https://jsdelivr.net" rel="stylesheet">
</head>
<body class="bg-light">
<nav class="navbar navbar-expand-lg navbar-dark bg-dark px-4">
    <a class="navbar-brand fw-bold" href="#">Royal Blades</a>
    <div class="ms-auto d-flex align-items-center text-white">
        <span class="me-3"><%= nombreSeguro %> <span class="badge bg-warning text-dark"><%= u.getRol() %></span></span>
        <a href="../logout" class="btn btn-outline-light btn-sm">Cerrar sesión</a>
    </div>
</nav>

<div class="container py-5">
    <h4 class="mb-4">Bienvenido al Sistema de Barbería</h4>
    <div class="row g-4">
        <div class="col-md-4">
            <div class="card h-100 shadow-sm">
                <div class="card-body">
                    <h5 class="card-title">Gestión de Citas</h5>
                    <p class="card-text text-muted">Registrar citas con asignación equitativa o manual.</p>
                    <a href="citas" class="btn btn-dark">Abrir Agenda</a>
                </div>
            </div>
        </div>
        <% if ("ADMIN".equals(u.getRol())) { %>
        <div class="col-md-4">
            <div class="card h-100 shadow-sm border-warning">
                <div class="card-body">
                    <h5 class="card-title">Usuarios y Barberos</h5>
                    <p class="card-text text-muted">Alta de personal y control de turnos.</p>
                    <a href="admin/barberos" class="btn btn-warning fw-semibold">Administrar Personal</a>
                </div>
            </div>
        </div>
        <% } %>
    </div>
</div>
</body>
</html>
