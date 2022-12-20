package cloud4webdevs.mythaistar.booking.adapter.in.lambda;

import com.fasterxml.jackson.jr.ob.JSON;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Getter
@FieldDefaults(makeFinal=true, level= AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class Response {
    private static final Map<String, String> DEFAULT_CORS_HEADERS = Map.of(
            "Access-Control-Allow-Origin", "*",
            "Access-Control-Allow-Methods", "*");
    int statusCode;
    String body;
    Map<String, String> headers;

    static Response okWithJsonContent(Object content) {
        try {
            return new Response(200, JSON.std.asString(content), headersOf(Map.of("Content-Type", "application/json")));
        } catch (IOException e) {
            throw new IllegalArgumentException("Response could not be transformed to JSON", e);
        }
    }

    static Response okWithTextContent(String textContent) {
        return new Response(200, textContent, headersOf(Map.of("Content-Type", "text/plain")));
    }

    private static Map<String, String> headersOf(Map<String, String> newHeaders) {
        final var headers = new HashMap<>(DEFAULT_CORS_HEADERS);
        headers.putAll(newHeaders);
        return headers;
    }
}
