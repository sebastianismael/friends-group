package ar.edu.unlam.tallerweb1.infrastructure;

import ar.edu.unlam.tallerweb1.domain.model.User;
import ar.edu.unlam.tallerweb1.infrastructure.utils.DataSource;
import ar.edu.unlam.tallerweb1.infrastructure.utils.HSQLDB4TestDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static ar.edu.unlam.tallerweb1.infrastructure.utils.TestsPersistenceHelper.*;

public abstract class JdbcRepositoryTestIT {

    protected Connection connection;
    protected DataSource dataSource = HSQLDB4TestDataSource.instance();

    @BeforeEach
    public void init() throws SQLException {
        connection = dataSource.getConnection();
    }
    @AfterEach
    public void tearDown() throws SQLException {
        connection.createStatement().execute("delete from payment");
        connection.createStatement().execute("delete from shared_expenses");
        connection.createStatement().execute("delete from user");
        connection.createStatement().execute("delete from friends_group");
        connection.close();
    }

    protected User givenAGroupUser(String user, Long friendsGroupId) throws SQLException {
        createUser(connection, user, friendsGroupId);
        return new User(getUserId(connection, user), user);
    }

    protected Long givenAgroup(String groupName) throws SQLException {
        createGroup(connection, groupName);
        return getFriendsGroupId(connection, groupName);
    }
    protected long givenAnExpent(Long friendsGroupId, String user, Double amount, String detail) throws SQLException {
        createExpent(connection, getUserId(connection, user), friendsGroupId, 120.0, "Cena");
        return getExpentId();
    }

    private long getExpentId() throws SQLException {
        final ResultSet results = connection.createStatement().executeQuery("select id from shared_expenses");
        results.next();
        return results.getLong("id");
    }

    protected void givenAPayment(Long user, double amount, Long expentId) throws SQLException {
        final String sql = "INSERT INTO payment (payer, expent_id, amount) VALUES (?,?,?)";
        final PreparedStatement psUser = connection.prepareStatement(sql);
        psUser.setLong(1, user);
        psUser.setLong(2, expentId);
        psUser.setDouble(3, amount);
        psUser.execute();
    }
}
