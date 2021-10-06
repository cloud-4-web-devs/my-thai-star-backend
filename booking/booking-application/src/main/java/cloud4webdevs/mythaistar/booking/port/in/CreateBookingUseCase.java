package cloud4webdevs.mythaistar.booking.port.in;

/**
 * Books a table of given size and for given period of time.
 */
public interface CreateBookingUseCase {
    CreateBookingResult createBooking(CreateBookingCommand command);
}
