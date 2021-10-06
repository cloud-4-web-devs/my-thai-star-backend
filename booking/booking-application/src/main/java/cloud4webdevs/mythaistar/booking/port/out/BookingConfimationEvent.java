package cloud4webdevs.mythaistar.booking.port.out;

import cloud4webdevs.mythaistar.booking.domain.Booking;
import cloud4webdevs.mythaistar.booking.domain.BookingId;
import lombok.Value;

import java.time.Instant;

@Value
public class BookingConfimationEvent {

    BookingId bookingId;

    String email;

    String token;

    Instant bookingDate;

    Instant bookingFromTime;

    Instant bookingToTime;

    int seatsNumber;

    public static BookingConfimationEvent fromBooking(Booking booking) {
        return new BookingConfimationEvent(
                booking.getId(),
                booking.getEmail(),
                booking.getToken(),
                booking.getBookingDate(),
                booking.getBookingFromTime(),
                booking.getBookingToTime(),
                booking.getSeatsNumber());
    }
}
