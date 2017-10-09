package com.crossover.trial.weather.helper;

import org.springframework.stereotype.Component;

/**
 * Created by sogla on 24/09/2017.
 */
@Component
public class CsvHelper {
    public String[] split(String line) {
        return line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
    }
}
