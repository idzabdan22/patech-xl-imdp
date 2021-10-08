package com.example.xl_imdp_patech.main;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.xl_imdp_patech.fragments.DialogFragment;
import com.example.xl_imdp_patech.fragments.HomeFragment;
import com.example.xl_imdp_patech.fragments.WeatherFragment;
import com.example.xl_imdp_patech.R;
//import com.example.xl_imdp_patech.ml.PatechModel;
import com.example.xl_imdp_patech.ml.PatechModel;
import com.example.xl_imdp_patech.model.main.ArduinoDataModel;
import com.example.xl_imdp_patech.service.NotifServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//import org.tensorflow.lite.DataType;
//import org.tensorflow.lite.Interpreter;
//import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Locale;

public class Home extends AppCompatActivity {
    BottomNavigationView btmNavId;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    HomeFragment homeFragment = new HomeFragment();
    WeatherFragment weatherFragment = new WeatherFragment();
    FrameLayout frameLayout;
    Interpreter tflite;
    private final int PERMISSION_CODE = 1;
    private final String FIREBASE_URL = "https://patech-xl-imdp-default-rtdb.asia-southeast1.firebasedatabase.app/";


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Set Screen Fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Home.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
        }

        //Send location to Home Fragment
        //Initialize Location by Location Manager
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        String cityName = getCityName(location.getLongitude(), location.getLatitude());
        Bundle bundle = new Bundle();
        String latlong = location.getLatitude() + "," + location.getLongitude();
        bundle.putString("city", cityName);
        bundle.putString("latlong", latlong);
        weatherFragment.setArguments(bundle);

        //init Home Fragment when home is on
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, homeFragment);
        fragmentTransaction.commit();

        //Widget initialize
        btmNavId = findViewById(R.id.bot_nav);
        frameLayout = findViewById(R.id.frame_layout);

//        processForNotification("mod");
        showDialog();

        //fragment select
        btmNavId.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.homeButton:
                        fragmentTransaction.replace(R.id.frame_layout, homeFragment);
                        fragmentTransaction.commit();
                        break;
                    case R.id.weatherButton:
                        fragmentTransaction.replace(R.id.frame_layout, weatherFragment, "WF");
                        fragmentTransaction.commit();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });


        firebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_URL);
        databaseReference = firebaseDatabase.getReference("data");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 1;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (i == snapshot.getChildrenCount()) {
                        ArduinoDataModel arduinoDataModel = dataSnapshot.getValue(ArduinoDataModel.class);
                        assert arduinoDataModel != null;
                        Float currTemp = arduinoDataModel.getTemp();
                        Float currRainDur = arduinoDataModel.getRain_dur();
                        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(2*4);
                        byteBuffer.asFloatBuffer();
                        byteBuffer.putFloat(44.8f);
                        byteBuffer.putFloat(6.3f);

                        Log.d("indeks" , String.valueOf(byteBuffer.get(1)));
//
//                        byteBuffer.putFloat(currTemp);
//                        byteBuffer.putFloat(currRainDur);
//                        byteBuffer.asReadOnlyBuffer();


                        try {
                            PatechModel model = PatechModel.newInstance(Home.this);

                            // Creates inputs for reference.
                            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 2}, DataType.FLOAT32);
                            inputFeature0.loadBuffer(byteBuffer);
                            Log.d("length", String.valueOf(inputFeature0.getBuffer()));


//                            try{
//                                MappedByteBuffer tfliteModel = FileUtil.loadMappedFile(Home.this, "patechModel.tflite");
//                                Interpreter tflite = new Interpreter(tfliteModel);
//                            } catch (IOException e){
//                                Log.e("tfliteSupport", "Error reading model", e);
//                            }
//
//                            if(null != tflite) {
//                                tflite.run(byteBuffer, .getBuffer());
//
//                            }


//                            inputFeature0.getBuffer();
//
//                            // Runs model inference and gets result.
                            PatechModel.Outputs outputs = model.process(inputFeature0);
                            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

//                            outputFeature0.getFloatValue(i);

                            Log.d("length", String.valueOf(outputFeature0.getFloatValue(1)));
//
//                            for (float v : out) {
//                                Log.d("indeks", String.valueOf(v));
//                            }

                            // Releases model resources if no longer used.
//                            model.close();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    i++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG", error.getMessage());
            }
        });


        }

        private void processData(){

        }
//
////        @RequiresApi(api = Build.VERSION_CODES.O)
//        private void processForNotification(String status){
//            if(status.equals("mod") || status.equals("high") || status.equals("low")){
//                Intent intent = new Intent(this, NotifServices.class);
//                Log.d("test", "ON");
////                startService(intent);
//            }
//        }

        public String getCityName ( double longitude, double latitude){
            String cityName = "Not Found";
            Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 10);
                for (Address address : addresses) {
                    if (address != null) {
                        String city = address.getLocality();
                        if (city != null && !city.equals("")) {
                            cityName = city;
                            Toast.makeText(Home.this, "Lokasi ditemukan!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return cityName;
        }


        @Override
        public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults){
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (requestCode == PERMISSION_CODE) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Please provide the permission", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }


        private void showDialog () {
            DialogFragment dialog = new DialogFragment();
            dialog.show(getFragmentManager(), "DialogFragment");
        }

        private MappedByteBuffer loadModelFile() throws IOException {
            AssetFileDescriptor fileDescriptor = this.getAssets().openFd("patechModel.tflite");
            FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
            FileChannel fileChannel = inputStream.getChannel();
            long startOffset = fileDescriptor.getStartOffset();
            long declaredLength = fileDescriptor.getDeclaredLength();
            return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
        }

}
