package ar.edu.unlam.tallerweb1.infrastructure;

import ar.edu.unlam.tallerweb1.domain.model.FriendsGroup;
import ar.edu.unlam.tallerweb1.domain.model.SharedExpent;
import ar.edu.unlam.tallerweb1.domain.model.User;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static ar.edu.unlam.tallerweb1.infrastructure.utils.TestsPersistenceHelper.*;
import static org.assertj.core.api.Assertions.assertThat;

public class SharedExpensesJdbcRepositoryTest extends JdbcRepositoryTest {

    private final String USER_1 = "user_1";
    private final String USER_2 = "user_2";
    private final String USER_3 = "user_3";
    private final String GROUP = "group_1";
    private SharedExpensesJdbcRepository repository = new SharedExpensesJdbcRepository(dataSource);

    @Test
    public void getSharedExpensesIfExist() throws SQLException {
        givenUserHasAGroupWithThreeExpenses(USER_1);
        final List<SharedExpent> sharedExpenses = whenSearchExpensesOfUserGroup(USER_1);
        thenGetThreeExpenses(sharedExpenses);
    }

    @Test
    public void getEmptySharedExpensesIfNotExist() throws SQLException {
        givenUserHasAGroupWithOutExpenses(USER_1);
        final List<SharedExpent> sharedExpenses = whenSearchExpensesOfUserGroup(USER_1);
        thenNotGetExpenses(sharedExpenses);
    }

    @Test
    public void shouldAddAnExpent() throws SQLException {
        final Long friendsGroupId = givenAgroup(GROUP);
        final User owner = givenAGroupUser(USER_1, friendsGroupId);
        whenAddAnExpent(owner, friendsGroupId);
        thenThereIsOneExpentInGroupOf(USER_1);
    }

    private Long givenUserHasAGroupWithOutExpenses(String user) throws SQLException {
        createGroup(connection, GROUP);
        final Long friendsGroupId = getFriendsGroupId(connection, GROUP);
        createUser(connection, user, friendsGroupId);
        return friendsGroupId;
    }

    private void givenUserHasAGroupWithThreeExpenses(String user) throws SQLException {
        createGroup(connection, GROUP);
        final Long friendsGroupId = getFriendsGroupId(connection, GROUP);
        createUser(connection, user, friendsGroupId);
        createUser(connection, USER_2, friendsGroupId);
        createUser(connection, USER_3, friendsGroupId);
        createExpent(connection, getUserId(connection, USER_2), friendsGroupId, 123.0, "Cena");
        createExpent(connection, getUserId(connection, USER_2), friendsGroupId, 30.0, "Desayuno");
        createExpent(connection, getUserId(connection, USER_3), friendsGroupId, 100.5, "Almuerzo");
    }

    private void whenAddAnExpent(User owner, Long groupId) {
        SharedExpent expent = new SharedExpent(owner, 333.3, "detail", LocalDateTime.now(), new FriendsGroup(groupId, ""));
        repository.save(expent);
    }

    private List<SharedExpent> whenSearchExpensesOfUserGroup(String user) {
        return getExpentBy(user);
    }

    private List<SharedExpent> getExpentBy(String user) {
        return repository.findBy(user);
    }

    private void thenGetThreeExpenses(List<SharedExpent> sharedExpenses) {
        assertThat(sharedExpenses).hasSize(3);
    }

    private void thenNotGetExpenses(List<SharedExpent> sharedExpenses) {
        assertThat(sharedExpenses).isEmpty();
    }

    private void thenThereIsOneExpentInGroupOf(String user) {
        assertThat(getExpentBy(user)).hasSize(1);
    }


}
