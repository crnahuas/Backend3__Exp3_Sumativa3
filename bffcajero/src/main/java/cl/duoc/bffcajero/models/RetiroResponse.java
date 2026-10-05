package cl.duoc.bffcajero.models;

import java.math.BigDecimal;

public class RetiroResponse {

    private Long cuentaId;
    private BigDecimal montoSolicitado;
    private BigDecimal saldoDisponible;
    private BigDecimal saldoRestante;
    private boolean aprobado;
    private String mensaje;

    public RetiroResponse() {
    }

    public RetiroResponse(Long cuentaId,
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

    public void setCuentaId(Long cuentaId) {
        this.cuentaId = cuentaId;
    }

    public BigDecimal getMontoSolicitado() {
        return montoSolicitado;
    }

    public void setMontoSolicitado(BigDecimal montoSolicitado) {
        this.montoSolicitado = montoSolicitado;
    }

    public BigDecimal getSaldoDisponible() {
        return saldoDisponible;
    }

    public void setSaldoDisponible(BigDecimal saldoDisponible) {
        this.saldoDisponible = saldoDisponible;
    }

    public BigDecimal getSaldoRestante() {
        return saldoRestante;
    }

    public void setSaldoRestante(BigDecimal saldoRestante) {
        this.saldoRestante = saldoRestante;
    }

    public boolean isAprobado() {
        return aprobado;
    }

    public void setAprobado(boolean aprobado) {
        this.aprobado = aprobado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}