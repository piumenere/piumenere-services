package piumenere.services.service;

import java.util.Collection;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import piumenere.services.entities.Creable;
import piumenere.services.controller.CreableController;

public abstract class AbstractCreableService<T extends Creable, C extends CreableController<T>> extends AbstractIdentifiableService<T, C> implements CreableService<T> {
  
    @Override
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<T> create(Collection<T> resources) {
        return getController().create(resources);
    }

}