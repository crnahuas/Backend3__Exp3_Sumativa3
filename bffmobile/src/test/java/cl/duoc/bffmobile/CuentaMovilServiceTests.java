package cl.duoc.bffmobile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.duoc.bffmobile.client.CuentaCoreClient;
import cl.duoc.bffmobile.models.CuentaCompletaResponse;
import cl.duoc.bffmobile.models.CuentaMovilResponse;
import cl.duoc.bffmobile.models.ResumenMovilResponse;
import cl.duoc.bffmobile.service.CuentaMovilService;

@ExtendWith(MockitoExtension.class)
class CuentaMovilServiceTests {

    @Mock
    private CuentaCoreClient cuentaCoreClient;

    @InjectMocks
    private CuentaMovilService cuentaMovilService;

    @Test
    void reduceLaRespuestaAInformacionEsencial() {
        CuentaCompletaResponse completa = new CuentaCompletaResponse();
        completa.setCuentaId(101L);
        completa.setNombre("Diana Prince");
        completa.setTipo("prestamo");
        completa.setSaldoFinal(new BigDecimal("12240"));
        completa.setCantidadMovimientos(46);

        when(cuentaCoreClient.obtenerCuenta(eq(101L), anyString()))
                .thenReturn(completa);

        CuentaMovilResponse respuesta = cuentaMovilService.obtenerCuentaMovil(101L);

        assertEquals(101L, respuesta.getCuentaId());
        assertEquals("Diana Prince", respuesta.getNombre());
        assertEquals("prestamo", respuesta.getTipo());
        assertEquals(new BigDecimal("12240"), respuesta.getSaldoDisponible());
        assertEquals(46, respuesta.getCantidadMovimientos());
    }

    @Test
    void entregaResumenMovilConMensajeOperativo() {
        CuentaCompletaResponse completa = new CuentaCompletaResponse();
        completa.setCuentaId(101L);
        completa.setNombre("Diana Prince");
        completa.setSaldoFinal(new BigDecimal("12240"));
        completa.setSaldoNeto(new BigDecimal("2240"));
        completa.setCantidadMovimientos(46);

        when(cuentaCoreClient.obtenerCuenta(eq(101L), anyString()))
                .thenReturn(completa);

        ResumenMovilResponse respuesta =
                cuentaMovilService.obtenerResumenMovil(101L);

        assertEquals(101L, respuesta.getCuentaId());
        assertEquals(new BigDecimal("12240"), respuesta.getSaldoDisponible());
        assertEquals(new BigDecimal("2240"), respuesta.getSaldoNeto());
        assertEquals("Saldo disponible para operar", respuesta.getMensajeEstado());
    }
}
