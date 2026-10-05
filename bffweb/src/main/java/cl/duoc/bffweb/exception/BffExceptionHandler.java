package cl.duoc.bffweb.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BffExceptionHandler {

    @ExceptionHandler(CoreServiceException.class)
    public ResponseEntity<ErrorResponse> manejarErrorCore(
            CoreServiceException ex) {

        HttpStatus status = ex.getStatusCode() == 404
                ? HttpStatus.NOT_FOUND
                : HttpStatus.SERVICE_UNAVAILABLE;

        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(
                        ex.getMessage(),
                        "web",
                        ex.getRequestId()));
    }
}
