package piumenere.services.entities.reflections;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.InjectionPoint;
import piumenere.services.find.annotations.Values;

public abstract class AbstractReflectionsProducer implements ReflectionsProducer {
    
    protected abstract Reflections initReflections(Set<String> packages); 
    
    private final Map<Set<String>, Reflections> map = new ConcurrentHashMap();
    
    @Override
    public Reflections produceReflections(InjectionPoint injectionPoint) {
        
        Annotated annotated = injectionPoint.getAnnotated();
        if (Objects.nonNull(annotated)){
            Values annotation = annotated.getAnnotation(Values.class);
            if (Objects.nonNull(annotation)){
                Set<String> packages = new HashSet(Arrays.asList(annotation.value()));
                if (!map.containsKey(packages)){
                    map.put(packages, initReflections(packages));
                }
                return map.get(packages);
            }
        }
        
        return initReflections(new HashSet());
    }
    
}
