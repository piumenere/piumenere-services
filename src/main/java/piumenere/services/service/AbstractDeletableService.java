package piumenere.services.service;

import java.util.Collection;
import javax.ws.rs.DELETE;
import piumenere.services.controller.DeletableController;
import piumenere.services.entities.Deletable;

public abstract class AbstractDeletableService<T extends Deletable, C extends DeletableController<T>> extends AbstractEditableService<T, C> implements DeletableService<T> {

    @Override
    @DELETE
    public void delete(Collection<T> resources) {
        getController().delete(resources);
    }
    
}
