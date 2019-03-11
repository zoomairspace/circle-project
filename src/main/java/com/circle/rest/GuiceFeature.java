package com.circle.rest;

import com.circle.guice.CircleModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.InjectionManagerProvider;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;
import java.util.concurrent.ExecutorService;

/**
 * Jersey uses HK2 and knows nothing about Guice. This code bridges the Guice injector with HK2.
 *
 * @see https://stackoverflow.com/questions/52546689/java-guice-di-error-unsatisfieddependencyexception-there-was-no-object-availab
 */
@Provider
public class GuiceFeature implements Feature {

    @Override
    public boolean configure(FeatureContext context) {

        final ServiceLocator locator = InjectionManagerProvider
                .getInjectionManager(context).getInstance(ServiceLocator.class);

        final Injector injector = Guice.createInjector(new CircleModule());
        // initialize data poller
        injector.getInstance(ExecutorService.class);

        GuiceBridge.getGuiceBridge().initializeGuiceBridge(locator);
        locator.getService(GuiceIntoHK2Bridge.class).bridgeGuiceInjector(injector);
        return true;
    }
}
