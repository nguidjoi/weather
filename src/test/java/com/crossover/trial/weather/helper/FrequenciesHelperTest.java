package com.crossover.trial.weather.helper;

import com.crossover.trial.weather.dao.daoImpl.AirportItemsRepository;
import com.crossover.trial.weather.dao.daoImpl.AtmosphericItemsRepository;
import com.crossover.trial.weather.dao.daoImpl.StatisticItemsRepository;
import com.crossover.trial.weather.model.AirportItem;
import com.crossover.trial.weather.model.AtmosphericItem;
import org.easymock.EasyMockRule;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.*;

/**
 * Created by sogla on 08/10/2017.
 */
public class FrequenciesHelperTest {
    @Rule
    public EasyMockRule mocks = new EasyMockRule(this);

    @TestSubject
    FrequenciesHelper frequenciesHelper = new FrequenciesHelper();
    AirportItem airportItem;
    List<AirportItem> airports;
    @Mock
    private AirportItemsRepository airportItemsRepository;
    @Mock
    private AtmosphericItemsRepository atmosphericItemsRepository;
    @Mock
    private StatisticItemsRepository statisticItemsRepository;

    @Before
    public void setUp() {
        airportItem = new AirportItem();
        airportItem.setIataCode("NSI");
        airports = new ArrayList<>();
        airports.add(airportItem);

    }


    @Test
    public void statisticInformationAreEqualsZeroIsNoAirportAndAtmosphericItem() {
        expect(airportItemsRepository.read("NSI")).andReturn(Optional.empty());
        expect(airportItemsRepository.readAll()).andReturn(new ArrayList<AirportItem>());
        expect(statisticItemsRepository.getAllRadiusFrequency()).andReturn(new HashMap<Double, Integer>()).times(2);
        expect(atmosphericItemsRepository.readAll()).andReturn(new HashMap<String, AtmosphericItem>());

        replay(airportItemsRepository);
        replay(statisticItemsRepository);
        replay(atmosphericItemsRepository);

        frequenciesHelper.updateRequestAndRadiusFrequency("NSI", 34.0);
        assertThat(frequenciesHelper.calculateFrequency().size() == 0);
        assertThat(frequenciesHelper.calculateHistorique().length == 0);
        assertThat(frequenciesHelper.calculateDataSize() == 0);

        verify(airportItemsRepository);
        verify(statisticItemsRepository);
        verify(atmosphericItemsRepository);
    }

    @Test
    public void statisticInformationAreNotEqualTozeroIfAirportAndAtmosphericItemExist() {

        expect(airportItemsRepository.read("NSI")).andReturn(Optional.ofNullable(airportItem));
        expect(airportItemsRepository.readAll()).andReturn(airports);
        expect(statisticItemsRepository.getAllRadiusFrequency()).andReturn(new HashMap<Double, Integer>()).times(2);

        expect(statisticItemsRepository.requestFrequencyDataSize()).andReturn(1);
        expect(statisticItemsRepository.readRequestFrequency(airportItem)).andReturn(new Integer(1));
        statisticItemsRepository.incrementRequestFrequency(airportItem);
        statisticItemsRepository.incrementRadiusFrequency(34.0);

        replay(airportItemsRepository);
        replay(statisticItemsRepository);
        replay(atmosphericItemsRepository);

        frequenciesHelper.updateRequestAndRadiusFrequency("NSI", 34.0);

        assertTrue(frequenciesHelper.calculateFrequency().size() != 0);
        assertTrue(frequenciesHelper.calculateHistorique().length != 0);
        verify(airportItemsRepository);
        verify(statisticItemsRepository);

    }


}