package cl.duoc.formativa1.service;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import cl.duoc.formativa1.dto.CuentaResponse;

@Service
public class CuentaService {

    static final String BUSCAR_CUENTA_SQL = """
        SELECT
            i.cuenta_id,
            i.nombre,
            i.edad,
            i.tipo,
            i.saldo_inicial,
            i.tasa_interes,
            i.interes_calculado,
            i.saldo_final,
            NVL(e.total_depositos, 0) AS total_ingresos,
            NVL(e.total_retiros, 0) + NVL(e.total_compras_pagos, 0) AS total_egresos,
            NVL(e.movimiento_neto, 0) AS saldo_neto,
            NVL(e.cantidad_operaciones, 0) AS cantidad_movimientos,
            NVL(e.operaciones_rechazadas, 0) AS cantidad_anomalias
        FROM (
            SELECT i.*,
                   ROW_NUMBER() OVER (
                       PARTITION BY i.cuenta_id
                       ORDER BY i.created_at DESC, i.source_line DESC
                   ) AS rn
            FROM intereses_calculados i
            WHERE i.cuenta_id = ?
        ) i
        LEFT JOIN (
            SELECT e.*,
                   ROW_NUMBER() OVER (
                       PARTITION BY e.cuenta_id
                       ORDER BY e.anio DESC, e.created_at DESC
                   ) AS rn
            FROM estados_cuenta_anuales e
        ) e
            ON i.cuenta_id = e.cuenta_id
           AND e.rn = 1
        WHERE i.rn = 1
        """;

    private final JdbcTemplate jdbcTemplate;

    public CuentaService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<CuentaResponse> buscarCuentaPorId(Long cuentaId) {
        List<CuentaResponse> cuentas =
                jdbcTemplate.query(
                        BUSCAR_CUENTA_SQL,
                        (rs, rowNum) -> {

                            CuentaResponse cuenta =
                                    new CuentaResponse();

                            cuenta.setCuentaId(
                                    rs.getLong("cuenta_id"));

                            cuenta.setNombre(
                                    rs.getString("nombre"));

                            cuenta.setEdad(
                                    rs.getInt("edad"));

                            cuenta.setTipo(
                                    rs.getString("tipo"));

                            cuenta.setSaldoInicial(
                                    rs.getBigDecimal("saldo_inicial"));

                            cuenta.setTasaInteres(
                                    rs.getBigDecimal("tasa_interes"));

                            cuenta.setInteresCalculado(
                                    rs.getBigDecimal("interes_calculado"));

                            cuenta.setSaldoFinal(
                                    rs.getBigDecimal("saldo_final"));

                            cuenta.setTotalIngresos(
                                    rs.getBigDecimal("total_ingresos"));

                            cuenta.setTotalEgresos(
                                    rs.getBigDecimal("total_egresos"));

                            cuenta.setSaldoNeto(
                                    rs.getBigDecimal("saldo_neto"));

                            cuenta.setCantidadMovimientos(
                                    rs.getInt("cantidad_movimientos"));

                            cuenta.setCantidadAnomalias(
                                    rs.getInt("cantidad_anomalias"));

                            return cuenta;
                        },
                        cuentaId
                );

        return cuentas.stream().findFirst();
    }
}
