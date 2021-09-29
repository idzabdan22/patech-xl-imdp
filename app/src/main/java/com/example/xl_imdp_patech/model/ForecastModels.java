package com.example.xl_imdp_patech.model;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.xl_imdp_patech.ml.Patech;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import android.annotation.SuppressLint;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class ForecastModels extends AppCompatActivity {
    Interpreter tflite;

    @SerializedName("temp")
    @Expose
    private Float temp;

    @SerializedName("rain_dur")
    @Expose
    private Float rain_dur;

    public void ArduinoDataModel(Float temp, Float rain_dur) {
        this.temp = temp;
        this.rain_dur = rain_dur; }

    public Float getTempF() { return temp=(temp*9/5)+32; }
    public void setTempF(Float temp) {
        this.temp = temp;
    }
    public Float getRain_dur() {
        return rain_dur;
    }
    public void setRain_dur(Float rain_dur) {
        this.rain_dur = rain_dur;
    }

    //ByteBuffer
    private ByteBuffer byteBuffer = ByteBuffer.allocateDirect(2*4);
    public ByteBuffer getByteBuffer() {
        byteBuffer.putFloat(getTempF());
        byteBuffer.putFloat(getRain_dur());
        return byteBuffer;
    }


        protected void onCreate (Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            try {
                Patech model = Patech.newInstance(this);
                tflite = new Interpreter(loadModelFile());
                TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 2}, DataType.FLOAT32);
                inputFeature0.loadBuffer(byteBuffer);

                // Runs model inference and gets result.
                Patech.Outputs outputs = model.process(inputFeature0);
                TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
                //tv :TextView = findViewById(R.id.TextView);
                //tv.setText(outputFeature0[0].toString()+"\n"+outputFeature0[1].toString()),outputFeature0[2].toString();

                //Releases model resources if no longer used.
                model.close();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    //Mapped model on Asset
    private MappedByteBuffer loadModelFile() throws IOException {
            AssetFileDescriptor fileDescriptor = this.getAssets().openFd("patech (2).tflite");
            FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
            FileChannel fileChannel = inputStream.getChannel();
            long startOffset = fileDescriptor.getStartOffset();
            long declaredLength = fileDescriptor.getDeclaredLength();
            return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
        }
    }

