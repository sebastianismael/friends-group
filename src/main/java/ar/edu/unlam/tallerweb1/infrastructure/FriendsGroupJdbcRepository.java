package ar.edu.unlam.tallerweb1.infrastructure;

import ar.edu.unlam.tallerweb1.domain.FriendsGroupRepository;
import ar.edu.unlam.tallerweb1.domain.model.FriendsGroup;
import ar.edu.unlam.tallerweb1.domain.model.User;
import ar.edu.unlam.tallerweb1.infrastructure.utils.DataSource;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

import static ar.edu.unlam.tallerweb1.infrastructure.utils.JdbcUtil.*;
import static java.lang.Long.valueOf;

public class FriendsGroupJdbcRepository extends JdbcRepository implements FriendsGroupRepository {
    public FriendsGroupJdbcRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<User> getMembers(FriendsGroup group) {
        return searchInTransaction((connection, groupId) -> {
            List<User> found = new LinkedList<>();

            final String sql = "select * from user where friends_group_id = ?";
            final PreparedStatement ps = prepareStatement(connection, sql);
            setLong(1, valueOf(groupId), ps);
            final ResultSet users = executeQuery(ps);
            while(next(users)){
                found.add(new User(getLong("id", users), getString("name", users)));
            }
            return found;
        }, group.getId().toString());
    }

    @Override
    public FriendsGroup getGroupOf(User user) {
        return findInTransaction((connection, username) -> {
            final PreparedStatement ps = prepareStatement(connection, "select friends_group_id from user where name = ?");
            setString(1, username, ps);
            final ResultSet rs = executeQuery(ps);
            if(next(rs)){
                Long groupId = getLong("friends_group_id", rs);
                final String sql = "select * from friends_group where id = ?";
                final PreparedStatement psGroup = prepareStatement(connection, sql);
                setLong(1, groupId, psGroup);
                final ResultSet rsGroup = executeQuery(psGroup);
                if(next(rsGroup))
                    return new FriendsGroup(groupId, getString("name", rsGroup));
            }
            return null;
        }, user.getName());
    }
}
