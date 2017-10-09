package com.crossover.trial.weather.dao.daoImpl;

import com.crossover.trial.weather.dao.daoIface.AirportAtmosphericInformationDaoIface;
import com.crossover.trial.weather.model.AtmosphericItem;
import com.crossover.trial.weather.model.DataPoint;
import com.crossover.trial.weather.tool.DataPointType;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * Created by sogla on 23/09/2017.
 */
@Component
public class AtmosphericItemsRepository implements AirportAtmosphericInformationDaoIface {

    /**
     * In memory repository of all atmospheric information.
     */
    public static final Map<String, AtmosphericItem> AllAtmosphericInformationData = new ConcurrentHashMap<String, AtmosphericItem>();

    private static final io.vavr.collection.Map<DataPointType, Tuple2<Predicate<DataPoint>, BiConsumer<AtmosphericItem.Builder, DataPoint>>> datapointTypePredicateFunction = HashMap.ofEntries(
            Tuple.of(DataPointType.WIND, Tuple.of((Predicate<DataPoint>) dataPoint -> dataPoint.getMean() >= 0.0, (BiConsumer<AtmosphericItem.Builder, DataPoint>) AtmosphericItem.Builder::wind)),
            Tuple.of(DataPointType.TEMPERATURE, Tuple.of((Predicate<DataPoint>) dataPoint -> (dataPoint.getMean() >= -50 && dataPoint.getMean() < 100), (BiConsumer<AtmosphericItem.Builder, DataPoint>) AtmosphericItem.Builder::temperature)),
            Tuple.of(DataPointType.HUMIDTY, Tuple.of((Predicate<DataPoint>) dataPoint -> (dataPoint.getMean() >= 0 && dataPoint.getMean() < 100), (BiConsumer<AtmosphericItem.Builder, DataPoint>) AtmosphericItem.Builder::humidity)),
            Tuple.of(DataPointType.PRESSURE, Tuple.of((Predicate<DataPoint>) dataPoint -> (dataPoint.getMean() >= 650 && dataPoint.getMean() < 800), (BiConsumer<AtmosphericItem.Builder, DataPoint>) AtmosphericItem.Builder::pressure)),
            Tuple.of(DataPointType.CLOUDCOVER, Tuple.of((Predicate<DataPoint>) dataPoint -> (dataPoint.getMean() >= 0 && dataPoint.getMean() < 100), (BiConsumer<AtmosphericItem.Builder, DataPoint>) AtmosphericItem.Builder::cloudCover)),
            Tuple.of(DataPointType.PRECIPITATION, Tuple.of((Predicate<DataPoint>) dataPoint -> (dataPoint.getMean() >= 0 && dataPoint.getMean() < 100), (BiConsumer<AtmosphericItem.Builder, DataPoint>) AtmosphericItem.Builder::precipitation))
    );

    /**
     * Read atmospheric information.
     *
     * @param atmosphericPoint atmospheric information point
     * @return
     */
    @Override
    public AtmosphericItem read(String atmosphericPoint) {

        return AllAtmosphericInformationData.getOrDefault(atmosphericPoint, null);
    }

    /**
     * Read all atmospheric information.
     *
     * @return
     */
    @Override
    public Map<String, AtmosphericItem> readAll() {
        return AllAtmosphericInformationData;
    }


    /**
     * Save airport atmospheric information.
     *
     * @param atmosphericPoint
     * @param atmosphericItem
     * @return
     */
    @Override
    public AtmosphericItem save(String atmosphericPoint, AtmosphericItem atmosphericItem) {
        return AllAtmosphericInformationData.putIfAbsent(atmosphericPoint, atmosphericItem);
    }

    /**
     * Update atmospheric information given by atmosphericPoint with the collected data.
     *
     * @param atmosphericPoint the atmospheric information point
     * @param pointType        the point type {@link DataPointType}
     * @param dataPoint        a datapoint object holding pointType data
     */
    @Override
    public void update(String atmosphericPoint, String pointType, DataPoint dataPoint) {

        final DataPointType dataPointType = DataPointType.valueOf(pointType.toUpperCase());

        save(atmosphericPoint, new AtmosphericItem());
        final Optional<AtmosphericItem> optionalAtmosphericInformation = Optional
                .ofNullable(read(atmosphericPoint));

        optionalAtmosphericInformation.ifPresent(atmosphericInformation ->
                updateAtmosphericInformation(atmosphericInformation, dataPointType, dataPoint));
    }

    /**
     * Update existing atmospheric information object.
     *
     * @param atmosphericItem
     * @param pointType
     * @param dataPoint
     */
    void updateAtmosphericInformation(AtmosphericItem atmosphericItem, DataPointType pointType, DataPoint dataPoint) {
        Tuple2<Predicate<DataPoint>, BiConsumer<AtmosphericItem.Builder, DataPoint>> tupleOfdataType = datapointTypePredicateFunction.get(pointType).get();
        AtmosphericItem.Builder builder = new AtmosphericItem.Builder();

        if (dataPoint != null && tupleOfdataType._1.test(dataPoint)) {
            builder.from(atmosphericItem);
            tupleOfdataType._2().accept(builder, dataPoint);
            builder.lastUpdateTime(System.currentTimeMillis());
            builder.build();
        }
    }

    /**
     * Delete  atmospheric information of atmospheric point.
     *
     * @param atmosphericPoint
     * @return
     */
    public void delete(String atmosphericPoint) {
        AllAtmosphericInformationData.remove(atmosphericPoint);
    }

    @PostConstruct
    public void init() {
        save("BOS", new AtmosphericItem());
        save("EWR", new AtmosphericItem());
        save("JFK", new AtmosphericItem());
        save("LGA", new AtmosphericItem());
        save("MMU", new AtmosphericItem());
    }

    @PreDestroy
    public void release() {
        AllAtmosphericInformationData.clear();
    }


}