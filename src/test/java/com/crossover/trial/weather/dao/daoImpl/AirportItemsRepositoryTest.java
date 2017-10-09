package com.crossover.trial.weather.dao.daoImpl;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by sogla on 08/10/2017.
 */
public class AirportItemsRepositoryTest {


    private AirportItemsRepository repository;

    @Before
    public void setUp() {
        repository = new AirportItemsRepository();
    }

    @Test
    public void repositoryIsEmptyBeforeAnyInitialization() {
        assertThat(repository.readAll().isEmpty());
    }

    @Test
    public void emptyResultWasReturnedInCaseOfNonExitingAirportSearch() {
        assertThat(repository.read("BEL").equals(Optional.empty()));
    }

    @Test
    public void saveAirportPersisteThatOneInRepository() {
        repository.save("BEL", 33, 89.6);
        assertThat(repository.read("BEL").isPresent());
        assertThat(repository.read("BEL").get().getIataCode().equals("BEL"));
    }

    @Test
    public void deletingANonExistentAirportDoesntChangeRepositoryState() {
        repository.delete("CRO");
        assertThat(repository.readAll().isEmpty());
    }

    @Test
    public void deletedAirportDoesntExistInRepository() {
        repository.save("ROG", 53.4, 13.7);
        assertThat(repository.read("ROG").isPresent());

        repository.delete("ROG");
        assertThat(!repository.read("ROG").isPresent());
    }
}