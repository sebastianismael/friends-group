package ar.edu.unlam.tallerweb1.infrastructure

import ar.edu.unlam.tallerweb1.domain.PaymentRepository
import ar.edu.unlam.tallerweb1.domain.exceptions.UserNotExists
import ar.edu.unlam.tallerweb1.domain.model.Payment
import ar.edu.unlam.tallerweb1.domain.model.SharedExpent
import ar.edu.unlam.tallerweb1.domain.model.User
import ar.edu.unlam.tallerweb1.infrastructure.utils.DataSource
import ar.edu.unlam.tallerweb1.infrastructure.utils.JdbcUtil.executeQuery
import ar.edu.unlam.tallerweb1.infrastructure.utils.JdbcUtil.getDate
import ar.edu.unlam.tallerweb1.infrastructure.utils.JdbcUtil.getDouble
import ar.edu.unlam.tallerweb1.infrastructure.utils.JdbcUtil.getLong
import ar.edu.unlam.tallerweb1.infrastructure.utils.JdbcUtil.getString
import ar.edu.unlam.tallerweb1.infrastructure.utils.JdbcUtil.next
import ar.edu.unlam.tallerweb1.infrastructure.utils.JdbcUtil.prepareStatement
import ar.edu.unlam.tallerweb1.infrastructure.utils.withLong
import java.sql.Connection
import java.sql.ResultSet

class PaymentJdbcRepository(dataSource: DataSource) : JdbcRepository(dataSource), PaymentRepository {
    override fun findPaymentsOf(expentId: Long): List<Payment> {
        return searchInTransaction(
            { connection: Connection, x: String ->
                val found = mutableListOf<Payment>()
                val expent = getExpent(connection, expentId)

                val sql = "select * from payment where expent_id = ?"
                val ps = prepareStatement(connection, sql)
                    .withLong(1, expentId)
                val rs = executeQuery(ps)
                while (next(rs))
                    found.add(buildPayment(connection, expent, rs))
                found
            },
            expentId.toString())
    }

    private fun buildPayment(connection: Connection, expent: SharedExpent, payments: ResultSet) =
        Payment(
            getLong("id", payments),
            getUser(connection, getLong("payer", payments)),
            getDouble("amount", payments),
            expent
        )

    private fun getExpent(connection: Connection, expentId: Long): SharedExpent {
        val ps = prepareStatement(connection, "select * from shared_expenses where id = ?")
            .withLong(1, expentId)
        val rs = executeQuery(ps)
        if (next(rs))
            return SharedExpent(
                expentId,
                getUser(connection, getLong("owner", rs)),
                getDouble("amount", rs),
                getString("detail", rs),
                getDate("date", rs),
        null // TODO binding del grupo de amigos
            )
        throw RuntimeException()
    }

    private fun getUser(connection: Connection, payerId: Long): User {
        val ps = prepareStatement(connection, "select name from user where id = ?")
            .withLong(1, payerId)
        val rs = executeQuery(ps)
        if (next(rs))
            return  User(payerId, getString("name", rs))
        throw UserNotExists(payerId.toString())
    }
}
