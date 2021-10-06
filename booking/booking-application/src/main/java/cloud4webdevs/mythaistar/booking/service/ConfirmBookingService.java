package cloud4webdevs.mythaistar.booking.service;

import cloud4webdevs.mythaistar.booking.domain.Booking;
import cloud4webdevs.mythaistar.booking.domain.exception.BookingNotFoundException;
import cloud4webdevs.mythaistar.booking.domain.exception.IncompleteBookingException;
import cloud4webdevs.mythaistar.booking.port.in.ConfirmBookingCommand;
import cloud4webdevs.mythaistar.booking.port.in.ConfirmBookingUseCase;
import cloud4webdevs.mythaistar.booking.port.out.FindBookingByTokenPort;
import cloud4webdevs.mythaistar.booking.port.out.SaveBookingPort;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ConfirmBookingService implements ConfirmBookingUseCase {

    private final FindBookingByTokenPort findBookingByTokenPort;

    private final SaveBookingPort saveBookingPort;

    @Override
    public void confirm(ConfirmBookingCommand command) {
        final Booking booking = findBookingByTokenPort.find(command.getToken()).orElseThrow(() -> new BookingNotFoundException(command.getToken()));
        if (!booking.hasSufficientTables()) {
            throw new IncompleteBookingException(booking.getId(), "lack of table booking");
        }
        booking.confirm();
        saveBookingPort.save(booking);
    }
}
