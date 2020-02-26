package piumenere.services.security.certificate;

import java.io.IOException;
import javax.annotation.Resource;
import javax.batch.api.AbstractBatchlet;
import javax.batch.api.BatchProperty;
import javax.batch.runtime.BatchStatus;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import org.apache.commons.lang3.StringUtils;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.dmr.ModelNode;
import org.slf4j.Logger;
import piumenere.services.utils.Constants;
import static piumenere.services.utils.Constants.HOSTNAME;

@Named
@Dependent
public class CertificateRenewerBatchlet extends AbstractBatchlet {

    @Inject
    private Logger logger;

    @Inject
    @BatchProperty
    protected String certificateKeystore;

    @Resource(lookup = "java:global/superadminPassword")
    protected String superadminPassword;

    @Inject
    @BatchProperty
    protected String keyManager;
    
    @Override
    public String process() throws Exception {
        
        if (StringUtils.isAnyBlank(certificateKeystore, superadminPassword, keyManager)) return BatchStatus.FAILED.name();;
        
        logger.info("renew " + certificateKeystore);
        
        try {

            ModelControllerClient client = ModelControllerClient.Factory.create("https-remoting", HOSTNAME, 9993, (Callback[] callbacks) -> {
                for (Callback current : callbacks) {
                    if (current instanceof NameCallback) {
                        NameCallback ncb = (NameCallback) current;
                        ncb.setName(Constants.SUPERADMIN_USERNAME);
                    } else if (current instanceof PasswordCallback) {
                        PasswordCallback pcb = (PasswordCallback) current;
                        pcb.setPassword(superadminPassword.toCharArray());
                    }
                }
            });

            ModelNode shouldRenewCertificate = new ModelNode();
            shouldRenewCertificate.get("operation").set("should-renew-certificate");

            shouldRenewCertificate.get("address").add("subsystem", "elytron");
            shouldRenewCertificate.get("address").add("key-store", certificateKeystore);

            shouldRenewCertificate.get("alias").set("piumenere");

            ModelNode shouldRenewCertificateResponse = client.execute(shouldRenewCertificate);

            if (shouldRenewCertificateResponse.has("result") && shouldRenewCertificateResponse.get("result").has("should-renew-certificate") && shouldRenewCertificateResponse.get("result").get("should-renew-certificate").asBoolean()) {

                ModelNode obtainCertificate = new ModelNode();
                obtainCertificate.get("operation").set("obtain-certificate");

                obtainCertificate.get("address").add("subsystem", "elytron");
                obtainCertificate.get("address").add("key-store", certificateKeystore);

                obtainCertificate.get("alias").set("piumenere");
                obtainCertificate.get("certificate-authority-account").set("piumenere");
                obtainCertificate.get("domain-names").add(Constants.HOSTNAME);
                obtainCertificate.get("agree-to-terms-of-service").set(true);

                ModelNode obtainCertificateResponse = client.execute(obtainCertificate);

                if ("success".equals(obtainCertificateResponse.get("outcome").asString())) {

                    ModelNode init = new ModelNode();
                    init.get("operation").set("init");

                    init.get("address").add("subsystem", "elytron");
                    init.get("address").add("key-manager", keyManager);

                    client.execute(init);

                }

            }

        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        return BatchStatus.COMPLETED.name();

    }

}
