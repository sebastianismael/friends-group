package ar.edu.unlam.tallerweb1.infrastructure.utils

import java.sql.Connection
import java.sql.SQLException

interface DataSource {
    @Throws(SQLException::class)
    fun getConnection(): Connection
}
