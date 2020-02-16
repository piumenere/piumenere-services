package piumenere.services.security;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;
import javax.inject.Inject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.dmr.ModelNode;
import org.slf4j.Logger;
import piumenere.services.utils.Constants;
import static piumenere.services.utils.Constants.HOSTNAME;

@Startup
@Singleton
public class CertificateRenewer {

    @Resource(lookup = "java:global/superadminPassword")
    private String superadminPassword;

    @Resource(lookup = "java:global/certificateKeystore")
    private String certificateKeystore;

    @Resource(lookup = "java:global/keyManager")
    private String keyManager;

    @Resource
    private ManagedScheduledExecutorService managedScheduledExecutorService;

    @Inject
    private Logger logger;

    @PostConstruct
    protected void init() {
        //managedScheduledExecutorService.submit(() -> renew());
        managedScheduledExecutorService.scheduleAtFixedRate(
                this::renew,
                0,
                12,
                TimeUnit.HOURS
        );
    }

    protected void renew() {

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

    }

}
