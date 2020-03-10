package piumenere.services.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.function.Consumer;
import org.apache.commons.beanutils.PropertyUtils;
import piumenere.services.entities.Deletable;
import piumenere.services.entities.Identifiable;
import piumenere.services.entities.Validable;
import piumenere.services.find.FindCriteria;
import piumenere.services.find.RangeCriteria;
import piumenere.services.find.SimpleCriteria;

public class CriteriaUtils {
    
    public static <D extends Identifiable> void apply(Collection<FindCriteria<D>> criterias, Consumer<D> consumer) {
        criterias.stream().forEach(c -> {
            if (c instanceof SimpleCriteria) {
                SimpleCriteria<D> simpleCriteria = SimpleCriteria.class.cast(c);
                consumer.accept(simpleCriteria.getCriteria());
            } else if (c instanceof RangeCriteria) {
                RangeCriteria<D> rangeCriteria = RangeCriteria.class.cast(c);
                consumer.accept(rangeCriteria.getFrom());
                consumer.accept(rangeCriteria.getTo());
            }
        });
    }

    public static <D extends Identifiable> SimpleCriteria<D> getInstance(D criteria) {
        SimpleCriteria<D> simpleCriteria = new SimpleCriteria<>();
        simpleCriteria.setCriteria(criteria);
        return simpleCriteria;
    }

    public static <D extends Deletable> SimpleCriteria<D> getInstance(D criteria, Class<D> clazz, Boolean deleted) {
        D criteriaCopy = null;
        try {
            Constructor<D> constructor = clazz.getConstructor();
            criteriaCopy = constructor.newInstance();
            PropertyUtils.copyProperties(criteriaCopy, criteria);
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        criteriaCopy.setDeleted(deleted);
        SimpleCriteria<D> simpleCriteria = new SimpleCriteria<>();
        simpleCriteria.setCriteria(criteriaCopy);
        return simpleCriteria;
    }

    public static <D extends Validable> SimpleCriteria<D> getInstance(D criteria, Class<D> clazz, Boolean deleted, Boolean valid) {
        SimpleCriteria<D> simpleCriteria = getInstance(criteria, clazz, deleted);
        simpleCriteria.getCriteria().setValid(valid);
        return simpleCriteria;
    }
    
}
