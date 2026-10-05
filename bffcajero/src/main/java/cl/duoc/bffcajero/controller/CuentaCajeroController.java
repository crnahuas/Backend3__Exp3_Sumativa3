package cl.duoc.bffcajero.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;

import cl.duoc.bffcajero.models.CuentaCajeroResponse;
import cl.duoc.bffcajero.service.CuentaCajeroService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import cl.duoc.bffcajero.models.RetiroRequest;
import cl.duoc.bffcajero.models.RetiroResponse;

@RestController
@RequestMapping("/api/cajero/cuentas")
@Validated
public class CuentaCajeroController {

    private final CuentaCajeroService cuentaCajeroService;

    public CuentaCajeroController(CuentaCajeroService cuentaCajeroService) {
        this.cuentaCajeroService = cuentaCajeroService;
    }

    @GetMapping("/{cuentaId}")
    public ResponseEntity<CuentaCajeroResponse> obtenerCuenta(
            @PathVariable @Positive(message = "El identificador de cuenta debe ser positivo") Long cuentaId) {

        CuentaCajeroResponse cuenta =
                cuentaCajeroService.obtenerCuentaCajero(cuentaId);

        return ResponseEntity.ok(cuenta);
    }
    
    @PostMapping("/{cuentaId}/retiro")
    public ResponseEntity<RetiroResponse> procesarRetiro(
            @PathVariable @Positive(message = "El identificador de cuenta debe ser positivo") Long cuentaId,
            @RequestHeader(value = "X-Correlation-Id", required = false) String correlationId,
            @Valid @RequestBody RetiroRequest request) {

        RetiroResponse respuesta = cuentaCajeroService.procesarRetiro(cuentaId, request, correlationId);

        return ResponseEntity.ok(respuesta);
    }
}
