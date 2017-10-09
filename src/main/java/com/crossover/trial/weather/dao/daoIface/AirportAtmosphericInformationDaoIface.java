package com.crossover.trial.weather.dao.daoIface;

import com.crossover.trial.weather.model.AtmosphericItem;
import com.crossover.trial.weather.model.DataPoint;
import com.crossover.trial.weather.tool.DataPointType;

import java.util.Map;

/**
 * Created by sogla on 23/09/2017.
 */
public interface AirportAtmosphericInformationDaoIface {


    /**
     * Read airport atmospheric information.
     *
     * @param atmosphericPoint
     * @return
     */
    AtmosphericItem read(String atmosphericPoint);

    /**
     * Read all atmospheric information.
     *
     * @return
     */
    Map<String, AtmosphericItem> readAll();

    /**
     * Save  atmospheric information.
     *
     * @param atmosphericPoint
     * @param atmosphericItem
     * @return
     */
    public AtmosphericItem save(String atmosphericPoint, AtmosphericItem atmosphericItem);

    /**
     * Update the atmospheric information of atmosphericPoint weather data with the collected data.
     *
     * @param atmosphericPoint the atmospheric point
     * @param pointType        the point type {@link DataPointType}
     * @param dataPoint        a datapoint object holding pointType data
     */
    public void update(String atmosphericPoint, String pointType, DataPoint dataPoint);

    /**
     * Delete airport atmospheric information.
     *
     * @param atmosphericPoint
     * @return
     */
    void delete(String atmosphericPoint);
}
