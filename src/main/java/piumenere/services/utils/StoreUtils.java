package piumenere.services.utils;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;
import org.apache.commons.collections4.CollectionUtils;
import piumenere.services.entities.Creable;
import piumenere.services.entities.Editable;
import piumenere.services.store.CreableStore;
import piumenere.services.store.EditableStore;

public class StoreUtils {

    public static <T extends Creable, R extends T> R supplyIfNotFound(R toFind, Supplier<R> toCreate, CreableStore<T> store) {
        return CollectionUtils.emptyIfNull(
                store.find(Arrays.asList(CriteriaUtils.getInstance(toFind)), null, 1, 0).getResults()
        ).stream().findFirst().orElseGet(
                () -> store.create(Arrays.asList(toCreate.get())).stream().findFirst().get()
        );
    }

    public static <T extends Creable, R extends T> R createIfNotFound(R toFind, R toCreate, CreableStore<T> store) {
        return supplyIfNotFound(toFind, () -> toCreate, store);
    }

    public static <T extends Editable, R extends T> R upsert(R old, R updated, EditableStore<T> store) {
        return CollectionUtils.emptyIfNull(
                store.find(Arrays.asList(CriteriaUtils.getInstance(old)), null, 1, 0).getResults()
        ).stream().findFirst().map(found -> {
            updated.setId(found.getId());
            updated.setVersion(found.getVersion());
            return store.edit(Arrays.asList(updated)).stream().findFirst().get();
        }).orElseGet(
                () -> store.create(Arrays.asList(updated)).stream().findFirst().get()
        );
    }

}
