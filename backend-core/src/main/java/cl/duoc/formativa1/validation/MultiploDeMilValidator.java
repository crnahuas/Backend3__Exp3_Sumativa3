package cl.duoc.formativa1.validation;

import java.math.BigDecimal;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MultiploDeMilValidator implements ConstraintValidator<MultiploDeMil, BigDecimal> {

    private static final BigDecimal MIL = new BigDecimal("1000");

    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        return value == null || value.remainder(MIL).compareTo(BigDecimal.ZERO) == 0;
    }
}
