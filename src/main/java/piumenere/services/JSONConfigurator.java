package piumenere.services;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Provider
public class JSONConfigurator implements ContextResolver<Jsonb> {

    @Inject
    private JsonbPolymorphicDeserializer deserializer;
    private Jsonb jsonb;

    @PostConstruct
    protected void init() {
        JsonbConfig config = new JsonbConfig()
                .withDeserializers(
                        deserializer
                );
        jsonb = JsonbBuilder.create(config);
        deserializer.setJsonb(jsonb);
    }

    @Override
    public Jsonb getContext(Class type) {
        return this.jsonb;
    }
}
