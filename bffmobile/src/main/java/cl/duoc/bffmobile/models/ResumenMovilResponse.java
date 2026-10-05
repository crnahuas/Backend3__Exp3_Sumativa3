package cl.duoc.bffmobile.models;

import java.math.BigDecimal;

public class ResumenMovilResponse {

    private Long cuentaId;
    private String nombre;
    private BigDecimal saldoDisponible;
    private BigDecimal saldoNeto;
    private Integer cantidadMovimientos;
    private String mensajeEstado;

    public ResumenMovilResponse() {
    }

    public ResumenMovilResponse(Long cuentaId,
                                String nombre,
                                BigDecimal saldoDisponible,
                                BigDecimal saldoNeto,
                                Integer cantidadMovimientos,
                                String mensajeEstado) {
        this.cuentaId = cuentaId;
        this.nombre = nombre;
        this.saldoDisponible = saldoDisponible;
        this.saldoNeto = saldoNeto;
        this.cantidadMovimientos = cantidadMovimientos;
        this.mensajeEstado = mensajeEstado;
    }

    public Long getCuentaId() {
        return cuentaId;
    }

    public void setCuentaId(Long cuentaId) {
        this.cuentaId = cuentaId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getSaldoDisponible() {
        return saldoDisponible;
    }

    public void setSaldoDisponible(BigDecimal saldoDisponible) {
        this.saldoDisponible = saldoDisponible;
    }

    public BigDecimal getSaldoNeto() {
        return saldoNeto;
    }

    public void setSaldoNeto(BigDecimal saldoNeto) {
        this.saldoNeto = saldoNeto;
    }

    public Integer getCantidadMovimientos() {
        return cantidadMovimientos;
    }

    public void setCantidadMovimientos(Integer cantidadMovimientos) {
        this.cantidadMovimientos = cantidadMovimientos;
    }

    public String getMensajeEstado() {
        return mensajeEstado;
    }

    public void setMensajeEstado(String mensajeEstado) {
        this.mensajeEstado = mensajeEstado;
    }
}
