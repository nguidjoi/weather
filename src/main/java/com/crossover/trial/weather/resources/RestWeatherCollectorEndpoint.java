package com.crossover.trial.weather.resources;

import com.crossover.trial.weather.WeatherCollectorEndpoint;
import com.crossover.trial.weather.dao.daoIface.AirPortDaoIface;
import com.crossover.trial.weather.dao.daoImpl.AtmosphericItemsRepository;
import com.crossover.trial.weather.model.AtmosphericItem;
import com.crossover.trial.weather.model.DataPoint;
import com.google.gson.Gson;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.crossover.trial.weather.tool.WeatherUrlUtils.COLLECT_URL;

/**
 * A REST implementation of the WeatherCollector API.
 * <p>Accessible only to airport weather collection
 * sites via secure VPN.
 *
 * @author code test administrator
 */

@Path(COLLECT_URL)
@Controller
public class RestWeatherCollectorEndpoint implements WeatherCollectorEndpoint {
    public final static Logger log = Logger.getLogger(RestWeatherCollectorEndpoint.class.getName());

    /**
     * shared gson json to object factory
     */

    public static Gson gson = new Gson();

    @Inject
    private AirPortDaoIface airPortDao;

    @Inject
    private AtmosphericItemsRepository atmosphericItemsRepository;


    @Override
    public Response ping() {
        return Response.status(Response.Status.OK).entity("ready").build();
    }

    @Override
    public Response updateWeather(@PathParam("iata") String iataCode,
                                  @PathParam("pointType") String pointType,
                                  String datapointJson) {

        if (!airPortDao.read(iataCode).isPresent()) {
            log.log(Level.INFO, " update weather not possible with that value : " + iataCode + " " + pointType + " " + datapointJson);
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }

        if (!Optional.ofNullable(pointType).isPresent() || pointType.trim().isEmpty()) {
            log.log(Level.INFO, " update weather impossible bad point type value : " + pointType);
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }

        atmosphericItemsRepository.update(iataCode, pointType, gson.fromJson(datapointJson, DataPoint.class));

        return Response.status(Response.Status.OK).build();
    }

    @Override
    public Response getAirports() {
        List<String> allAirports = new ArrayList<>();
        Optional.ofNullable(airPortDao.readAll())
                .ifPresent((AllAirports) -> {
                            AllAirports.stream().forEach(airport -> allAirports.add(airport.getIataCode()));
                        }
                );
        return Response.status(Response.Status.OK).entity(allAirports).build();
    }

    @Override
    @GET
    @Path("/airport/{iata}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAirport(@PathParam("iata") String iata) {
        log.info("You requested Airport data for airport with iata code :" + iata);

        return airPortDao.read(iata)
                .map(airportItem -> Response
                        .status(Response.Status.OK)
                        .entity(airportItem).build()
                ).orElse(Response
                        .status(Response.Status.NOT_FOUND)
                        .build());
    }

    @Override
    @POST
    @Path("/airport/{iata}/{lat}/{long}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addAirport(@PathParam("iata") String iata,
                               @PathParam("lat") String latString,
                               @PathParam("long") String longString) {

        if (airPortDao.read(iata).isPresent()) {
            log.info("Airport data for airport with iata code " + iata + " already existed");
            return Response.status(Response.Status.FOUND).build();
        }
        log.info("Airport data for airport with iata code " + iata + " are about to be registered ");

        airPortDao.save(iata, Double.valueOf(latString), Double.valueOf(longString));
        atmosphericItemsRepository.save(iata, new AtmosphericItem());
        return Response.status(Response.Status.OK).build();
    }

    @Override
    @DELETE
    @Path("/airport/{iata}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAirport(@PathParam("iata") String iata) {
        if (!airPortDao.read(iata).isPresent()) {
            log.info("Airport data for airport with iata code " + iata + " does not existed");
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        log.info("Airport data for airport with iata code " + iata + " are about to be deleted ");

        airPortDao.delete(iata);
        atmosphericItemsRepository.delete(iata);
        return Response.status(Response.Status.OK).build();
    }

    @Override
    public Response exit() {
        log.info("exit() shutting down server");

        System.exit(0);
        return Response.noContent().build();
    }

}
