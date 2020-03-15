package piumenere.services.service;

import java.util.Collection;
import javax.ws.rs.Consumes;
import piumenere.services.controller.EditableController;
import piumenere.services.entities.Editable;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

public abstract class AbstractEditableService<T extends Editable, C extends EditableController<T>> extends AbstractCreableService<T, C> implements EditableService<T> {
    
    @Override
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<T> edit(Collection<T> resource) {
        return getController().edit(resource);
    }

}
