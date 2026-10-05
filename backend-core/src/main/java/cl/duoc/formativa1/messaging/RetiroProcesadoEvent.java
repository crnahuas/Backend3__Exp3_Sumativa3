package cl.duoc.formativa1.messaging;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import cl.duoc.formativa1.dto.RetiroResponse;

public class RetiroProcesadoEvent {

    public static final String EVENT_TYPE = "RetiroProcesado";
    public static final String EVENT_VERSION = "1.0.0";

    private final String eventId;
    private final String eventType;
    private final String eventVersion;
    private final OffsetDateTime occurredAt;
    private final String correlationId;
    private final Long cuentaId;
    private final BigDecimal montoSolicitado;
    private final BigDecimal saldoDisponible;
    private final BigDecimal saldoRestante;
    private final boolean aprobado;
    private final String mensaje;

    private RetiroProcesadoEvent(RetiroResponse response, String correlationId) {
        this.eventId = UUID.randomUUID().toString();
        this.eventType = EVENT_TYPE;
        this.eventVersion = EVENT_VERSION;
        this.occurredAt = OffsetDateTime.now(ZoneOffset.UTC);
        this.correlationId = correlationId;
        this.cuentaId = response.getCuentaId();
        this.montoSolicitado = response.getMontoSolicitado();
        this.saldoDisponible = response.getSaldoDisponible();
        this.saldoRestante = response.getSaldoRestante();
        this.aprobado = response.isAprobado();
        this.mensaje = response.getMensaje();
    }

    public static RetiroProcesadoEvent from(RetiroResponse response, String correlationId) {
        return new RetiroProcesadoEvent(response, correlationId);
    }

    public String getEventId() {
        return eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public String getEventVersion() {
        return eventVersion;
    }

    public OffsetDateTime getOccurredAt() {
        return occurredAt;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public Long getCuentaId() {
        return cuentaId;
    }

    public BigDecimal getMontoSolicitado() {
        return montoSolicitado;
    }

    public BigDecimal getSaldoDisponible() {
        return saldoDisponible;
    }

    public BigDecimal getSaldoRestante() {
        return saldoRestante;
    }

    public boolean isAprobado() {
        return aprobado;
    }

    public String getMensaje() {
        return mensaje;
    }
}
