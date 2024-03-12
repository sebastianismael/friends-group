package ar.edu.unlam.tallerweb1.domain

import ar.edu.unlam.tallerweb1.infrastructure.utils.DataSource
import ar.edu.unlam.tallerweb1.infrastructure.utils.MySqlDataSource
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import kotlin.reflect.KClass

object Repositories {

    private val implementations = mutableMapOf<KClass<out Any>, Any>()

    fun getSharedExpensesRepository(): SharedExpensesRepository =
        getImplementation(SharedExpensesRepository::class, MySqlDataSource.instance()) as SharedExpensesRepository

    fun getUserRepository(): UserRepository =
        getImplementation(UserRepository::class, MySqlDataSource.instance()) as UserRepository

    fun getFriendsGroupRepository(): FriendsGroupRepository =
        getImplementation(FriendsGroupRepository::class, MySqlDataSource.instance()) as FriendsGroupRepository

    fun getPaymentRepository(): PaymentRepository =
        getImplementation(PaymentRepository::class, MySqlDataSource.instance()) as PaymentRepository

    // TODO mecanismo para proveer las implementaciones de los repositorios que estan en infraestructura.
    //  * mejora #1: usa una libreria externa, no enccontre un mecanismo nativo en java para conocer las clases que
    //  implementan una interface, buscar alguna forma nativa de resolverlo
    //  * mejora #2: manejar los repositorios como singleton, ya que estamos instanciando el repo cada vez. DONE!!!!
    //  * mejora #3: no soporta mas de una implementacion de la interface
    private fun <T : Any> getImplementation(repository: KClass<T>, dataSource: MySqlDataSource): Any? {

        if(!this.implementations.contains(repository))
            this.implementations[repository] = getSubclassOf(repository).instantiate(dataSource)
        return this.implementations[repository]
    }

    private fun <T : Any> getSubclassOf(repository: KClass<T>): Class<out T> {
        val reflections = Reflections("ar.edu.unlam.tallerweb1.infrastructure", SubTypesScanner(false))
        val implementations = reflections.getSubTypesOf(repository.java)
        if (implementations.isEmpty()) throw RuntimeException("$repository hasn't implementations!!!")
        return implementations.iterator().next()
    }

    private fun <T : Any> Class<T>.instantiate(dataSource: MySqlDataSource): T {
        try {
            return this.getConstructor(DataSource::class.java).newInstance(dataSource)
        } catch (e: Exception) {
            throw RuntimeException("$this implementation can't be instantiated: ", e)
        }
    }
}


