package cloud4webdevs.mythaistar.booking.port.in;

import java.util.List;

public interface ShowTablesUseCase {
    List<ShowTablesResult> showTables(ShowTablesQuery showTablesQuery);
}
