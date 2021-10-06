package cloud4webdevs.mythaistar.booking.domain.exception;

import cloud4webdevs.mythaistar.common.domain.exception.BusinessException;
import cloud4webdevs.mythaistar.booking.domain.BookingId;
import cloud4webdevs.mythaistar.booking.domain.BookingStatus;

import java.util.Optional;

public class IllegalBookingStateException extends BusinessException {

    public IllegalBookingStateException(BookingId id, BookingStatus from, BookingStatus to) {
        super(String.format("Cannot change status of booking %s: illegal transition %s -> %s.",
                Optional.ofNullable(id)
                        .map(BookingId::getValue)
                        .map(String::valueOf)
                        .orElse("(not persisted)"),
                from.name(),
                to.name()));
    }
}
