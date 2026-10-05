package cl.duoc.formativa1.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import cl.duoc.formativa1.dto.CuentaResponse;

@ExtendWith(MockitoExtension.class)
class CuentaServiceTests {

    @Mock
    private JdbcTemplate jdbcTemplate;

    private CuentaService cuentaService;

    @BeforeEach
    void setUp() {
        cuentaService = new CuentaService(jdbcTemplate);
    }

    @Test
    @SuppressWarnings("unchecked")
    void consultaElEsquemaOracleVigenteSinUsarIdCalculo() {
        CuentaResponse cuenta = new CuentaResponse();
        cuenta.setCuentaId(101L);
        cuenta.setSaldoFinal(new BigDecimal("8120"));

        when(jdbcTemplate.query(
                anyString(),
                any(RowMapper.class),
                eq(101L)))
            .thenReturn(List.of(cuenta));

        CuentaResponse resultado = cuentaService.buscarCuentaPorId(101L).orElseThrow();

        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        verify(jdbcTemplate).query(
                sqlCaptor.capture(),
                any(RowMapper.class),
                eq(101L));

        String sql = sqlCaptor.getValue().toLowerCase();
        assertFalse(sql.contains("id_calculo"));
        assertTrue(sql.contains("row_number()"));
        assertTrue(sql.contains("created_at"));
        assertTrue(sql.contains("source_line"));
        assertTrue(sql.contains("total_depositos"));
        assertEquals(101L, resultado.getCuentaId());
        assertEquals(new BigDecimal("8120"), resultado.getSaldoFinal());
    }
}
