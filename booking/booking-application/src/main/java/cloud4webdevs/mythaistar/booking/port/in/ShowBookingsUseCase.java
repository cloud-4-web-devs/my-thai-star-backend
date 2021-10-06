package cloud4webdevs.mythaistar.booking.port.in;

import java.util.List;

public interface ShowBookingsUseCase {

    List<ShowBookingsResult> showBookings(ShowBookingsQuery showBookingsQuery);
}
