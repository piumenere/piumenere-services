package piumenere.services;

import javax.json.bind.annotation.JsonbProperty;

public interface JsonbPolymorphic {
    
    static final String TYPE_PROPERTY = "@type";
    
    @JsonbProperty(TYPE_PROPERTY)
    default String getJsonbType(){
        return this.getClass().getName();
    }
    
}
