package com.example.mydemofullweb;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api")
public class HelloApplication extends Application {

}


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