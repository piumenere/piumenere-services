package piumenere.services.entities;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import org.apache.commons.lang3.StringUtils;

@MappedSuperclass
public abstract class Codifiable extends Validable {

    @Column(nullable = false)
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @PrePersist
    protected void onPrePersistCodifiable() {
        if (StringUtils.isBlank(getCode())){
            setCode(UUID.randomUUID().toString());
        }
    }
    
}