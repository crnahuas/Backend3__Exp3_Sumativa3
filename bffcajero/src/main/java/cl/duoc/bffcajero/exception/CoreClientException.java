package cl.duoc.bffcajero.exception;

/**
 * Respuesta 4xx valida de backend-core. Se propaga al cliente, pero no debe
 * contabilizarse como indisponibilidad para abrir el circuit breaker.
 */
public class CoreClientException extends CoreServiceException {

    public CoreClientException(String message,
                               int statusCode,
                               String requestId,
                               Throwable cause) {
        super(message, statusCode, requestId, cause);
    }
}

