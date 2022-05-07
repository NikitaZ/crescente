package com.example.mydemofullweb.web.service;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api")
public class HelloApplication extends Application {

}

/*
 We extend Resource config instead of extends Application due to
 https://eclipse-ee4j.github.io/jersey.github.io/documentation/latest/deployment.html#environmenmt.appmodel
 i.e. it's a courtesy from Jersey (quote):

 Alternatively it is possible to reuse ResourceConfig - Jersey's own implementations of Application class.
 This class can either be directly instantiated and then configured or it can be extended and the configuration
 code placed into the constructor of the extending class. The approach typically depends on the chosen deployment runtime.

Compared to Application, the ResourceConfig provides advanced capabilities
to simplify registration of JAX-RS components, such as scanning for root resource
and provider classes in a provided classpath or a set of package names etc.
All JAX-RS component classes that are either manually registered or found during scanning
are automatically added to the set of classes that are returned by getClasses.
For example, the following application class that extends from ResourceConfig
scans during deployment for JAX-RS components in packages org.foo.rest and org.bar.rest:

Note: Package scanning ignores an inheritance and therefore @Path annotation on parent classes and interfaces
will be ignored. These classes won't be registered as the JAX-RS component classes.

Example 4.2. Reusing Jersey implementation in your custom application model
public class MyApplication extends ResourceConfig {
    public MyApplication() {
        packages("org.foo.rest;org.bar.rest");
    }
}
 */
//import jakarta.ws.rs.ApplicationPath;
//import jakarta.ws.rs.core.Application;
//import org.glassfish.jersey.message.GZipEncoder;
//import org.glassfish.jersey.server.ResourceConfig;
//import org.glassfish.jersey.server.filter.EncodingFilter;
//
//import java.util.logging.Logger;
//@ApplicationPath("/api")
//public class HelloApplication extends ResourceConfig {
//
//    private static final Logger LOGGER = Logger.getLogger(HelloApplication.class.getName());
//
//    public HelloApplication() {
//
//        LOGGER.info("Configuration Hello REST Application");
//
////        LOGGER.info("Register Jackson");
////        register(JacksonFeature.class);
//
////        LOGGER.info("Register PuzzleJSONMapper");
////        register(PuzzleJSONMapper.class);
//
////        disabled to avoid extra logging
////        LOGGER.info("Register UserCookieFilter");
////        register(UserCookieFilter.class);
////        register(UserAuthenticationFilter.class);
//
//        LOGGER.info("Register packages");
//        packages("com.example.mydemofullweb", "com.example.mydemofullweb.web", "com.example.mydemofullweb.web.service");
//
//        LOGGER.info("Enabling GZip filter");
//        EncodingFilter.enableFor(this, GZipEncoder.class);
//
//        // Enable for debugging:
//        //LOGGER.info("Enable logging");
//        //registerInstances(new LoggingFilter(LOGGER, true));
//    }
//
//}