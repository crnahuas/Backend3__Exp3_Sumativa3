package cl.duoc.bffweb.models;

import java.math.BigDecimal;

public class CuentaWebResponse {

    private Long cuentaId;
    private String nombre;
    private Integer edad;
    private String tipo;
    private BigDecimal saldoInicial;
    private BigDecimal tasaInteres;
    private BigDecimal interesCalculado;
    private BigDecimal saldoActual;
    private BigDecimal totalIngresos;
    private BigDecimal totalEgresos;
    private BigDecimal saldoNeto;
    private Integer cantidadMovimientos;
    private Integer cantidadAnomalias;

    public CuentaWebResponse() {
    }

    public CuentaWebResponse(Long cuentaId,
                             String nombre,
                             Integer edad,
                             String tipo,
                             BigDecimal saldoInicial,
                             BigDecimal tasaInteres,
                             BigDecimal interesCalculado,
                             BigDecimal saldoActual,
                             BigDecimal totalIngresos,
                             BigDecimal totalEgresos,
                             BigDecimal saldoNeto,
                             Integer cantidadMovimientos,
                             Integer cantidadAnomalias) {

        this.cuentaId = cuentaId;
        this.nombre = nombre;
        this.edad = edad;
        this.tipo = tipo;
        this.saldoInicial = saldoInicial;
        this.tasaInteres = tasaInteres;
        this.interesCalculado = interesCalculado;
        this.saldoActual = saldoActual;
        this.totalIngresos = totalIngresos;
        this.totalEgresos = totalEgresos;
        this.saldoNeto = saldoNeto;
        this.cantidadMovimientos = cantidadMovimientos;
        this.cantidadAnomalias = cantidadAnomalias;
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

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public BigDecimal getTasaInteres() {
        return tasaInteres;
    }

    public void setTasaInteres(BigDecimal tasaInteres) {
        this.tasaInteres = tasaInteres;
    }

    public BigDecimal getInteresCalculado() {
        return interesCalculado;
    }

    public void setInteresCalculado(BigDecimal interesCalculado) {
        this.interesCalculado = interesCalculado;
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
}