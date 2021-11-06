package com.example.xl_imdp_patech.model.main;

import com.google.gson.annotations.SerializedName;

public class ReportModel {
    public String time_stamp;
    public String input_petani;
    public String prediction;
    public Float leaf_wetness;
    public Float temp;

    public ReportModel() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public ReportModel(String time_stamp, String input_petani, String prediction, Float leaf_wetness, Float temp) {
        this.time_stamp = time_stamp;
        this.input_petani = input_petani;
        this.prediction = prediction;
        this.leaf_wetness = leaf_wetness;
        this.temp = temp;
    }
}
