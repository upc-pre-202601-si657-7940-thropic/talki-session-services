package com.thropic.talki.livecoach.infrastructure.messaging.producer;

import com.thropic.talki.livecoach.domain.event.SessionLiveFinalizedEvent;
import com.thropic.talki.livecoach.infrastructure.messaging.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class SessionFinalizedPublisher {

    private static final Logger log = LoggerFactory.getLogger(SessionFinalizedPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public SessionFinalizedPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(SessionLiveFinalizedEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE,
                RabbitMQConfig.OUT_ROUTING,
                event
        );
        log.info("[live-coach-service] Published session.live.finalized — sessionId={} mode={}",
                event.getSessionId(), event.getMode());
    }
}
