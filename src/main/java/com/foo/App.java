package com.foo;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

/**
 * Hello world!
 *
 */
public class App 
{

    @Inject
    VelocityTransformer velocityTransformer;
    public static void main( String[] args )
    {

        Injector injector = Guice.createInjector();
        App main = injector.getInstance(App.class);

        WireMockServer wireMockServer = new WireMockServer(wireMockConfig().port(8080).extensions(main.velocityTransformer).usingFilesUnderDirectory("/tmp").containerThreads(500)); //No-args constructor will start on port 8080, no HTTPS


        wireMockServer.start();

    }
}
