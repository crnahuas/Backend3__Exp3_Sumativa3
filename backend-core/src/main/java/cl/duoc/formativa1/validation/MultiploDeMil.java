package cl.duoc.formativa1.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = MultiploDeMilValidator.class)
public @interface MultiploDeMil {

    String message() default "El monto debe ser multiplo de 1000";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
