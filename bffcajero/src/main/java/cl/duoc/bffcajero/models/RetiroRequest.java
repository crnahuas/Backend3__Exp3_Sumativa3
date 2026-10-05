package cl.duoc.bffcajero.models;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import cl.duoc.bffcajero.validation.MultiploDeMil;

public class RetiroRequest {

    @NotNull(message = "Debe informar un monto para retirar")
    @DecimalMin(value = "0", inclusive = false, message = "El monto debe ser mayor que cero")
    @DecimalMax(value = "200000", message = "El monto supera el maximo permitido por operacion")
    @Digits(integer = 6, fraction = 0, message = "El monto debe ser un numero entero")
    @MultiploDeMil
    private BigDecimal monto;

    public RetiroRequest() {
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
}
