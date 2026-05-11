package com.thropic.talki.session.domain.model.entities;

import com.thropic.talki.session.domain.model.valueobjects.SessionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class SessionTest {

    private AppUser user;

    @BeforeEach
    void setUp() {
        user = new AppUser("alejo@upc.edu.pe", "AlejandroOA", "ciclos_6_10");
    }

    @Test
    void constructor_shouldInitializeWithDraftStatusAndCreatedAt() {
        Session session = new Session("Mi pitch de tesis", "PITCH", user);

        assertThat(session.getTitle()).isEqualTo("Mi pitch de tesis");
        assertThat(session.getSessionType()).isEqualTo("PITCH");
        assertThat(session.getStatus()).isEqualTo(SessionStatus.DRAFT);
        assertThat(session.getUser()).isEqualTo(user);
        assertThat(session.getCreatedAt()).isNotNull();
        assertThat(session.getFinalizedAt()).isNull();
    }

    @Test
    void start_whenStatusIsDraft_shouldTransitionToRecording() {
        Session session = new Session("T", "PITCH", user);

        session.start();

        assertThat(session.getStatus()).isEqualTo(SessionStatus.RECORDING);
    }

    @Test
    void start_whenStatusIsAlreadyRecording_shouldThrow() {
        Session session = new Session("T", "PITCH", user);
        session.start();

        assertThatThrownBy(session::start)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("must be DRAFT");
    }

    @Test
    void start_whenStatusIsProcessing_shouldThrow() {
        Session session = new Session("T", "PITCH", user);
        session.start();
        session.finalizeSession();

        assertThatThrownBy(session::start)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("must be DRAFT");
    }

    @Test
    void finalizeSession_whenStatusIsRecording_shouldTransitionToProcessingAndSetFinalizedAt() {
        Session session = new Session("T", "PITCH", user);
        session.start();

        session.finalizeSession();

        assertThat(session.getStatus()).isEqualTo(SessionStatus.PROCESSING);
        assertThat(session.getFinalizedAt()).isNotNull();
    }

    @Test
    void finalizeSession_whenStatusIsDraft_shouldThrow() {
        Session session = new Session("T", "PITCH", user);

        assertThatThrownBy(session::finalizeSession)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("must be RECORDING");

        // La transición fallida no debe haber marcado finalizedAt.
        assertThat(session.getFinalizedAt()).isNull();
    }

    @Test
    void finalizeSession_whenAlreadyProcessing_shouldThrow() {
        Session session = new Session("T", "PITCH", user);
        session.start();
        session.finalizeSession();

        assertThatThrownBy(session::finalizeSession)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void markAsCompleted_fromProcessing_shouldTransitionToCompleted() {
        Session session = new Session("T", "PITCH", user);
        session.start();
        session.finalizeSession();

        session.markAsCompleted();

        assertThat(session.getStatus()).isEqualTo(SessionStatus.COMPLETED);
    }

    @Test
    void markAsAnalysisPending_fromProcessing_shouldTransitionToAnalysisPending() {
        Session session = new Session("T", "PITCH", user);
        session.start();
        session.finalizeSession();

        session.markAsAnalysisPending();

        assertThat(session.getStatus()).isEqualTo(SessionStatus.ANALYSIS_PENDING);
    }

    @Test
    void fullHappyPath_draftToRecordingToProcessingToCompleted_shouldFlowSmoothly() {
        Session session = new Session("End-to-end", "INTERVIEW", user);

        assertThat(session.getStatus()).isEqualTo(SessionStatus.DRAFT);
        session.start();
        assertThat(session.getStatus()).isEqualTo(SessionStatus.RECORDING);
        session.finalizeSession();
        assertThat(session.getStatus()).isEqualTo(SessionStatus.PROCESSING);
        session.markAsCompleted();
        assertThat(session.getStatus()).isEqualTo(SessionStatus.COMPLETED);

        assertThat(session.getFinalizedAt()).isAfter(session.getCreatedAt().minusNanos(1));
    }
}
