package piumenere.services.entities.reflections;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.reflections.util.ConfigurationBuilder;

public class ReflectionsImpl implements Reflections {

    private final org.reflections.Reflections reflections;
    private final Map<Class<?>,Set<?>> subtypes = new ConcurrentHashMap<>();
    private final Map<Class<? extends Annotation>,Set<Class<?>>> annotated = new ConcurrentHashMap<>();

    public ReflectionsImpl(Set<String> packages) {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        ConfigurationBuilder forPackages = configurationBuilder.forPackages(packages.stream().toArray(String[]::new));
        reflections = new org.reflections.Reflections(forPackages);
    }
    
    @Override
    public <T> Set<Class<? extends T>> getSubTypesOf(Class<T> type) {
        if (subtypes.containsKey(type)){
            return (Set<Class<? extends T>>) subtypes.get(type);
        }
        Set<Class<? extends T>> subTypesOf = new HashSet<>();
        subTypesOf.addAll(reflections.getSubTypesOf(type));
        subtypes.put(type, subTypesOf);
        return subTypesOf;
    }

    @Override
    public Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotation) {
        if (annotated.containsKey(annotation)){
            return annotated.get(annotation);
        }
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(annotation);
        annotated.put(annotation, typesAnnotatedWith);
        return typesAnnotatedWith;
    }
    
}
