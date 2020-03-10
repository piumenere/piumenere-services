package piumenere.services.store;

import java.util.Collection;
import org.apache.commons.collections4.CollectionUtils;
import piumenere.services.entities.Deletable;

public abstract class AbstractDeletableStore<T extends Deletable> extends AbstractEditableStore<T> implements DeletableStore<T> {

    @Override
    public <D extends T> void delete(Collection<D> entities) {
        CollectionUtils.emptyIfNull(entities).stream().forEach(entity -> {
            getEntityManager().remove(getEntityManager().contains(entity) ? entity : getEntityManager().merge(entity));
            getEntityManager().flush();
        });
    }

}
