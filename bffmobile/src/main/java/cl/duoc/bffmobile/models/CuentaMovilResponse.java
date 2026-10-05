package cl.duoc.bffmobile.models;

import java.math.BigDecimal;

public class CuentaMovilResponse {

    private Long cuentaId;
    private String nombre;
    private String tipo;
    private BigDecimal saldoDisponible;
    private Integer cantidadMovimientos;

    public CuentaMovilResponse() {
    }

    public CuentaMovilResponse(Long cuentaId,
                               String nombre,
                               String tipo,
                               BigDecimal saldoDisponible,
                               Integer cantidadMovimientos) {
        this.cuentaId = cuentaId;
        this.nombre = nombre;
        this.tipo = tipo;
        this.saldoDisponible = saldoDisponible;
        this.cantidadMovimientos = cantidadMovimientos;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getSaldoDisponible() {
        return saldoDisponible;
    }

    public void setSaldoDisponible(BigDecimal saldoDisponible) {
        this.saldoDisponible = saldoDisponible;
    }

    public Integer getCantidadMovimientos() {
        return cantidadMovimientos;
    }

    public void setCantidadMovimientos(Integer cantidadMovimientos) {
        this.cantidadMovimientos = cantidadMovimientos;
    }
}