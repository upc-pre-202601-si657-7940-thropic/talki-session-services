package com.thropic.talki.session.domain.model.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "feedbacks")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    @Column(name = "feedback_type", nullable = false)
    private String feedbackType;

    @Column(nullable = false, length = 2000)
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected Feedback() {}

    public Feedback(Session session, String feedbackType, String content) {
        this.session = session;
        this.feedbackType = feedbackType;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Session getSession() { return session; }
    public String getFeedbackType() { return feedbackType; }
    public String getContent() { return content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
