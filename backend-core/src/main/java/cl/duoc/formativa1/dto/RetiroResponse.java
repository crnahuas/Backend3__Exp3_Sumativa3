package cl.duoc.formativa1.dto;

import java.math.BigDecimal;

public class RetiroResponse {

    private final Long cuentaId;
    private final BigDecimal montoSolicitado;
    private final BigDecimal saldoDisponible;
    private final BigDecimal saldoRestante;
    private final boolean aprobado;
    private final String mensaje;

    public RetiroResponse(
            Long cuentaId,
            BigDecimal montoSolicitado,
            BigDecimal saldoDisponible,
            BigDecimal saldoRestante,
            boolean aprobado,
            String mensaje) {
        this.cuentaId = cuentaId;
        this.montoSolicitado = montoSolicitado;
        this.saldoDisponible = saldoDisponible;
        this.saldoRestante = saldoRestante;
        this.aprobado = aprobado;
        this.mensaje = mensaje;
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
