package ar.edu.unlam.tallerweb1.infrastructure;

import ar.edu.unlam.tallerweb1.domain.UserRepository;
import ar.edu.unlam.tallerweb1.domain.model.FriendsGroup;
import ar.edu.unlam.tallerweb1.domain.model.User;
import ar.edu.unlam.tallerweb1.infrastructure.utils.DataSource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static ar.edu.unlam.tallerweb1.infrastructure.utils.JdbcUtil.*;

public class UserJdbcRepostory extends JdbcRepository implements UserRepository {
    public UserJdbcRepostory(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public void save(User user) {
        executeInTransaction((connection) -> {
            final String sql = "INSERT INTO user (name) VALUES (?)";
            final PreparedStatement ps = prepareStatement(connection, sql);
            setString(1, user.getName(), ps);
            execute(ps);
        });
    }

    public void saveWithFriendGroup(User user) {
        executeInTransaction((connection) -> {
            final String sql = "INSERT INTO user (name, friends_group_id) VALUES (?,?)";
            final PreparedStatement ps = prepareStatement(connection, sql);
            setString(1, user.getName(), ps);
            setLong(2, user.getFriendsGroup().getId(), ps);
            execute(ps);
        });
    }

    @Override
    public User findByName(String name) {
        return findInTransaction((connection, username) -> {
            final String sql = "select * from user where name = ?";
            final PreparedStatement ps = prepareStatement(connection, sql);
            setString(1, username, ps);
            final ResultSet users = executeQuery(ps);
            if(next(users)){
                final User user = new User(getLong("id", users), getString("name", users));
                user.setFriendsGroup(new FriendsGroup(getLong("friends_group_id", users), ""));
                return user;
            }
            return null;
        }, name);
    }
}
