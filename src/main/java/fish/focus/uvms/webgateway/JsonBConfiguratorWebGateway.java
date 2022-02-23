package fish.focus.uvms.webgateway;

import fish.focus.uvms.commons.date.JsonBConfigurator;
import fish.focus.uvms.webgateway.dto.SearchBranchDeserializer;

public class JsonBConfiguratorWebGateway extends JsonBConfigurator {

    public JsonBConfiguratorWebGateway() {
        super();
        config.withDeserializers(new SearchBranchDeserializer());
    }
}
