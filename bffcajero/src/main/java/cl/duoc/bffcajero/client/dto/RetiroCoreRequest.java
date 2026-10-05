package cl.duoc.bffcajero.client.dto;

import java.math.BigDecimal;

public class RetiroCoreRequest {

    private BigDecimal monto;

    public RetiroCoreRequest() {
    }

    public RetiroCoreRequest(BigDecimal monto) {
        this.monto = monto;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
}
