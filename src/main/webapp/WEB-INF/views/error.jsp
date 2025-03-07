<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="<c:url value='/css/styles.css'/>">
</head>
<body>
<div class="container py-5">
    <div class="row justify-content-center mt-5">
        <div class="col-md-8">
            <div class="card shadow-lg glass-effect">
                <div class="card-header bg-danger text-white">
                    <h3 class="mb-0"><i class="fas fa-exclamation-triangle me-2"></i>Error</h3>
                </div>
                <div class="card-body p-5 text-center">
                    <div class="mb-4">
                        <i class="fas fa-times-circle text-danger" style="font-size: 5rem;"></i>
                    </div>
                    <h4 class="mb-3 fw-bold">Ha ocurrido un error</h4>
                    <p class="mb-4">${errorMessage}</p>
                    <div class="d-grid gap-3 col-md-6 mx-auto">
                        <a href="<c:url value='/'/>" class="btn btn-primary">
                            <i class="fas fa-home me-2"></i>Volver al inicio
                        </a>
                        <button class="btn btn-outline-secondary" onclick="window.history.back()">
                            <i class="fas fa-arrow-left me-2"></i>Volver atrás
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <footer class="text-center mt-5">
        <p class="text-muted">&copy; 2025 Sistema de Gestión de Invitados</p>
    </footer>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

