package com.crossover.trial.weather.dao.daoIface;

import com.crossover.trial.weather.model.AirportItem;

import java.util.List;
import java.util.Optional;

/**
 *
 */
public interface AirPortDaoIface {
    /**
     * Read Airport data.
     *
     * @param iata
     * @return
     */
    Optional<AirportItem> read(String iata);

    /**
     * Read all airport data.
     *
     * @return
     */
    List<AirportItem> readAll();

    /**
     * Save Airport data object.
     *
     * @param airportItem
     * @return
     */
    void save(AirportItem airportItem);

    /**
     * Save Airport data.
     *
     * @param iataCode
     * @param latitude
     * @param longitude
     * @return
     */
    AirportItem save(String iataCode, double latitude, double longitude);

    /**
     * Delete Airport data.
     *
     * @param iata
     * @return
     */
    void delete(String iata);
}
