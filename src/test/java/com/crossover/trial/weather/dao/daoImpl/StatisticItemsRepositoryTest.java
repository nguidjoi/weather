package com.crossover.trial.weather.dao.daoImpl;

import com.crossover.trial.weather.model.AirportItem;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * Created by sogla on 08/10/2017.
 */
public class StatisticItemsRepositoryTest {

    private StatisticItemsRepository repository;
    private AirportItem airportItem;
    private AirportItem secondAirportItem;

    @Before
    public void setUp() {
        repository = new StatisticItemsRepository();
        airportItem = new AirportItem();
        secondAirportItem = new AirportItem();
        airportItem.setIataCode("OVR");
        repository.getAllRadiusFrequency().clear();
        repository.getAllRequestFrequency().clear();

    }

    @Before


    @Test
    public void radiusFrequencyOfNotExistantRaduisreturnZero() {
        assertThat(repository.readRadiusFrequency(10.0).equals(new Integer(0)));
    }

    @Test
    public void radiusFrequencyOfExistantRaduisSavedFrequency() {
        repository.updateRadiusFrequency(10.0, 1);
        assertThat(repository.readRadiusFrequency(10.0).equals(new Integer(1)));
    }

    @Test
    public void updateRadiusFrequencyOfAnyRadiusSavedUpdatedFrequency() {
        repository.updateRadiusFrequency(34.0, 7);
        assertThat(repository.readRadiusFrequency(34.0).equals(new Integer(7)));

    }

    @Test
    public void incrementRadiusFrequencyOfAnyRadiusSavedPreviousFrequencyPlusOne() {

        assertThat(repository.readRadiusFrequency(36.4).equals(new Integer(0)));

        repository.incrementRadiusFrequency(36.4);
        assertThat(repository.readRadiusFrequency(36.4).equals(new Integer(1)));
    }

    @Test
    public void requestFrequencyOfNotExistantAirportRequestReturnZero() {
        assertThat(repository.readRequestFrequency(airportItem).equals(new Integer(0)));
    }

    @Test
    public void requestFrequencyOfExistantAirportRequestReturnSavedFrequency() {
        assertThat(repository.readRequestFrequency(airportItem).equals(new Integer(0)));

        repository.updateRequestFrequency(airportItem, 9);
        assertThat(repository.readRequestFrequency(airportItem).equals(new Integer(9)));
    }

    @Test
    public void incrementRequestFrequencyOfAnyAirportItemSavedPreviousFrequencyPlusOne() {
        assertThat(repository.readRequestFrequency(airportItem).equals(new Integer(0)));

        repository.incrementRequestFrequency(airportItem);
        assertThat(repository.readRequestFrequency(airportItem)).isEqualTo(new Integer(1));
    }

    @Test
    public void updateRequestFrequencyOfAnyAirportItemSavedUpdatedFrequency() {
        assertThat(repository.readRequestFrequency(airportItem)).isEqualTo(new Integer(0));

        repository.updateRequestFrequency(airportItem, 8);
        assertThat(repository.readRequestFrequency(airportItem)).isEqualTo(new Integer(8));
    }

    @Test
    public void requestFrequencyDataSizeEqualZeroBeforeAnyRequestFrequencyIsSaved() {
        assertThat(repository.requestFrequencyDataSize()).isEqualTo(0);
    }

    @Test
    public void requestFrequencyDataSizeEqualSavedAirportRequest() {
        repository.updateRequestFrequency(airportItem, 8);
        repository.updateRequestFrequency(secondAirportItem, 4);
        assertThat(repository.requestFrequencyDataSize()).isEqualTo(2);
    }

    @Test
    public void thereIsNotRadiusFrequencyIfAnyFrequencyIsSaved() {
        assertThat(repository.getAllRadiusFrequency().isEmpty());
    }

    @Test
    public void AllRadiusFrequencyContainsAllSavedRadiusFrequency() {
        assertThat(repository.getAllRadiusFrequency().isEmpty());

        repository.updateRadiusFrequency(45.0, 5);
        repository.updateRadiusFrequency(56.0, 9);
        repository.updateRadiusFrequency(89.0, 7);

        assertThat(!repository.getAllRadiusFrequency().isEmpty());

        assertThat(repository.getAllRadiusFrequency().get(45.0)).isEqualTo(5);
        assertThat(repository.getAllRadiusFrequency().get(56.0)).isEqualTo(9);
        assertThat(repository.getAllRadiusFrequency().get(89.0)).isEqualTo(7);
    }
}