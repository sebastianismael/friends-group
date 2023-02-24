package ar.edu.unlam.tallerweb1.infrastructure;

import ar.edu.unlam.tallerweb1.domain.UserRepository;
import ar.edu.unlam.tallerweb1.domain.model.FriendsGroup;
import ar.edu.unlam.tallerweb1.domain.model.User;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static ar.edu.unlam.tallerweb1.infrastructure.utils.TestsPersistenceHelper.createGroup;
import static ar.edu.unlam.tallerweb1.infrastructure.utils.TestsPersistenceHelper.getFriendsGroupId;
import static org.assertj.core.api.Assertions.assertThat;

public class UserJdbcRepositoryTest extends JdbcRepositoryTest {

    private UserRepository userRepository = new UserJdbcRepostory(dataSource);
    private final String USER = "user_18989";
    private final String GROUP = "group_sfaf1";
    @Test
    public void createANewUser() {
        whenCreateAnUser(USER);
        thenCanFindIt(USER);
    }

    @Test
    public void createANewUserWithFriendGroup() throws SQLException {
        createGroup(connection, GROUP);
        whenCreateAnUser(USER, GROUP);
        thenCanFindItAndHasAFriendsGroup(USER);
    }

    private void whenCreateAnUser(String user, String group) throws SQLException {
        final Long friendsGroupId = getFriendsGroupId(connection, group);
        User userToBeCreated = new User(user);
        userToBeCreated.setFriendsGroup( new FriendsGroup(new Long(friendsGroupId), group));
        userRepository.saveWithFriendGroup(userToBeCreated);
    }

    @Test
    public void searchAnUnexistingUser(){
        whenNotExistsTheUser(USER);
        thenCantFindIt(USER);
    }

    private void whenNotExistsTheUser(String user) {
    }

    private void whenCreateAnUser(String user) {
        userRepository.save(new User(user));
    }

    private void thenCantFindIt(String user) {
        assertThat(userRepository.findByName(user)).isNull();
    }

    private void thenCanFindItAndHasAFriendsGroup(String user) {
        final User found = userRepository.findByName(user);
        assertThat(found).isNotNull();
        assertThat(found.getFriendsGroup()).isNotNull();
    }

    private void thenCanFindIt(String user) {
        assertThat(userRepository.findByName(user)).isNotNull();
    }
}
