package cloud4webdevs.mythaistar.booking.adapter.in.lambda;

import cloud4webdevs.mythaistar.booking.port.in.ShowTablesQuery;
import cloud4webdevs.mythaistar.booking.port.in.ShowTablesUseCase;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ShowTablesHandler implements RequestHandler<Request, Response> {

    private final ShowTablesUseCase showTablesUseCase;

    @Override
    public Response handleRequest(Request input, Context context) {
        // in the future, ShowTablesQuery may contain something interesting
        final var tables = showTablesUseCase.showTables(new ShowTablesQuery());
        return Response.okWithJsonContent(tables);
    }
}
