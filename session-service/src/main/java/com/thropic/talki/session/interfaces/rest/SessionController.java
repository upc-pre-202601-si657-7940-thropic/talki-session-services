package com.thropic.talki.session.interfaces.rest;

import com.thropic.talki.session.application.services.SessionCommandService;
import com.thropic.talki.session.application.services.SessionQueryService;
import com.thropic.talki.session.domain.model.entities.Feedback;
import com.thropic.talki.session.domain.model.entities.Session;
import com.thropic.talki.session.interfaces.rest.dto.CreateSessionRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/sessions")
public class SessionController {

    private final SessionCommandService commandService;
    private final SessionQueryService queryService;

    public SessionController(SessionCommandService commandService,
                              SessionQueryService queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @PostMapping
    public ResponseEntity<Session> create(@RequestBody CreateSessionRequest req) {
        Session session = commandService.create(req.userId(), req.title(), req.sessionType());
        return ResponseEntity.status(HttpStatus.CREATED).body(session);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Session> getById(@PathVariable Long id) {
        return queryService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Session>> getByUser(@RequestParam Long userId) {
        return ResponseEntity.ok(queryService.findByUserId(userId));
    }

    @PostMapping("/{id}/finalize")
    public ResponseEntity<Session> finalize(@PathVariable Long id) {
        return ResponseEntity.accepted().body(commandService.finalize(id));
    }

    @PostMapping("/{id}/feedbacks")
    public ResponseEntity<Feedback> addFeedback(@PathVariable Long id,
                                                 @RequestBody Map<String, String> body) {
        Feedback feedback = commandService.addFeedback(
                id,
                body.getOrDefault("feedbackType", "general"),
                body.getOrDefault("content", "")
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(feedback);
    }

    @GetMapping("/{id}/feedbacks")
    public ResponseEntity<List<Feedback>> listFeedbacks(@PathVariable Long id) {
        return ResponseEntity.ok(queryService.findFeedbacksBySessionId(id));
    }
}
