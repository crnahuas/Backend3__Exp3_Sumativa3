package cl.duoc.formativa1;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.duoc.formativa1.dto.CuentaResponse;
import cl.duoc.formativa1.dto.RetiroRequest;
import cl.duoc.formativa1.dto.RetiroResponse;
import cl.duoc.formativa1.exception.ReglaNegocioException;
import cl.duoc.formativa1.messaging.RetiroEventPublisher;
import cl.duoc.formativa1.messaging.RetiroProcesadoEvent;
import cl.duoc.formativa1.service.CuentaService;
import cl.duoc.formativa1.service.RetiroService;

@ExtendWith(MockitoExtension.class)
class RetiroServiceTests {

    @Mock
    private CuentaService cuentaService;
    @Mock
    private RetiroEventPublisher eventPublisher;

    private RetiroService retiroService;

    @BeforeEach
    void setUp() {
        retiroService = new RetiroService(cuentaService, eventPublisher);
    }

    @Test
    void apruebaYPublicaElEventoDesdeElMicroservicioDeNegocio() {
        when(cuentaService.buscarCuentaPorId(101L)).thenReturn(Optional.of(cuenta("12240")));

        RetiroResponse response = retiroService.procesar(101L, request("5000"), "corr-123");

        ArgumentCaptor<RetiroProcesadoEvent> eventCaptor = ArgumentCaptor.forClass(RetiroProcesadoEvent.class);
        verify(eventPublisher).publicar(eventCaptor.capture());
        RetiroProcesadoEvent event = eventCaptor.getValue();

        assertTrue(response.isAprobado());
        assertEquals(new BigDecimal("7240"), response.getSaldoRestante());
        assertEquals("1.0.0", event.getEventVersion());
        assertEquals("corr-123", event.getCorrelationId());
        assertEquals(101L, event.getCuentaId());
    }

    @Test
    void rechazaMontosQueNoSonMultiplosDeMilAntesDeConsultarLaCuenta() {
        ReglaNegocioException exception = assertThrows(
                ReglaNegocioException.class,
                () -> retiroService.procesar(101L, request("1500"), "corr-123"));

        assertEquals("El monto debe ser multiplo de 1000", exception.getMessage());
    }

    private RetiroRequest request(String monto) {
        RetiroRequest request = new RetiroRequest();
        request.setMonto(new BigDecimal(monto));
        return request;
    }

    private CuentaResponse cuenta(String saldoFinal) {
        CuentaResponse cuenta = new CuentaResponse();
        cuenta.setCuentaId(101L);
        cuenta.setSaldoFinal(new BigDecimal(saldoFinal));
        return cuenta;
    }
}
