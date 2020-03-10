package piumenere.services.generics;

import java.lang.reflect.Type;

public interface DoubleGenericClass<T, S> extends SingleGenericClass<T>{
    
    public Class<S> getSecondGenericsClass();
    public Type getSecondGenericsType();
    
}
