package cloud4webdevs.mythaistar.booking.domain;

import lombok.*;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString
public class Table {
    @Getter @NonNull private final TableId id;

    @Getter private final int maxSeats;
}
