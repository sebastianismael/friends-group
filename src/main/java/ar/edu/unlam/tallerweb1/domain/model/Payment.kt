package ar.edu.unlam.tallerweb1.domain.model

class Payment(val id: Long, val payer: User, val amount: Double, val sharedExpent: SharedExpent)
