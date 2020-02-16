package piumenere.services.utils;

import java.util.Objects;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class LoggerProducer {

    @Produces
    public Logger produceLogger(InjectionPoint injectionPoint) {
        Bean<?> bean = injectionPoint.getBean();
        if (Objects.nonNull(bean)) {
            return LoggerFactory.getLogger(bean.getBeanClass());
        }
        if (Objects.nonNull(injectionPoint.getMember())){
            return LoggerFactory.getLogger(injectionPoint.getMember().getDeclaringClass());
        }
        return LoggerFactory.getLogger(getClass());    }

}