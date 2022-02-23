package fish.focus.uvms.webgateway.mock;

import fish.focus.schema.exchange.v1.*;
import fish.focus.uvms.asset.client.model.AssetDTO;
import fish.focus.uvms.asset.client.model.AssetListResponse;
import fish.focus.uvms.asset.client.model.Note;
import fish.focus.uvms.rest.security.RequiresFeature;
import fish.focus.uvms.rest.security.UnionVMSFeature;
import org.slf4j.MDC;

import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

@Path("/exchange/rest")
@Stateless
@Consumes(value = {MediaType.APPLICATION_JSON})
@Produces(value = {MediaType.APPLICATION_JSON})
public class ExchangeModuleMock {

    @GET
    @Path(value = "unsecured/api/poll/{typeRefGuid}")
    public Response getPollStatus(@PathParam("typeRefGuid") String typeRefGuid) {
        if (typeRefGuid == null) {
            throw new IllegalArgumentException("Invalid id");
        }
        ExchangeLogStatusType response = new ExchangeLogStatusType();
        response.setGuid(UUID.randomUUID().toString());
        response.setIdentifier("Log recipient");

        LogRefType logRefType = new LogRefType();
        logRefType.setMessage("LogRefTyp message");
        logRefType.setRefGuid(typeRefGuid);
        logRefType.setType(TypeRefType.POLL);
        response.setTypeRef(logRefType);

        LogRefType relatedData = new LogRefType();
        relatedData.setRefGuid(UUID.randomUUID().toString());
        relatedData.setType(TypeRefType.MOVEMENT);
        response.setRelatedLogData(relatedData);

        response.getHistory().add(createExchangeLogStatusHistory(ExchangeLogStatusTypeType.SENT));
        response.getHistory().add(createExchangeLogStatusHistory(ExchangeLogStatusTypeType.PENDING));
        response.getHistory().add(createExchangeLogStatusHistory(ExchangeLogStatusTypeType.SUCCESSFUL));

        return Response.ok(response).build();
    }

    private ExchangeLogStatusHistoryType createExchangeLogStatusHistory(ExchangeLogStatusTypeType status){
        ExchangeLogStatusHistoryType statusHistory = new ExchangeLogStatusHistoryType();
        statusHistory.setStatus(status);
        statusHistory.setTimestamp(new Date());

        return statusHistory;
    }

}
