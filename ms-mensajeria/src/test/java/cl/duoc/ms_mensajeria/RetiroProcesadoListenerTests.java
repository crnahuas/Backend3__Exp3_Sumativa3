package cl.duoc.ms_mensajeria;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import cl.duoc.ms_mensajeria.messaging.EventoInvalidoException;
import cl.duoc.ms_mensajeria.messaging.RetiroProcesadoListener;
import jakarta.jms.TextMessage;
import jakarta.validation.Validation;

@ExtendWith(MockitoExtension.class)
class RetiroProcesadoListenerTests {

    @Mock
    private TextMessage message;

    private RetiroProcesadoListener listener;

    @BeforeEach
    void setUp() {
        ObjectMapper objectMapper = JsonMapper.builder().findAndAddModules().build();
        var validator = Validation.buildDefaultValidatorFactory().getValidator();
        listener = new RetiroProcesadoListener(objectMapper, validator);
    }

    @Test
    void mantieneCompatibilidadConElEventoLegadoEIgnoraCamposNuevos() throws Exception {
        when(message.getText()).thenReturn("""
                {
                  "cuentaId": 101,
                  "montoSolicitado": 1000,
                  "saldoDisponible": 12240,
                  "saldoRestante": 11240,
                  "aprobado": true,
                  "mensaje": "Retiro aprobado",
                  "campoFuturo": "compatible"
                }
                """);
        when(message.propertyExists("JMSXDeliveryCount")).thenReturn(false);
        when(message.getJMSCorrelationID()).thenReturn("corr-legacy");

        assertDoesNotThrow(() -> listener.recibir(message));
    }

    @Test
    void propagaErroresDeContratoParaActivarReintentosYDlq() throws Exception {
        when(message.getText()).thenReturn("{\"eventType\":\"RetiroProcesado\",\"eventVersion\":\"1.0.0\"}");
        when(message.propertyExists("JMSXDeliveryCount")).thenReturn(true);
        when(message.getIntProperty("JMSXDeliveryCount")).thenReturn(2);
        when(message.getJMSCorrelationID()).thenReturn("corr-invalid");

        assertThrows(EventoInvalidoException.class, () -> listener.recibir(message));
    }
}
