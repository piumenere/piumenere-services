package piumenere.services.generics;

import java.lang.reflect.Type;

public abstract class AbstractSingleGenericClass<T> extends AbstractGenericClass implements SingleGenericClass<T> {

    @Override
    public Class<T> getFirstGenericsClass() {
        return getGenericsClass(0);
    }
    
    @Override
    public Type getFirstGenericsType() {
        return getGenericsType(0);
    }
    
}
