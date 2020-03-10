package piumenere.services.store;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import piumenere.services.entities.Editable;
import piumenere.services.find.FindCriteria;
import piumenere.services.find.FindResult;
import piumenere.services.utils.CriteriaUtils;

public abstract class AbstractEditableStore<T extends Editable> extends AbstractCreableStore<T> implements EditableStore<T> {

    @Override
    public <D extends T> Collection<D> edit(Collection<D> entities) {

        Map<String, D> map = new HashMap<>();

        List<FindCriteria<D>> collect = CollectionUtils.emptyIfNull(entities).stream().filter(
                e -> StringUtils.isNotBlank(e.getId()) && Objects.nonNull(e.getVersion())
        ).map(
                e -> CriteriaUtils.getInstance(Editable.getIdentity(e))
        ).collect(Collectors.toList());

        CollectionUtils.emptyIfNull(entities).forEach(
                e -> map.put(e.getId(), e)
        );

        FindResult<D> find = findAttached(collect, null, null, null);

        CollectionUtils.emptyIfNull(find.getResults()).forEach(
                e -> {
                    if (map.containsKey(e.getId())) {
                        D get = map.get(e.getId());

                        Collection<Field> fields = new HashSet<>();
                        fields.addAll(Arrays.asList(get.getClass().getDeclaredFields()));

                        Class<?> superclass = get.getClass().getSuperclass();

                        while (Objects.nonNull(superclass)) {
                            fields.addAll(Arrays.asList(superclass.getDeclaredFields()));
                            superclass = superclass.getSuperclass();
                        }

                        try {
                            for (Field field : fields) {
                                field.setAccessible(true);
                                if (Objects.nonNull(field.get(get))) {
                                    field.set(e, field.get(get));
                                }
                            }
                        } catch (IllegalAccessException ex) {
                            getLogger().error(ex.getMessage());
                            throw new RuntimeException(ex.getMessage());
                        }

                        getEntityManager().merge(e);
                        getEntityManager().flush();
                    }
                }
        );

        return find(CollectionUtils.emptyIfNull(entities).stream().map(
                e -> {
                    D modifiable = Editable.getIdentity(e);
                    modifiable.setVersion(null);
                    return CriteriaUtils.getInstance(modifiable);
                }
        ).collect(Collectors.toList()), null, null, null).getResults();
    }

}

