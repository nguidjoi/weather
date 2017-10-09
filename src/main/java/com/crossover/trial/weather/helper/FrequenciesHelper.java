package com.crossover.trial.weather.helper;

import com.crossover.trial.weather.dao.daoImpl.AirportItemsRepository;
import com.crossover.trial.weather.dao.daoImpl.AtmosphericItemsRepository;
import com.crossover.trial.weather.dao.daoImpl.StatisticItemsRepository;
import com.crossover.trial.weather.model.AirportItem;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Alain Roger NGUIDJOI BELL on 26/09/2017.
 */
@Component
public class FrequenciesHelper {

    @Inject
    private AirportItemsRepository airportItemsRepository;

    @Inject
    private AtmosphericItemsRepository atmosphericItemsRepository;

    @Inject
    private StatisticItemsRepository statisticItemsRepository;

    /**
     * Records information about how often requests are made.
     *
     * @param iataCode an iata code
     * @param radius   query radius
     */
    public void updateRequestAndRadiusFrequency(String iataCode, Double radius) {
        airportItemsRepository.read(iataCode)
                .ifPresent(
                        airportItem -> {
                            statisticItemsRepository.incrementRequestFrequency(airportItem);
                            statisticItemsRepository.incrementRadiusFrequency(radius);
                        }
                );
    }


    public int[] calculateHistorique() {
        int m = statisticItemsRepository.getAllRadiusFrequency().keySet().stream()
                .max(Double::compare)
                .orElse(1000.0).intValue() + 1;

        int[] hist = new int[m];
        for (Map.Entry<Double, Integer> frequency : statisticItemsRepository.getAllRadiusFrequency().entrySet()) {
            int i = frequency.getKey().intValue() % 10;
            hist[i] += frequency.getValue();
        }
        return hist;
    }

    public Map<String, Double> calculateFrequency() {
        Map<String, Double> frequency = new HashMap<>();
        // fraction of queries
        for (AirportItem data : airportItemsRepository.readAll()) {
            double frac = (double) statisticItemsRepository.readRequestFrequency(data) / statisticItemsRepository.requestFrequencyDataSize();
            frequency.put(data.getIataCode(), frac);
        }
        return frequency;
    }

    public int calculateDataSize() {
        final AtomicInteger dataSize = new AtomicInteger();

        atmosphericItemsRepository.readAll().values().forEach(
                atmosphericInformation -> {
                    if (atmosphericInformation.getCloudCover() != null
                            || atmosphericInformation.getHumidity() != null
                            || atmosphericInformation.getPressure() != null
                            || atmosphericInformation.getPrecipitation() != null
                            || atmosphericInformation.getTemperature() != null
                            || atmosphericInformation.getWind() != null) {

                        if (atmosphericInformation.getLastUpdateTime() > System.currentTimeMillis() - 86400000) {
                            dataSize.incrementAndGet();
                        }
                    }
                }
        );
        return dataSize.get();
    }

}
