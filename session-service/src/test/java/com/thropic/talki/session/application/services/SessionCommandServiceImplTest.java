package com.thropic.talki.session.application.services;

import com.thropic.talki.session.application.acl.SessionContextFacade;
import com.thropic.talki.session.domain.model.entities.AppUser;
import com.thropic.talki.session.domain.model.entities.Feedback;
import com.thropic.talki.session.domain.model.entities.Session;
import com.thropic.talki.session.domain.model.valueobjects.SessionStatus;
import com.thropic.talki.session.domain.model.valueobjects.SessionUserContext;
import com.thropic.talki.session.infrastructure.persistence.jpa.repositories.AppUserRepository;
import com.thropic.talki.session.infrastructure.persistence.jpa.repositories.FeedbackRepository;
import com.thropic.talki.session.infrastructure.persistence.jpa.repositories.SessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionCommandServiceImplTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private FeedbackRepository feedbackRepository;

    @Mock
    private SessionContextFacade sessionContextFacade;

    @InjectMocks
    private SessionCommandServiceImpl commandService;

    private AppUser sampleUser;
    private Session sampleSession;
    private SessionUserContext sampleContext;

    @BeforeEach
    void setUp() {
        sampleUser = new AppUser("manuel@upc.edu.pe", "ManuelTumi", "ciclos_6_10");
        sampleSession = new Session("Mi pitch", "PITCH", sampleUser);
        sampleContext = new SessionUserContext(1L, "manuel@upc.edu.pe", "ManuelTumi", "ciclos_6_10");
    }

    @Test
    void create_whenUserExists_shouldPersistSessionInDraftStatus() {
        when(sessionContextFacade.fromLocalUserId(1L)).thenReturn(sampleContext);
        when(appUserRepository.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(sessionRepository.save(any(Session.class))).thenAnswer(inv -> inv.getArgument(0));

        Session result = commandService.create(1L, "Mi pitch", "PITCH");

        assertThat(result.getTitle()).isEqualTo("Mi pitch");
        assertThat(result.getSessionType()).isEqualTo("PITCH");
        assertThat(result.getStatus()).isEqualTo(SessionStatus.DRAFT);
        verify(sessionRepository).save(any(Session.class));
    }

    @Test
    void create_whenUserDisappearsMidTransaction_shouldThrow() {
        when(sessionContextFacade.fromLocalUserId(99L)).thenReturn(
                new SessionUserContext(99L, "x@y.com", "x", "ciclos_1_5"));
        when(appUserRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commandService.create(99L, "T", "PITCH"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("AppUser disappeared");

        verify(sessionRepository, never()).save(any());
    }

    @Test
    void finalize_whenSessionIsRecording_shouldTransitionToProcessing() {
        sampleSession.start();
        when(sessionRepository.findById(5L)).thenReturn(Optional.of(sampleSession));
        when(sessionRepository.save(any(Session.class))).thenAnswer(inv -> inv.getArgument(0));

        Session result = commandService.finalize(5L);

        assertThat(result.getStatus()).isEqualTo(SessionStatus.PROCESSING);
        assertThat(result.getFinalizedAt()).isNotNull();
    }

    @Test
    void finalize_whenSessionNotFound_shouldThrow() {
        when(sessionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commandService.finalize(99L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Session not found");
    }

    @Test
    void finalize_whenSessionIsDraft_shouldThrowFromDomainInvariant() {
        when(sessionRepository.findById(5L)).thenReturn(Optional.of(sampleSession));

        assertThatThrownBy(() -> commandService.finalize(5L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("must be RECORDING");

        verify(sessionRepository, never()).save(any());
    }

    @Test
    void addFeedback_whenSessionExists_shouldPersistFeedback() {
        when(sessionRepository.findById(5L)).thenReturn(Optional.of(sampleSession));
        when(feedbackRepository.save(any(Feedback.class))).thenAnswer(inv -> inv.getArgument(0));

        Feedback result = commandService.addFeedback(5L, "general", "Buena fluidez en la introducción");

        assertThat(result.getFeedbackType()).isEqualTo("general");
        assertThat(result.getContent()).isEqualTo("Buena fluidez en la introducción");
        assertThat(result.getSession()).isEqualTo(sampleSession);
        verify(feedbackRepository).save(any(Feedback.class));
    }

    @Test
    void addFeedback_whenSessionNotFound_shouldThrow() {
        when(sessionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commandService.addFeedback(99L, "general", "..."))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Session not found");
    }
}
