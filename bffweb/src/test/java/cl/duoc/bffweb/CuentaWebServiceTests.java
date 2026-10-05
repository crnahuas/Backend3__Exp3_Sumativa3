package cl.duoc.bffweb;

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

import cl.duoc.bffweb.client.CuentaCoreClient;
import cl.duoc.bffweb.models.CuentaCompletaResponse;
import cl.duoc.bffweb.models.ResumenWebResponse;
import cl.duoc.bffweb.service.CuentaWebService;

@ExtendWith(MockitoExtension.class)
class CuentaWebServiceTests {

    @Mock
    private CuentaCoreClient cuentaCoreClient;

    @InjectMocks
    private CuentaWebService cuentaWebService;

    @Test
    void entregaResumenFinancieroParaCanalWeb() {
        CuentaCompletaResponse completa = new CuentaCompletaResponse();
        completa.setCuentaId(101L);
        completa.setNombre("Diana Prince");
        completa.setSaldoFinal(new BigDecimal("12240"));
        completa.setTotalIngresos(new BigDecimal("5000"));
        completa.setTotalEgresos(new BigDecimal("1200"));
        completa.setSaldoNeto(new BigDecimal("3800"));
        completa.setCantidadMovimientos(46);
        completa.setCantidadAnomalias(0);

        when(cuentaCoreClient.obtenerCuenta(eq(101L), anyString()))
                .thenReturn(completa);

        ResumenWebResponse respuesta =
                cuentaWebService.obtenerResumenWeb(101L);

        assertEquals(101L, respuesta.getCuentaId());
        assertEquals(new BigDecimal("12240"), respuesta.getSaldoActual());
        assertEquals(new BigDecimal("3800"), respuesta.getSaldoNeto());
        assertEquals("Movimientos normales", respuesta.getEstadoMovimientos());
    }
}
