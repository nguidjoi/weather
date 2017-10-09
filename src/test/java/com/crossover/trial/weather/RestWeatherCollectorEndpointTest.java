package com.crossover.trial.weather;


import com.crossover.trial.weather.model.AirportItem;
import com.crossover.trial.weather.resources.RestWeatherCollectorEndpoint;
import com.crossover.trial.weather.resources.RestWeatherQueryEndpoint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WeatherServer.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RestWeatherCollectorEndpointTest {
    @Inject
    private RestWeatherQueryEndpoint queryEndpoint;

    @Inject
    private RestWeatherCollectorEndpoint collectorEndpoint;


    @Test
    public void pingResultAlwayToOKStatusBeforeEndpointInitialization() {
        Response response = collectorEndpoint.ping();
        assertThat(response.getStatus(), equalTo(200));
    }

    @Test
    public void updateWeatherAirportWithBadPointTypeIsNotAcceptable() throws Exception {
        Response response = collectorEndpoint.updateWeather("AAA", "", "");
        assertThat(response.getStatus(), equalTo(406));
    }

    @Test
    public void updateWeatherAirportWithNullPointTypeIsnotAcceptable() throws Exception {
        collectorEndpoint.addAirport("AAA", "12", "19");
        Response response = collectorEndpoint.updateWeather("AAA", null, "");
        assertThat(response.getStatus(), equalTo(406));
    }

    @Test
    public void addExistingAirportIsNotPossible() {
        collectorEndpoint.addAirport("AAA", "49", "11");
        Response response = collectorEndpoint.getAirports();

        assertThat(response.getStatus(), equalTo(200));

        response = collectorEndpoint.addAirport("AAA", "67", "19");
        assertThat(response.getStatus(), equalTo(302));

    }

    @Test
    public void searchAllAirportsResultToallrepositoryItemList() {
        collectorEndpoint.addAirport("AAA", "49", "11");
        Response response = collectorEndpoint.getAirports();

        assertThat(response.getStatus(), equalTo(200));
        assertThat(response.getEntity(), instanceOf(java.util.List.class));

        ArrayList<String> entity = (ArrayList<String>) response.getEntity();
        assertThat(entity, hasItems("AAA"));
    }

    @Test
    public void deleteRegisteredAirportEraseThatAirportToRepository() {
        collectorEndpoint.addAirport("ABC", "49", "11");
        Response response = collectorEndpoint.getAirport("ABC");

        AirportItem entity = (AirportItem) response.getEntity();
        assertThat(entity.getIataCode(), equalTo("ABC"));

        collectorEndpoint.deleteAirport("ABC");

        response = collectorEndpoint.getAirport("ABC");
        entity = (AirportItem) response.getEntity();

        assertThat(entity, nullValue());
    }

    @Test
    public void deleteNotRegisteredAirportResultToNotFoundStatus() {
        Response response = collectorEndpoint.deleteAirport("BON");
        assertThat(response.getStatus(), equalTo(404));
    }

}
