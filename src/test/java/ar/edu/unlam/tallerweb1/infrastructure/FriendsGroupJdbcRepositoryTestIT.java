package ar.edu.unlam.tallerweb1.infrastructure;

import ar.edu.unlam.tallerweb1.domain.FriendsGroupRepository;
import ar.edu.unlam.tallerweb1.domain.model.FriendsGroup;
import ar.edu.unlam.tallerweb1.domain.model.User;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static ar.edu.unlam.tallerweb1.infrastructure.utils.TestsPersistenceHelper.*;
import static org.assertj.core.api.Assertions.assertThat;

public class FriendsGroupJdbcRepositoryTestIT extends JdbcRepositoryTestIT {

    private final String USER_1 = "user_1";
    private final String USER_2 = "user_2";
    private final String USER_3 = "user_3";
    private final String GROUP_A = "group_A";
    private final String GROUP_B = "group_B";
    private final String OTHER = "other";

    private FriendsGroupRepository friendsGroupRepository = new FriendsGroupJdbcRepository(dataSource);
    @Test
    public void shouldReturnMemebersWhenGroupHasFriends() throws SQLException {
        final Long groupId = givenUserHasAGroupWithTwoFriends(USER_1, GROUP_A);
        givenUserHasAGroupWithTwoFriends(OTHER, GROUP_B);
        final List<User> members = whenGetGroupMembers(groupId);
        thenGroupHasThreeMemebers(members);
    }

    @Test
    public void shouldReturnNoMemebersWhenGroupIsEmpty() throws SQLException {
        createGroup(connection, GROUP_A);
        final Long groupId = getFriendsGroupId(connection, GROUP_A);
        final List<User> members = whenGetGroupMembers(groupId);
        thenGroupHasNotMemebers(members);
    }

    @Test
    public void getTheGroupOfUser() throws SQLException {
        givenUserHasAGroup(USER_1, GROUP_A);
        FriendsGroup group = whenSearchGroupOf(USER_1);
        thenGetGroup(GROUP_A, group);
    }

    private void givenUserHasAGroup(String user, String group) throws SQLException {
        createGroup(connection, group);
        final Long friendsGroupId = getFriendsGroupId(connection, group);
        createUser(connection, user, friendsGroupId);
    }

    private Long givenUserHasAGroupWithTwoFriends(String user, String group) throws SQLException {
        createGroup(connection, group);
        final Long friendsGroupId = getFriendsGroupId(connection, group);
        createUser(connection, user, friendsGroupId);
        createUser(connection, USER_2, friendsGroupId);
        createUser(connection, USER_3, friendsGroupId);
        return friendsGroupId;
    }

    private List<User> whenGetGroupMembers(Long groupId) {
        return friendsGroupRepository.getMembers(new FriendsGroup(groupId, ""));
    }

    private FriendsGroup whenSearchGroupOf(String user) {
        return friendsGroupRepository.getGroupOf(new User(user));
    }

    private void thenGroupHasNotMemebers(List<User> members) {
        assertThat(members).isEmpty();
    }

    private void thenGetGroup(String expected, FriendsGroup group) {
        assertThat(group.getName()).isEqualTo(expected);
    }

    private void thenGroupHasThreeMemebers(List<User> members) {
        assertThat(members).hasSize(3);
    }
}
