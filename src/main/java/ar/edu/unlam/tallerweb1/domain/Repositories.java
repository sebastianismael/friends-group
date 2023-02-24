package ar.edu.unlam.tallerweb1.domain;

import ar.edu.unlam.tallerweb1.infrastructure.utils.DataSource;
import ar.edu.unlam.tallerweb1.infrastructure.utils.MySqlDataSource;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.util.Set;

public class Repositories {

    public static SharedExpensesRepository getSharedExpensesRepository() {
        return getRepository(SharedExpensesRepository.class, MySqlDataSource.instance());
    }

    public static UserRepository getUserRepository() {
        return getRepository(UserRepository.class, MySqlDataSource.instance());
    }

    public static FriendsGroupRepository getFriendsGroupRepository(){
        return getRepository(FriendsGroupRepository.class, MySqlDataSource.instance());
    }

    public static PaymentRepository getPaymentRepository(){
        return getRepository(PaymentRepository.class, MySqlDataSource.instance());
    }

    // TODO mecanismo para proveer las implementaciones de los repositorios que estan en infraestructura.
    //  * mejora #1: usa una libreria externa, no ecncontre un mecanismo nativo en java para conocer las clases que
    //  implementan una interface, buscar alguna forma nativa de resolverlo
    //  * mejora #2: manejar los repositorios como singleton, ya que estamos instanciando el repo cada vez.
    //  * mejora #3: no soporta mas de una implementacion de la interface
    private static <T> T getRepository(Class<T> interfaze, MySqlDataSource dataSource) {
        Reflections reflections = new Reflections("ar.edu.unlam.tallerweb1.infrastructure", new SubTypesScanner(false));
        final Set<Class<? extends T>> implementations = reflections.getSubTypesOf(interfaze);
        if (implementations.isEmpty())
            throw new RuntimeException(interfaze + " hasn't implementations!!!");
        try {
            return implementations.iterator().next().getConstructor(DataSource.class).newInstance(dataSource);
        } catch (Exception e) {
            throw new RuntimeException(interfaze + " implementation can't be instantiated: ", e);
        }
    }

}
