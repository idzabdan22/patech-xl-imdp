package com.example.xl_imdp_patech.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Forecastday {
    public HourForecast[] getHourForecasts() {
        return hourForecasts;
    }

    public void setHourForecasts(HourForecast[] hourForecasts) {
        this.hourForecasts = hourForecasts;
    }

    @SerializedName("hour")
    public HourForecast [] hourForecasts;
}
