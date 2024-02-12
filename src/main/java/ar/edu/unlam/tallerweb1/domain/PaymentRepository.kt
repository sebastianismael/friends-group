package ar.edu.unlam.tallerweb1.domain

import ar.edu.unlam.tallerweb1.domain.model.Payment

interface PaymentRepository {
    fun findPaymentsOf(id: Long): List<Payment>
}
