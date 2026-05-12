package com.thropic.talki.session.infrastructure.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de OpenAPI 3 para el session-service.
 *
 * Publica la especificación en /v3/api-docs y la UI en /swagger-ui.html.
 * Cumple el criterio "RESTful API con documentación de cada servicio" de la
 * rúbrica TP1 del curso SI657 (4 pts). El servicio implementa el bounded
 * context Practice Session (DDD) con separación CQRS — SessionCommandService
 * y SessionQueryService — sobre el agregado raíz Session.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI sessionServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Talki — Session Service API")
                        .description("API del bounded context Practice Session (DDD) del producto Talki. "
                                + "Aplica el patrón CQRS separando comandos (creación y finalización de "
                                + "sesiones) de queries (consulta de historial e información de sesión). "
                                + "El agregado Session transita entre los estados DRAFT → RECORDING → "
                                + "PROCESSING → COMPLETED / ANALYSIS_PENDING y publica el evento "
                                + "session.live.finalized al exchange talki.events de RabbitMQ.")
                        .version("v1")
                        .contact(new Contact()
                                .name("Thropic — Equipo Talki")
                                .url("https://github.com/upc-pre-202601-si657-7940-thropic"))
                        .license(new License()
                                .name("Académico — UPC SI657 2026-10")))
                .servers(List.of(
                        new Server().url("http://localhost:8082").description("Local (perfil dev)"),
                        new Server().url("https://api.talki.app").description("Producción (Railway)")
                ));
    }
}
