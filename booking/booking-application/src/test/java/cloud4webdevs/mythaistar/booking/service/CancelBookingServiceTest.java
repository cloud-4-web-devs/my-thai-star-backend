package cloud4webdevs.mythaistar.booking.service;

import cloud4webdevs.mythaistar.booking.domain.Booking;
import cloud4webdevs.mythaistar.booking.domain.BookingId;
import cloud4webdevs.mythaistar.booking.domain.BookingStatus;
import cloud4webdevs.mythaistar.booking.domain.Table;
import cloud4webdevs.mythaistar.booking.domain.TableId;
import cloud4webdevs.mythaistar.booking.domain.exception.BookingNotFoundException;
import cloud4webdevs.mythaistar.booking.domain.exception.BookingValidationException;
import cloud4webdevs.mythaistar.booking.port.in.CancelBookingCommand;
import cloud4webdevs.mythaistar.booking.port.out.FindBookingByTokenPort;
import cloud4webdevs.mythaistar.booking.port.out.SaveBookingPort;
import cloud4webdevs.mythaistar.common.adapter.out.TestTransactionAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

class CancelBookingServiceTest {

    private static final String VALID_EMAIL = "john.doe@acme.inc";

    private final Instant now = Instant.now();

    private final Instant validFromTime = now.plus(25, ChronoUnit.HOURS);

    private final Instant validToTime = now.plus(27, ChronoUnit.HOURS);

    private final String token = "CB_467365462647836274673";

    private FindBookingByTokenPort findBookingByTokenPort;


    private TestTransactionAdapter testTransactionAdapter;

    private CancelBookingService cancelBookingService;

    private SaveBookingPort saveBookingPort;


    @BeforeEach
    public void setUpMocks() {
        testTransactionAdapter = new TestTransactionAdapter();
        findBookingByTokenPort = mock(FindBookingByTokenPort.class);
        saveBookingPort = mock(SaveBookingPort.class);
        cancelBookingService = new CancelBookingService(findBookingByTokenPort, saveBookingPort, testTransactionAdapter);
    }

    @Test
    public void shouldThrowExceptionIfBookingNotFound() {
        // given
        when(findBookingByTokenPort.find(token)).thenReturn(Optional.empty());
        final CancelBookingCommand command = new CancelBookingCommand(token);
        // then
        assertThrows(BookingNotFoundException.class, () -> cancelBookingService.cancel(command));
    }

    @Test
    public void shouldThrowExceptionIfCancellationIsTooLate() {
        // given
        final Instant now = Instant.now();
        final Booking booking = Booking.builder()
                .bookingFromTime(now.plus(Duration.ofHours(3)))
                .status(BookingStatus.NEW)
                .tableBookings(new HashSet<>())
                .build();
        when(findBookingByTokenPort.find(token)).thenReturn(Optional.of(booking));
        final CancelBookingCommand command = new CancelBookingCommand(token);
        // then
        assertThrows(BookingValidationException.class, () -> cancelBookingService.cancel(command));
    }

    @Test
    public void shouldCancelBookingAndReleaseTable() {
        // given
        final BookingId id = new BookingId(678L);
        final Booking booking = Booking.createNewBooking(
                id,
                validFromTime,
                validToTime,
                VALID_EMAIL,
                5);
        final Table table = Table.builder()
                .id(new TableId(456))
                .maxSeats(10)
                .build();
        booking.addTableBooking(table);

        when(findBookingByTokenPort.find(token)).thenReturn(Optional.of(booking));
        final CancelBookingCommand command = new CancelBookingCommand(token);
        // when
        cancelBookingService.cancel(command);
        // then
        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
        assertTrue(testTransactionAdapter.hasBeenCalled());
        verify(saveBookingPort).save(booking);
    }
}
