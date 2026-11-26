<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Acceso - Royal Blades Barber Shop</title>
    <link href="https://jsdelivr.net" rel="stylesheet">
</head>
<body class="bg-dark d-flex align-items-center vh-100">
<div class="container">
    <div class="row justify-content-center">
        <div class="col-md-5 col-lg-4">
            <div class="card shadow-lg border-0 rounded-4">
                <div class="card-body p-4">
                    <h3 class="text-center fw-bold mb-1">Royal Blades</h3>
                    <p class="text-center text-muted mb-4">Sistema de Gestión de Citas</p>

                    <% String error = request.getParameter("error");
                       if ("credenciales".equals(error)) { %>
                        <div class="alert alert-danger py-2">Correo o contraseña incorrectos.</div>
                    <% } else if ("sesion".equals(error)) { %>
                        <div class="alert alert-warning py-2">Inicia sesión para continuar.</div>
                    <% } else if ("servidor".equals(error)) { %>
                        <div class="alert alert-danger py-2">Error de conexión con la base de datos.</div>
                    <% } %>

                    <form action="login" method="post">
                        <div class="mb-3">
                            <label class="form-label fw-semibold">Correo electrónico</label>
                            <input type="email" name="correo" class="form-control" placeholder="admin@royalblades.com" required>
                        </div>
                        <div class="mb-4">
                            <label class="form-label fw-semibold">Contraseña</label>
                            <input type="password" name="password" class="form-control" required>
                        </div>
                        <button type="submit" class="btn btn-dark w-100 py-2 fw-semibold">Iniciar Sesión</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
