package cl.duoc.formativa1.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class RetiroEventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(RetiroEventPublisher.class);

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;
    private final String retiroQueue;

    public RetiroEventPublisher(
            JmsTemplate jmsTemplate,
            ObjectMapper objectMapper,
            @Value("${app.jms.queue.retiros}") String retiroQueue) {
        this.jmsTemplate = jmsTemplate;
        this.objectMapper = objectMapper;
        this.retiroQueue = retiroQueue;
    }

    public void publicar(RetiroProcesadoEvent event) {
        try {
            String json = objectMapper.writeValueAsString(event);
            jmsTemplate.send(retiroQueue, session -> {
                var message = session.createTextMessage(json);
                message.setJMSCorrelationID(event.getCorrelationId());
                message.setStringProperty("eventId", event.getEventId());
                message.setStringProperty("eventType", event.getEventType());
                message.setStringProperty("eventVersion", event.getEventVersion());
                return message;
            });
            logger.info(
                    "event=jms_event_published destination={} eventId={} eventType={} eventVersion={} correlationId={}",
                    retiroQueue,
                    event.getEventId(),
                    event.getEventType(),
                    event.getEventVersion(),
                    event.getCorrelationId());
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("No fue posible serializar RetiroProcesadoEvent", ex);
        }
    }
}
