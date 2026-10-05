package cl.duoc.bffmobile.exception;

public class CoreServiceException extends RuntimeException {

    private final int statusCode;
    private final String requestId;

    public CoreServiceException(String message,
                                int statusCode,
                                String requestId,
                                Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
        this.requestId = requestId;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getRequestId() {
        return requestId;
    }
}
