package com.thropic.talki.session.application.services;

import com.thropic.talki.session.domain.model.entities.AppUser;
import com.thropic.talki.session.domain.model.entities.Feedback;
import com.thropic.talki.session.domain.model.entities.Session;
import com.thropic.talki.session.infrastructure.persistence.jpa.repositories.FeedbackRepository;
import com.thropic.talki.session.infrastructure.persistence.jpa.repositories.SessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionQueryServiceImplTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private FeedbackRepository feedbackRepository;

    @InjectMocks
    private SessionQueryServiceImpl queryService;

    private Session sampleSession;

    @BeforeEach
    void setUp() {
        AppUser user = new AppUser("a@b.com", "Alejo", "ciclos_6_10");
        sampleSession = new Session("Sustentación TIN", "THESIS_DEFENSE", user);
    }

    @Test
    void findById_whenSessionExists_shouldReturnIt() {
        when(sessionRepository.findById(5L)).thenReturn(Optional.of(sampleSession));

        Optional<Session> result = queryService.findById(5L);

        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Sustentación TIN");
    }

    @Test
    void findById_whenSessionNotFound_shouldReturnEmpty() {
        when(sessionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThat(queryService.findById(99L)).isEmpty();
    }

    @Test
    void findByUserId_shouldDelegateToRepository() {
        when(sessionRepository.findByUserId(1L)).thenReturn(List.of(sampleSession));

        List<Session> result = queryService.findByUserId(1L);

        assertThat(result).hasSize(1);
        verify(sessionRepository).findByUserId(1L);
    }

    @Test
    void findFeedbacksBySessionId_shouldDelegateToRepository() {
        Feedback fb = new Feedback(sampleSession, "general", "Excelente apertura");
        when(feedbackRepository.findBySessionId(5L)).thenReturn(List.of(fb));

        List<Feedback> result = queryService.findFeedbacksBySessionId(5L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getContent()).isEqualTo("Excelente apertura");
    }
}
