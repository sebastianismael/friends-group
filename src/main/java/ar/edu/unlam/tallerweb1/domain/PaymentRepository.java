package ar.edu.unlam.tallerweb1.domain;

import ar.edu.unlam.tallerweb1.domain.model.Payment;

import java.util.List;

public interface PaymentRepository {
    List<Payment> findPaymentsOf(Long id);
}
