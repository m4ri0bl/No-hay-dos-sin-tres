<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lista de Invitados</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="<c:url value='/css/styles.css'/>">
</head>
<body>
<div class="container py-4">
    <header class="mb-4">
        <div class="d-flex justify-content-between align-items-center">
            <h1><i class="fas fa-list-ul me-2" style="color: var(--electric-blue);"></i>Lista de Invitados</h1>
            <div>
                <a href="<c:url value='/guests?action=dashboard'/>" class="btn btn-outline-info me-2">
                    <i class="fas fa-tachometer-alt me-2"></i>Dashboard
                </a>
                <a href="<c:url value='/'/>" class="btn btn-outline-secondary">
                    <i class="fas fa-home me-2"></i>Inicio
                </a>
            </div>
        </div>
    </header>

    <!-- Mensajes de éxito o error -->
    <c:if test="${not empty sessionScope.successMessage}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="fas fa-check-circle me-2"></i>${sessionScope.successMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <c:remove var="successMessage" scope="session" />
    </c:if>

    <c:if test="${not empty sessionScope.errorMessage}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="fas fa-exclamation-circle me-2"></i>${sessionScope.errorMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <c:remove var="errorMessage" scope="session" />
    </c:if>

    <!-- Resumen de estadísticas -->
    <div class="row mb-4 g-3">
        <div class="col-md-4">
            <div class="card bg-primary text-white">
                <div class="card-body text-center p-4">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h5 class="card-title fw-bold mb-3">Total Invitados</h5>
                            <p class="card-text display-4 fw-bold mb-0">${totalGuests}</p>
                        </div>
                        <div>
                            <i class="fas fa-users fa-3x opacity-50"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card bg-success text-white">
                <div class="card-body text-center p-4">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h5 class="card-title fw-bold mb-3">Confirmados</h5>
                            <p class="card-text display-4 fw-bold mb-0">${confirmedGuests}</p>
                        </div>
                        <div>
                            <i class="fas fa-check-circle fa-3x opacity-50"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card bg-warning text-dark">
                <div class="card-body text-center p-4">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h5 class="card-title fw-bold mb-3">Pendientes</h5>
                            <p class="card-text display-4 fw-bold mb-0">${pendingGuests}</p>
                        </div>
                        <div>
                            <i class="fas fa-clock fa-3x opacity-50"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="row mb-4">
        <div class="col-md-6">
            <a href="<c:url value='/guests?action=new'/>" class="btn btn-success">
                <i class="fas fa-user-plus me-2"></i>Añadir Nuevo Invitado
            </a>
        </div>
        <div class="col-md-6">
            <form action="<c:url value='/guests'/>" method="get" class="d-flex">
                <input type="hidden" name="action" value="search">
                <input type="text" name="searchTerm" class="form-control me-2" placeholder="Buscar por nombre..." value="${searchTerm}">
                <button type="submit" class="btn btn-primary">
                    <i class="fas fa-search me-2"></i>Buscar
                </button>
            </form>
        </div>
    </div>

    <c:if test="${not empty searchTerm}">
        <div class="alert alert-info">
            <i class="fas fa-search me-2"></i>Resultados para: <strong>"${searchTerm}"</strong>
            <a href="<c:url value='/guests?action=list'/>" class="ms-2 btn btn-sm btn-outline-secondary">Ver todos</a>
        </div>
    </c:if>

    <div class="card shadow glass-effect">
        <div class="card-body p-0">
            <c:choose>
                <c:when test="${empty guests}">
                    <div class="alert alert-warning m-4">
                        <i class="fas fa-exclamation-triangle me-2"></i>No hay invitados registrados.
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="table-responsive">
                        <table class="table table-striped table-hover">
                            <thead class="table-dark">
                            <tr>
                                <th>ID</th>
                                <th>Nombre</th>
                                <th>Email</th>
                                <th>Teléfono</th>
                                <th>Confirmado</th>
                                <th>Acciones</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="guest" items="${guests}">
                                <tr>
                                    <td>${guest.id}</td>
                                    <td>${guest.name}</td>
                                    <td>${guest.email}</td>
                                    <td>${guest.phone}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${guest.confirmed}">
                                                <span class="badge bg-success"><i class="fas fa-check me-1"></i>Sí</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-warning text-dark"><i class="fas fa-times me-1"></i>No</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <div class="btn-group" role="group">
                                            <a href="<c:url value='/guests?action=edit&id=${guest.id}'/>" class="btn btn-sm btn-primary" title="Editar">
                                                <i class="fas fa-edit"></i>
                                            </a>
                                            <a href="#" onclick="confirmDelete(${guest.id})" class="btn btn-sm btn-danger" title="Eliminar">
                                                <i class="fas fa-trash"></i>
                                            </a>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <footer class="text-center mt-5">
        <p class="text-muted">&copy; 2025 Sistema de Gestión de Invitados</p>
    </footer>
</div>

<script>
    function confirmDelete(id) {
        if (confirm('¿Estás seguro de que deseas eliminar este invitado?')) {
            window.location.href = '<c:url value="/guests?action=delete&id="/>' + id;
        }
    }
</script>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

