package fish.focus.uvms.webgateway;

import fish.focus.uvms.asset.client.AssetClient;
import fish.focus.uvms.commons.date.DateUtils;
import fish.focus.uvms.movement.client.MovementRestClient;
import fish.focus.uvms.rest.security.RequiresFeature;
import fish.focus.uvms.rest.security.UnionVMSFeature;
import fish.focus.uvms.webgateway.dto.TracksByAssetSearchRequestDto;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@ApplicationScoped
@Path("reports")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ReportRestResource {

    @Inject
    private AssetClient assetClient;

    @Inject
    private MovementRestClient movementClient;

    @POST
    @Path("tracksByAssetSearch")
    @RequiresFeature(UnionVMSFeature.viewVesselsAndMobileTerminals)
    public Response getTracksByAssetSearch(TracksByAssetSearchRequestDto request)  {

        List<String> assetIds = assetClient.getAssetIdList(request.getAssetQuery(),
                request.getPage(),
                request.getSize(),
                request.isIncludeInactivated());

        String response = movementClient.getMovementsForConnectIdsBetweenDates(assetIds,
                DateUtils.stringToDate(request.getStartDate()),
                DateUtils.stringToDate(request.getEndDate()),
                request.getSources());

        return Response.ok(response).build();
    }


}
