package cl.duoc.bffweb.models;

import java.math.BigDecimal;

public class ResumenWebResponse {

    private Long cuentaId;
    private String nombre;
    private BigDecimal saldoActual;
    private BigDecimal totalIngresos;
    private BigDecimal totalEgresos;
    private BigDecimal saldoNeto;
    private Integer cantidadMovimientos;
    private Integer cantidadAnomalias;
    private String estadoMovimientos;

    public ResumenWebResponse() {
    }

    public ResumenWebResponse(Long cuentaId,
                              String nombre,
                              BigDecimal saldoActual,
                              BigDecimal totalIngresos,
                              BigDecimal totalEgresos,
                              BigDecimal saldoNeto,
                              Integer cantidadMovimientos,
                              Integer cantidadAnomalias,
                              String estadoMovimientos) {
        this.cuentaId = cuentaId;
        this.nombre = nombre;
        this.saldoActual = saldoActual;
        this.totalIngresos = totalIngresos;
        this.totalEgresos = totalEgresos;
        this.saldoNeto = saldoNeto;
        this.cantidadMovimientos = cantidadMovimientos;
        this.cantidadAnomalias = cantidadAnomalias;
        this.estadoMovimientos = estadoMovimientos;
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

    public BigDecimal getSaldoActual() {
        return saldoActual;
    }

    public void setSaldoActual(BigDecimal saldoActual) {
        this.saldoActual = saldoActual;
    }

    public BigDecimal getTotalIngresos() {
        return totalIngresos;
    }

    public void setTotalIngresos(BigDecimal totalIngresos) {
        this.totalIngresos = totalIngresos;
    }

    public BigDecimal getTotalEgresos() {
        return totalEgresos;
    }

    public void setTotalEgresos(BigDecimal totalEgresos) {
        this.totalEgresos = totalEgresos;
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

    public Integer getCantidadAnomalias() {
        return cantidadAnomalias;
    }

    public void setCantidadAnomalias(Integer cantidadAnomalias) {
        this.cantidadAnomalias = cantidadAnomalias;
    }

    public String getEstadoMovimientos() {
        return estadoMovimientos;
    }

    public void setEstadoMovimientos(String estadoMovimientos) {
        this.estadoMovimientos = estadoMovimientos;
    }
}
