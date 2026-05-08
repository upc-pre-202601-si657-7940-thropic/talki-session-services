package com.thropic.talki.livecoach.interfaces.rest;

import com.thropic.talki.livecoach.application.services.LiveCoachOrchestrator;
import com.thropic.talki.livecoach.domain.event.SessionLiveFinalizedEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/coach")
public class LiveCoachController {

    private final LiveCoachOrchestrator orchestrator;

    public LiveCoachController(LiveCoachOrchestrator orchestrator) {
        this.orchestrator = orchestrator;
    }

    @GetMapping("/modes")
    public ResponseEntity<Map<String, Object>> getModes() {
        return ResponseEntity.ok(Map.of(
                "modes", java.util.List.of("quick_practice", "interview", "thesis_defense", "scenario"),
                "description", "Modos disponibles para sesiones con Gemini Live"
        ));
    }

    @PostMapping("/{sessionId}/finalize")
    public ResponseEntity<SessionLiveFinalizedEvent> finalize(
            @PathVariable String sessionId,
            @RequestParam String userId,
            @RequestParam String mode,
            @RequestParam(required = false) String scenarioId,
            @RequestParam(defaultValue = "300") int durationSeconds,
            @RequestParam(defaultValue = "ciclos_6_10") String academicSegment) {

        String audioUri = "supabase://talki-audio-sessions/sessions/" + sessionId + "/audio.wav";

        SessionLiveFinalizedEvent event = orchestrator.finalizeAndPublish(
                sessionId, userId, mode, scenarioId,
                audioUri, durationSeconds, academicSegment
        );

        return ResponseEntity.accepted().body(event);
    }
}
