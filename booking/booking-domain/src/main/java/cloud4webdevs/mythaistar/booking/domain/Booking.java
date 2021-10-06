package cloud4webdevs.mythaistar.booking.domain;

import cloud4webdevs.mythaistar.booking.domain.exception.BookingValidationException;
import cloud4webdevs.mythaistar.booking.domain.exception.IllegalBookingStateException;
import cloud4webdevs.mythaistar.common.domain.exception.BusinessException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Booking {
    @Getter
    private final BookingId id;

    @Getter
    private final Instant creationDate;

    @Getter
    private final Instant bookingFromTime;

    @Getter
    private final Instant bookingToTime;

    @Getter
    private final Instant bookingDate;

    @Getter
    private final Instant expirationDate;

    @Getter
    private final String email;

    @Getter
    private int seatsNumber;

    @Getter
    private BookingStatus status;

    @Getter
    private final String token;

    @Getter
    @NonNull
    private final Set<TableBooking> tableBookings;

    /**
     * Create new booking instance.
     */
    public static Booking createNewBooking(@NonNull Instant bookingFromTime, @NonNull Instant bookingToTime, @NonNull String email, int seatsNumber) {
        return createNewBooking(null, bookingFromTime, bookingToTime, email, seatsNumber);
    }

    /**
     * For tests mostly.
     */
    public static Booking createNewBooking(BookingId id, @NonNull Instant bookingFromTime, @NonNull Instant bookingToTime, @NonNull String email, int seatsNumber) {
        final Instant now = Instant.now();
        final Duration expiry = Duration.ofDays(1);
        final Instant bookingDate = bookingFromTime.truncatedTo(ChronoUnit.DAYS);
        if (bookingToTime.isBefore(bookingFromTime)) {
            throw new BookingValidationException(String.format("The booking time is invalid, end time %2$s precedes start time %1$s.", bookingFromTime, bookingToTime));
        }
        if (bookingFromTime.isBefore(now.plus(expiry))) {
            throw new BookingValidationException(String.format("The booking date %s is too late to do the booking for given time.", bookingDate));
        }

        return createNewBooking(id, now, bookingFromTime, bookingToTime, bookingDate, bookingDate.minus(expiry), email, seatsNumber, BookingStatus.NEW,
                buildToken(email, "CB_"));
    }

    /**
     * For adapters.
     */
    public static Booking createNewBooking(BookingId id, @NonNull Instant creationDate, @NonNull Instant bookingFromTime,
                                           @NonNull Instant bookingToTime, @NonNull Instant bookingDate, @NonNull Instant expirationDate,
                                           @NonNull String email, int seatsNumber, @NonNull BookingStatus status, @NonNull String token) {
        return new Booking(
                id,
                creationDate,
                bookingFromTime,
                bookingToTime,
                bookingDate,
                expirationDate,
                email,
                seatsNumber,
                status,
                token,
                new HashSet<>());
    }

    private static String buildToken(String email, String type) {

        // TODO: please refactor this ugly code from original mts
        Instant now = Instant.now();
        LocalDateTime ldt1 = LocalDateTime.ofInstant(now, ZoneId.systemDefault());
        String date = String.format("%04d", ldt1.getYear()) + String.format("%02d", ldt1.getMonthValue())
                + String.format("%02d", ldt1.getDayOfMonth()) + "_";

        String time = String.format("%02d", ldt1.getHour()) + String.format("%02d", ldt1.getMinute())
                + String.format("%02d", ldt1.getSecond());

        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new BusinessException(e);
        }
        md.update((email + date + time).getBytes());
        byte[] digest = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return type + date + sb;
    }

    /**
     * Checks whether Booking is already persisted in the booking storage.
     */
    public boolean isPersisted() {
        return id != null;
    }

    /**
     * Checks if given table is already included in this Booking.
     */
    public boolean containsTable(@NonNull TableId tableId) {
        return findTable(tableId).isPresent();
    }

    /**
     * Returns TableBooking for given table if exists.
     */
    public Optional<TableBooking> findTable(@NonNull TableId tableId) {
        return tableBookings.stream().filter(tableBooking -> tableId.equals(tableBooking.getTableId())).findFirst();
    }

    /**
     * Returns set of TableIds as contained in this Booking.
     */
    public Stream<TableId> bookingsAsTableIdStream() {
        return tableBookings.stream().map(TableBooking::getTableId);
    }

    /**
     * Checks is booked tables capacity fulfils overall Booking size.
     */
    public boolean hasSufficientTables() {
        return tableBookings.stream().mapToInt(TableBooking::getSeatsNumber).sum() >= seatsNumber;
    }

    /**
     * Cancels booking.
     */
    public void cancel() {
        if (status == BookingStatus.NEW || status == BookingStatus.CONFIRMED) {
            status = BookingStatus.CANCELLED;
            tableBookings.clear();
        } else {
            throw new IllegalBookingStateException(id, status, BookingStatus.CANCELLED);
        }
    }

    /**
     * Confirms booking.
     */
    public void confirm() {
        if (status == BookingStatus.NEW) {
            status = BookingStatus.CONFIRMED;
        } else {
            throw new IllegalBookingStateException(id, status, BookingStatus.CONFIRMED);
        }
    }

    /**
     * Adds new table to the booking.
     */
    public void addTableBooking(@NonNull Table table) {
        addTableBooking(table.getId(), table.getMaxSeats());
    }

    /**
     * Change number of seats in the booking.
     * <p>
     * TODO tricky, maybe we should disallow this; use an use case for this and drop & create new booking instead.
     * But maybe we should just treat it as eventual consistency case?
     */
    public void changeBookingSize(int newSeatsNumber) {
        seatsNumber = newSeatsNumber;
    }

    /**
     * Adds new table to the booking.
     */
    private void addTableBooking(@NonNull TableId tableId, int seatsNumber) {
        if (!isPersisted()) {
            throw new BookingValidationException("The booking must be persisted to add tables to it.");
        }
        if (containsTable(tableId)) {
            throw new BookingValidationException(String.format("The booking %d contains table %d already.", id.getValue(), tableId.getValue()));
        }
        tableBookings.add(TableBooking.createTableBooking(tableId, seatsNumber));
    }
}
