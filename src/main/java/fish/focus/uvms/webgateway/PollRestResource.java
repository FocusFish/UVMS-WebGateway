package fish.focus.uvms.webgateway;

import fish.focus.uvms.rest.security.RequiresFeature;
import fish.focus.uvms.rest.security.UnionVMSFeature;
import fish.focus.uvms.webgateway.dto.PollInfoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.UUID;

@ApplicationScoped
@Path("poll")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PollRestResource {

    private final static Logger LOG = LoggerFactory.getLogger(PollRestResource.class);

    @Inject
    PollService pollService;


    @GET
    @Path("pollsForAsset/{assetId}")
    @RequiresFeature(UnionVMSFeature.viewVesselsAndMobileTerminals)
    public Response incidentLogForIncident(@Context HttpServletRequest request, @PathParam("assetId") UUID assetId)  {
        try{
            Map<String, PollInfoDto> response = pollService.getPollInformationForAssetInTheLastDay(assetId);
            return Response.ok(response).build();
        }catch (Exception e){
            LOG.error("Error getting polls for asset {}: ", assetId ,e.getMessage(), e);
            throw e;
        }
    }
}
