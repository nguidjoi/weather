package com.crossover.trial.weather.helper;

import com.crossover.trial.weather.model.GeographicPoint;
import org.assertj.core.data.Offset;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by sogla on 08/10/2017.
 */
public class GeographicCalculationHelperTest {

    GeographicCalculationHelper geographicCalculationHelper;

    @Before
    public void setUp() {
        geographicCalculationHelper = new GeographicCalculationHelper();
    }


    @Test
    public void distanceBeetweenTwoPointsShouldBeReflexiveOperation() {
        GeographicPoint point1 = new GeographicPoint(6, 4);
        GeographicPoint point2 = new GeographicPoint(6, 184);

        assertThat(geographicCalculationHelper.calculateDistance(point1, point2)).isEqualTo(geographicCalculationHelper.calculateDistance(point2, point1));
        assertThat(geographicCalculationHelper.calculateDistance(point2, point1)).isEqualTo(16411.375, Offset.offset(1e-3));
    }

    @Test
    public void distanceBeetweenPointsAndHimselfShouldBeEqualToZero() {
        GeographicPoint point = new GeographicPoint(9, 43);

        assertThat(geographicCalculationHelper.calculateDistance(point, point)).isEqualTo(0.0);
    }
}