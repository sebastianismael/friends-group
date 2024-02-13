package ar.edu.unlam.tallerweb1.infrastructure

import ar.edu.unlam.tallerweb1.domain.PaymentRepository
import ar.edu.unlam.tallerweb1.domain.model.Payment
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.sql.SQLException

class PaymentJdbcRepositoryTestIT : JdbcRepositoryTestIT() {
    private val USER_1 = "user_1"
    private val USER_2 = "user_2"
    private val USER_3 = "user_3"
    private val GROUP = "group_1"

    private val paymentRepository: PaymentRepository = PaymentJdbcRepository(dataSource)
    @Test
    @Throws(SQLException::class)
    fun shouldReturnPaymentsOfExpent() {
        val friendsGroupId = givenAgroup(GROUP)
        givenAGroupUser(USER_1, friendsGroupId)
        val user2Id = givenAGroupUser(USER_2, friendsGroupId).id!!
        val user3Id = givenAGroupUser(USER_3, friendsGroupId).id!!
        val expentId = givenAnExpent(friendsGroupId, USER_1, 900.0, "Cena")

        givenAPayment(user2Id, 300.0, expentId)
        givenAPayment(user3Id, 300.0, expentId)

        val payments = whenGetPaymentsOf(expentId)

        assertThat(payments).hasSize(2)
    }

    @Test
    @Throws(SQLException::class)
    fun shouldReturnNoPaymentsOfExpentWIthoutPayments() {
        val friendsGroupId = givenAgroup(GROUP)
        givenAGroupUser(USER_1, friendsGroupId)
        givenAGroupUser(USER_2, friendsGroupId)
        givenAGroupUser(USER_3, friendsGroupId)
        val expentId = givenAnExpent(friendsGroupId, USER_1, 900.0, "Cena")

        val payments = whenGetPaymentsOf(expentId)

        assertThat(payments).isEmpty()
    }

    private fun whenGetPaymentsOf(expentId: Long): List<Payment> {
        return paymentRepository.findPaymentsOf(expentId)
    }
}
