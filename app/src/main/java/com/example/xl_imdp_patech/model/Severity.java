package com.example.xl_imdp_patech.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Severity {
        @SerializedName("temp")
        @Expose
        private Float temp;

        @SerializedName("rain_dur")
        @Expose
        private Float rain_dur;

        public void ArduinoDataModel(Float temp, Float rain_dur) {
            this.temp = temp;
            this.rain_dur = rain_dur;
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

        public void Severity(){

        double sev = 4.0233-(0.2283*rain_dur)-(0.5308*temp)-(0.0013*rain_dur)+(0.0197*(temp*temp))+(0.0155*(rain_dur*temp));
        return;
        }}

