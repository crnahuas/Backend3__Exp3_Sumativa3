package cl.duoc.bffcajero.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cl.duoc.bffcajero.client.CuentaCoreClient;
import cl.duoc.bffcajero.models.CuentaCajeroResponse;
import cl.duoc.bffcajero.models.CuentaCompletaResponse;
import java.util.UUID;

import cl.duoc.bffcajero.client.dto.RetiroCoreRequest;
import cl.duoc.bffcajero.client.dto.RetiroCoreResponse;
import cl.duoc.bffcajero.mapper.RetiroMapper;
import cl.duoc.bffcajero.models.RetiroRequest;
import cl.duoc.bffcajero.models.RetiroResponse;

@Service
public class CuentaCajeroService {

        private static final Logger logger = LoggerFactory.getLogger(CuentaCajeroService.class);
        private final CuentaCoreClient cuentaCoreClient;
        private final RetiroMapper retiroMapper;

        public CuentaCajeroService(CuentaCoreClient cuentaCoreClient, RetiroMapper retiroMapper) {
                this.cuentaCoreClient = cuentaCoreClient;
                this.retiroMapper = retiroMapper;
        }

        public CuentaCajeroResponse obtenerCuentaCajero(Long cuentaId) {

                String requestId = UUID.randomUUID().toString();
                logger.info(
                                "event=bff_service_request canal=cajero operacion=detalle requestId={} cuentaId={}",
                                requestId,
                                cuentaId);

                CuentaCompletaResponse cuentaCompleta = cuentaCoreClient.obtenerCuenta(cuentaId, requestId);

                return new CuentaCajeroResponse(
                                cuentaCompleta.getCuentaId(),
                                cuentaCompleta.getNombre(),
                                cuentaCompleta.getTipo(),
                                cuentaCompleta.getSaldoFinal());
        }

        public RetiroResponse procesarRetiro(Long cuentaId, RetiroRequest request, String correlationId) {

                String requestId = correlationId == null || correlationId.isBlank()
                                ? UUID.randomUUID().toString()
                                : correlationId;
                logger.info(
                                "event=bff_service_request canal=cajero operacion=retiro requestId={} cuentaId={} monto={}",
                                requestId,
                                cuentaId,
                                request.getMonto());

                RetiroCoreRequest coreRequest = retiroMapper.toCoreRequest(request);
                RetiroCoreResponse coreResponse = cuentaCoreClient.procesarRetiro(
                                cuentaId,
                                coreRequest,
                                requestId);
                return retiroMapper.toApiResponse(coreResponse);
        }
}
