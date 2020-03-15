package piumenere.services.bean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Set;
import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.CreationalContext;

@ApplicationScoped
public class GenericBeanProviderImpl implements GenericBeanProvider {
    
    private final Map<Type, Collection<?>> map = new ConcurrentHashMap<>();
    
    @Inject
    private BeanManager beanManager;

    @Override
    public <T> Optional<T> getBean(Type type, Annotation... qualifiers) {
        Collection<T> beans = getBeans(type, qualifiers);
        return beans.stream().findFirst();
    }
    
    @Override
    public <T> Collection<T> getBeans(Type type, Annotation... qualifiers) {
        
        if (map.containsKey(type)){
            return Collection.class.cast(map.get(type));
        }
        
        Collection<T> result = new ArrayList();
        
        Set<Bean<T>> beans = Set.class.cast(beanManager.getBeans(type, qualifiers));
        
        beans.stream().forEachOrdered((storeBean) -> {
            Context context = beanManager.getContext(storeBean.getScope());
            CreationalContext<T> createCreationalContext = beanManager.createCreationalContext(storeBean);
            result.add(context.get(storeBean, createCreationalContext));
        });
        
        map.put(type, result);
        
        return result;
    }
    
}
