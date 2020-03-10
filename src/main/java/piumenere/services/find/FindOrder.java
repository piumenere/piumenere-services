package piumenere.services.find;

import piumenere.services.entities.Identifiable;

public class FindOrder<T extends Identifiable> {
    
    private Boolean reverse;
    private String field;

    public Boolean getReverse() {
        return reverse;
    }

    public void setReverse(Boolean reverse) {
        this.reverse = reverse;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
    
}
