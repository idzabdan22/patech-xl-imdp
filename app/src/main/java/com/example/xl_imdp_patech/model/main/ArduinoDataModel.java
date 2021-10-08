package com.example.xl_imdp_patech.model.main;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ArduinoDataModel {

    @SerializedName("humidity")
    @Expose
    private Float humidity;

    @SerializedName("rain_dur")
    @Expose
    private Float rain_dur;

    @SerializedName("temp")
    @Expose
    private Float temp;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("hour")
    @Expose
    private Integer hour;

    @SerializedName("last_update")
    @Expose
    private String last_update;

    @SerializedName("temp_f")
    @Expose
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

    public ArduinoDataModel() {
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
