package com.thropic.talki.session.interfaces.rest.dto;

public record CreateSessionRequest(String title, String sessionType, Long userId) {}
