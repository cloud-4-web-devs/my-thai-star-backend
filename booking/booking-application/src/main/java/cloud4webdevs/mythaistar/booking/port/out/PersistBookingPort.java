package cloud4webdevs.mythaistar.booking.port.out;

import cloud4webdevs.mythaistar.booking.domain.Booking;

public interface PersistBookingPort {
    Booking persist(Booking booking);
}
