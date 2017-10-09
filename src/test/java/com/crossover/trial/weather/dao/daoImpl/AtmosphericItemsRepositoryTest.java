package com.crossover.trial.weather.dao.daoImpl;

import com.crossover.trial.weather.model.AtmosphericItem;
import com.crossover.trial.weather.model.DataPoint;
import com.crossover.trial.weather.tool.DataPointType;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertNull;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by sogla on 08/10/2017.
 */
public class AtmosphericItemsRepositoryTest {

    AtmosphericItemsRepository repository;
    DataPoint dataPoint;
    DataPoint newDataPoint;
    AtmosphericItem atmosphericItem;
    AtmosphericItem newAtmosphericItem;

    @Before
    public void setUp() throws Exception {
        repository = new AtmosphericItemsRepository();
        dataPoint = new DataPoint.Builder().withCount(12)
                .withFirst(10)
                .withMean(109)
                .build();

        newDataPoint = dataPoint = new DataPoint.Builder().withCount(12)
                .withFirst(30)
                .withMean(53)
                .withMedian(42)
                .build();

        atmosphericItem = new AtmosphericItem.Builder()
                .temperature(dataPoint).build();

        newAtmosphericItem = new AtmosphericItem.Builder()
                .precipitation(dataPoint).build();
    }

    @Test
    public void repositoryIsEmptyBeforeAnyInitialization() {
        assertThat(repository.readAll().isEmpty());
    }

    @Test
    public void nullWasReturnedInCaseOfNonExitingAtmosphericItemSearch() {
        assertNull(repository.read("BIL"));
    }

    @Test
    public void updateAtmosphericItemdoesntUpdatedNotSpecifiedDatapointTypeOfThatAtmosphericItem() {
        repository.save("BOL", atmosphericItem);
        repository.updateAtmosphericInformation(atmosphericItem, DataPointType.TEMPERATURE, newDataPoint);
        assertNull(repository.read("BOL").getCloudCover());
        assertNull(repository.read("BOL").getHumidity());
        assertNull(repository.read("BOL").getPrecipitation());
        assertNull(repository.read("BOL").getPressure());
    }

    @Test
    public void updateAtmosphericItemUpdatedSpecifiedDatapointTypeOfThatAtmosphericItem() {
        repository.save("BOL", atmosphericItem);
        repository.updateAtmosphericInformation(atmosphericItem, DataPointType.TEMPERATURE, newDataPoint);
        assertThat(repository.read("BOL").getTemperature().equals(newDataPoint));
    }

    @Test
    public void saveNonExistingAtmosphericItemPersisteThatOneInRepository() {
        repository.save("BEL", atmosphericItem);
        assertThat(repository.read("BEL").getTemperature().equals(dataPoint));
    }

    @Test
    public void saveAnExistingAtmosphericItemDoesntChangeTheExistingOneInRepository() {
        repository.save("BEL", atmosphericItem);
        repository.save("BEL", newAtmosphericItem);
        assertThat(repository.read("BEL").getTemperature().equals(dataPoint));
    }

    @Test
    public void deletingANonExistentAtmosphericItemDoesntChangeRepositoryState() {
        repository.delete("CRO");
        assertThat(repository.readAll().isEmpty());
    }

    @Test
    public void deletedAtmosphericItemOfAtmosphericPointDoesntExistInRepository() {
        repository.save("ROG", atmosphericItem);
        assertThat(repository.read("ROG").getTemperature().equals(atmosphericItem));

        repository.delete("ROG");
        assertNull(repository.read("ROG"));
    }
}