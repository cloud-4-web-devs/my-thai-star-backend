package cloud4webdevs.mythaistar.booking.port.out;

import cloud4webdevs.mythaistar.booking.domain.Booking;

import java.util.List;

public interface FindBookingsPort {
    List<Booking> findBookings();
}
