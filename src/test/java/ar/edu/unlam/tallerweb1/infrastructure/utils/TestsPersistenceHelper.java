package ar.edu.unlam.tallerweb1.infrastructure.utils;

import ar.edu.unlam.tallerweb1.domain.model.ExpentStatus;

import java.sql.*;

public class TestsPersistenceHelper {

    public static void createUser(Connection connection, String user, Long friendsGroupId) throws SQLException {
        final PreparedStatement psUser = connection.prepareStatement("INSERT INTO user (name, friends_group_id) VALUES (?,?)");
        psUser.setString(1, user);
        psUser.setLong(2, friendsGroupId);
        psUser.execute();
    }

    public static void createGroup(Connection connection, String group) throws SQLException {
        connection.createStatement().execute("INSERT INTO friends_group (name) VALUES ('"+group+"')");
    }

    public static void createExpent(Connection connection, Long owner, Long friendsGroupId, Double amount, String detail) throws SQLException {
        final PreparedStatement ps = connection.prepareStatement("INSERT INTO shared_expenses " +
                "(friends_group_id, owner, amount, detail, status, date) VALUES (?,?,?,?,?,?)");
        ps.setLong(1, friendsGroupId);
        ps.setLong(2, owner);
        ps.setDouble(3, amount);
        ps.setString(4, detail);
        ps.setString(5, ExpentStatus.OPEN.name());
        ps.setDate(6, new Date(System.currentTimeMillis()));
        ps.execute();
    }

    public static Long getFriendsGroupId(Connection connection, String group) throws SQLException {
        final ResultSet results = connection.createStatement().executeQuery("select id from friends_group where name = '"+group+"'");
        results.next();
        return Long.valueOf(results.getString("id"));
    }

    public static Long getUserId(Connection connection, String user) throws SQLException {
        final ResultSet results = connection.createStatement().executeQuery("select id from user where name = '"+user+"'");
        results.next();
        return Long.valueOf(results.getString("id"));
    }

    public static void cleanExpenses(Connection connection) throws SQLException {
        connection.createStatement().execute("delete from shared_expenses");
    }
}
