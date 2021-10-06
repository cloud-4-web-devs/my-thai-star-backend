package cloud4webdevs.mythaistar.booking.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class TableBooking {

    @Getter
    @NonNull
    private final TableId tableId;

    @Getter
    private final int seatsNumber;

    public static TableBooking createTableBooking(@NonNull TableId tableId, int seatsNumber) {
        return new TableBooking(tableId, seatsNumber);
    }
}
