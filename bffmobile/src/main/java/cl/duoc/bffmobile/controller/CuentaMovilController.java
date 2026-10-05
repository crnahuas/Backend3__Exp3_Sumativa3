package cl.duoc.bffmobile.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.bffmobile.models.CuentaMovilResponse;
import cl.duoc.bffmobile.models.ResumenMovilResponse;
import cl.duoc.bffmobile.service.CuentaMovilService;

@RestController
@RequestMapping("/api/mobile/cuentas")
public class CuentaMovilController {

    private final CuentaMovilService cuentaMovilService;

    public CuentaMovilController(CuentaMovilService cuentaMovilService) {
        this.cuentaMovilService = cuentaMovilService;
    }

    @GetMapping("/{cuentaId}")
    public ResponseEntity<CuentaMovilResponse> obtenerCuenta(
            @PathVariable Long cuentaId) {

        CuentaMovilResponse cuenta =
                cuentaMovilService.obtenerCuentaMovil(cuentaId);

        return ResponseEntity.ok(cuenta);
    }

    @GetMapping("/{cuentaId}/resumen")
    public ResponseEntity<ResumenMovilResponse> obtenerResumen(
            @PathVariable Long cuentaId) {

        ResumenMovilResponse resumen =
                cuentaMovilService.obtenerResumenMovil(cuentaId);

        return ResponseEntity.ok(resumen);
    }
}
