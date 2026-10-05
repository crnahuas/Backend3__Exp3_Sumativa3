package cl.duoc.ms_mensajeria.messaging;

public class EventoInvalidoException extends RuntimeException {

    public EventoInvalidoException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventoInvalidoException(String message) {
        super(message);
    }
}
