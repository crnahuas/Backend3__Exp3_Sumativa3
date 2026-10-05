package cl.duoc.formativa1.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import cl.duoc.formativa1.dto.CuentaResponse;
import cl.duoc.formativa1.dto.RetiroRequest;
import cl.duoc.formativa1.dto.RetiroResponse;
import cl.duoc.formativa1.exception.ReglaNegocioException;
import cl.duoc.formativa1.messaging.RetiroEventPublisher;
import cl.duoc.formativa1.messaging.RetiroProcesadoEvent;

@Service
public class RetiroService {

    private static final BigDecimal MULTIPLO_RETIRO = new BigDecimal("1000");

    private final CuentaService cuentaService;
    private final RetiroEventPublisher eventPublisher;

    public RetiroService(CuentaService cuentaService, RetiroEventPublisher eventPublisher) {
        this.cuentaService = cuentaService;
        this.eventPublisher = eventPublisher;
    }

    public RetiroResponse procesar(Long cuentaId, RetiroRequest request, String correlationId) {
        BigDecimal monto = request.getMonto();
        if (monto.remainder(MULTIPLO_RETIRO).compareTo(BigDecimal.ZERO) != 0) {
            throw new ReglaNegocioException("El monto debe ser multiplo de 1000");
        }

        CuentaResponse cuenta = cuentaService.buscarCuentaPorId(cuentaId)
                .orElseThrow(() -> new ReglaNegocioException("La cuenta solicitada no existe"));

        BigDecimal saldoDisponible = cuenta.getSaldoFinal();
        boolean aprobado = saldoDisponible.compareTo(monto) >= 0;
        BigDecimal saldoRestante = aprobado ? saldoDisponible.subtract(monto) : saldoDisponible;
        String mensaje = aprobado ? "Retiro aprobado" : "Saldo insuficiente";

        RetiroResponse response = new RetiroResponse(
                cuentaId,
                monto,
                saldoDisponible,
                saldoRestante,
                aprobado,
                mensaje);

        eventPublisher.publicar(RetiroProcesadoEvent.from(response, correlationId));
        return response;
    }
}
