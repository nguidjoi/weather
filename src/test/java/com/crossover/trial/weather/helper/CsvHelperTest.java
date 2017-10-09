package com.crossover.trial.weather.helper;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by sogla on 08/10/2017.
 */
public class CsvHelperTest {

    private CsvHelper csvSplitter = new CsvHelper();


    @Test
    public void CsvSplitterSplitDataSeparatedByComma() {
        assertThat(csvSplitter.split("La Guardia,New York,United States")).containsExactly("La Guardia", "New York", "United States");
    }


    @Test
    public void CsvSplitterIgnoreCommaInsideQuote() {
        assertThat(csvSplitter.split("\"Morristown, Municipal Airport\",Morristown")).containsExactly("\"Morristown, Municipal Airport\"", "Morristown");
    }
}