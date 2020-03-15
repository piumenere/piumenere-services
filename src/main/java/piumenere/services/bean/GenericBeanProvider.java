package piumenere.services.bean;


import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Optional;

public interface GenericBeanProvider {
    
    public <T> Optional<T> getBean(Type type, Annotation... qualifiers);
    public <T> Collection<T> getBeans(Type type, Annotation... qualifiers);
    
}

