package com.circle.jetty;

import com.circle.rest.GuiceFeature;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

/**
 * This class launches the web application in an embedded Jetty container.
 * The Java command that is used for launching should fire this main method.
 */
public class Main {

    public static void main(String[] args) throws Exception {

//        uncomment below to test data poller
//        Injector injector = Guice.createInjector(new CircleModule());
//        ExecutorService executorService = injector.getInstance(ExecutorService.class);
//        executorService.awaitTermination(10, TimeUnit.SECONDS);
//        System.exit(0);

        final ResourceConfig config = new ResourceConfig()
                .packages("com.circle")
                .register(GuiceFeature.class);

        String webPort = System.getenv("PORT");
        if (webPort == null || webPort.isEmpty()) {
            webPort = "8080";
        }

        final Server server = new Server(Integer.valueOf(webPort));

        final ServletContextHandler contextHandler = new ServletContextHandler(server, "/");
        final ServletHolder jerseyServlet = new ServletHolder(new ServletContainer(config));
        contextHandler.addServlet(jerseyServlet, "/*");

        server.start();
        server.join();

    }
}
