package cl.duoc.bffcajero;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.duoc.bffcajero.client.CuentaCoreClient;
import cl.duoc.bffcajero.client.dto.RetiroCoreRequest;
import cl.duoc.bffcajero.client.dto.RetiroCoreResponse;
import cl.duoc.bffcajero.mapper.RetiroMapper;
import cl.duoc.bffcajero.models.RetiroRequest;
import cl.duoc.bffcajero.models.RetiroResponse;
import cl.duoc.bffcajero.service.CuentaCajeroService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

@ExtendWith(MockitoExtension.class)
class CuentaCajeroServiceTests {

    @Mock
    private CuentaCoreClient cuentaCoreClient;

    private CuentaCajeroService cuentaCajeroService;
    private Validator validator;

    @BeforeEach
    void setUp() {
        cuentaCajeroService = new CuentaCajeroService(cuentaCoreClient, new RetiroMapper());
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void adaptaElRequestYLaRespuestaSinAplicarReglasDeNegocioEnElBff() {
        RetiroCoreResponse coreResponse = coreResponse(true, "7240", "Retiro aprobado");
        when(cuentaCoreClient.procesarRetiro(eq(101L), any(RetiroCoreRequest.class), anyString()))
                .thenReturn(coreResponse);

        RetiroRequest request = request("5000");
        RetiroResponse response = cuentaCajeroService.procesarRetiro(101L, request, "corr-prueba-001");

        ArgumentCaptor<RetiroCoreRequest> requestCaptor = ArgumentCaptor.forClass(RetiroCoreRequest.class);
        verify(cuentaCoreClient).procesarRetiro(eq(101L), requestCaptor.capture(), eq("corr-prueba-001"));
        assertEquals(new BigDecimal("5000"), requestCaptor.getValue().getMonto());
        assertTrue(response.isAprobado());
        assertEquals(new BigDecimal("7240"), response.getSaldoRestante());
        assertEquals("Retiro aprobado", response.getMensaje());
    }

    @Test
    void conservaUnaRespuestaDeRechazoDelServicioDeNegocio() {
        when(cuentaCoreClient.procesarRetiro(eq(101L), any(RetiroCoreRequest.class), anyString()))
                .thenReturn(coreResponse(false, "12240", "Saldo insuficiente"));

        RetiroResponse response = cuentaCajeroService.procesarRetiro(101L, request("20000"), null);

        assertFalse(response.isAprobado());
        assertEquals(new BigDecimal("12240"), response.getSaldoRestante());
        assertEquals("Saldo insuficiente", response.getMensaje());
    }

    @Test
    void beanValidationRechazaMontosNulosNoPositivosDecimalesYSobreElMaximo() {
        assertInvalid(new RetiroRequest());
        assertInvalid(request("0"));
        assertInvalid(request("1000.50"));
        assertInvalid(request("1500"));
        assertInvalid(request("201000"));
        assertTrue(validator.validate(request("1000")).isEmpty());
    }

    private void assertInvalid(RetiroRequest request) {
        Set<ConstraintViolation<RetiroRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    private RetiroRequest request(String monto) {
        RetiroRequest request = new RetiroRequest();
        request.setMonto(new BigDecimal(monto));
        return request;
    }

    private RetiroCoreResponse coreResponse(boolean aprobado, String saldoRestante, String mensaje) {
        RetiroCoreResponse response = new RetiroCoreResponse();
        response.setCuentaId(101L);
        response.setMontoSolicitado(new BigDecimal(aprobado ? "5000" : "20000"));
        response.setSaldoDisponible(new BigDecimal("12240"));
        response.setSaldoRestante(new BigDecimal(saldoRestante));
        response.setAprobado(aprobado);
        response.setMensaje(mensaje);
        return response;
    }
}
