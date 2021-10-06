package cloud4webdevs.mythaistar.booking.domain;


import cloud4webdevs.mythaistar.booking.domain.exception.BookingValidationException;
import cloud4webdevs.mythaistar.booking.domain.exception.IllegalBookingStateException;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class BookingTest {

    private static final String VALID_EMAIL = "john.doe@acme.inc";

    private final Instant now = Instant.now();

    private final Instant validFromTime = now.plus(25, ChronoUnit.HOURS);

    private final Instant validToTime = now.plus(27, ChronoUnit.HOURS);

    @Test
    void shouldAllowToCreateBookingInProperAdvance() {
        // when
        final Booking booking = createValidBooking();
        // then
        assertNotNull(booking);
        assertEquals(VALID_EMAIL, booking.getEmail());
        assertEquals(validFromTime, booking.getBookingFromTime());
        assertEquals(validToTime, booking.getBookingToTime());
        assertEquals(validFromTime.truncatedTo(ChronoUnit.DAYS), booking.getBookingDate());
        assertEquals(2, booking.getSeatsNumber());
        assertEquals(BookingStatus.NEW, booking.getStatus());
        assertNotNull(booking.getToken());
        assertTrue(booking.getToken().startsWith("CB_"));
    }

    @Test
    void shouldFailIfCreatingBookingWithInvalidTimeWindow() {
        assertThrows(BookingValidationException.class, () -> Booking.createNewBooking(
                now.plus(26, ChronoUnit.HOURS),
                now.plus(25, ChronoUnit.HOURS),
                VALID_EMAIL,
                2));
    }

    @Test
    void shouldFailIfTryingToBookInThePast() {
        assertThrows(BookingValidationException.class, () -> Booking.createNewBooking(
                now.minus(24, ChronoUnit.HOURS),
                now.minus(20, ChronoUnit.HOURS),
                VALID_EMAIL,
                2));
    }

    @Test
    void shouldAllowCancellingOfNewBooking() {
        // given
        final Booking booking = createValidBooking();
        // when
        booking.cancel();
        // then
        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
    }

    @Test
    void shouldAllowConfirmingOfNewBooking() {
        // given
        final Booking booking = createValidBooking();
        // when
        booking.confirm();
        // then
        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
    }

    @Test
    void shouldDenyConfirmingOfCancelledBooking() {
        // given
        final Booking booking = createValidBooking();
        booking.cancel();
        // when
        assertThrows(IllegalBookingStateException.class, booking::confirm);
    }

    private Booking createValidBooking() {
        return Booking.createNewBooking(validFromTime, validToTime, VALID_EMAIL, 2);
    }
}
