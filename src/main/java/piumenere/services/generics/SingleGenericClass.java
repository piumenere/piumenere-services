package piumenere.services.generics;

import java.lang.reflect.Type;

public interface SingleGenericClass<T> {
    
    public Class<T> getFirstGenericsClass();
    public Type getFirstGenericsType();
    
}
