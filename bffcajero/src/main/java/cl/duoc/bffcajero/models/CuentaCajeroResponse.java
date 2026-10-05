package cl.duoc.bffcajero.models;

import java.math.BigDecimal;

public class CuentaCajeroResponse {

    private Long cuentaId;
    private String nombre;
    private String tipo;
    private BigDecimal saldoDisponible;

    public CuentaCajeroResponse() {
    }

    public CuentaCajeroResponse(Long cuentaId,
                                String nombre,
                                String tipo,
                                BigDecimal saldoDisponible) {
        this.cuentaId = cuentaId;
        this.nombre = nombre;
        this.tipo = tipo;
        this.saldoDisponible = saldoDisponible;
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
}