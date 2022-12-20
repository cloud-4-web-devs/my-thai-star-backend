package cloud4webdevs.mythaistar.booking.adapter.in.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.jr.ob.JSON;

import java.io.IOException;
import java.util.Map;

public class HealthCheckHandler implements RequestHandler<Map<String, Object>, Response> {
    @Override
    public Response handleRequest(Map<String, Object> event, Context context) {
        LambdaLogger logger = context.getLogger();
        try {
            logger.log("EVENT: " + JSON.std.asString(event) + "\n");
        } catch (IOException e) {
            throw new IllegalStateException("Request could not be parsed!");
        }
        return Response.okWithTextContent("Status: healthy :)");
    }
}
