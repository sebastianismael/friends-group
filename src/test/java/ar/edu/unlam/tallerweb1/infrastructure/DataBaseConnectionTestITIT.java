package ar.edu.unlam.tallerweb1.infrastructure;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

public class DataBaseConnectionTestITIT extends JdbcRepositoryTestIT {

    @Test
    public void checkDbConnection(){
        try {
            assertFalse(connection.isClosed());
        } catch (SQLException e) {
            fail(e);
        }
    }
}
