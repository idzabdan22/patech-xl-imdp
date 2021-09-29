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
    private ByteBuffer byteBuffer = ByteBuffer.allocateDirect(2*4);

    @SerializedName("humidity")
    @Expose
    private Integer humidity;

    @SerializedName("rain_condition")
    @Expose
    private Integer rain_condition;

    @SerializedName("temp")
    @Expose
    private Float temp;

    public void ArduinoDataModel(Integer humidity, Integer rain_condition, Float temp, Float rain_dur) {
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

    public ByteBuffer getByteBuffer() {
        byteBuffer.putFloat(getTemp());
        byteBuffer.putFloat(getRain_dur());
        return byteBuffer;
    }
    //mapped model on Asset
        
        protected void onCreate (Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            try {
                Patech model = Patech.newInstance(this);
                tflite = new Interpreter(loadModelFile());
                TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 2}, DataType.FLOAT32);
                inputFeature0.loadBuffer(byteBuffer);
                // Runs model inference and gets result.
                Patech.Outputs outputs = model.process(inputFeature0);
                TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer().getFloatArray();
                // tv :TextView = findViewById(R.id.TextView);
                //tv.setText(outputFeature0[0].toString()+"\n"+outputFeature0[1].toString()),outputFeature0[2].toString();
                // Releases model resources if no longer used.
                model.close();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    private MappedByteBuffer loadModelFile() throws IOException {
            AssetFileDescriptor fileDescriptor = this.getAssets().openFd("patech (2).tflite");
            FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
            FileChannel fileChannel = inputStream.getChannel();
            long startOffset = fileDescriptor.getStartOffset();
            long declaredLength = fileDescriptor.getDeclaredLength();
            return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
        }
    }





    /*public float doInference(String inputString){
        //Input shape is [1]. Single valued input
        float[]inputVal=new float[1];
        inputVal[0]=Float.valueOf(inputString);
        inputVal[1]=Float.valueOf(inputString);
        //Output shape is [1][1]
        float[][] outputval=new float[1][1];

        //Run inference passing the input shape and getting the output shape
        tflite.run(inputVal, outputval);

        //Inferred value is at [0][0]
        float inferredValue=outputval[0][0];

        return inferredValue;
    }*/


  /*  private ByteBuffer loadModelFile() {
    }*/


 /*   public class new_class {
        private Context context;

        public new_class(Context context) {
            this.context = context;
        }
*/


  /*  protected void onCreate (Bundle savedInstanceState){
       // super.onCreate(savedInstanceState);
        try {
        Patech model = Patech.newInstance(loadModelFile());

        // Creates inputs for reference.
        TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 2}, DataType.FLOAT32);
        ByteBuffer byteBuffer;
        inputFeature0.loadBuffer(byteBuffer);

        // Runs model inference and gets result.
        Patech.Outputs outputs = model.process(inputFeature0);
        TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

        // Releases model resources if no longer used.
        model.close();
    }
    catch(
    IOException e) {

    }}}*/