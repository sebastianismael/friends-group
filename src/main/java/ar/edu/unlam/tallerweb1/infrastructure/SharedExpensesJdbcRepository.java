package ar.edu.unlam.tallerweb1.infrastructure;

import ar.edu.unlam.tallerweb1.domain.SharedExpensesRepository;
import ar.edu.unlam.tallerweb1.domain.model.ExpentStatus;
import ar.edu.unlam.tallerweb1.domain.model.FriendsGroup;
import ar.edu.unlam.tallerweb1.domain.model.SharedExpent;
import ar.edu.unlam.tallerweb1.domain.model.User;
import ar.edu.unlam.tallerweb1.infrastructure.utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

import static ar.edu.unlam.tallerweb1.infrastructure.utils.JdbcUtil.*;

public class SharedExpensesJdbcRepository extends JdbcRepository implements SharedExpensesRepository {

    public SharedExpensesJdbcRepository(DataSource dataSource) {
        super(dataSource);
    }

    public List<SharedExpent> findBy(String user) {
        return searchInTransaction((connection, _user) -> {

            List<SharedExpent> found = new LinkedList<>();

            final ResultSet group = searchGroupOf(connection, _user);
            if(exist(group)){
                final FriendsGroup friendsGroup = buildFriendsGroup(group);
                final ResultSet expenses = searchSharedExpenses(connection, group);
                while(exist(expenses)) {
                    final User owner = searchOwner(connection, getLong("owner", expenses));
                    found.add(buildExpent(expenses, friendsGroup, owner));
                }
            }
            return found;
        }, user);
    }

    @Override
    public void save(SharedExpent sharedExpent) {
        executeInTransaction(connection -> {
            final String sql = "INSERT INTO shared_expenses " +
                    "(friends_group_id, owner, amount, detail, status, date) " +
                    "VALUES (?,?,?,?,?,?)";
            final PreparedStatement ps = prepareStatement(connection, sql);
            setLong(1, sharedExpent.getFriendsGroup().getId(), ps);
            setLong(2, sharedExpent.getOwner().getId(), ps);
            setDouble(3, sharedExpent.getAmount(), ps);
            setString(4, sharedExpent.getDetail(), ps);
            setString(5, ExpentStatus.OPEN.name(), ps);
            setDate(6, sharedExpent.getDate(), ps);
            execute(ps);
            return null;
        });
    }

    private FriendsGroup buildFriendsGroup(ResultSet group) {
        return new FriendsGroup(getLong("friends_group_id", group), "");
        // TODO obtener el nombre grupo, ver si es necesario
    }

    private SharedExpent buildExpent(ResultSet expenses, FriendsGroup friendsGroup, User owner) {
        return new SharedExpent(
                getLong("id", expenses),
                owner,
                getDouble("amount", expenses),
                getString("detail", expenses),
                getDate("date", expenses),
                friendsGroup
        );
    }

    private boolean exist(ResultSet resultSet) {
        return next(resultSet);
    }

    private User searchOwner(Connection connection, Long userId){
        final String sql = "select * from user where id = ?";
        final PreparedStatement expensesStatement = prepareStatement(connection, sql);
        setLong(1, userId, expensesStatement);
        final ResultSet users = executeQuery(expensesStatement);
        if(next(users))
            return new User(getLong("id", users), getString("name", users));
        return null;
    }

    private ResultSet searchSharedExpenses(Connection connection, ResultSet resultSet) {
        final String sql = "select * from shared_expenses where friends_group_id = ? and status = 'OPEN' order by date desc";
        final PreparedStatement expensesStatement = prepareStatement(connection, sql);
        setLong(1, getLong("friends_group_id", resultSet), expensesStatement);
        return executeQuery(expensesStatement);
    }

    private ResultSet searchGroupOf(Connection connection, String userName) {
        final String sql = "select * from user where name = ?";
        final PreparedStatement userStatement = prepareStatement(connection, sql);
        setString(1, userName, userStatement);
        return executeQuery(userStatement);
    }

}
