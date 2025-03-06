package com.eventmanager.service;

import com.eventmanager.model.Guest;
import com.eventmanager.util.SupabaseConfig;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de servicio para gestionar las operaciones CRUD de invitados usando la API de Supabase.
 */
public class GuestService {
    private static final Logger logger = LoggerFactory.getLogger(GuestService.class);
    private static final String REST_PATH = "/rest/v1/guests";
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    /**
     * Obtiene todos los invitados de la base de datos.
     * @return Lista de invitados
     */
    public List<Guest> getAllGuests() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SupabaseConfig.getSupabaseUrl() + REST_PATH + "?order=name"))
                    .header("apikey", SupabaseConfig.getSupabaseKey())
                    .header("Authorization", "Bearer " + SupabaseConfig.getSupabaseKey())
                    .header("Content-Type", "application/json")
                    .header("Prefer", "return=representation")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<Guest> guests = objectMapper.readValue(response.body(), new TypeReference<List<Guest>>() {});
                logger.info("Obtenidos {} invitados de Supabase", guests.size());
                return guests;
            } else {
                logger.error("Error al obtener invitados: Código {}, Respuesta: {}", 
                        response.statusCode(), response.body());
                throw new RuntimeException("Error al obtener la lista de invitados: " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            logger.error("Error al obtener la lista de invitados", e);
            throw new RuntimeException("Error al obtener la lista de invitados", e);
        }
    }

    /**
     * Obtiene un invitado por su ID.
     * @param id ID del invitado
     * @return Invitado encontrado o null si no existe
     */
    public Guest getGuestById(int id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SupabaseConfig.getSupabaseUrl() + REST_PATH + "?id=eq." + id + "&limit=1"))
                    .header("apikey", SupabaseConfig.getSupabaseKey())
                    .header("Authorization", "Bearer " + SupabaseConfig.getSupabaseKey())
                    .header("Content-Type", "application/json")
                    .header("Prefer", "return=representation")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<Guest> guests = objectMapper.readValue(response.body(), new TypeReference<List<Guest>>() {});
                if (!guests.isEmpty()) {
                    logger.info("Obtenido invitado con ID: {}", id);
                    return guests.get(0);
                } else {
                    logger.warn("No se encontró ningún invitado con ID: {}", id);
                    return null;
                }
            } else {
                logger.error("Error al obtener invitado con ID {}: Código {}, Respuesta: {}", 
                        id, response.statusCode(), response.body());
                return null;
            }
        } catch (IOException | InterruptedException e) {
            logger.error("Error al obtener el invitado con ID: " + id, e);
            throw new RuntimeException("Error al obtener el invitado", e);
        }
    }

    /**
     * Añade un nuevo invitado a la base de datos.
     * @param guest Invitado a añadir
     * @return ID del invitado añadido
     */
    public int addGuest(Guest guest) {
        try {
            String jsonBody = objectMapper.writeValueAsString(guest);
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SupabaseConfig.getSupabaseUrl() + REST_PATH))
                    .header("apikey", SupabaseConfig.getSupabaseKey())
                    .header("Authorization", "Bearer " + SupabaseConfig.getSupabaseKey())
                    .header("Content-Type", "application/json")
                    .header("Prefer", "return=representation")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 201) {
                List<Guest> createdGuests = objectMapper.readValue(response.body(), new TypeReference<List<Guest>>() {});
                if (!createdGuests.isEmpty()) {
                    int newId = createdGuests.get(0).getId();
                    guest.setId(newId);
                    logger.info("Añadido nuevo invitado con ID: {}", newId);
                    return newId;
                }
            }
            
            logger.error("Error al añadir invitado: Código {}, Respuesta: {}", 
                    response.statusCode(), response.body());
            throw new RuntimeException("Error al añadir un nuevo invitado: " + response.body());
        } catch (IOException | InterruptedException e) {
            logger.error("Error al añadir un nuevo invitado", e);
            throw new RuntimeException("Error al añadir un nuevo invitado", e);
        }
    }

    /**
     * Actualiza los datos de un invitado existente.
     * @param guest Invitado con los datos actualizados
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean updateGuest(Guest guest) {
        try {
            String jsonBody = objectMapper.writeValueAsString(guest);
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SupabaseConfig.getSupabaseUrl() + REST_PATH + "?id=eq." + guest.getId()))
                    .header("apikey", SupabaseConfig.getSupabaseKey())
                    .header("Authorization", "Bearer " + SupabaseConfig.getSupabaseKey())
                    .header("Content-Type", "application/json")
                    .header("Prefer", "return=representation")
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<Guest> updatedGuests = objectMapper.readValue(response.body(), new TypeReference<List<Guest>>() {});
                boolean success = !updatedGuests.isEmpty();
                if (success) {
                    logger.info("Actualizado invitado con ID: {}", guest.getId());
                } else {
                    logger.warn("No se pudo actualizar el invitado con ID: {}", guest.getId());
                }
                return success;
            } else {
                logger.error("Error al actualizar invitado con ID {}: Código {}, Respuesta: {}", 
                        guest.getId(), response.statusCode(), response.body());
                return false;
            }
        } catch (IOException | InterruptedException e) {
            logger.error("Error al actualizar el invitado con ID: " + guest.getId(), e);
            throw new RuntimeException("Error al actualizar el invitado", e);
        }
    }

    /**
     * Elimina un invitado de la base de datos.
     * @param id ID del invitado a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean deleteGuest(int id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SupabaseConfig.getSupabaseUrl() + REST_PATH + "?id=eq." + id))
                    .header("apikey", SupabaseConfig.getSupabaseKey())
                    .header("Authorization", "Bearer " + SupabaseConfig.getSupabaseKey())
                    .DELETE()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            boolean success = response.statusCode() == 204;
            if (success) {
                logger.info("Eliminado invitado con ID: {}", id);
            } else {
                logger.error("Error al eliminar invitado con ID {}: Código {}, Respuesta: {}", 
                        id, response.statusCode(), response.body());
            }
            return success;
        } catch (IOException | InterruptedException e) {
            logger.error("Error al eliminar el invitado con ID: " + id, e);
            throw new RuntimeException("Error al eliminar el invitado", e);
        }
    }
    
    /**
     * Busca invitados por nombre.
     * @param searchTerm Término de búsqueda
     * @return Lista de invitados que coinciden con la búsqueda
     */
    public List<Guest> searchGuestsByName(String searchTerm) {
        try {
            String encodedSearchTerm = URLEncoder.encode("%" + searchTerm + "%", StandardCharsets.UTF_8.toString());
            
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SupabaseConfig.getSupabaseUrl() + REST_PATH + "?name=ilike." + encodedSearchTerm + "&order=name"))
                    .header("apikey", SupabaseConfig.getSupabaseKey())
                    .header("Authorization", "Bearer " + SupabaseConfig.getSupabaseKey())
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<Guest> guests = objectMapper.readValue(response.body(), new TypeReference<List<Guest>>() {});
                logger.info("Búsqueda por '{}' encontró {} invitados", searchTerm, guests.size());
                return guests;
            } else {
                logger.error("Error al buscar invitados por nombre '{}': Código {}, Respuesta: {}", 
                        searchTerm, response.statusCode(), response.body());
                return new ArrayList<>();
            }
        } catch (IOException | InterruptedException e) {
            logger.error("Error al buscar invitados por nombre: " + searchTerm, e);
            throw new RuntimeException("Error al buscar invitados", e);
        }
    }
    
    /**
     * Obtiene el número total de invitados.
     * @return Número total de invitados
     */
    public int getTotalGuestCount() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SupabaseConfig.getSupabaseUrl() + REST_PATH + "?select=id"))
                    .header("apikey", SupabaseConfig.getSupabaseKey())
                    .header("Authorization", "Bearer " + SupabaseConfig.getSupabaseKey())
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<Object> guests = objectMapper.readValue(response.body(), new TypeReference<List<Object>>() {});
                int count = guests.size();
                logger.info("Total de invitados: {}", count);
                return count;
            } else {
                logger.error("Error al obtener el número total de invitados: Código {}, Respuesta: {}", 
                        response.statusCode(), response.body());
                return 0;
            }
        } catch (IOException | InterruptedException e) {
            logger.error("Error al obtener el número total de invitados", e);
            throw new RuntimeException("Error al obtener el número total de invitados", e);
        }
    }
    
    /**
     * Obtiene el número de invitados confirmados.
     * @return Número de invitados confirmados
     */
    public int getConfirmedGuestCount() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SupabaseConfig.getSupabaseUrl() + REST_PATH + "?confirmed=eq.true&select=id"))
                    .header("apikey", SupabaseConfig.getSupabaseKey())
                    .header("Authorization", "Bearer " + SupabaseConfig.getSupabaseKey())
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                List<Object> guests = objectMapper.readValue(response.body(), new TypeReference<List<Object>>() {});
                int count = guests.size();
                logger.info("Total de invitados confirmados: {}", count);
                return count;
            } else {
                logger.error("Error al obtener el número de invitados confirmados: Código {}, Respuesta: {}", 
                        response.statusCode(), response.body());
                return 0;
            }
        } catch (IOException | InterruptedException e) {
            logger.error("Error al obtener el número de invitados confirmados", e);
            throw new RuntimeException("Error al obtener el número de invitados confirmados", e);
        }
    }
    
    /**
     * Verifica la conexión con la API de Supabase.
     * @return true si la conexión es exitosa, false en caso contrario
     */
    public boolean testConnection() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SupabaseConfig.getSupabaseUrl() + REST_PATH + "?limit=1"))
                    .header("apikey", SupabaseConfig.getSupabaseKey())
                    .header("Authorization", "Bearer " + SupabaseConfig.getSupabaseKey())
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            boolean success = response.statusCode() == 200;
            if (success) {
                logger.info("Conexión con Supabase exitosa");
            } else {
                logger.error("Error al conectar con Supabase: Código {}, Respuesta: {}", 
                        response.statusCode(), response.body());
            }
            return success;
        } catch (Exception e) {
            logger.error("Error al verificar la conexión con Supabase", e);
            return false;
        }
    }
}

