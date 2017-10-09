package com.crossover.trial.weather.resources;

import com.crossover.trial.weather.WeatherQueryEndpoint;
import com.crossover.trial.weather.dao.daoIface.AirPortDaoIface;
import com.crossover.trial.weather.dao.daoImpl.AtmosphericItemsRepository;
import com.crossover.trial.weather.dao.daoImpl.StatisticItemsRepository;
import com.crossover.trial.weather.helper.FrequenciesHelper;
import com.crossover.trial.weather.helper.GeographicCalculationHelper;
import com.crossover.trial.weather.model.AirportItem;
import com.crossover.trial.weather.model.AtmosphericItem;
import com.google.gson.Gson;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static com.crossover.trial.weather.tool.WeatherUrlUtils.QUERY_URL;
import static java.util.stream.Collectors.toList;

/**
 * The Weather App REST endpoint allows clients to query, update and check health stats. Currently, all data is
 * held in memory. The end point deploys to a single container
 *
 * @author code test administrator
 */
@Controller
@Path(QUERY_URL)
public class RestWeatherQueryEndpoint implements WeatherQueryEndpoint {

    public final static Logger log = Logger.getLogger("WeatherQuery");
    /**
     * shared gson json to object factory
     */
    public static final Gson gson = new Gson();

    @Inject
    private AirPortDaoIface airPortDao;

    @Inject
    private AtmosphericItemsRepository atmosphericItemsRepository;

    @Inject
    private GeographicCalculationHelper geographicCalculationHelper;

    @Inject
    private FrequenciesHelper frequenciesHelper;

    @Inject
    private StatisticItemsRepository statisticItemsRepository;

    /**
     * Retrieve service health including total size of valid data points and request frequency information.
     *
     * @return health stats for the service as a string
     */
    @Override
    public String ping() {
        Map<String, Object> retval = new HashMap<>();
        int dataSize = frequenciesHelper.calculateDataSize();
        retval.put("datasize", dataSize);

        Map<String, Double> frequency = frequenciesHelper.calculateFrequency();
        retval.put("iata_freq", frequency);

        int[] hist = frequenciesHelper.calculateHistorique();
        retval.put("radius_freq", hist);

        return gson.toJson(retval);
    }


    /**
     * Given a query in json format {'iata': CODE, 'radius': km} extracts the requested airport information.
     *
     * @param iataCode     the iata code
     * @param radiusString the radius in km
     * @return a list of atmospheric information
     */
    @Override
    public Response weather(String iataCode, String radiusString) {
        double radius = radiusString == null || radiusString.trim().isEmpty() ? 0 : Double.valueOf(radiusString);
        frequenciesHelper.updateRequestAndRadiusFrequency(iataCode, radius);

        List<AtmosphericItem> atmosphericItemOfNearAirports = new ArrayList<>();
        if (radius == 0) {
            atmosphericItemOfNearAirports.add(atmosphericItemsRepository.read(iataCode));
        } else {
            AirportItem mainAirport = airPortDao.read(iataCode).get();
            List<AirportItem> allAirportItem = airPortDao.readAll();

            atmosphericItemOfNearAirports = allAirportItem.stream()
                    .filter(secondAirport -> (geographicCalculationHelper.calculateDistance(mainAirport, secondAirport) <= radius))
                    .map(airport -> atmosphericItemsRepository.read(airport.getIataCode()))
                    .filter(atmosphericItem -> (atmosphericItem.getCloudCover() != null
                            || atmosphericItem.getHumidity() != null
                            || atmosphericItem.getPrecipitation() != null
                            || atmosphericItem.getPressure() != null
                            || atmosphericItem.getTemperature() != null
                            || atmosphericItem.getWind() != null))
                    .collect(toList());
        }


        return Response.status(Response.Status.OK).entity(atmosphericItemOfNearAirports).build();
    }

}
