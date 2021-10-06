package cloud4webdevs.mythaistar.booking.domain.exception;

import cloud4webdevs.mythaistar.common.domain.exception.BusinessException;

public class BookingValidationException extends BusinessException {
    public BookingValidationException(String msg) {
        super(msg);
    }
}
