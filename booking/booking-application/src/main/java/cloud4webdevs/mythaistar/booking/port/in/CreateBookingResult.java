package cloud4webdevs.mythaistar.booking.port.in;

import lombok.Value;

@Value
public class CreateBookingResult {
    long bookingId;
    Long tableId;
    String token;
}
