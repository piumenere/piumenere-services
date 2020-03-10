package piumenere.services.generics;

import java.lang.reflect.Type;

public abstract class AbstractDoubleGenericClass<T,S> extends AbstractSingleGenericClass<T> implements DoubleGenericClass<T,S> {

    @Override
    public Class<S> getSecondGenericsClass() {
        return getGenericsClass(1);
    }

    @Override
    public Type getSecondGenericsType() {
        return getGenericsType(1);
    }
    
}