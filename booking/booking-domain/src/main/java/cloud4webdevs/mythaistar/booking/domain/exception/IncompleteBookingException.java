package cloud4webdevs.mythaistar.booking.domain.exception;

import cloud4webdevs.mythaistar.common.domain.exception.BusinessException;
import cloud4webdevs.mythaistar.booking.domain.BookingId;

public class IncompleteBookingException extends BusinessException {
    public IncompleteBookingException(BookingId id, String reason) {
        super(String.format("The booking %d is incomplete due to %s.", id.getValue(), reason));
    }
}
