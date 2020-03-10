package piumenere.services.generics;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public abstract class AbstractGenericClass {

    protected Type getGenericsType(int genericsPosition) {
        Type genericSuperClass = getClass().getGenericSuperclass();

        ParameterizedType parametrizedType = null;
        while (parametrizedType == null && genericSuperClass != null) {
            if (genericSuperClass instanceof ParameterizedType) {
                parametrizedType = ParameterizedType.class.cast(genericSuperClass);
            } else {
                genericSuperClass = Class.class.cast(genericSuperClass).getGenericSuperclass();
            }
        }
        
        if (parametrizedType != null){
            return parametrizedType.getActualTypeArguments()[genericsPosition];
        }

        return null;
    }
    
    protected Class getGenericsClass(int genericsPosition) {
        Type genericsType = getGenericsType(genericsPosition);

        if (genericsType == null){
            return null;
        }
        
        if (genericsType instanceof ParameterizedType){
            genericsType = ParameterizedType.class.cast(genericsType).getRawType();
        } else if (genericsType instanceof TypeVariable){
            return Class.class.cast(TypeVariable.class.cast(genericsType).getAnnotatedBounds()[0].getType());
        }
        
        return Class.class.cast(genericsType);
    }
    
}
