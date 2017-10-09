package com.crossover.trial.weather.model;

/**
 * encapsulates sensor information for a particular location
 */
public class AtmosphericItem {

    /**
     * temperature in degrees celsius
     */
    private DataPoint temperature;

    /**
     * wind speed in km/h
     */
    private DataPoint wind;

    /**
     * humidity in percent
     */
    private DataPoint humidity;

    /**
     * precipitation in cm
     */
    private DataPoint precipitation;

    /**
     * pressure in mmHg
     */
    private DataPoint pressure;

    /**
     * cloud cover percent from 0 - 100 (integer)
     */
    private DataPoint cloudCover;

    /**
     * the last time this data was updated, in milliseconds since UTC epoch
     */
    private long lastUpdateTime;

    public AtmosphericItem() {

    }

    public AtmosphericItem(DataPoint temperature, DataPoint wind, DataPoint humidity, DataPoint percipitation, DataPoint pressure, DataPoint cloudCover) {
        this.temperature = temperature;
        this.wind = wind;
        this.humidity = humidity;
        this.precipitation = percipitation;
        this.pressure = pressure;
        this.cloudCover = cloudCover;
        this.lastUpdateTime = System.currentTimeMillis();
    }

    public DataPoint getTemperature() {
        return temperature;
    }

    public void setTemperature(DataPoint temperature) {
        this.temperature = temperature;
    }

    public DataPoint getWind() {
        return wind;
    }

    public void setWind(DataPoint wind) {

        this.wind = wind;
    }

    public DataPoint getHumidity() {
        return humidity;
    }

    public void setHumidity(DataPoint humidity) {
        this.humidity = humidity;
    }

    public DataPoint getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(DataPoint precipitation) {
        this.precipitation = precipitation;
    }

    public DataPoint getPressure() {
        return pressure;
    }

    public void setPressure(DataPoint pressure) {
        this.pressure = pressure;
    }

    public DataPoint getCloudCover() {
        return cloudCover;
    }

    public void setCloudCover(DataPoint cloudCover) {
        this.cloudCover = cloudCover;
    }

    public long getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    static public class Builder {
        DataPoint temperature;
        DataPoint wind;
        DataPoint humidity;
        DataPoint precipitation;
        DataPoint pressure;
        DataPoint cloudCover;
        long lastUpdateTime;
        AtmosphericItem previous;

        public Builder() {
        }

        public Builder temperature(DataPoint temperature) {
            if (previous != null) {
                previous.setTemperature(temperature);
            } else this.temperature = temperature;
            return this;
        }

        public Builder wind(DataPoint wind) {
            if (previous != null) {
                previous.setWind(wind);
            } else this.wind = wind;
            return this;
        }

        public Builder humidity(DataPoint humidity) {
            if (previous != null) {
                previous.setHumidity(humidity);
            } else this.humidity = humidity;
            return this;
        }

        public Builder precipitation(DataPoint precipitation) {
            if (previous != null) {
                previous.setPrecipitation(precipitation);
            } else this.precipitation = precipitation;

            return this;
        }

        public Builder pressure(DataPoint pressure) {
            if (previous != null) {
                previous.setPressure(pressure);
            } else this.pressure = pressure;

            return this;
        }

        public Builder cloudCover(DataPoint cloudCover) {
            if (previous != null) {
                previous.setCloudCover(cloudCover);
            } else this.cloudCover = cloudCover;
            return this;
        }

        public Builder lastUpdateTime(long lastUpdateTime) {
            if (previous != null) {
                previous.setLastUpdateTime(lastUpdateTime);
            } else this.lastUpdateTime = lastUpdateTime;
            return this;
        }

        public Builder from(AtmosphericItem previous) {
            this.previous = previous;
            return this;
        }

        public AtmosphericItem build() {
            return previous != null ? previous : new AtmosphericItem(this.temperature, this.wind, this.humidity, this.precipitation, this.pressure, this.cloudCover);
        }
    }

}
