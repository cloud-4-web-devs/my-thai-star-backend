package cloud4webdevs.mythaistar.booking.adapter.in.lambda;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class Request {
    private final Map<String, Object> rawRequest;

    public String getPath() {
        return String.valueOf(rawRequest.get("path"));
    }
}
