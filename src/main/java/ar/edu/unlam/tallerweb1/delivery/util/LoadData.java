package ar.edu.unlam.tallerweb1.delivery.util;

import ar.edu.unlam.tallerweb1.delivery.Controller;
import ar.edu.unlam.tallerweb1.domain.model.ExpentStatus;
import ar.edu.unlam.tallerweb1.domain.model.User;
import ar.edu.unlam.tallerweb1.infrastructure.utils.MySqlDataSource;

import javax.servlet.http.HttpServletRequest;
import java.sql.*;

public class LoadData implements Controller {
    protected Connection connection;
    private final String USER_1 = "Ana";
    private final String USER_2 = "Pedro";
    private final String USER_3 = "Silvia";
    private final String USER_4 = "Hector";
    private final String GROUP = "group_1";
    @Override
    public String invoke(HttpServletRequest request) {

        try {
            connection = MySqlDataSource.instance().getConnection();

            final Long friendsGroupId = createAgroup(GROUP);
            createUser(USER_1, friendsGroupId);
            createUser(USER_2, friendsGroupId);
            final Long user3Id = createUser(USER_3, friendsGroupId).getId();
            createUser(USER_4, friendsGroupId);
            final Long expent1Id = createAnExpent(friendsGroupId, USER_1, 120.0, "Cena");
            final Long expent2Id = createAnExpent(friendsGroupId, USER_2, 120.0, "Almuerzo");

            createAPayment(user3Id, 30.0, expent1Id);
            createAPayment(user3Id, 30.0, expent2Id);

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return "Data loaded!";
    }

    protected void createAPayment(Long user, double amount, Long expentId) throws SQLException {
        final String sql = "INSERT INTO payment (payer, expent_id, amount) VALUES (?,?,?)";
        final PreparedStatement psUser = connection.prepareStatement(sql);
        psUser.setLong(1, user);
        psUser.setLong(2, expentId);
        psUser.setDouble(3, amount);
        psUser.execute();
    }

    protected User createUser(String user, Long friendsGroupId) throws SQLException {
        final PreparedStatement psUser = connection.prepareStatement("INSERT INTO user (name, friends_group_id) VALUES (?,?)");
        psUser.setString(1, user);
        psUser.setLong(2, friendsGroupId);
        psUser.execute();
        return new User(getUserId(connection, user), user);
    }

    protected long createAnExpent(Long friendsGroupId, String user, Double amount, String detail) throws SQLException {
        final PreparedStatement ps = connection.prepareStatement("INSERT INTO shared_expenses " +
                "(friends_group_id, owner, amount, detail, status, date) VALUES (?,?,?,?,?,?)");
        ps.setLong(1, friendsGroupId);
        ps.setLong(2, getUserId(connection, user));
        ps.setDouble(3, amount);
        ps.setString(4, detail);
        ps.setString(5, ExpentStatus.OPEN.name());
        ps.setDate(6, new Date(System.currentTimeMillis()));
        ps.execute();

        final ResultSet results = connection.createStatement().executeQuery("select id from shared_expenses");
        results.next();
        return results.getLong("id");
    }

    public static Long getUserId(Connection connection, String user) throws SQLException {
        final ResultSet results = connection.createStatement().executeQuery("select id from user where name = '"+user+"'");
        results.next();
        return Long.valueOf(results.getString("id"));
    }

    protected Long createAgroup(String groupName) throws SQLException {
        connection.createStatement().execute("INSERT INTO friends_group (name) VALUES ('"+groupName+"')");
        final ResultSet results = connection.createStatement().executeQuery("select id from friends_group where name = '"+groupName+"'");
        results.next();
        return Long.valueOf(results.getString("id"));
    }

}
