package eu.europa.ec.fisheries.uvms.webgateway.filter;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class WebGatewayRestExceptionMapper implements ExceptionMapper<Exception> {


    private static final Logger LOG = LoggerFactory.getLogger(WebGatewayRestExceptionMapper.class);
    public WebGatewayRestExceptionMapper() {
        super();
    }

    @Override
    public Response toResponse(Exception exception) {

        AppError error = new AppError(500, ExceptionUtils.getRootCauseMessage(exception));
        return Response.ok(error).header("MDC", MDC.get("requestId")).build();

    }
}
