package com.crossover.trial.weather.dao.daoImpl;

import com.crossover.trial.weather.model.AirportItem;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by sogla on 26/09/2017.
 */
@Component
public class StatisticItemsRepository {


    private final static Map<AirportItem, Integer> requestFrequency = new ConcurrentHashMap<AirportItem, Integer>();

    private final static Map<Double, Integer> radiusFrequency = new ConcurrentHashMap<Double, Integer>();

    public Integer readRadiusFrequency(Double radius) {
        return radiusFrequency.getOrDefault(radius, 0);
    }

    public void updateRadiusFrequency(Double radius, int frequency) {
        radiusFrequency.put(radius, frequency);
    }

    public void incrementRadiusFrequency(Double radius) {
        updateRadiusFrequency(radius, readRadiusFrequency(radius) + 1);
    }

    public Integer readRequestFrequency(AirportItem airportItem) {
        return requestFrequency.getOrDefault(airportItem, 0);
    }

    public void incrementRequestFrequency(AirportItem airportItem) {
        updateRequestFrequency(airportItem, readRequestFrequency(airportItem) + 1);
    }

    public void updateRequestFrequency(AirportItem airportItem, Integer frequency) {
        requestFrequency.put(airportItem, frequency);
    }

    public int requestFrequencyDataSize() {
        return requestFrequency.size();
    }

    public Map<Double, Integer> getAllRadiusFrequency() {
        return radiusFrequency;
    }

    public Map<AirportItem, Integer> getAllRequestFrequency() {
        return requestFrequency;
    }


}
