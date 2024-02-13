package ar.edu.unlam.tallerweb1.domain

import ar.edu.unlam.tallerweb1.domain.model.SharedExpent

interface SharedExpensesRepository {
    fun findBy(user: String): List<SharedExpent>
    fun save(SharedExpent: SharedExpent)
}
