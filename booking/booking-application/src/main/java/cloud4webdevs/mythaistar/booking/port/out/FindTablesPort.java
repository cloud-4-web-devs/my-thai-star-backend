package cloud4webdevs.mythaistar.booking.port.out;

import cloud4webdevs.mythaistar.booking.domain.Table;

import java.util.List;

public interface FindTablesPort {
    List<Table> findTables();
}
