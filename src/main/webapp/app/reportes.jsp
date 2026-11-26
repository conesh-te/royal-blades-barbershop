<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, com.royalblades.modelo.ResumenComision" %>
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
    <title>Reportes e Ingresos - Royal Blades</title>
    <link href="https://jsdelivr.net" rel="stylesheet">
</head>
<body class="bg-light">
<nav class="navbar navbar-dark bg-dark px-4">
    <a class="navbar-brand fw-bold" href="dashboard.jsp">← Volver al Panel</a>
    <span class="navbar-text text-white">Reporte de Ingresos y Comisiones</span>
</nav>

<div class="container py-4">
    <form action="reportes" method="get" class="row g-3 align-items-end mb-4 bg-white p-3 rounded shadow-sm">
        <div class="col-md-4">
            <label class="form-label fw-semibold">Fecha inicio</label>
            <input type="date" name="inicio" value="<%= esc((String) request.getAttribute("inicio")) %>" class="form-control" required>
        </div>
        <div class="col-md-4">
            <label class="form-label fw-semibold">Fecha fin</label>
            <input type="date" name="fin" value="<%= esc((String) request.getAttribute("fin")) %>" class="form-control" required>
        </div>
        <div class="col-md-4">
            <button type="submit" class="btn btn-dark w-100">Filtrar Reporte</button>
        </div>
    </form>

    <div class="row g-3 mb-4">
        <div class="col-md-6">
            <div class="card border-success shadow-sm">
                <div class="card-body">
                    <h6 class="text-muted">Ingresos Brutos (Citas Completadas)</h6>
                    <h3 class="text-success fw-bold">$<%= String.format("%.2f", (Double) request.getAttribute("totalIngresos")) %></h3>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="card border-primary shadow-sm">
                <div class="card-body">
                    <h6 class="text-muted">Total Comisiones a Barberos (50%)</h6>
                    <h3 class="text-primary fw-bold">$<%= String.format("%.2f", (Double) request.getAttribute("totalComisiones")) %></h3>
                </div>
            </div>
        </div>
    </div>

    <div class="card shadow-sm">
        <div class="card-header bg-white fw-semibold">Desglose por Barbero</div>
        <div class="card-body p-0">
            <table class="table table-striped mb-0">
                <thead class="table-light">
                    <tr>
                        <th>Barbero</th>
                        <th>Citas Atendidas</th>
                        <th>Total Generado</th>
                        <th>Comisión Barbero</th>
                        <th>Utilidad Barbería</th>
                    </tr>
                </thead>
                <tbody>
                <% List<ResumenComision> lista = (List<ResumenComision>) request.getAttribute("reporte");
                   if (lista != null && !lista.isEmpty()) {
                       for (ResumenComision r : lista) { %>
                    <tr>
                        <td class="fw-semibold"><%= esc(r.getNombreBarbero()) %></td>
                        <td><%= r.getCitasAtendidas() %></td>
                        <td>$<%= String.format("%.2f", r.getTotalGenerado()) %></td>
                        <td class="text-primary fw-semibold">$<%= String.format("%.2f", r.getComisionBarbero()) %></td>
                        <td class="text-success fw-semibold">$<%= String.format("%.2f", r.getTotalGenerado() - r.getComisionBarbero()) %></td>
                    </tr>
                <% } } else { %>
                    <tr><td colspan="5" class="text-center text-muted py-3">Sin registros en este rango de fechas.</td></tr>
                <% } %>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>
