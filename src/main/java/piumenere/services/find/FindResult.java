package piumenere.services.find;

import java.util.Collection;

public class FindResult<T> {

    private Long count;
    private Collection<T> results;

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Collection<T> getResults() {
        return results;
    }

    public void setResults(Collection<T> results) {
        this.results = results;
    }
    
}