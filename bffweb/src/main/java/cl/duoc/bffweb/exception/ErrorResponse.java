package cl.duoc.bffweb.exception;

public class ErrorResponse {

    private String mensaje;
    private String canal;
    private String requestId;

    public ErrorResponse() {
    }

    public ErrorResponse(String mensaje, String canal, String requestId) {
        this.mensaje = mensaje;
        this.canal = canal;
        this.requestId = requestId;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
