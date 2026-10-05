package cl.duoc.bffweb.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cl.duoc.bffweb.client.CuentaCoreClient;
import cl.duoc.bffweb.models.CuentaCompletaResponse;
import cl.duoc.bffweb.models.ResumenWebResponse;
import cl.duoc.bffweb.models.CuentaWebResponse;

@Service
public class CuentaWebService {

    private static final Logger logger =
            LoggerFactory.getLogger(CuentaWebService.class);

    private final CuentaCoreClient cuentaCoreClient;

    public CuentaWebService(CuentaCoreClient cuentaCoreClient) {
        this.cuentaCoreClient = cuentaCoreClient;
    }

    public CuentaWebResponse obtenerCuentaWeb(Long cuentaId) {

        String requestId = UUID.randomUUID().toString();
        logger.info(
                "event=bff_service_request canal=web operacion=detalle requestId={} cuentaId={}",
                requestId,
                cuentaId);

        CuentaCompletaResponse cuentaCompleta =
                cuentaCoreClient.obtenerCuenta(cuentaId, requestId);

        return new CuentaWebResponse(
                cuentaCompleta.getCuentaId(),
                cuentaCompleta.getNombre(),
                cuentaCompleta.getEdad(),
                cuentaCompleta.getTipo(),
                cuentaCompleta.getSaldoInicial(),
                cuentaCompleta.getTasaInteres(),
                cuentaCompleta.getInteresCalculado(),
                cuentaCompleta.getSaldoFinal(),
                cuentaCompleta.getTotalIngresos(),
                cuentaCompleta.getTotalEgresos(),
                cuentaCompleta.getSaldoNeto(),
                cuentaCompleta.getCantidadMovimientos(),
                cuentaCompleta.getCantidadAnomalias()
        );
    }

    public ResumenWebResponse obtenerResumenWeb(Long cuentaId) {

        String requestId = UUID.randomUUID().toString();
        logger.info(
                "event=bff_service_request canal=web operacion=resumen requestId={} cuentaId={}",
                requestId,
                cuentaId);

        CuentaCompletaResponse cuentaCompleta =
                cuentaCoreClient.obtenerCuenta(cuentaId, requestId);

        String estado = cuentaCompleta.getCantidadAnomalias() != null
                && cuentaCompleta.getCantidadAnomalias() > 0
                        ? "Requiere revision"
                        : "Movimientos normales";

        return new ResumenWebResponse(
                cuentaCompleta.getCuentaId(),
                cuentaCompleta.getNombre(),
                cuentaCompleta.getSaldoFinal(),
                cuentaCompleta.getTotalIngresos(),
                cuentaCompleta.getTotalEgresos(),
                cuentaCompleta.getSaldoNeto(),
                cuentaCompleta.getCantidadMovimientos(),
                cuentaCompleta.getCantidadAnomalias(),
                estado
        );
    }
}
