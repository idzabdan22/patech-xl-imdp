package com.example.xl_imdp_patech.model.main;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ArduinoDataModel {

    private Float humidity;
    private Float rain_dur;
    private Float temp;
    private String date;
    private Integer hour;
    private String last_update;
    private Float temp_f;

    public ArduinoDataModel(Float humidity, Float temp, Float rain_dur, String date, Integer hour, Float temp_f, String last_update) {
        this.humidity = humidity;
        this.last_update = last_update;
        this.temp = temp;
        this.temp_f = temp_f;
        this.rain_dur = rain_dur;
        this.date = date;
        this.hour = hour;
    }

    public ArduinoDataModel(){

    }

    public String getLast_update() {
        return last_update;
    }

    public Float getTemp_f() {
        return temp_f;
    }

    public Integer getHour() {
        return hour;
    }

    public String getDate() {
        return date;
    }

    public Float getHumidity() {
        return humidity;
    }

    public Float getTemp() {
        return temp;
    }

    public Float getRain_dur() {
        return rain_dur;
    }
}
