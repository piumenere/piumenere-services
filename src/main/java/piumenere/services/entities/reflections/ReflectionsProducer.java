package piumenere.services.entities.reflections;

import javax.enterprise.inject.spi.InjectionPoint;

public interface ReflectionsProducer {
    
    public Reflections produceReflections(InjectionPoint injectionPoint);
    
}