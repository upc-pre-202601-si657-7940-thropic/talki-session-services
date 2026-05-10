package com.thropic.talki.livecoach.domain.event;

import java.time.Instant;
import java.util.UUID;

public class SessionLiveFinalizedEvent {
    private String eventId;
    private String eventType = "session.live.finalized";
    private Instant occurredAt;
    private String traceId;
    private String version = "1.0";
    private String sessionId;
    private String userId;
    private String mode;
    private String scenarioId;
    private String transcriptGemini;
    private int wordsPerMinute;
    private double silenceRatio;
    private double volumeRmsAvg;
    private int durationSeconds;
    private String academicSegment;

    public SessionLiveFinalizedEvent() {}

    public SessionLiveFinalizedEvent(String sessionId, String userId, String mode,
                                      String scenarioId, String transcriptGemini,
                                      int wordsPerMinute, double silenceRatio,
                                      double volumeRmsAvg, int durationSeconds,
                                      String academicSegment) {
        this.eventId = UUID.randomUUID().toString();
        this.occurredAt = Instant.now();
        this.traceId = UUID.randomUUID().toString();
        this.sessionId = sessionId;
        this.userId = userId;
        this.mode = mode;
        this.scenarioId = scenarioId;
        this.transcriptGemini = transcriptGemini;
        this.wordsPerMinute = wordsPerMinute;
        this.silenceRatio = silenceRatio;
        this.volumeRmsAvg = volumeRmsAvg;
        this.durationSeconds = durationSeconds;
        this.academicSegment = academicSegment;
    }

    public String getEventId() { return eventId; }
    public void setEventId(String e) { this.eventId = e; }
    public String getEventType() { return eventType; }
    public void setEventType(String e) { this.eventType = e; }
    public Instant getOccurredAt() { return occurredAt; }
    public void setOccurredAt(Instant o) { this.occurredAt = o; }
    public String getTraceId() { return traceId; }
    public void setTraceId(String t) { this.traceId = t; }
    public String getVersion() { return version; }
    public void setVersion(String v) { this.version = v; }
    public String getSessionId() { return sessionId; }
    public void setSessionId(String s) { this.sessionId = s; }
    public String getUserId() { return userId; }
    public void setUserId(String u) { this.userId = u; }
    public String getMode() { return mode; }
    public void setMode(String m) { this.mode = m; }
    public String getScenarioId() { return scenarioId; }
    public void setScenarioId(String s) { this.scenarioId = s; }
    public String getTranscriptGemini() { return transcriptGemini; }
    public void setTranscriptGemini(String t) { this.transcriptGemini = t; }
    public int getWordsPerMinute() { return wordsPerMinute; }
    public void setWordsPerMinute(int w) { this.wordsPerMinute = w; }
    public double getSilenceRatio() { return silenceRatio; }
    public void setSilenceRatio(double s) { this.silenceRatio = s; }
    public double getVolumeRmsAvg() { return volumeRmsAvg; }
    public void setVolumeRmsAvg(double v) { this.volumeRmsAvg = v; }
    public int getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(int d) { this.durationSeconds = d; }
    public String getAcademicSegment() { return academicSegment; }
    public void setAcademicSegment(String a) { this.academicSegment = a; }
}
