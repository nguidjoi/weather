package com.crossover.trial.weather;

import com.crossover.trial.weather.model.AtmosphericItem;
import com.crossover.trial.weather.model.DataPoint;
import com.crossover.trial.weather.resources.RestWeatherCollectorEndpoint;
import com.crossover.trial.weather.resources.RestWeatherQueryEndpoint;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WeatherServer.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RestWeatherQueryEndpointTest {

    @Inject
    private RestWeatherQueryEndpoint queryEndpoint;

    @Inject
    private RestWeatherCollectorEndpoint collectorEndpoint;

    private Gson _gson = new Gson();

    private DataPoint dataPoint;

    @Before
    public void setUp() throws Exception {
        dataPoint = new DataPoint.Builder()
                .withCount(10).withFirst(10).withMedian(20).withLast(30).withMean(22).build();
        collectorEndpoint.updateWeather("BOS", "wind", _gson.toJson(dataPoint));
        queryEndpoint.weather("BOS", "0").getEntity();
    }

    @Test
    public void testPing() throws Exception {
        String ping = queryEndpoint.ping();
        JsonElement pingResult = new JsonParser().parse(ping);
        assertThat(1, equalTo(pingResult.getAsJsonObject().get("datasize").getAsInt()));
        assertThat(5, equalTo(pingResult.getAsJsonObject().get("iata_freq").getAsJsonObject().entrySet().size()));
    }

    @Test
    public void testGet() throws Exception {
        List<AtmosphericItem> ais = (List<AtmosphericItem>) queryEndpoint.weather("BOS", "0").getEntity();
        assertThat(ais.get(0).getWind(), equalTo(dataPoint));
    }

    @Test
    public void testGetNearby() throws Exception {
        // check datasize response
        collectorEndpoint.updateWeather("JFK", "wind", _gson.toJson(dataPoint));
        dataPoint.setMean(40);
        collectorEndpoint.updateWeather("EWR", "wind", _gson.toJson(dataPoint));
        dataPoint.setMean(30);
        collectorEndpoint.updateWeather("LGA", "wind", _gson.toJson(dataPoint));

        List<AtmosphericItem> ais = (List<AtmosphericItem>) queryEndpoint.weather("JFK", "200").getEntity();
        assertThat(ais, hasSize(3));
    }

    @Test
    public void testUpdate() throws Exception {

        DataPoint windDp = new DataPoint.Builder()
                .withCount(10).withFirst(10).withMedian(20).withLast(30).withMean(22).build();
        collectorEndpoint.updateWeather("BOS", "wind", _gson.toJson(windDp));
        queryEndpoint.weather("BOS", "0").getEntity();

        String ping = queryEndpoint.ping();
        JsonElement pingResult = new JsonParser().parse(ping);
        assertThat(1, equalTo(pingResult.getAsJsonObject().get("datasize").getAsInt()));

        DataPoint cloudCoverDp = new DataPoint.Builder()
                .withCount(4).withFirst(10).withMedian(60).withLast(100).withMean(50).build();
        collectorEndpoint.updateWeather("BOS", "cloudcover", _gson.toJson(cloudCoverDp));

        List<AtmosphericItem> ais = (List<AtmosphericItem>) queryEndpoint.weather("BOS", "0").getEntity();
        assertThat(ais.get(0).getWind(), equalTo(windDp));
        assertThat(ais.get(0).getCloudCover(), equalTo(cloudCoverDp));
    }

}