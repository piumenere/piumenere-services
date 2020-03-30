package piumenere.services.resources;

import java.util.Collection;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import piumenere.services.entities.Creable;
import piumenere.services.controller.CreableController;

public abstract class AbstractCreableResource<T extends Creable, C extends CreableController<T>> extends AbstractIdentifiableResource<T, C> implements CreableResource<T> {
  
    @Override
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<T> create(Collection<T> resources) {
        return getController().create(resources);
    }

}