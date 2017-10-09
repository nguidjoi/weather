package com.crossover.trial.weather;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;
import org.springframework.stereotype.Component;

/**
 * Created by sogla on 27/09/2017.
 */
@Component
public class RestWeatherResourceConfig extends ResourceConfig {

    public RestWeatherResourceConfig() {

        //
        register(RequestContextFilter.class);

        //
        register(JacksonFeature.class);

        // Register resources using package-scanning.
        packages("com.crossover.trial.weather.resources");

    }

}
