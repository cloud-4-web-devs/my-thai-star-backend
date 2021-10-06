package cloud4webdevs.mythaistar.booking.port.in;

import cloud4webdevs.mythaistar.booking.domain.TableId;
import cloud4webdevs.mythaistar.booking.domain.exception.BookingValidationException;
import lombok.NonNull;
import lombok.Value;

import java.time.Instant;
import java.util.Optional;
import java.util.regex.Pattern;

@Value
public class CreateBookingCommand {

    Instant bookingFrom;

    Instant bookingTo;

    String email;

    int seatsNumber;

    Optional<TableId> suggestedTable;

    public CreateBookingCommand(@NonNull Instant bookingFrom, @NonNull Instant bookingTo, @NonNull String email, int seatsNumber, TableId suggestedTable) {
        this.bookingFrom = bookingFrom;
        this.bookingTo = bookingTo;
        this.email = email;
        this.seatsNumber = seatsNumber;
        this.suggestedTable = Optional.ofNullable(suggestedTable);
        if (bookingFrom.isAfter(bookingTo)) {
            throw new BookingValidationException("Invalid booking window");
        }
        checkEmail(this.email);
        checkSeatsNumber(this.seatsNumber);
    }

    private static void checkEmail(String email) {
        final String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        final Pattern pattern = Pattern.compile(regex);
        if (!pattern.matcher(email).matches()) {
            throw new BookingValidationException("Invalid email provided.");
        }
    }

    private static void checkSeatsNumber(int seatsNumber) {
        if (seatsNumber < 1) {
            throw new BookingValidationException(String.format("Illegal number of seats in the booking: %d.", seatsNumber));
        }
    }
}
