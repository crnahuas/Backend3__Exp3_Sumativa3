package cl.duoc.formativa1.exception;

public record ApiError(String mensaje, String correlationId) {
}
