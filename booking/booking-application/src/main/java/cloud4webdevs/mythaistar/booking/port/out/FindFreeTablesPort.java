package cloud4webdevs.mythaistar.booking.port.out;

import cloud4webdevs.mythaistar.booking.domain.Table;

import java.time.Instant;
import java.util.List;

/**
 * Finds free (unbooked) tables for given time period.
 */
public interface FindFreeTablesPort {
    List<Table> find(Instant from, Instant to);
}
