package cloud4webdevs.mythaistar.booking.service;

import cloud4webdevs.mythaistar.booking.domain.Booking;
import cloud4webdevs.mythaistar.booking.domain.exception.BookingNotFoundException;
import cloud4webdevs.mythaistar.booking.domain.exception.BookingValidationException;
import cloud4webdevs.mythaistar.booking.port.in.CancelBookingCommand;
import cloud4webdevs.mythaistar.booking.port.in.CancelBookingUseCase;
import cloud4webdevs.mythaistar.booking.port.out.FindBookingByTokenPort;
import cloud4webdevs.mythaistar.booking.port.out.SaveBookingPort;
import cloud4webdevs.mythaistar.common.port.out.TransactionPort;
import lombok.AllArgsConstructor;

import java.time.Duration;
import java.time.Instant;

@AllArgsConstructor
public class CancelBookingService implements CancelBookingUseCase {

    private final FindBookingByTokenPort findBookingByTokenPort;

    private final SaveBookingPort saveBookingPort;

    private final TransactionPort transactionPort;

    @Override
    public void cancel(CancelBookingCommand command) {
        transactionPort.withTransaction(() -> {
            final Booking booking = findBookingByTokenPort.find(command.getToken()).orElseThrow(() -> new BookingNotFoundException(command.getToken()));

            final Instant now = Instant.now();
            if (now.plus(Duration.ofHours(4L)).isAfter(booking.getBookingFromTime())) {
                throw new BookingValidationException("Too late to cancel the booking.");
            }

            booking.cancel();
            saveBookingPort.save(booking);
        });
    }
}
