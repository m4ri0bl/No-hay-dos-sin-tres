<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty guest ? 'Añadir Nuevo Invitado' : 'Editar Invitado'}</title>
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
            <h1>
                <i class="${empty guest ? 'fas fa-user-plus' : 'fas fa-user-edit'} me-2" style="color: var(--electric-blue);"></i>
                ${empty guest ? 'Añadir Nuevo Invitado' : 'Editar Invitado'}
            </h1>
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

    <!-- Mensajes de error -->
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="fas fa-exclamation-circle me-2"></i>${errorMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>

    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card shadow glass-effect">
                <div class="card-body p-4">
                    <form action="<c:url value='/guests'/>" method="post" id="guestForm" novalidate>
                        <input type="hidden" name="action" value="${empty guest ? 'add' : 'update'}">
                        <c:if test="${not empty guest}">
                            <input type="hidden" name="id" value="${guest.id}">
                        </c:if>

                        <div class="mb-4">
                            <label for="name" class="form-label">Nombre <span class="text-danger">*</span></label>
                            <input type="text" class="form-control" id="name" name="name" value="${guest.name}" required>
                            <div class="invalid-feedback">
                                El nombre es obligatorio.
                            </div>
                        </div>

                        <div class="mb-4">
                            <label for="email" class="form-label">Email</label>
                            <input type="email" class="form-control" id="email" name="email" value="${guest.email}">
                            <div class="invalid-feedback">
                                Por favor, introduce un email válido.
                            </div>
                        </div>

                        <div class="mb-4">
                            <label for="phone" class="form-label">Teléfono</label>
                            <input type="tel" class="form-control" id="phone" name="phone" value="${guest.phone}"
                                   pattern="[0-9]{9}" title="Debe contener 9 dígitos">
                            <div class="invalid-feedback">
                                Por favor, introduce un número de teléfono válido (9 dígitos).
                            </div>
                            <div class="form-text" style="color: #d1d8e6;">Formato: 612345678</div>
                        </div>

                        <div class="mb-4 form-check">
                            <input type="checkbox" class="form-check-input" id="confirmed" name="confirmed" ${guest.confirmed ? 'checked' : ''}>
                            <label class="form-check-label" for="confirmed">Asistencia confirmada</label>
                        </div>

                        <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                            <button type="button" class="btn btn-secondary" onclick="window.history.back()">
                                <i class="fas fa-times me-2"></i>Cancelar
                            </button>
                            <button type="submit" class="btn btn-primary">
                                <i class="fas fa-save me-2"></i>${empty guest ? 'Guardar' : 'Actualizar'}
                            </button>
                        </div>
                    </form>
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
    // Validación del formulario
    document.getElementById('guestForm').addEventListener('submit', function(event) {
        const form = event.target;
        const nameField = document.getElementById('name');
        const emailField = document.getElementById('email');
        const phoneField = document.getElementById('phone');

        let isValid = true;

        // Validar nombre (obligatorio)
        if (!nameField.value.trim()) {
            nameField.classList.add('is-invalid');
            isValid = false;
        } else {
            nameField.classList.remove('is-invalid');
            nameField.classList.add('is-valid');
        }

        // Validar email (formato correcto si no está vacío)
        if (emailField.value.trim() && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(emailField.value.trim())) {
            emailField.classList.add('is-invalid');
            isValid = false;
        } else {
            emailField.classList.remove('is-invalid');
            if (emailField.value.trim()) {
                emailField.classList.add('is-valid');
            }
        }

        // Validar teléfono (formato correcto si no está vacío)
        if (phoneField.value.trim() && !/^[0-9]{9}$/.test(phoneField.value.trim())) {
            phoneField.classList.add('is-invalid');
            isValid = false;
        } else {
            phoneField.classList.remove('is-invalid');
            if (phoneField.value.trim()) {
                phoneField.classList.add('is-valid');
            }
        }

        if (!isValid) {
            event.preventDefault();
        }
    });
</script>
</body>
</html>

