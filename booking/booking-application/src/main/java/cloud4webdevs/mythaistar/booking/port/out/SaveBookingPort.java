package cloud4webdevs.mythaistar.booking.port.out;

import cloud4webdevs.mythaistar.booking.domain.Booking;

public interface SaveBookingPort {
    void save(Booking booking);
}
