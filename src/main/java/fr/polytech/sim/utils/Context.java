package fr.polytech.sim.utils;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * Simplified dependency injection context.
 */
public class Context {

    /**
     * Provide an instance of the class passed in parameter if a service
     * provider exists for that class. If no such a provider exists, the method
     * return null.
     *
     * @param klass expected injection type
     * @return      instance provided by first service provider, null if none
     * @param <T>   expected instance type
     */
    public static <T> T inject(Class<T> klass) {
        Iterator<T> iterator = ServiceLoader.load(klass).iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return null;
    }

    /**
     * Provide an iterator over all instances of the class passed in parameter
     * if a service provider exists for that class. If no such a provider
     * exists, the method return an empty iterator.
     *
     * @param klass expected injection type
     * @return      an iterator over all instances provided by service provider
     * @param <T>   expected instance type
     */
    public static <T> Iterator<T> injectAll(Class<T> klass) {
        /* TODO: implement the right logic */
        throw new UnsupportedOperationException("Method not implemented");
    }
}
