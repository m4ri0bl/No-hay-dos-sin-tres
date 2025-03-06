package com.eventmanager.controller;

import com.eventmanager.model.Guest;
import com.eventmanager.service.GuestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet que maneja las peticiones relacionadas con los invitados.
 */
@WebServlet("/guests")
public class GuestServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(GuestServlet.class);
    private GuestService guestService;

    @Override
    public void init() throws ServletException {
        super.init();
        guestService = new GuestService();
        logger.info("GuestServlet inicializado");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "list":
                    listGuests(request, response);
                    break;
                case "new":
                    showNewForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteGuest(request, response);
                    break;
                case "search":
                    searchGuests(request, response);
                    break;
                case "dashboard":
                    showDashboard(request, response);
                    break;
                default:
                    listGuests(request, response);
                    break;
            }
        } catch (Exception e) {
            logger.error("Error en GuestServlet.doGet", e);
            request.setAttribute("errorMessage", "Ha ocurrido un error: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        try {
            switch (action) {
                case "add":
                    addGuest(request, response);
                    break;
                case "update":
                    updateGuest(request, response);
                    break;
                default:
                    listGuests(request, response);
                    break;
            }
        } catch (Exception e) {
            logger.error("Error en GuestServlet.doPost", e);
            request.setAttribute("errorMessage", "Ha ocurrido un error: " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }

    private void listGuests(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Guest> guests = guestService.getAllGuests();
        request.setAttribute("guests", guests);
        
        // Obtener estadísticas para mostrar en la página
        int totalGuests = guestService.getTotalGuestCount();
        int confirmedGuests = guestService.getConfirmedGuestCount();
        
        request.setAttribute("totalGuests", totalGuests);
        request.setAttribute("confirmedGuests", confirmedGuests);
        request.setAttribute("pendingGuests", totalGuests - confirmedGuests);
        
        logger.info("Mostrando lista de invitados. Total: {}", guests.size());
        request.getRequestDispatcher("/WEB-INF/views/guest-list.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Mostrando formulario para nuevo invitado");
        request.getRequestDispatcher("/WEB-INF/views/guest-form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Guest guest = guestService.getGuestById(id);
        
        if (guest == null) {
            logger.warn("Intento de editar un invitado inexistente con ID: {}", id);
            request.setAttribute("errorMessage", "El invitado solicitado no existe");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
            return;
        }
        
        request.setAttribute("guest", guest);
        logger.info("Mostrando formulario para editar invitado con ID: {}", id);
        request.getRequestDispatcher("/WEB-INF/views/guest-form.jsp").forward(request, response);
    }

    private void addGuest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        boolean confirmed = "on".equals(request.getParameter("confirmed"));

        if (name == null || name.trim().isEmpty()) {
            logger.warn("Intento de añadir un invitado sin nombre");
            request.setAttribute("errorMessage", "El nombre del invitado es obligatorio");
            request.getRequestDispatcher("/WEB-INF/views/guest-form.jsp").forward(request, response);
            return;
        }

        Guest guest = new Guest(name, email, phone, confirmed);
        int newId = guestService.addGuest(guest);
        
        if (newId > 0) {
            // Añadir mensaje de éxito
            request.getSession().setAttribute("successMessage", "Invitado añadido correctamente");
            logger.info("Invitado añadido correctamente con ID: {}", newId);
        } else {
            request.getSession().setAttribute("errorMessage", "No se pudo añadir el invitado");
            logger.warn("No se pudo añadir el invitado");
        }

        response.sendRedirect(request.getContextPath() + "/guests?action=list");
    }

    private void updateGuest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        boolean confirmed = "on".equals(request.getParameter("confirmed"));

        if (name == null || name.trim().isEmpty()) {
            logger.warn("Intento de actualizar un invitado sin nombre");
            request.setAttribute("errorMessage", "El nombre del invitado es obligatorio");
            request.setAttribute("guest", new Guest(id, "", email, phone, confirmed));
            request.getRequestDispatcher("/WEB-INF/views/guest-form.jsp").forward(request, response);
            return;
        }

        Guest guest = new Guest(id, name, email, phone, confirmed);
        boolean updated = guestService.updateGuest(guest);
        
        if (updated) {
            // Añadir mensaje de éxito
            request.getSession().setAttribute("successMessage", "Invitado actualizado correctamente");
            logger.info("Invitado actualizado correctamente con ID: {}", id);
        } else {
            request.getSession().setAttribute("errorMessage", "No se pudo actualizar el invitado");
            logger.warn("No se pudo actualizar el invitado con ID: {}", id);
        }

        response.sendRedirect(request.getContextPath() + "/guests?action=list");
    }

    private void deleteGuest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        boolean deleted = guestService.deleteGuest(id);
        
        if (deleted) {
            // Añadir mensaje de éxito
            request.getSession().setAttribute("successMessage", "Invitado eliminado correctamente");
            logger.info("Invitado eliminado correctamente con ID: {}", id);
        } else {
            request.getSession().setAttribute("errorMessage", "No se pudo eliminar el invitado");
            logger.warn("No se pudo eliminar el invitado con ID: {}", id);
        }
        
        response.sendRedirect(request.getContextPath() + "/guests?action=list");
    }

    private void searchGuests(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchTerm = request.getParameter("searchTerm");
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/guests?action=list");
            return;
        }
        
        List<Guest> guests = guestService.searchGuestsByName(searchTerm);
        request.setAttribute("guests", guests);
        request.setAttribute("searchTerm", searchTerm);
        
        // Obtener estadísticas para mostrar en la página
        int totalGuests = guestService.getTotalGuestCount();
        int confirmedGuests = guestService.getConfirmedGuestCount();
        
        request.setAttribute("totalGuests", totalGuests);
        request.setAttribute("confirmedGuests", confirmedGuests);
        request.setAttribute("pendingGuests", totalGuests - confirmedGuests);
        
        logger.info("Búsqueda por '{}' encontró {} invitados", searchTerm, guests.size());
        request.getRequestDispatcher("/WEB-INF/views/guest-list.jsp").forward(request, response);
    }
    
    private void showDashboard(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener estadísticas para el dashboard
        int totalGuests = guestService.getTotalGuestCount();
        int confirmedGuests = guestService.getConfirmedGuestCount();
        int pendingGuests = totalGuests - confirmedGuests;
        
        request.setAttribute("totalGuests", totalGuests);
        request.setAttribute("confirmedGuests", confirmedGuests);
        request.setAttribute("pendingGuests", pendingGuests);
        
        // Obtener los últimos 5 invitados añadidos
        List<Guest> recentGuests = guestService.getAllGuests();
        if (recentGuests.size() > 5) {
            recentGuests = recentGuests.subList(0, 5);
        }
        request.setAttribute("recentGuests", recentGuests);
        
        logger.info("Mostrando dashboard");
        request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
    }
}

