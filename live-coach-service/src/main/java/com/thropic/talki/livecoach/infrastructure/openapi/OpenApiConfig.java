package com.thropic.talki.livecoach.infrastructure.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuración de OpenAPI 3 para el live-coach-service.
 *
 * Publica la especificación en /v3/api-docs y la UI en /swagger-ui.html.
 * Cumple el criterio "RESTful API con documentación de cada servicio" de la
 * rúbrica TP1 del curso SI657. El servicio orquesta la conversación con
 * Google Gemini Live aplicando el patrón Strategy (QuickPracticeStrategy,
 * InterviewStrategy, ThesisDefenseStrategy, ScenarioStrategy) y publica
 * session.live.finalized con transcript_gemini y métricas acústicas.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI liveCoachServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Talki — Live Coach Service API")
                        .description("API del orquestador de sesiones en tiempo real con Google Gemini Live. "
                                + "Aplica el patrón Strategy con cuatro implementaciones de SessionModeStrategy "
                                + "(QuickPractice, Interview, ThesisDefense, Scenario) instanciadas por una "
                                + "SessionModeStrategyFactory. Al cerrar la sesión calcula métricas acústicas "
                                + "en memoria (words_per_minute, silence_ratio, volume_rms_avg) y publica el "
                                + "evento session.live.finalized con transcript_gemini al exchange talki.events.")
                        .version("v1")
                        .contact(new Contact()
                                .name("Thropic — Equipo Talki")
                                .url("https://github.com/upc-pre-202601-si657-7940-thropic"))
                        .license(new License()
                                .name("Académico — UPC SI657 2026-10")))
                .servers(List.of(
                        new Server().url("http://localhost:8083").description("Local (perfil dev)"),
                        new Server().url("https://api.talki.app").description("Producción (Railway)")
                ));
    }
}
