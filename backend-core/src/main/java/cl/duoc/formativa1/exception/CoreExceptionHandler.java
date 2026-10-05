package cl.duoc.formativa1.exception;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class CoreExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> manejarPayloadInvalido(MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("El payload de entrada no es valido");
        return ResponseEntity.badRequest().body(new ApiError(mensaje, UUID.randomUUID().toString()));
    }

    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<ApiError> manejarReglaNegocio(ReglaNegocioException ex) {
        return ResponseEntity.badRequest().body(new ApiError(ex.getMessage(), UUID.randomUUID().toString()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> manejarParametroInvalido(ConstraintViolationException ex) {
        String mensaje = ex.getConstraintViolations().stream()
                .findFirst()
                .map(violation -> violation.getMessage())
                .orElse("El parametro de entrada no es valido");
        return ResponseEntity.badRequest().body(new ApiError(mensaje, UUID.randomUUID().toString()));
    }
}
