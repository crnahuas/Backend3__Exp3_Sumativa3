package cl.duoc.bffcajero.mapper;

import org.springframework.stereotype.Component;

import cl.duoc.bffcajero.client.dto.RetiroCoreRequest;
import cl.duoc.bffcajero.client.dto.RetiroCoreResponse;
import cl.duoc.bffcajero.models.RetiroRequest;
import cl.duoc.bffcajero.models.RetiroResponse;

@Component
public class RetiroMapper {

    public RetiroCoreRequest toCoreRequest(RetiroRequest request) {
        return new RetiroCoreRequest(request.getMonto());
    }

    public RetiroResponse toApiResponse(RetiroCoreResponse response) {
        return new RetiroResponse(
                response.getCuentaId(),
                response.getMontoSolicitado(),
                response.getSaldoDisponible(),
                response.getSaldoRestante(),
                response.isAprobado(),
                response.getMensaje());
    }
}
