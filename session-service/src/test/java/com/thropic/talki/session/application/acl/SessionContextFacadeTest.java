package com.thropic.talki.session.application.acl;

import com.thropic.talki.session.domain.model.entities.AppUser;
import com.thropic.talki.session.domain.model.valueobjects.SessionUserContext;
import com.thropic.talki.session.infrastructure.persistence.jpa.repositories.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SessionContextFacadeTest {

    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private SessionContextFacade facade;

    @Test
    void fromJwtClaims_whenClaimsAreComplete_shouldBuildContext() {
        Map<String, Object> claims = Map.of(
                "sub", "42",
                "email", "manuel@upc.edu.pe",
                "username", "ManuelTumi",
                "academic_segment", "ciclos_6_10"
        );

        SessionUserContext result = facade.fromJwtClaims(claims);

        assertThat(result.userId()).isEqualTo(42L);
        assertThat(result.email()).isEqualTo("manuel@upc.edu.pe");
        assertThat(result.username()).isEqualTo("ManuelTumi");
        assertThat(result.academicSegment()).isEqualTo("ciclos_6_10");
    }

    @Test
    void fromJwtClaims_whenSubMissing_shouldThrow() {
        Map<String, Object> claims = Map.of("email", "x@y.com");

        assertThatThrownBy(() -> facade.fromJwtClaims(claims))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("'sub'");
    }

    @Test
    void fromJwtClaims_whenClaimsNull_shouldThrow() {
        assertThatThrownBy(() -> facade.fromJwtClaims(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void fromJwtClaims_whenOptionalClaimsMissing_shouldUseDefaults() {
        Map<String, Object> claims = Map.of("sub", "7");

        SessionUserContext result = facade.fromJwtClaims(claims);

        assertThat(result.userId()).isEqualTo(7L);
        assertThat(result.email()).isEmpty();
        assertThat(result.username()).isEmpty();
        assertThat(result.academicSegment()).isEqualTo("ciclos_6_10");
    }

    @Test
    void fromLocalUserId_whenUserExists_shouldBuildContextFromProjection() {
        AppUser user = new AppUser("gael@upc.edu.pe", "GaelRS", "ciclos_1_5");
        when(appUserRepository.findById(3L)).thenReturn(Optional.of(user));

        SessionUserContext result = facade.fromLocalUserId(3L);

        assertThat(result.email()).isEqualTo("gael@upc.edu.pe");
        assertThat(result.username()).isEqualTo("GaelRS");
        assertThat(result.academicSegment()).isEqualTo("ciclos_1_5");
    }

    @Test
    void fromLocalUserId_whenUserMissing_shouldThrow() {
        when(appUserRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> facade.fromLocalUserId(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("not found in session-service");
    }
}
