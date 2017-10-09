package com.crossover.trial.weather.dao.daoImpl;

import com.crossover.trial.weather.dao.daoIface.AirPortDaoIface;
import com.crossover.trial.weather.model.AirportItem;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by sogla on 23/09/2017.
 */
@Component
public class AirportItemsRepository implements AirPortDaoIface {
    /**
     * In memory repository of all Airports information.
     */
    public static final Map<String, AirportItem> AllAirportDataItems = new ConcurrentHashMap<String, AirportItem>();

    @Override
    public Optional<AirportItem> read(String iataCode) {
        return Optional.ofNullable(AllAirportDataItems.get(iataCode));
    }

    @Override
    public List<AirportItem> readAll() {
        return new CopyOnWriteArrayList<>(AllAirportDataItems.values());
    }

    @Override
    public void save(AirportItem airportItemItem) {
        AllAirportDataItems.put(airportItemItem.getIataCode(), airportItemItem);
    }

    /**
     * Add a new known airport to our list.
     *
     * @param iataCode  3 letter code
     * @param latitude  in degrees
     * @param longitude in degrees
     * @return the added airport
     */
    public AirportItem save(String iataCode, double latitude, double longitude) {
        AirportItem airportItem = new AirportItem();
        airportItem.setIataCode(iataCode);
        airportItem.setLatitude(latitude);
        airportItem.setLongitude(longitude);
        save(airportItem);
        return airportItem;

    }

    @Override
    public void delete(String iataCode) {
        AllAirportDataItems.remove(iataCode);
    }

    @PostConstruct
    public void init() {
        save("BOS", 42.364347, -71.005181);
        save("EWR", 40.6925, -74.168667);
        save("JFK", 40.639751, -73.778925);
        save("LGA", 40.777245, -73.872608);
        save("MMU", 40.79935, -74.4148747);
    }


    @PreDestroy
    public void release() {
        AllAirportDataItems.clear();
    }
}
