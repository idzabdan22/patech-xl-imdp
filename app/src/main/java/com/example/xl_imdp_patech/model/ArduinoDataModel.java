package com.example.xl_imdp_patech.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ArduinoDataModel {

    @SerializedName("humidity")
    @Expose
    private Integer humidity;

    @SerializedName("rain_condition")
    @Expose
    private Integer rain_condition;

    @SerializedName("temp")
    @Expose
    private Float temp;

    public ArduinoDataModel(Integer humidity, Integer rain_condition, Float temp, Float rain_dur) {
        this.humidity = humidity;
        this.rain_condition = rain_condition;
        this.temp = temp;
        this.rain_dur = rain_dur;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public Integer getRain_condition() {
        return rain_condition;
    }

    public void setRain_condition(Integer rain_condition) {
        this.rain_condition = rain_condition;
    }

    public Float getTemp() {
        return temp;
    }

    public void setTemp(Float temp) {
        this.temp = temp;
    }

    public Float getRain_dur() {
        return rain_dur;
    }

    public void setRain_dur(Float rain_dur) {
        this.rain_dur = rain_dur;
    }

    @SerializedName("rain_dur")
    @Expose
    private Float rain_dur;

    public ArduinoDataModel(){
    }



}
