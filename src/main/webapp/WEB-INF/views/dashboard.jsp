<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - Sistema de Gestión de Invitados</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="<c:url value='/css/styles.css'/>">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
<div class="container py-4">
    <header class="mb-4">
        <div class="d-flex justify-content-between align-items-center">
            <h1><i class="fas fa-tachometer-alt me-2" style="color: var(--electric-blue);"></i>Dashboard</h1>
            <div>
                <a href="<c:url value='/guests?action=list'/>" class="btn btn-outline-primary me-2">
                    <i class="fas fa-list me-2"></i>Ver Lista
                </a>
                <a href="<c:url value='/'/>" class="btn btn-outline-secondary">
                    <i class="fas fa-home me-2"></i>Inicio
                </a>
            </div>
        </div>
    </header>

    <!-- Tarjetas de estadísticas -->
    <div class="row mb-4 g-3">
        <div class="col-md-4">
            <div class="card bg-primary text-white shadow-sm">
                <div class="card-body p-4">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h6 class="card-title fw-bold">Total Invitados</h6>
                            <h2 class="mb-0 fw-bold">${totalGuests}</h2>
                        </div>
                        <div>
                            <i class="fas fa-users fa-3x opacity-50"></i>
                        </div>
                    </div>
                </div>
                <div class="card-footer text-center">
                    <a href="<c:url value='/guests?action=list'/>" class="text-white text-decoration-none">
                        <small>Ver detalles <i class="fas fa-arrow-right ms-1"></i></small>
                    </a>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card bg-success text-white shadow-sm">
                <div class="card-body p-4">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h6 class="card-title fw-bold">Confirmados</h6>
                            <h2 class="mb-0 fw-bold">${confirmedGuests}</h2>
                        </div>
                        <div>
                            <i class="fas fa-check-circle fa-3x opacity-50"></i>
                        </div>
                    </div>
                </div>
                <div class="card-footer text-center">
                    <a href="<c:url value='/guests?action=list'/>" class="text-white text-decoration-none">
                        <small>Ver detalles <i class="fas fa-arrow-right ms-1"></i></small>
                    </a>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card bg-warning text-dark shadow-sm">
                <div class="card-body p-4">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h6 class="card-title fw-bold">Pendientes</h6>
                            <h2 class="mb-0 fw-bold">${pendingGuests}</h2>
                        </div>
                        <div>
                            <i class="fas fa-clock fa-3x opacity-50"></i>
                        </div>
                    </div>
                </div>
                <div class="card-footer text-center">
                    <a href="<c:url value='/guests?action=list'/>" class="text-dark text-decoration-none">
                        <small>Ver detalles <i class="fas fa-arrow-right ms-1"></i></small>
                    </a>
                </div>
            </div>
        </div>
    </div>

    <!-- Gráfico y tabla de invitados recientes -->
    <div class="row mb-4 g-3">
        <div class="col-md-6">
            <div class="card shadow-sm glass-effect">
                <div class="card-header">
                    <h5 class="card-title mb-0 fw-bold">Distribución de Invitados</h5>
                </div>
                <div class="card-body p-4">
                    <canvas id="guestsChart" width="400" height="300"></canvas>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="card shadow-sm glass-effect">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <h5 class="card-title mb-0 fw-bold">Invitados Recientes</h5>
                    <a href="<c:url value='/guests?action=list'/>" class="btn btn-sm btn-primary">
                        Ver todos
                    </a>
                </div>
                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table table-hover mb-0">
                            <thead class="table-light">
                            <tr>
                                <th>Nombre</th>
                                <th>Email</th>
                                <th>Estado</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="guest" items="${recentGuests}">
                                <tr>
                                    <td>${guest.name}</td>
                                    <td>${guest.email}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${guest.confirmed}">
                                                <span class="badge bg-success">Confirmado</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-warning text-dark">Pendiente</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty recentGuests}">
                                <tr>
                                    <td colspan="3" class="text-center py-3">No hay invitados registrados</td>
                                </tr>
                            </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Acciones rápidas -->
    <div class="row mb-4">
        <div class="col-12">
            <div class="card shadow-sm glass-effect">
                <div class="card-header">
                    <h5 class="card-title mb-0 fw-bold">Acciones Rápidas</h5>
                </div>
                <div class="card-body p-4">
                    <div class="row g-3">
                        <div class="col-md-4">
                            <a href="<c:url value='/guests?action=new'/>" class="btn btn-success w-100 d-flex align-items-center justify-content-center gap-2 py-3">
                                <i class="fas fa-user-plus"></i>
                                <span>Añadir Invitado</span>
                            </a>
                        </div>
                        <div class="col-md-4">
                            <a href="<c:url value='/guests?action=list'/>" class="btn btn-primary w-100 d-flex align-items-center justify-content-center gap-2 py-3">
                                <i class="fas fa-list"></i>
                                <span>Ver Lista</span>
                            </a>
                        </div>
                        <div class="col-md-4">
                            <button type="button" class="btn btn-info w-100 d-flex align-items-center justify-content-center gap-2 py-3" onclick="window.print()">
                                <i class="fas fa-print"></i>
                                <span>Imprimir Informe</span>
                            </button>
                        </div>
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
<script>
    // Gráfico de distribución de invitados
    document.addEventListener('DOMContentLoaded', function() {
        const ctx = document.getElementById('guestsChart').getContext('2d');
        const confirmedGuests = ${confirmedGuests};
        const pendingGuests = ${pendingGuests};

        Chart.defaults.color = '#d1d8e6';
        Chart.defaults.font.family = "'Inter', sans-serif";

        const guestsChart = new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: ['Confirmados', 'Pendientes'],
                datasets: [{
                    data: [confirmedGuests, pendingGuests],
                    backgroundColor: [
                        'rgba(12, 202, 152, 0.8)',
                        'rgba(255, 183, 0, 0.8)'
                    ],
                    borderColor: [
                        'rgba(12, 202, 152, 1)',
                        'rgba(255, 183, 0, 1)'
                    ],
                    borderWidth: 2,
                    hoverOffset: 6
                }]
            },
            options: {
                responsive: true,
                cutout: '70%',
                plugins: {
                    legend: {
                        position: 'bottom',
                        labels: {
                            padding: 20,
                            usePointStyle: true,
                            pointStyle: 'circle'
                        }
                    }
                }
            }
        });
    });
</script>
</body>
</html>

