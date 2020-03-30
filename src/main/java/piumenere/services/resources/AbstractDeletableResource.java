package piumenere.services.resources;

import java.util.Collection;
import javax.ws.rs.DELETE;
import piumenere.services.controller.DeletableController;
import piumenere.services.entities.Deletable;

public abstract class AbstractDeletableResource<T extends Deletable, C extends DeletableController<T>> extends AbstractEditableResource<T, C> implements DeletableResource<T> {

    @Override
    @DELETE
    public void delete(Collection<T> resources) {
        getController().delete(resources);
    }
    
}
