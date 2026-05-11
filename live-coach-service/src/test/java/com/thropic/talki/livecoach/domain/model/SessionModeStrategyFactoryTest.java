package com.thropic.talki.livecoach.domain.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class SessionModeStrategyFactoryTest {

    private SessionModeStrategyFactory factory;

    @BeforeEach
    void setUp() {
        factory = new SessionModeStrategyFactory();
    }

    @Test
    void create_whenModeIsQuickPractice_shouldReturnQuickPracticeStrategy() {
        SessionModeStrategy strategy = factory.create("quick_practice");

        assertThat(strategy).isInstanceOf(QuickPracticeStrategy.class);
        assertThat(strategy.getMode()).isEqualTo("quick_practice");
        assertThat(strategy.maxDurationMinutes()).isEqualTo(3);
    }

    @Test
    void create_whenModeIsInterview_shouldReturnInterviewStrategy() {
        SessionModeStrategy strategy = factory.create("interview");

        assertThat(strategy).isInstanceOf(InterviewStrategy.class);
        assertThat(strategy.getMode()).isEqualTo("interview");
    }

    @Test
    void create_whenModeIsThesisDefense_shouldReturnThesisDefenseStrategy() {
        SessionModeStrategy strategy = factory.create("thesis_defense");

        assertThat(strategy).isInstanceOf(ThesisDefenseStrategy.class);
        assertThat(strategy.getMode()).isEqualTo("thesis_defense");
    }

    @Test
    void create_whenModeIsScenario_shouldReturnScenarioStrategy() {
        SessionModeStrategy strategy = factory.create("scenario");

        assertThat(strategy).isInstanceOf(ScenarioStrategy.class);
        assertThat(strategy.getMode()).isEqualTo("scenario");
    }

    @Test
    void create_whenModeIsUnknown_shouldThrowIllegalArgumentException() {
        assertThatThrownBy(() -> factory.create("freestyle"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unknown session mode: freestyle");
    }

    @Test
    void create_whenModeIsNull_shouldThrowNullPointerException() {
        // switch sobre null lanza NPE en Java
        assertThatThrownBy(() -> factory.create(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void create_whenModeIsEmptyString_shouldThrowIllegalArgumentException() {
        assertThatThrownBy(() -> factory.create(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unknown session mode:");
    }

    @Test
    void buildSystemPrompt_quickPractice_shouldNotDependOnScenarioId() {
        SessionModeStrategy strategy = factory.create("quick_practice");

        String promptA = strategy.buildSystemPrompt("scn-1");
        String promptB = strategy.buildSystemPrompt(null);

        assertThat(promptA).isEqualTo(promptB);
        assertThat(promptA).contains("coach");
    }

    @Test
    void buildSystemPrompt_allStrategies_shouldReturnNonEmptyPrompt() {
        for (String mode : new String[]{"quick_practice", "interview", "thesis_defense", "scenario"}) {
            SessionModeStrategy strategy = factory.create(mode);
            String prompt = strategy.buildSystemPrompt("scn-test");
            assertThat(prompt).as("prompt for mode=%s", mode).isNotBlank();
        }
    }
}
