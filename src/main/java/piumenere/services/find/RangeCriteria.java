package piumenere.services.find;

import java.util.Objects;
import java.util.Optional;
import piumenere.services.entities.Identifiable;

public class RangeCriteria<D extends Identifiable> extends FindCriteria<D> {
    
    private D from;
    private D to;

    public D getFrom() {
        return from;
    }

    public void setFrom(D from) {
        this.from = from;
    }

    public D getTo() {
        return to;
    }

    public void setTo(D to) {
        this.to = to;
    }

    @Override
    public Optional<Class<D>> getCriteriaClass() {
        if (Objects.isNull(to) && Objects.isNull(from)){
            return Optional.empty();
        }
        return Optional.of(Objects.isNull(from) ? (Class<D>) to.getClass() : Objects.isNull(to) ? (Class<D>) from.getClass() : from.getClass().isAssignableFrom(to.getClass()) ? (Class<D>) from.getClass() : (Class<D>) to.getClass());
    }
    
}
