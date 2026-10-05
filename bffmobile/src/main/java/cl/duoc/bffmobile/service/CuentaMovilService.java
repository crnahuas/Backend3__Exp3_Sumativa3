package cl.duoc.bffmobile.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cl.duoc.bffmobile.client.CuentaCoreClient;
import cl.duoc.bffmobile.models.CuentaCompletaResponse;
import cl.duoc.bffmobile.models.CuentaMovilResponse;
import cl.duoc.bffmobile.models.ResumenMovilResponse;

@Service
public class CuentaMovilService {

    private static final Logger logger =
            LoggerFactory.getLogger(CuentaMovilService.class);

    private final CuentaCoreClient cuentaCoreClient;

    public CuentaMovilService(CuentaCoreClient cuentaCoreClient) {
        this.cuentaCoreClient = cuentaCoreClient;
    }

    public CuentaMovilResponse obtenerCuentaMovil(Long cuentaId) {

        String requestId = UUID.randomUUID().toString();
        logger.info(
                "event=bff_service_request canal=mobile operacion=detalle requestId={} cuentaId={}",
                requestId,
                cuentaId);

        CuentaCompletaResponse cuentaCompleta =
                cuentaCoreClient.obtenerCuenta(cuentaId, requestId);

        return new CuentaMovilResponse(
                cuentaCompleta.getCuentaId(),
                cuentaCompleta.getNombre(),
                cuentaCompleta.getTipo(),
                cuentaCompleta.getSaldoFinal(),
                cuentaCompleta.getCantidadMovimientos()
        );
    }

    public ResumenMovilResponse obtenerResumenMovil(Long cuentaId) {

        String requestId = UUID.randomUUID().toString();
        logger.info(
                "event=bff_service_request canal=mobile operacion=resumen requestId={} cuentaId={}",
                requestId,
                cuentaId);

        CuentaCompletaResponse cuentaCompleta =
                cuentaCoreClient.obtenerCuenta(cuentaId, requestId);

        String mensaje = construirMensajeSaldo(cuentaCompleta.getSaldoFinal());

        return new ResumenMovilResponse(
                cuentaCompleta.getCuentaId(),
                cuentaCompleta.getNombre(),
                cuentaCompleta.getSaldoFinal(),
                cuentaCompleta.getSaldoNeto(),
                cuentaCompleta.getCantidadMovimientos(),
                mensaje
        );
    }

    private String construirMensajeSaldo(BigDecimal saldoDisponible) {

        if (saldoDisponible == null) {
            return "Saldo no disponible";
        }

        if (saldoDisponible.compareTo(BigDecimal.ZERO) > 0) {
            return "Saldo disponible para operar";
        }

        return "Saldo sin disponibilidad";
    }
}
