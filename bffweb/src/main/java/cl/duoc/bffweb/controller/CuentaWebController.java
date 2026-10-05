package cl.duoc.bffweb.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.bffweb.models.CuentaWebResponse;
import cl.duoc.bffweb.models.ResumenWebResponse;
import cl.duoc.bffweb.service.CuentaWebService;

@RestController
@RequestMapping("/api/web/cuentas")
public class CuentaWebController {

    private final CuentaWebService cuentaWebService;

    public CuentaWebController(CuentaWebService cuentaWebService) {
        this.cuentaWebService = cuentaWebService;
    }

    @GetMapping("/{cuentaId}")
    public ResponseEntity<CuentaWebResponse> obtenerCuenta(
            @PathVariable Long cuentaId) {

        CuentaWebResponse cuenta =
                cuentaWebService.obtenerCuentaWeb(cuentaId);

        return ResponseEntity.ok(cuenta);
    }

    @GetMapping("/{cuentaId}/resumen")
    public ResponseEntity<ResumenWebResponse> obtenerResumen(
            @PathVariable Long cuentaId) {

        ResumenWebResponse resumen =
                cuentaWebService.obtenerResumenWeb(cuentaId);

        return ResponseEntity.ok(resumen);
    }
}
