package piumenere.services.store;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.AbstractQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.persistence.metamodel.SingularAttribute;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import piumenere.services.entities.Identifiable;
import piumenere.services.entities.Identifiable_;
import piumenere.services.find.ConjunctiveCriteria;
import piumenere.services.find.DisjunctiveCriteria;
import piumenere.services.find.FindCriteria;
import piumenere.services.find.FindOrder;
import piumenere.services.find.FindResult;
import piumenere.services.find.NegateCriteria;
import piumenere.services.find.RangeCriteria;
import piumenere.services.find.RelationalCriteria;
import piumenere.services.find.SimpleCriteria;

public abstract class AbstractIdentifiableStore<T extends Identifiable> extends AbstractStore<T> implements IdentifiableStore<T> {

    private final Collection<String> identifyingAttributeNames = Arrays.asList(Identifiable_.id.getName(), Identifiable_.version.getName());

    @Override
    public <D extends T> FindResult<D> find(Collection<FindCriteria<D>> criterias, Collection<FindOrder<D>> orders, Integer quantity, Integer offset) {
        return find(criterias, orders, quantity, offset, (Root<D> root) -> Arrays.asList());
    }

    protected <D extends T> FindResult<D> findAttached(Collection<FindCriteria<D>> criterias, Collection<FindOrder<D>> orders, Integer quantity, Integer offset) {
        return findAttached(criterias, orders, quantity, offset, (Root<D> root) -> Arrays.asList());
    }

    protected <D extends T> FindResult<D> find(Collection<FindCriteria<D>> criterias, Collection<FindOrder<D>> orders, Integer quantity, Integer offset, Function<Root<D>, Collection<Predicate>> morePredicates) {
        return find(criterias, orders, quantity, offset, (CriteriaQuery criteriaQuery, Root<D> root) -> {
            return morePredicates.apply(root);
        });
    }

    protected <D extends T> FindResult<D> findAttached(Collection<FindCriteria<D>> criterias, Collection<FindOrder<D>> orders, Integer quantity, Integer offset, Function<Root<D>, Collection<Predicate>> morePredicates) {
        return findAttached(criterias, orders, quantity, offset, (CriteriaQuery criteriaQuery, Root<D> root) -> {
            return morePredicates.apply(root);
        });
    }

    protected <D extends T> FindResult<D> find(Collection<FindCriteria<D>> findCriterias, Collection<FindOrder<D>> findOrders, Integer quantity, Integer offset, BiFunction<CriteriaQuery, Root<D>, Collection<Predicate>> morePredicates) {
        FindResult<D> findResult = findAttached(findCriterias, findOrders, quantity, offset, morePredicates);
        CollectionUtils.emptyIfNull(findResult.getResults()).forEach(entity -> getEntityManager().detach(entity));

        return findResult;
    }

    protected <D extends T> FindResult<D> findAttached(Collection<FindCriteria<D>> findCriterias, Collection<FindOrder<D>> findOrders, Integer quantity, Integer offset, BiFunction<CriteriaQuery, Root<D>, Collection<Predicate>> morePredicates) {
        FindResult<D> findResult = new FindResult<>();

        if (CollectionUtils.isEmpty(findCriterias)) {
            return findResult;
        }

        Optional<Class<D>> type = FindCriteria.getSpecializedAncestor(findCriterias);

        if (type.isEmpty()){
            return findResult;
        }
        
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<D> criteriaQuery = criteriaBuilder.createQuery(type.get());
        Root<D> root = criteriaQuery.from(type.get());

        Map<Collection<Path<? extends Identifiable>>, From<? extends Identifiable, ? extends Identifiable>> paths = new HashMap();
        paths.put(Arrays.asList(root), root);

        Collection<Predicate> predicates = computePredicates(paths, findCriterias, root, criteriaQuery, criteriaBuilder);

        Predicate or = criteriaBuilder.or(predicates.stream().toArray(Predicate[]::new));

        Collection<Predicate> apply = morePredicates.apply(criteriaQuery, root);

        Predicate and = criteriaBuilder.and(apply.stream().toArray(Predicate[]::new));

        criteriaQuery.select(root).distinct(true);
        criteriaQuery.where(criteriaBuilder.and(or, and));

        List<Order> orders = CollectionUtils.emptyIfNull(findOrders).stream()
                .filter(order -> StringUtils.isNotEmpty(order.getField()) && Objects.nonNull(FieldUtils.getField(type.get(), order.getField(), true)))
                .map(order -> Boolean.FALSE.equals(order.getReverse()) ? criteriaBuilder.desc(root.get(order.getField())) : criteriaBuilder.asc(root.get(order.getField())))
                .collect(Collectors.toList());

        criteriaQuery.orderBy(orders);

        TypedQuery<D> typedQuery = getEntityManager().createQuery(criteriaQuery);

        if (Objects.nonNull(quantity) && Objects.nonNull(offset)) {
            typedQuery.setFirstResult(offset);
            typedQuery.setMaxResults(quantity);
            findResult.setResults(typedQuery.getResultList());
            findResult.setCount(count(findCriterias, morePredicates));
        } else {
            findResult.setResults(typedQuery.getResultList());
            findResult.setCount(Long.valueOf(findResult.getResults().size()));
        }

        return findResult;
    }

    protected <D extends T> Long count(Collection<FindCriteria<D>> criterias, BiFunction<CriteriaQuery, Root<D>, Collection<Predicate>> morePredicates) {

        if (CollectionUtils.isEmpty(criterias)) {
            return 0L;
        }

        Optional<Class<D>> type = FindCriteria.getSpecializedAncestor(criterias);

        if (type.isEmpty()){
            return 0L;
        }
        
        CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<D> root = criteriaQuery.from(type.get());

        Map<Collection<Path<? extends Identifiable>>, From<? extends Identifiable, ? extends Identifiable>> paths = new HashMap();
        paths.put(Arrays.asList(root), root);

        Collection<Predicate> predicates = computePredicates(paths, criterias, root, criteriaQuery, criteriaBuilder);

        Predicate or = criteriaBuilder.or(predicates.stream().toArray(Predicate[]::new));

        Collection<Predicate> apply = morePredicates.apply(criteriaQuery, root);

        Predicate and = criteriaBuilder.and(apply.stream().toArray(Predicate[]::new));

        criteriaQuery.select(criteriaBuilder.countDistinct(root));
        criteriaQuery.where(criteriaBuilder.and(or, and));

        TypedQuery<Long> typedQuery = getEntityManager().createQuery(criteriaQuery);
        return typedQuery.getSingleResult();
    }

    protected <D extends Identifiable> Collection<Predicate> computePredicates(Map<Collection<Path<? extends Identifiable>>, From<? extends Identifiable, ? extends Identifiable>> paths, Collection<FindCriteria<D>> criterias, Root<D> root, AbstractQuery criteriaQuery, CriteriaBuilder criteriaBuilder) {
        Collection<Predicate> predicates = new ArrayList<>();

        CollectionUtils.emptyIfNull(criterias).forEach((findCriteria) -> {

            Collection<Predicate> criteriaPredicate = new ArrayList<>();

            if (findCriteria instanceof SimpleCriteria) {
                SimpleCriteria<D> cast = SimpleCriteria.class.cast(findCriteria);
                criteriaPredicate.addAll(computePredicates(paths, Optional.ofNullable(cast.getCriteria()), root, criteriaBuilder));
            } else if (findCriteria instanceof RangeCriteria) {
                RangeCriteria<D> cast = RangeCriteria.class.cast(findCriteria);
                criteriaPredicate.addAll(computePredicates(paths, Optional.ofNullable(cast.getFrom()), Optional.ofNullable(cast.getTo()), root, criteriaBuilder));
            } else if (findCriteria instanceof RelationalCriteria) {
                RelationalCriteria<D, ?> cast = RelationalCriteria.class.cast(findCriteria);
                criteriaPredicate.addAll(computePredicates(paths, Arrays.asList(cast.getRelated()), root, criteriaQuery, criteriaBuilder));

                MapUtils.emptyIfNull(cast.getRelations()).entrySet().forEach((relation) -> {
                    FindCriteria value = relation.getValue();
                    Optional<Class> specializedAncestor = FindCriteria.getSpecializedAncestor(Arrays.asList(value));
                    if (specializedAncestor.isEmpty()){
                        return;
                    }
                    Subquery subquery = criteriaQuery.subquery(specializedAncestor.get());
                    Root relationRoot = subquery.from(specializedAncestor.get());
                    Map<Collection<Path<? extends Identifiable>>, From<? extends Identifiable, ? extends Identifiable>> relationPaths = new HashMap();
                    relationPaths.put(Arrays.asList(relationRoot), relationRoot);
                    subquery.select(relationRoot.join(relation.getKey())).distinct(true);
                    Collection<Predicate> computePredicates = computePredicates(relationPaths, Arrays.asList(value), relationRoot, subquery, criteriaBuilder);
                    subquery.where(computePredicates.stream().toArray(Predicate[]::new));
                    criteriaPredicate.add(root.in(subquery.getSelection()));
                });
            } else if (findCriteria instanceof DisjunctiveCriteria) {
                DisjunctiveCriteria<D> cast = DisjunctiveCriteria.class.cast(findCriteria);
                criteriaPredicate.add(criteriaBuilder.or(computePredicates(paths, cast.getCriterias(), root, criteriaQuery, criteriaBuilder).stream().toArray(Predicate[]::new)));
            } else if (findCriteria instanceof ConjunctiveCriteria) {
                ConjunctiveCriteria<D> cast = ConjunctiveCriteria.class.cast(findCriteria);
                criteriaPredicate.add(criteriaBuilder.and(computePredicates(paths, cast.getCriterias(), root, criteriaQuery, criteriaBuilder).stream().toArray(Predicate[]::new)));
            } else if (findCriteria instanceof NegateCriteria) {
                NegateCriteria<D> cast = NegateCriteria.class.cast(findCriteria);
                criteriaPredicate.add(criteriaBuilder.not(criteriaBuilder.and(computePredicates(paths, Arrays.asList(cast.getNegated()), root, criteriaQuery, criteriaBuilder).stream().toArray(Predicate[]::new))));
            }

            predicates.add(criteriaBuilder.and(criteriaPredicate.stream().toArray(Predicate[]::new)));

        });

        return predicates;
    }

    private <D extends Identifiable> From<D, Identifiable> getFrom(From<? extends Identifiable, D> base, SingularAttribute<? super D, Identifiable> identifiableAttribute, Map<Collection<Path<? extends Identifiable>>, From<? extends Identifiable, ? extends Identifiable>> paths) {
        Path<? extends Identifiable> node = base.get(identifiableAttribute);
        Collection<Path<? extends Identifiable>> path = new ArrayList<>();
        path.add(node);
        while (Objects.nonNull(node.getParentPath())) {
            Path<Identifiable> cast = Path.class.cast(node.getParentPath());
            path.add(cast);
            node = cast;
        }
        if (!paths.containsKey(path)) {
            paths.put(path, base.join(identifiableAttribute, JoinType.LEFT));
        }
        From<D, Identifiable> join = From.class.cast(paths.get(path));
        return join;
    }
    
    protected <D extends Identifiable> Collection<Predicate> computePredicates(Map<Collection<Path<? extends Identifiable>>, From<? extends Identifiable, ? extends Identifiable>> paths, Optional<D> criteria, From<? extends Identifiable, D> base, CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();

        if (!criteria.isPresent()) {
            return predicates;
        }

        if (!Objects.equals(base.getJavaType(), criteria.get().getClass())) {
            predicates.add(base.type().in(getTypes(criteria.get().getClass())));
        }

        SetUtils.emptyIfNull(getEntityManager().getMetamodel().entity(criteria.get().getClass()).getSingularAttributes()).forEach((var attribute) -> {

            SingularAttribute<? super D, ?> singularAttribute = (SingularAttribute<? super D, ?>) attribute;

            Optional<?> value = getProperty(criteria, attribute.getName());

            if (!value.isPresent()) {
                return;
            }

            if (!identifyingAttributeNames.contains(singularAttribute.getName()) && getProperty(criteria, Identifiable_.id.getName()).isPresent()) {
                getLogger().warn("PREDICATE ON PROPERTY " + singularAttribute.getName() + " WITH ID ALREADY SPECIFIED " + getProperty(criteria, Identifiable_.id.getName()).get());
            }

            if (value.get() instanceof Identifiable) {

                SingularAttribute<? super D, Identifiable> identifiableAttribute = (SingularAttribute<? super D, Identifiable>) singularAttribute;

                Optional<Identifiable> valueEntity = value.map(o -> Identifiable.class.cast(o));
                Collection<Predicate> computePredicates = new ArrayList<>();

                From<D, Identifiable> join = getFrom(base, identifiableAttribute, paths);

                computePredicates.addAll(computePredicates(paths, valueEntity, join, criteriaBuilder));

                if (CollectionUtils.isEmpty(computePredicates)) {
                    computePredicates.add(criteriaBuilder.equal(base.get(identifiableAttribute), join));
                }

                predicates.add(criteriaBuilder.and(computePredicates.stream().toArray(Predicate[]::new)));

            } else if (value.isPresent() && value.get() instanceof String) {
                predicates.add(criteriaBuilder.like(base.<String>get((SingularAttribute<? super D, String>) attribute), String.class.cast(value.get())));
            } else {
                predicates.add(criteriaBuilder.equal(base.get(singularAttribute), value.get()));
            }

        });

        SetUtils.emptyIfNull(getEntityManager().getMetamodel().entity(criteria.get().getClass()).getPluralAttributes()).forEach(attribute -> {

            Optional<?> value = getProperty(criteria, attribute.getName());

            if (!value.isPresent()) {
                return;
            }
            switch (attribute.getCollectionType()) {
                case MAP:
                    break;
                case COLLECTION:
                case LIST:
                case SET:
                default:
                    break;
            }

        });

        return predicates;

    }

    protected <D extends Identifiable> Collection<Predicate> computePredicates(Map<Collection<Path<? extends Identifiable>>, From<? extends Identifiable, ? extends Identifiable>> paths, Optional<D> from, Optional<D> to, From<? extends Identifiable, D> base, CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();

        if (!from.isPresent() && !to.isPresent()) {
            return predicates;
        }

        if (from.isPresent() && !Objects.equals(base.getJavaType(), from.get().getClass())) {
            predicates.add(base.type().in(getTypes(from.get().getClass())));
        } else if (to.isPresent() && !Objects.equals(base.getJavaType(), to.get().getClass())) {
            predicates.add(base.type().in(getTypes(to.get().getClass())));
        }

        SetUtils.emptyIfNull(getEntityManager().getMetamodel().entity(from.get().getClass()).getSingularAttributes()).forEach(attribute -> {

            SingularAttribute<? super D, ?> singularAttribute = (SingularAttribute<? super D, ?>) attribute;

            Optional<?> fromValue = getProperty(from, attribute.getName());
            Optional<?> toValue = getProperty(to, attribute.getName());

            if (!fromValue.isPresent() && !toValue.isPresent()) {
                return;
            }

            if (!identifyingAttributeNames.contains(singularAttribute.getName()) && (getProperty(from, Identifiable_.id.getName()).isPresent() || getProperty(to, Identifiable_.id.getName()).isPresent())) {
                getLogger().warn("PREDICATE ON PROPERTY " + singularAttribute.getName() + " WITH ID ALREADY SPECIFIED");
            }

            if (fromValue.isPresent() && fromValue.get() instanceof Identifiable || toValue.isPresent() && toValue.get() instanceof Identifiable) {

                SingularAttribute<? super D, Identifiable> identifiableAttribute = (SingularAttribute<? super D, Identifiable>) singularAttribute;

                Optional<Identifiable> lowerEntity = fromValue.map(o -> Identifiable.class.cast(o));
                Optional<Identifiable> upperEntity = toValue.map(o -> Identifiable.class.cast(o));

                Collection<Predicate> computePredicates = new ArrayList<>();

                From<D, Identifiable> join = getFrom(base, identifiableAttribute, paths);

                computePredicates.addAll(computePredicates(paths, lowerEntity, upperEntity, join, criteriaBuilder));

                if (CollectionUtils.isEmpty(computePredicates)) {
                    computePredicates.add(criteriaBuilder.equal(base.get(identifiableAttribute), join));
                }

                predicates.add(criteriaBuilder.and(computePredicates.stream().toArray(Predicate[]::new)));

            } else if (fromValue.isPresent() && toValue.isPresent() && Objects.deepEquals(fromValue.get(), toValue.get()) && fromValue.get() instanceof String) {
                predicates.add(criteriaBuilder.like(base.<String>get((SingularAttribute<? super D, String>) attribute), String.class.cast(fromValue.get())));
            } else if (fromValue.isPresent() && toValue.isPresent() && Objects.deepEquals(fromValue.get(), toValue.get())) {
                predicates.add(criteriaBuilder.equal(base.get(singularAttribute), fromValue.get()));
            } else if (fromValue.isPresent() && toValue.isPresent() && fromValue.get() instanceof String && toValue.get() instanceof String) {
                predicates.add(criteriaBuilder.like(base.<String>get((SingularAttribute<? super D, String>) attribute), String.class.cast(fromValue.get()).concat("%")));
                predicates.add(criteriaBuilder.like(base.<String>get((SingularAttribute<? super D, String>) attribute), "%".concat(String.class.cast(toValue.get()))));
            } else if (fromValue.isPresent() && toValue.isPresent() && fromValue.get() instanceof Comparable && toValue.get() instanceof Comparable) {
                predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(base.<Comparable>get((SingularAttribute<? super D, Comparable>) attribute), Comparable.class.cast(fromValue.get())), criteriaBuilder.lessThanOrEqualTo(base.<Comparable>get((SingularAttribute<? super D, Comparable>) attribute), Comparable.class.cast(toValue.get()))));
            } else if (fromValue.isPresent() && fromValue.get() instanceof String) {
                predicates.add(criteriaBuilder.like(base.<String>get((SingularAttribute<? super D, String>) attribute), String.class.cast(fromValue.get()).concat("%")));
            } else if (fromValue.isPresent() && fromValue.get() instanceof Comparable) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(base.<Comparable>get((SingularAttribute<? super D, Comparable>) attribute), Comparable.class.cast(fromValue.get())));
            } else if (toValue.isPresent() && toValue.get() instanceof String) {
                predicates.add(criteriaBuilder.like(base.<String>get((SingularAttribute<? super D, String>) attribute), "%".concat(String.class.cast(toValue.get()))));
            } else if (toValue.isPresent() && toValue.get() instanceof Comparable) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(base.<Comparable>get((SingularAttribute<? super D, Comparable>) attribute), Comparable.class.cast(toValue.get())));
            }

        });

        SetUtils.emptyIfNull(getEntityManager().getMetamodel().entity(from.get().getClass()).getPluralAttributes()).forEach(attribute -> {

            Optional<?> fromValue = getProperty(to, attribute.getName());
            Optional<?> toValue = getProperty(from, attribute.getName());

            if (!fromValue.isPresent() && !toValue.isPresent()) {
                return;
            }
            switch (attribute.getCollectionType()) {
                case MAP:
                    break;
                case COLLECTION:
                case LIST:
                case SET:
                default:
                    break;
            }

        });

        return predicates;

    }

    protected Optional<?> getProperty(Optional<? extends Identifiable> object, String propertyName) {
        Optional<?> result = Optional.empty();
        if (!object.isPresent()) {
            return result;
        }
        try {
            return Optional.ofNullable(PropertyUtils.getProperty(object.get(), propertyName));
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            getLogger().error("ERROR INIT QUERY WITH FIELD " + propertyName + " " + e.getClass() + " " + e.getMessage());
        }
        return result;
    }

}
