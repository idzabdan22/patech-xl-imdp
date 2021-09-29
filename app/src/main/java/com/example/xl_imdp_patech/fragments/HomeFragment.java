package com.example.xl_imdp_patech.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.xl_imdp_patech.R;
import com.example.xl_imdp_patech.model.ArduinoDataModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class HomeFragment extends Fragment {

    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    TextView tempTv, humTV, raindurTV, lastupdateTV, statusTV;

    private final String FIREBASE_URL = "https://patech-xl-imdp-default-rtdb.asia-southeast1.firebasedatabase.app/";


    public HomeFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        tempTv = v.findViewById(R.id.temperatureRecent);
        raindurTV = v.findViewById(R.id.raindurRecent);
        humTV = v.findViewById(R.id.humidityRecent);
        lastupdateTV = v.findViewById(R.id.lastUpdateTime);
        statusTV = v.findViewById(R.id.statusPatech);

        firebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_URL);
        databaseReference = firebaseDatabase.getReference("data");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArduinoDataModel arduinoDataModel = snapshot.getValue(ArduinoDataModel.class);
                assert arduinoDataModel != null;
                Float currTemp = arduinoDataModel.getTemp();
                Float currRainDur = arduinoDataModel.getRain_dur();
                Integer currHumidity = arduinoDataModel.getHumidity();
                Integer currRainCond = arduinoDataModel.getRain_condition();
                setData(currTemp,currRainDur,currHumidity,currRainCond);
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });
        return v;
    }

    private void initFirebaseDatabase(String firebaseUrl){
    }

    @SuppressLint("SetTextI18n")
    private void setData(Float temp, Float rain_dur, Integer hum, Integer rain_cond){
        tempTv.setText(temp.toString());
        raindurTV.setText(rain_dur.toString());
        humTV.setText(hum.toString());
    }
}