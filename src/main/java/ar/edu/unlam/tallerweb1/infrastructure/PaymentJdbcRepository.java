package ar.edu.unlam.tallerweb1.infrastructure;

import ar.edu.unlam.tallerweb1.domain.PaymentRepository;
import ar.edu.unlam.tallerweb1.domain.model.Payment;
import ar.edu.unlam.tallerweb1.domain.model.SharedExpent;
import ar.edu.unlam.tallerweb1.domain.model.User;
import ar.edu.unlam.tallerweb1.infrastructure.utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

import static ar.edu.unlam.tallerweb1.infrastructure.utils.JdbcUtil.*;

public class PaymentJdbcRepository extends JdbcRepository implements PaymentRepository {
    public PaymentJdbcRepository(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Payment> findPaymentsOf(Long expentId) {
        return searchInTransaction((connection, x) -> { // TODO mejorar esto, no es necesario.
            List<Payment> found = new LinkedList<>();
            SharedExpent expent = getExpent(connection, expentId);

            final String sql = "select * from payment where expent_id = ?";
            final PreparedStatement ps = prepareStatement(connection, sql);
            setLong(1, expentId, ps);
            final ResultSet rs = executeQuery(ps);
            while(next(rs)){
                found.add(buildPayment(connection, expent, rs));
            }
            return found;
        }, expentId.toString());
    }

    private Payment buildPayment(Connection connection, SharedExpent expent, ResultSet payments) {
        return new Payment(
                getLong("id", payments),
                getUser(connection, getLong("payer", payments)),
                getDouble("amount", payments),
                expent
        );
    }

    private SharedExpent getExpent(Connection connection, Long expentId) {
        final PreparedStatement ps = prepareStatement(connection, "select * from shared_expenses where id = ?");
        setLong(1, expentId, ps);
        final ResultSet rs = executeQuery(ps);
        SharedExpent expent = null;
        if(next(rs)){
            expent = new SharedExpent(
              expentId,
              getUser(connection, getLong("owner", rs)),
              getDouble("amount", rs),
              getString("detail", rs),
              getDate("date", rs), null // TODO binding del grupo de amigos
            );
        }
        return expent;
    }

    private User getUser(Connection connection, Long payerId) {
        final PreparedStatement ps = prepareStatement(connection, "select name from user where id = ?");
        setLong(1, payerId, ps);
        final ResultSet rs = executeQuery(ps);
        User payer = null;
        if(next(rs)){
            payer = new User(payerId, getString("name", rs));
        }
        return payer;
    }
}
