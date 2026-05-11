package com.thropic.talki.session.domain.model.valueobjects;

/**
 * Modelo local del contexto de usuario dentro del bounded context Practice Session.
 * Producto del Anti-Corruption Layer (SessionContextFacade) que traduce los
 * claims del JWT emitido por identity-service (upstream) al lenguaje del
 * dominio de session-service, evitando que el modelo upstream contamine
 * este BC.
 */
public record SessionUserContext(
        Long userId,
        String email,
        String username,
        String academicSegment
) {}
