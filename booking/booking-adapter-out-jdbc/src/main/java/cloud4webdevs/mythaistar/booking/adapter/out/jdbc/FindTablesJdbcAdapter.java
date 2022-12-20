package cloud4webdevs.mythaistar.booking.adapter.out.jdbc;

import cloud4webdevs.mythaistar.booking.domain.Table;
import cloud4webdevs.mythaistar.booking.domain.TableId;
import cloud4webdevs.mythaistar.booking.port.out.FindTablesPort;
import lombok.RequiredArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class FindTablesJdbcAdapter implements FindTablesPort {
    final ConnectionProvider connectionProvider;

    @Override
    public List<Table> findTables() {
        final var tables = new ArrayList<Table>();
        try {
            final var stmt = connectionProvider.getConnection()
                    .prepareStatement("SELECT id, max_seats from public.table_entity");
            ResultSet results = stmt.executeQuery();
            while (results.next()) {
                final var id = results.getLong(1);
                final var maxSeats = results.getInt(2);
                final var table = Table.builder()
                        .id(new TableId(id))
                        .maxSeats(maxSeats)
                        .build();
                tables.add(table);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Executing database query failed!", e);
        }
        return tables;
    }
}
