package piumenere.services.service;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.jberet.schedule.timer.TimerSchedulerBean;
import piumenere.services.controller.EditableController;
import piumenere.services.entities.Person;

@Path("/person")
@ApplicationScoped
public class PersonService extends AbstractEditableService<Person, EditableController<Person>> {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Person my(){
        return getCustomPrincipal().map(customPrincipal -> customPrincipal.getPerson()).orElse(new Person());
    }
    
}
