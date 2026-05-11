package com.thropic.talki.session.application.acl;

import com.thropic.talki.session.domain.model.entities.AppUser;
import com.thropic.talki.session.domain.model.valueobjects.SessionUserContext;
import com.thropic.talki.session.infrastructure.persistence.jpa.repositories.AppUserRepository;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Anti-Corruption Layer entre el bounded context Practice Session y el
 * upstream identity-service. Traduce los claims del JWT emitido por
 * identity-service y/o el AppUser persistido localmente al modelo de
 * dominio interno SessionUserContext, evitando que session-service
 * dependa directamente del shape del token o de la entidad upstream.
 */
@Component
public class SessionContextFacade {

    private final AppUserRepository appUserRepository;

    public SessionContextFacade(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    /**
     * Traduce los claims de un JWT validado por el API Gateway al
     * contexto local SessionUserContext. No valida el token (eso lo
     * hace el Gateway); solo lee los claims ya extraídos.
     */
    public SessionUserContext fromJwtClaims(Map<String, Object> claims) {
        if (claims == null || !claims.containsKey("sub")) {
            throw new IllegalArgumentException("JWT claims missing required 'sub' field");
        }
        Long userId = Long.valueOf(claims.get("sub").toString());
        String email = (String) claims.getOrDefault("email", "");
        String username = (String) claims.getOrDefault("username", "");
        String academicSegment = (String) claims.getOrDefault("academic_segment", "ciclos_6_10");
        return new SessionUserContext(userId, email, username, academicSegment);
    }

    /**
     * Resuelve el SessionUserContext desde el AppUser ya proyectado
     * localmente en el schema sessions. Se usa cuando ya se tiene el
     * userId y se necesita enriquecer con datos del modelo local.
     */
    public SessionUserContext fromLocalUserId(Long userId) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "User not found in session-service local projection: " + userId));
        return new SessionUserContext(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getAcademicSegment()
        );
    }
}
