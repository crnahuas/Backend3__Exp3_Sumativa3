package cl.duoc.formativa1;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import cl.duoc.formativa1.dto.CuentaResponse;

class CuentaResponseTests {

    @Test
    void conservaLosDatosFinancierosDeLaCuenta() {
        CuentaResponse cuenta = new CuentaResponse();
        cuenta.setCuentaId(101L);
        cuenta.setSaldoFinal(new BigDecimal("12240"));

        assertEquals(101L, cuenta.getCuentaId());
        assertEquals(new BigDecimal("12240"), cuenta.getSaldoFinal());
    }
}
