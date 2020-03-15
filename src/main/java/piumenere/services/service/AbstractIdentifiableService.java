package piumenere.services.service;

import java.util.Collection;
import java.util.Optional;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import piumenere.services.bean.GenericBeanProvider;
import piumenere.services.controller.IdentifiableController;
import piumenere.services.entities.Identifiable;
import piumenere.services.find.FindCriteria;
import piumenere.services.find.FindOrder;
import piumenere.services.find.FindResult;
import piumenere.services.generics.AbstractDoubleGenericClass;
import piumenere.services.security.CustomPrincipal;

public abstract class AbstractIdentifiableService<T extends Identifiable, C extends IdentifiableController<T>> extends AbstractDoubleGenericClass<T, C> implements IdentifiableService<T> {

    @Inject
    private GenericBeanProvider genericBeanProvider;
    
    @Inject
    private Logger logger;
    
    @Inject
    protected SecurityContext securityContext;
    
    protected Optional<CustomPrincipal> getCustomPrincipal(){
        if (securityContext.getCallerPrincipal() instanceof CustomPrincipal){
            return Optional.of(CustomPrincipal.class.cast(securityContext.getCallerPrincipal()));
        }
        return Optional.empty();
    }
        
    protected C getController(){
        Optional<C> bean = genericBeanProvider.getBean(getSecondGenericsType());
        if(!bean.isPresent()){
            logger.error("CONTROLLER NOT FOUND " + getSecondGenericsType());
        }
        return bean.get();
    };
    
    @Override
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/find")
    public FindResult<T> find(Collection<FindCriteria<T>> criterias, Collection<FindOrder<T>> orders, Integer quantity, Integer offset) {
        return getController().find(criterias, orders, quantity, offset);
    }

}