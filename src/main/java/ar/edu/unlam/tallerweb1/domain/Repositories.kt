package ar.edu.unlam.tallerweb1.domain

import ar.edu.unlam.tallerweb1.infrastructure.utils.DataSource
import ar.edu.unlam.tallerweb1.infrastructure.utils.MySqlDataSource
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner

object Repositories {

    fun getSharedExpensesRepository() =
        getRepository(SharedExpensesRepository::class.java, MySqlDataSource.instance())

    fun getUserRepository() =
        getRepository(UserRepository::class.java, MySqlDataSource.instance())

    fun getFriendsGroupRepository() =
        getRepository(FriendsGroupRepository::class.java, MySqlDataSource.instance())

    fun getPaymentRepository() =
        getRepository(PaymentRepository::class.java, MySqlDataSource.instance())

    // TODO mecanismo para proveer las implementaciones de los repositorios que estan en infraestructura.
    //  * mejora #1: usa una libreria externa, no ecncontre un mecanismo nativo en java para conocer las clases que
    //  implementan una interface, buscar alguna forma nativa de resolverlo
    //  * mejora #2: manejar los repositorios como singleton, ya que estamos instanciando el repo cada vez.
    //  * mejora #3: no soporta mas de una implementacion de la interface
    private fun <T> getRepository(interfaze: Class<T>, dataSource: MySqlDataSource): T {
        val reflections = Reflections("ar.edu.unlam.tallerweb1.infrastructure", SubTypesScanner(false))
        val implementations = reflections.getSubTypesOf(interfaze)
        if (implementations.isEmpty()) throw RuntimeException("$interfaze hasn't implementations!!!")
        try {
            return implementations.iterator().next().getConstructor(DataSource::class.java).newInstance(dataSource)
        } catch (e: Exception) {
            throw RuntimeException("$interfaze implementation can't be instantiated: ", e)
        }
    }

}