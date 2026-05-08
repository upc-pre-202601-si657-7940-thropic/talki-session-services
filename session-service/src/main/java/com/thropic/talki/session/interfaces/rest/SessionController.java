package com.thropic.talki.session.interfaces.rest;

import com.thropic.talki.session.domain.model.entities.Session;
import com.thropic.talki.session.domain.model.entities.User;
import com.thropic.talki.session.infrastructure.persistence.jpa.repositories.SessionRepository;
import com.thropic.talki.session.infrastructure.persistence.jpa.repositories.UserRepository;
import com.thropic.talki.session.interfaces.rest.dto.CreateSessionRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/sessions")
public class SessionController {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    public SessionController(SessionRepository sessionRepository, UserRepository userRepository) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<Session> create(@RequestBody CreateSessionRequest req) {
        User user = userRepository.findById(req.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + req.userId()));
        Session session = new Session(req.title(), req.sessionType(), user);
        return ResponseEntity.status(HttpStatus.CREATED).body(sessionRepository.save(session));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Session> getById(@PathVariable Long id) {
        return sessionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Session>> getByUser(@RequestParam Long userId) {
        return ResponseEntity.ok(sessionRepository.findByUserId(userId));
    }

    @PostMapping("/{id}/finalize")
    public ResponseEntity<Session> finalize(@PathVariable Long id) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Session not found: " + id));
        session.finalizeSession();
        return ResponseEntity.accepted().body(sessionRepository.save(session));
    }
}
