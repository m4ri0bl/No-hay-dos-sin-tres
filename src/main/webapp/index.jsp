<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sistema de Gestión de Invitados</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="<c:url value='/css/styles.css'/>">
</head>
<body>
<div class="container">
    <header class="text-center my-5">
        <h1 class="display-4 fw-bold">Sistema de Gestión de Invitados</h1>
        <p class="lead">Administra fácilmente la lista de invitados para tu evento</p>
    </header>

    <!-- Mensaje de error de configuración -->
    <c:if test="${not empty applicationScope.configError}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <h4 class="alert-heading"><i class="fas fa-exclamation-triangle me-2"></i>Error de configuración</h4>
            <p>${applicationScope.configError}</p>
            <p>Por favor, configura las variables de entorno SUPABASE_URL y SUPABASE_KEY correctamente.</p>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card shadow-lg">
                <div class="card-body text-center p-5">
                    <div class="mb-4">
                        <i class="fas fa-users fa-4x" style="background: var(--gradient-primary); -webkit-background-clip: text; -webkit-text-fill-color: transparent;"></i>
                    </div>
                    <h2 class="mb-4 fw-bold">¡Bienvenido!</h2>
                    <p class="mb-4 text-secondary">Desde aquí podrás gestionar todos los invitados a tu evento.</p>

                    <div class="d-grid gap-3 col-md-8 mx-auto">
                        <a href="<c:url value='/guests?action=dashboard'/>" class="btn btn-info btn-lg">
                            <i class="fas fa-tachometer-alt me-2"></i>Dashboard
                        </a>
                        <a href="<c:url value='/guests?action=list'/>" class="btn btn-primary btn-lg">
                            <i class="fas fa-list me-2"></i>Ver todos los invitados
                        </a>
                        <a href="<c:url value='/guests?action=new'/>" class="btn btn-success btn-lg">
                            <i class="fas fa-user-plus me-2"></i>Añadir nuevo invitado
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <footer class="text-center mt-5 mb-3">
        <p class="text-muted">&copy; 2025 Sistema de Gestión de Invitados</p>
    </footer>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

