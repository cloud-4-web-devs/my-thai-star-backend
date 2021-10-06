package cloud4webdevs.mythaistar.booking.port.out;

import cloud4webdevs.mythaistar.booking.domain.Booking;

import java.util.Optional;

public interface FindBookingByTokenPort {
    Optional<Booking> find(String token);
}
