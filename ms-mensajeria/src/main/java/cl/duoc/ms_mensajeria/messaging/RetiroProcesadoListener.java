package cl.duoc.ms_mensajeria.messaging;

import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.jms.JMSException;
import jakarta.jms.TextMessage;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@Component
public class RetiroProcesadoListener {

    private static final Logger logger = LoggerFactory.getLogger(RetiroProcesadoListener.class);

    private final ObjectMapper objectMapper;
    private final Validator validator;

    public RetiroProcesadoListener(ObjectMapper objectMapper, Validator validator) {
        this.objectMapper = objectMapper;
        this.validator = validator;
    }

    @JmsListener(
            destination = "${app.jms.queue.retiros}",
            containerFactory = "retiroJmsListenerContainerFactory")
    public void recibir(TextMessage message) throws JMSException {
        String json = message.getText();
        int deliveryCount = message.propertyExists("JMSXDeliveryCount")
                ? message.getIntProperty("JMSXDeliveryCount")
                : 1;
        String jmsCorrelationId = message.getJMSCorrelationID();

        try {
            RetiroProcesadoEvent event = objectMapper.readValue(json, RetiroProcesadoEvent.class);
            validar(event);

            String correlationId = firstNonBlank(event.getCorrelationId(), jmsCorrelationId, event.getEventId());
            logger.info(
                    "event=jms_event_consumed eventId={} eventType={} eventVersion={} correlationId={} cuentaId={} aprobado={} deliveryCount={}",
                    event.getEventId(),
                    event.getEventType(),
                    event.getEventVersion(),
                    correlationId,
                    event.getCuentaId(),
                    event.isAprobado(),
                    deliveryCount);

            // Punto de integración para el envío real de email, SMS o push.
            logger.info(
                    "event=notification_prepared correlationId={} cuentaId={} mensaje={}",
                    correlationId,
                    event.getCuentaId(),
                    event.getMensaje());
        } catch (JsonProcessingException ex) {
            logger.error(
                    "event=jms_event_rejected reason=invalid_json correlationId={} deliveryCount={}",
                    jmsCorrelationId,
                    deliveryCount,
                    ex);
            throw new EventoInvalidoException("JSON de RetiroProcesadoEvent invalido", ex);
        } catch (EventoInvalidoException ex) {
            logger.error(
                    "event=jms_event_rejected reason=contract_violation correlationId={} deliveryCount={} detail={}",
                    jmsCorrelationId,
                    deliveryCount,
                    ex.getMessage());
            throw ex;
        }
    }

    private void validar(RetiroProcesadoEvent event) {
        Set<ConstraintViolation<RetiroProcesadoEvent>> violations = validator.validate(event);
        if (!violations.isEmpty()) {
            String detail = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .sorted()
                    .collect(Collectors.joining(", "));
            throw new EventoInvalidoException(detail);
        }

        if (!"RetiroProcesado".equals(event.getEventType())) {
            throw new EventoInvalidoException("eventType no soportado: " + event.getEventType());
        }
        if (!event.getEventVersion().startsWith("0.") && !event.getEventVersion().startsWith("1.")) {
            throw new EventoInvalidoException("eventVersion no soportada: " + event.getEventVersion());
        }

    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return "sin-correlacion";
    }
}
