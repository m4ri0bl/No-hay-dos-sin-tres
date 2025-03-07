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
            .connectTimeout(Duration.ofSeconds(30))
            .build();

    /**
     * Obtiene todos los invitados de la base de datos.
     * @return Lista de invitados
     */
    public List<Guest> getAllGuests() {
        try {
            // Verificar configuración
            if (!SupabaseConfig.isConfigValid()) {
                logger.error("Configuración de Supabase no válida");
                throw new RuntimeException("Configuración de Supabase no válida. Verifica las variables de entorno.");
            }

            String url = SupabaseConfig.getSupabaseUrl() + REST_PATH + "?order=id.desc";
            logger.debug("URL de petición: {}", url);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
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
                throw new RuntimeException("Error al obtener la lista de invitados. Código: " + response.statusCode());
            }
        } catch (IOException e) {
            logger.error("Error al obtener la lista de invitados", e);
            throw new RuntimeException("Error al obtener la lista de invitados: " + e.getMessage(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Operación interrumpida al obtener la lista de invitados", e);
            throw new RuntimeException("Operación interrumpida al obtener la lista de invitados", e);
        } catch (Exception e) {
            logger.error("Error inesperado al obtener la lista de invitados", e);
            throw new RuntimeException("Error inesperado al obtener la lista de invitados: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene un invitado por su ID.
     * @param id ID del invitado
     * @return Invitado encontrado o null si no existe
     */
    public Guest getGuestById(int id) {
        try {
            // Verificar configuración
            if (!SupabaseConfig.isConfigValid()) {
                logger.error("Configuración de Supabase no válida");
                throw new RuntimeException("Configuración de Supabase no válida. Verifica las variables de entorno.");
            }

            String url = SupabaseConfig.getSupabaseUrl() + REST_PATH + "?id=eq." + id + "&limit=1";
            logger.debug("URL de petición: {}", url);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
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
        } catch (IOException e) {
            logger.error("Error al obtener el invitado con ID: " + id, e);
            throw new RuntimeException("Error al obtener el invitado: " + e.getMessage(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Operación interrumpida al obtener el invitado con ID: " + id, e);
            throw new RuntimeException("Operación interrumpida al obtener el invitado", e);
        } catch (Exception e) {
            logger.error("Error inesperado al obtener el invitado con ID: " + id, e);
            throw new RuntimeException("Error inesperado al obtener el invitado: " + e.getMessage(), e);
        }
    }

    /**
     * Añade un nuevo invitado a la base de datos.
     * @param guest Invitado a añadir
     * @return ID del invitado añadido
     */
    public int addGuest(Guest guest) {
        try {
            // Verificar configuración
            if (!SupabaseConfig.isConfigValid()) {
                logger.error("Configuración de Supabase no válida");
                throw new RuntimeException("Configuración de Supabase no válida. Verifica las variables de entorno.");
            }

            String url = SupabaseConfig.getSupabaseUrl() + REST_PATH;
            logger.debug("URL de petición: {}", url);

            // Eliminar campos que no deben enviarse en la creación
            guest.setId(0); // El ID será asignado por la base de datos

            String jsonBody = objectMapper.writeValueAsString(guest);
            logger.debug("Cuerpo de la petición: {}", jsonBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("apikey", SupabaseConfig.getSupabaseKey())
                    .header("Authorization", "Bearer " + SupabaseConfig.getSupabaseKey())
                    .header("Content-Type", "application/json")
                    .header("Prefer", "return=representation")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            logger.debug("Respuesta: Código {}, Cuerpo: {}", response.statusCode(), response.body());

            if (response.statusCode() == 201) {
                List<Guest> createdGuests = objectMapper.readValue(response.body(), new TypeReference<List<Guest>>() {});
                if (!createdGuests.isEmpty()) {
                    int newId = createdGuests.get(0).getId();
                    guest.setId(newId);
                    logger.info("Añadido nuevo invitado con ID: {}", newId);
                    return newId;
                } else {
                    logger.error("Respuesta vacía al añadir invitado");
                    throw new RuntimeException("Error al añadir un nuevo invitado: respuesta vacía");
                }
            } else {
                logger.error("Error al añadir invitado: Código {}, Respuesta: {}",
                        response.statusCode(), response.body());
                throw new RuntimeException("Error al añadir un nuevo invitado. Código: " + response.statusCode() + ", Respuesta: " + response.body());
            }
        } catch (IOException e) {
            logger.error("Error al añadir un nuevo invitado", e);
            throw new RuntimeException("Error al añadir un nuevo invitado: " + e.getMessage(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Operación interrumpida al añadir un nuevo invitado", e);
            throw new RuntimeException("Operación interrumpida al añadir un nuevo invitado", e);
        } catch (Exception e) {
            logger.error("Error inesperado al añadir un nuevo invitado", e);
            throw new RuntimeException("Error inesperado al añadir un nuevo invitado: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza los datos de un invitado existente.
     * @param guest Invitado con los datos actualizados
     * @return true si se actualizó correctamente, false en caso contrario
     */
    public boolean updateGuest(Guest guest) {
        try {
            // Verificar configuración
            if (!SupabaseConfig.isConfigValid()) {
                logger.error("Configuración de Supabase no válida");
                throw new RuntimeException("Configuración de Supabase no válida. Verifica las variables de entorno.");
            }

            String url = SupabaseConfig.getSupabaseUrl() + REST_PATH + "?id=eq." + guest.getId();
            logger.debug("URL de petición: {}", url);

            String jsonBody = objectMapper.writeValueAsString(guest);
            logger.debug("Cuerpo de la petición: {}", jsonBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("apikey", SupabaseConfig.getSupabaseKey())
                    .header("Authorization", "Bearer " + SupabaseConfig.getSupabaseKey())
                    .header("Content-Type", "application/json")
                    .header("Prefer", "return=representation")
                    .method("PATCH", HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            logger.debug("Respuesta: Código {}, Cuerpo: {}", response.statusCode(), response.body());

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
        } catch (IOException e) {
            logger.error("Error al actualizar el invitado con ID: " + guest.getId(), e);
            throw new RuntimeException("Error al actualizar el invitado: " + e.getMessage(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Operación interrumpida al actualizar el invitado con ID: " + guest.getId(), e);
            throw new RuntimeException("Operación interrumpida al actualizar el invitado", e);
        } catch (Exception e) {
            logger.error("Error inesperado al actualizar el invitado con ID: " + guest.getId(), e);
            throw new RuntimeException("Error inesperado al actualizar el invitado: " + e.getMessage(), e);
        }
    }

    /**
     * Elimina un invitado de la base de datos.
     * @param id ID del invitado a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    public boolean deleteGuest(int id) {
        try {
            // Verificar configuración
            if (!SupabaseConfig.isConfigValid()) {
                logger.error("Configuración de Supabase no válida");
                throw new RuntimeException("Configuración de Supabase no válida. Verifica las variables de entorno.");
            }

            String url = SupabaseConfig.getSupabaseUrl() + REST_PATH + "?id=eq." + id;
            logger.debug("URL de petición: {}", url);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("apikey", SupabaseConfig.getSupabaseKey())
                    .header("Authorization", "Bearer " + SupabaseConfig.getSupabaseKey())
                    .DELETE()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            logger.debug("Respuesta: Código {}, Cuerpo: {}", response.statusCode(), response.body());

            boolean success = response.statusCode() == 204;
            if (success) {
                logger.info("Eliminado invitado con ID: {}", id);
            } else {
                logger.error("Error al eliminar invitado con ID {}: Código {}, Respuesta: {}",
                        id, response.statusCode(), response.body());
            }
            return success;
        } catch (IOException e) {
            logger.error("Error al eliminar el invitado con ID: " + id, e);
            throw new RuntimeException("Error al eliminar el invitado: " + e.getMessage(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Operación interrumpida al eliminar el invitado con ID: " + id, e);
            throw new RuntimeException("Operación interrumpida al eliminar el invitado", e);
        } catch (Exception e) {
            logger.error("Error inesperado al eliminar el invitado con ID: " + id, e);
            throw new RuntimeException("Error inesperado al eliminar el invitado: " + e.getMessage(), e);
        }
    }

    /**
     * Busca invitados por nombre.
     * @param searchTerm Término de búsqueda
     * @return Lista de invitados que coinciden con la búsqueda
     */
    public List<Guest> searchGuestsByName(String searchTerm) {
        try {
            // Verificar configuración
            if (!SupabaseConfig.isConfigValid()) {
                logger.error("Configuración de Supabase no válida");
                throw new RuntimeException("Configuración de Supabase no válida. Verifica las variables de entorno.");
            }

            String encodedSearchTerm = URLEncoder.encode("%" + searchTerm + "%", StandardCharsets.UTF_8.toString());
            String url = SupabaseConfig.getSupabaseUrl() + REST_PATH + "?name=ilike." + encodedSearchTerm + "&order=name";
            logger.debug("URL de petición: {}", url);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("apikey", SupabaseConfig.getSupabaseKey())
                    .header("Authorization", "Bearer " + SupabaseConfig.getSupabaseKey())
                    .header("Content-Type", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            logger.debug("Respuesta: Código {}, Cuerpo: {}", response.statusCode(), response.body());

            if (response.statusCode() == 200) {
                List<Guest> guests = objectMapper.readValue(response.body(), new TypeReference<List<Guest>>() {});
                logger.info("Búsqueda por '{}' encontró {} invitados", searchTerm, guests.size());
                return guests;
            } else {
                logger.error("Error al buscar invitados por nombre '{}': Código {}, Respuesta: {}",
                        searchTerm, response.statusCode(), response.body());
                return new ArrayList<>();
            }
        } catch (IOException e) {
            logger.error("Error al buscar invitados por nombre: " + searchTerm, e);
            throw new RuntimeException("Error al buscar invitados: " + e.getMessage(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Operación interrumpida al buscar invitados por nombre: " + searchTerm, e);
            throw new RuntimeException("Operación interrumpida al buscar invitados", e);
        } catch (Exception e) {
            logger.error("Error inesperado al buscar invitados por nombre: " + searchTerm, e);
            throw new RuntimeException("Error inesperado al buscar invitados: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene el número total de invitados.
     * @return Número total de invitados
     */
    public int getTotalGuestCount() {
        try {
            // Verificar configuración
            if (!SupabaseConfig.isConfigValid()) {
                logger.error("Configuración de Supabase no válida");
                throw new RuntimeException("Configuración de Supabase no válida. Verifica las variables de entorno.");
            }

            String url = SupabaseConfig.getSupabaseUrl() + REST_PATH + "?select=id";
            logger.debug("URL de petición: {}", url);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("apikey", SupabaseConfig.getSupabaseKey())
                    .header("Authorization", "Bearer " + SupabaseConfig.getSupabaseKey())
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            logger.debug("Respuesta: Código {}", response.statusCode());

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
        } catch (IOException e) {
            logger.error("Error al obtener el número total de invitados", e);
            throw new RuntimeException("Error al obtener el número total de invitados: " + e.getMessage(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Operación interrumpida al obtener el número total de invitados", e);
            throw new RuntimeException("Operación interrumpida al obtener el número total de invitados", e);
        } catch (Exception e) {
            logger.error("Error inesperado al obtener el número total de invitados", e);
            throw new RuntimeException("Error inesperado al obtener el número total de invitados: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene el número de invitados confirmados.
     * @return Número de invitados confirmados
     */
    public int getConfirmedGuestCount() {
        try {
            // Verificar configuración
            if (!SupabaseConfig.isConfigValid()) {
                logger.error("Configuración de Supabase no válida");
                throw new RuntimeException("Configuración de Supabase no válida. Verifica las variables de entorno.");
            }

            String url = SupabaseConfig.getSupabaseUrl() + REST_PATH + "?confirmed=eq.true&select=id";
            logger.debug("URL de petición: {}", url);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("apikey", SupabaseConfig.getSupabaseKey())
                    .header("Authorization", "Bearer " + SupabaseConfig.getSupabaseKey())
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            logger.debug("Respuesta: Código {}", response.statusCode());

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
        } catch (IOException e) {
            logger.error("Error al obtener el número de invitados confirmados", e);
            throw new RuntimeException("Error al obtener el número de invitados confirmados: " + e.getMessage(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Operación interrumpida al obtener el número de invitados confirmados", e);
            throw new RuntimeException("Operación interrumpida al obtener el número de invitados confirmados", e);
        } catch (Exception e) {
            logger.error("Error inesperado al obtener el número de invitados confirmados", e);
            throw new RuntimeException("Error inesperado al obtener el número de invitados confirmados: " + e.getMessage(), e);
        }
    }

    /**
     * Verifica la conexión con la API de Supabase.
     * @return true si la conexión es exitosa, false en caso contrario
     */
    public boolean testConnection() {
        try {
            // Verificar configuración
            if (!SupabaseConfig.isConfigValid()) {
                logger.error("Configuración de Supabase no válida");
                return false;
            }

            String url = SupabaseConfig.getSupabaseUrl() + REST_PATH + "?limit=1";
            logger.debug("URL de prueba de conexión: {}", url);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("apikey", SupabaseConfig.getSupabaseKey())
                    .header("Authorization", "Bearer " + SupabaseConfig.getSupabaseKey())
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            logger.debug("Respuesta de prueba: Código {}", response.statusCode());

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

