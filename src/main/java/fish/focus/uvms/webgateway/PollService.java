package fish.focus.uvms.webgateway;

import fish.focus.schema.exchange.v1.ExchangeLogStatusType;
import fish.focus.schema.mobileterminal.polltypes.v1.PollId;
import fish.focus.uvms.asset.client.AssetClient;
import fish.focus.uvms.asset.client.model.SanePollDto;
import fish.focus.uvms.asset.client.model.mt.MobileTerminal;
import fish.focus.uvms.exchange.client.ExchangeRestClient;
import fish.focus.uvms.movement.client.MovementRestClient;
import fish.focus.uvms.movement.model.dto.MovementDto;
import fish.focus.uvms.webgateway.dto.PollInfoDto;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Stateless
public class PollService {


    @Inject
    AssetClient assetClient;

    @Inject
    ExchangeRestClient exchangeClient;

    @Inject
    MovementRestClient movementClient;

    public Map<String, PollInfoDto> getPollInformationForAssetInTheLastDay(UUID assetId){
        List<SanePollDto> pollsForAsset = assetClient.getPollsForAssetInTheLastDay(assetId);
        Map<String, PollInfoDto> returnMap = new HashMap<>(pollsForAsset.size());

        for (SanePollDto pollDto : pollsForAsset) {
            ExchangeLogStatusType pollStatus = exchangeClient.getPollStatus(pollDto.getId().toString());

            MobileTerminal mtAtDate = assetClient.getMtAtDate(pollDto.getMobileterminalId(), pollDto.getCreateTime());

            MovementDto movement = null;
            if (pollStatus != null && pollStatus.getRelatedLogData() != null) {
                UUID movementId = UUID.fromString(pollStatus.getRelatedLogData().getRefGuid());
                movement = movementClient.getMovementById(movementId);
            }
            returnMap.put(pollDto.getId().toString(), new PollInfoDto(pollDto, pollStatus, movement, mtAtDate));
        }

        return returnMap;

    }

    public PollInfoDto getPollInfo(UUID pollId){
        SanePollDto pollInfo = assetClient.getPollInfo(pollId);
        if(pollInfo == null){
            return null;
        }

        MobileTerminal mtAtDate = assetClient.getMtAtDate(pollInfo.getMobileterminalId(), pollInfo.getCreateTime());

        ExchangeLogStatusType pollStatus = exchangeClient.getPollStatus(pollId.toString());
        MovementDto movement = null;
        if (pollStatus != null && pollStatus.getRelatedLogData() != null) {
            UUID movementId = UUID.fromString(pollStatus.getRelatedLogData().getRefGuid());
            movement = movementClient.getMovementById(movementId);
        }

        return new PollInfoDto(pollInfo, pollStatus, movement, mtAtDate);
    }
}
