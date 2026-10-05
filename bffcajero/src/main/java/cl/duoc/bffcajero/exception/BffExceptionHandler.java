package cl.duoc.bffcajero.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.MethodArgumentNotValidException;

import jakarta.validation.ConstraintViolationException;

import java.util.UUID;

@RestControllerAdvice
public class BffExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> manejarPayloadInvalido(
            MethodArgumentNotValidException ex) {

        String mensaje = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("El payload de entrada no es valido");

        return respuestaValidacion(mensaje);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> manejarParametroInvalido(
            ConstraintViolationException ex) {

        String mensaje = ex.getConstraintViolations().stream()
                .findFirst()
                .map(violation -> violation.getMessage())
                .orElse("El parametro de entrada no es valido");

        return respuestaValidacion(mensaje);
    }

    private ResponseEntity<ErrorResponse> respuestaValidacion(String mensaje) {
        return ResponseEntity.badRequest().body(new ErrorResponse(
                mensaje,
                "cajero",
                UUID.randomUUID().toString()));
    }

    @ExceptionHandler(CoreServiceException.class)
    public ResponseEntity<ErrorResponse> manejarErrorCore(
            CoreServiceException ex) {

        HttpStatus status = switch (ex.getStatusCode()) {
            case 400 -> HttpStatus.BAD_REQUEST;
            case 404 -> HttpStatus.NOT_FOUND;
            default -> HttpStatus.SERVICE_UNAVAILABLE;
        };

        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(
                        ex.getMessage(),
                        "cajero",
                        ex.getRequestId()));
    }
}
