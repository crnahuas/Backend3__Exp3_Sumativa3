package cl.duoc.formativa1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import cl.duoc.formativa1.dto.CuentaResponse;
import cl.duoc.formativa1.dto.RetiroRequest;
import cl.duoc.formativa1.dto.RetiroResponse;
import cl.duoc.formativa1.service.CuentaService;
import cl.duoc.formativa1.service.RetiroService;

@RestController
@RequestMapping("/api/core/cuentas")
@Validated
public class CuentaController {

    private final CuentaService cuentaService;
    private final RetiroService retiroService;

    public CuentaController(CuentaService cuentaService, RetiroService retiroService) {
        this.cuentaService = cuentaService;
        this.retiroService = retiroService;
    }

    @GetMapping("/{cuentaId}")
    public ResponseEntity<CuentaResponse> buscarCuenta(
            @PathVariable @Positive(message = "El identificador de cuenta debe ser positivo") Long cuentaId) {

        return cuentaService.buscarCuentaPorId(cuentaId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{cuentaId}/retiros")
    public ResponseEntity<RetiroResponse> procesarRetiro(
            @PathVariable @Positive(message = "El identificador de cuenta debe ser positivo") Long cuentaId,
            @Valid @RequestBody RetiroRequest request,
            @RequestHeader(value = "X-Correlation-Id", required = false) String correlationId) {

        String effectiveCorrelationId = correlationId == null || correlationId.isBlank()
                ? UUID.randomUUID().toString()
                : correlationId;
        return ResponseEntity.ok(retiroService.procesar(cuentaId, request, effectiveCorrelationId));
    }
}
