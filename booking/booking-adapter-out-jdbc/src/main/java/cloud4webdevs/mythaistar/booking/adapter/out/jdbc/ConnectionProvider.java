package cloud4webdevs.mythaistar.booking.adapter.out.jdbc;

import java.sql.Connection;

public interface ConnectionProvider {
    Connection getConnection();
}
