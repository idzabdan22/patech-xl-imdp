package com.example.xl_imdp_patech.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.xl_imdp_patech.R;
import com.example.xl_imdp_patech.main.Home;
import com.example.xl_imdp_patech.model.main.ArduinoDataModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

//import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class HomeFragment extends Fragment implements View.OnClickListener {
    Fragment fragmentRTLC = new RTLineChart();
    FrameLayout frameLayout;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    TextView tempTv, humTV, raindurTV, lastupdateTV, statusTV, posisiAlatTV, tempUT, humUT, rainUT, statusUT;
    ProgressBar progressBar;
    LinearLayout utara, selatan, timur, barat;
    ImageView cancelDialog;
    Dialog dialog;

    private final String FIREBASE_URL = "https://patech-xl-imdp-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        dialog = new Dialog(getContext());

        frameLayout = v.findViewById(R.id.frame_layout_home_frag);

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_home_frag, fragmentRTLC);
        fragmentTransaction.commit();

        utara = v.findViewById(R.id.sensor_utara);
        selatan = v.findViewById(R.id.sensor_selatan);
        timur = v.findViewById(R.id.sensor_timur);
        barat = v.findViewById(R.id.sensor_barat);

        rainUT = v.findViewById(R.id.rain_utara);
        tempUT = v.findViewById(R.id.temp_utara);
        humUT = v.findViewById(R.id.hum_utara);
        statusUT = v.findViewById(R.id.status_utara);

        posisiAlatTV = v.findViewById(R.id.posisi_alat);
        tempTv = v.findViewById(R.id.temperatureRecent);
        raindurTV = v.findViewById(R.id.raindurRecent);
        humTV = v.findViewById(R.id.humidityRecent);
        lastupdateTV = v.findViewById(R.id.lastUpdateTime);
        statusTV = v.findViewById(R.id.statusPatech);

        utara.setOnClickListener(this);
        selatan.setOnClickListener(this);
        timur.setOnClickListener(this);
        barat.setOnClickListener(this);

        firebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_URL);
        databaseReference = firebaseDatabase.getReference("data");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 1;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(i == snapshot.getChildrenCount()){
                        ArduinoDataModel arduinoDataModel = dataSnapshot.getValue(ArduinoDataModel.class);
                        assert arduinoDataModel != null;
                        Float currTemp = arduinoDataModel.getTemp();
                        Float currRainDur = arduinoDataModel.getRain_dur();
                        Float currHumidity = arduinoDataModel.getHumidity();
                        String last_update = arduinoDataModel.getLast_update();
                        setData(currTemp, currRainDur, currHumidity, last_update);
                    }
                    i++;
                }
            }
            @Override
            public void onCancelled(@NonNull  DatabaseError error) {
                Log.d("TAG", error.getMessage());
            }
        });
        return v;
    }


    @SuppressLint("SetTextI18n")
    private void setData(Float temp, Float rain_dur, Float hum, String last_update){
        tempTv.setText(temp.toString());
        raindurTV.setText(rain_dur.toString());
        humTV.setText(hum.toString());
        tempUT.setText(temp.toString());
        rainUT.setText(rain_dur.toString());
        humUT.setText(hum.toString());
        lastupdateTV.setText("Update Terakhir: " + last_update);
    }

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sensor_utara:
                posisiAlatTV.setText("UTARA");
                posisiAlatTV.setText("UTARA");
                break;
            case R.id.sensor_selatan:
                posisiAlatTV.setText("SELATAN");
                posisiAlatTV.setText("SELATAN");
                break;
            case R.id.sensor_timur:
                posisiAlatTV.setText("TIMUR");
                posisiAlatTV.setText("TIMUR");
                break;
            case R.id.sensor_barat:
                posisiAlatTV.setText("BARAT");
                posisiAlatTV.setText("BARAT");
                break;
        }
    }
}