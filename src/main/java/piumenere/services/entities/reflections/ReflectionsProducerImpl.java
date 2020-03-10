package piumenere.services.entities.reflections;

import java.util.Set;
import javax.annotation.security.PermitAll;
import javax.ejb.Singleton;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

@Singleton
public class ReflectionsProducerImpl extends AbstractReflectionsProducer implements ReflectionsProducer {

    @Override
    protected Reflections initReflections(Set<String> packages) {
        return new ReflectionsImpl(packages);
    }
    
    @Override
    @Produces
    @PermitAll
    public Reflections produceReflections(InjectionPoint injectionPoint) {
        return super.produceReflections(injectionPoint);
    }
    
}