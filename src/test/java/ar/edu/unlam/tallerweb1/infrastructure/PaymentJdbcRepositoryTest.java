package ar.edu.unlam.tallerweb1.infrastructure;

import ar.edu.unlam.tallerweb1.domain.PaymentRepository;
import ar.edu.unlam.tallerweb1.domain.model.Payment;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PaymentJdbcRepositoryTest extends JdbcRepositoryTest {

    private final String USER_1 = "user_1";
    private final String USER_2 = "user_2";
    private final String USER_3 = "user_3";
    private final String GROUP = "group_1";

    private PaymentRepository paymentRepository = new PaymentJdbcRepository(dataSource);
    @Test
    public void shouldReturnPaymentsOfExpent() throws SQLException {
        final Long friendsGroupId = givenAgroup(GROUP);
        givenAGroupUser(USER_1, friendsGroupId);
        final Long user2Id = givenAGroupUser(USER_2, friendsGroupId).getId();
        final Long user3Id = givenAGroupUser(USER_3, friendsGroupId).getId();
        final Long expentId = givenAnExpent(friendsGroupId, USER_1, 900.0, "Cena");

        givenAPayment(user2Id, 300.0, expentId);
        givenAPayment(user3Id, 300.0, expentId);

        final List<Payment> payments = whenGetPaymentsOf(expentId);

        assertThat(payments).hasSize(2);

    }

    @Test
    public void shouldReturnNoPaymentsOfExpentWIthoutPayments() throws SQLException {
        final Long friendsGroupId = givenAgroup(GROUP);
        givenAGroupUser(USER_1, friendsGroupId);
        givenAGroupUser(USER_2, friendsGroupId);
        givenAGroupUser(USER_3, friendsGroupId);
        final Long expentId = givenAnExpent(friendsGroupId, USER_1, 900.0, "Cena");

        final List<Payment> payments = whenGetPaymentsOf(expentId);

        assertThat(payments).isEmpty();

    }

    private List<Payment> whenGetPaymentsOf(Long expentId) {
        return paymentRepository.findPaymentsOf(expentId);
    }

}
