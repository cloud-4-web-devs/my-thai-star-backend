package cloud4webdevs.mythaistar.serverless;

import cloud4webdevs.mythaistar.booking.adapter.in.lambda.HealthCheckHandler;
import cloud4webdevs.mythaistar.booking.adapter.in.lambda.Request;
import cloud4webdevs.mythaistar.booking.adapter.in.lambda.Response;
import cloud4webdevs.mythaistar.booking.adapter.in.lambda.ShowTablesHandler;
import cloud4webdevs.mythaistar.booking.adapter.out.jdbc.FindTablesJdbcAdapter;
import cloud4webdevs.mythaistar.booking.adapter.out.jdbc.SecretsManagerConnectionProvider;
import cloud4webdevs.mythaistar.booking.service.ShowTablesService;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.HashMap;
import java.util.Map;

public class MyThaiStarHandler implements RequestHandler<Map<String, Object>, Response> {
    private final Map<String, RequestHandler<Request, Response>> handlers = new HashMap<>();
    private final RequestHandler<Map<String, Object>, Response> healthCheckHandler;

    public MyThaiStarHandler() {
        final var dbSecretId = System.getenv("DB_SECRET");
        final var connectionProvider = new SecretsManagerConnectionProvider(dbSecretId);
        final var showTablesHandler = new ShowTablesHandler(new ShowTablesService(new FindTablesJdbcAdapter(connectionProvider)));
        handlers.put("/booking/tables", showTablesHandler);
        healthCheckHandler = new HealthCheckHandler();
    }

    @Override
    public Response handleRequest(Map<String, Object> input, Context context) {
        final var request = new Request(input);
        final var path = request.getPath();
        final var handler = handlers.get(path);
        if (handler == null) {
            return healthCheckHandler.handleRequest(input, context);
        }
        return handler.handleRequest(request, context);
    }
}
