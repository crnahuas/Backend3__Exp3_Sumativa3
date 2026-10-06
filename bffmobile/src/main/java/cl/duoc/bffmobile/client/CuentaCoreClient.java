package cl.duoc.bffmobile.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import cl.duoc.bffmobile.exception.CoreClientException;
import cl.duoc.bffmobile.exception.CoreServiceException;
import cl.duoc.bffmobile.models.CuentaCompletaResponse;

@Component
public class CuentaCoreClient {

    private static final Logger logger =
            LoggerFactory.getLogger(CuentaCoreClient.class);

    private final RestClient restClient;
    private final CircuitBreakerFactory<?, ?> circuitBreakerFactory;

    public CuentaCoreClient(
            RestClient restClient,
            CircuitBreakerFactory<?, ?> circuitBreakerFactory) {
        this.restClient = restClient;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    public CuentaCompletaResponse obtenerCuenta(Long cuentaId,
                                                String requestId) {

        String bearerToken = currentBearerToken();

        return circuitBreakerFactory.create("backendCore").run(
                () -> solicitarCuenta(cuentaId, requestId, bearerToken),
                throwable -> fallback(requestId, throwable));
    }

    private CuentaCompletaResponse solicitarCuenta(
            Long cuentaId,
            String requestId,
            String bearerToken) {
        try {
            logger.info(
                    "event=core_request canal=mobile requestId={} cuentaId={}",
                    requestId,
                    cuentaId);

            CuentaCompletaResponse respuesta = restClient.get()
                    .uri("/api/core/cuentas/{cuentaId}", cuentaId)
                    .headers(headers -> headers.setBearerAuth(bearerToken))
                    .retrieve()
                    .body(CuentaCompletaResponse.class);

            logger.info(
                    "event=core_response canal=mobile requestId={} cuentaId={}",
                    requestId,
                    cuentaId);

            return respuesta;
        } catch (RestClientResponseException ex) {
            logger.warn(
                    "event=core_http_error canal=mobile requestId={} cuentaId={} status={}",
                    requestId,
                    cuentaId,
                    ex.getStatusCode().value());
            if (ex.getStatusCode().is4xxClientError()) {
                throw new CoreClientException(
                        "El servicio central rechazo la consulta",
                        ex.getStatusCode().value(),
                        requestId,
                        ex);
            }
            throw new CoreServiceException(
                    "El servicio central no pudo responder la consulta",
                    ex.getStatusCode().value(),
                    requestId,
                    ex);
        } catch (ResourceAccessException ex) {
            logger.error(
                    "event=core_connection_error canal=mobile requestId={} cuentaId={}",
                    requestId,
                    cuentaId);
            throw new CoreServiceException(
                    "No fue posible comunicarse con el servicio central",
                    503,
                    requestId,
                    ex);
        }
    }

    private CuentaCompletaResponse fallback(String requestId, Throwable throwable) {
        if (throwable instanceof CoreClientException coreClientException) {
            throw coreClientException;
        }
        logger.warn("event=core_circuit_breaker_fallback canal=mobile requestId={}", requestId);
        if (throwable instanceof CoreServiceException coreServiceException) {
            throw coreServiceException;
        }
        throw new CoreServiceException(
                "El servicio central no está disponible temporalmente",
                503,
                requestId,
                throwable);
    }

    private String currentBearerToken() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof JwtAuthenticationToken jwtAuthentication) {
            return jwtAuthentication.getToken().getTokenValue();
        }
        throw new IllegalStateException("No existe un JWT autenticado para invocar backend-core");
    }
}
