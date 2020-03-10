package piumenere.services.entities.reflections;

import java.lang.annotation.Annotation;
import java.util.Set;

public interface Reflections {
    
    public <T> Set<Class<? extends T>> getSubTypesOf(final Class<T> type);
    public Set<Class<?>> getTypesAnnotatedWith(final Class<? extends Annotation> annotation);
    
}