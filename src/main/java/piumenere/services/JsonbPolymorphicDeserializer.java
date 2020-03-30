package piumenere.services;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.serializer.DeserializationContext;
import javax.json.bind.serializer.JsonbDeserializer;
import javax.json.stream.JsonParser;
import org.slf4j.Logger;

@ApplicationScoped
public class JsonbPolymorphicDeserializer implements JsonbDeserializer<JsonbPolymorphic> {

    private Jsonb jsonb = JsonbBuilder.create();
    
    @Inject
    private Logger logger;

    public void setJsonb(Jsonb jsonb) {
        this.jsonb = jsonb;
    }
    
    @Override
    public JsonbPolymorphic deserialize(JsonParser parser, DeserializationContext ctx, Type rtType) {
        JsonObject jsonObject = parser.getObject();
        Class<JsonbPolymorphic> rtClass;

        try {
            rtClass = Class.class.cast(Class.forName(jsonObject.getString(JsonbPolymorphic.TYPE_PROPERTY)));
        } catch (ClassNotFoundException ex) {
            rtClass = Class.class.cast(rtType);
        }
        
        try {
            Constructor<JsonbPolymorphic> constructor = rtClass.getConstructor();
            JsonbPolymorphic newInstance = constructor.newInstance();
            
            Collection<Field> fields = new HashSet<>();
            fields.addAll(Arrays.asList(rtClass.getDeclaredFields()));
            
            Class<?> superclass = rtClass.getSuperclass();

            while (Objects.nonNull(superclass)) {
                fields.addAll(Arrays.asList(superclass.getDeclaredFields()));
                superclass = superclass.getSuperclass();
            }
            
            for (Field field: fields){
                if (jsonObject.containsKey(field.getName())){
                    field.setAccessible(true);
                    field.set(newInstance, jsonb.fromJson(jsonObject.get(field.getName()).toString(), field.getType()));
                }
            }
            
            return newInstance;
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            logger.error(ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    
}
