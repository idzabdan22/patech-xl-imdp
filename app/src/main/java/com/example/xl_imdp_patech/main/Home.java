package com.example.xl_imdp_patech.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.xl_imdp_patech.fragments.HomeFragment;
import com.example.xl_imdp_patech.fragments.ProfileFragment;
import com.example.xl_imdp_patech.fragments.WeatherFragment;
import com.example.xl_imdp_patech.R;
import com.example.xl_imdp_patech.model.main.ArduinoDataModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Home extends AppCompatActivity {
    BottomNavigationView btmNavId;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    ImageView cancelButton;
    HomeFragment homeFragment = new HomeFragment();
    WeatherFragment weatherFragment = new WeatherFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    FrameLayout frameLayout;
    Dialog dialog;
    private final int PERMISSION_CODE = 1;
    private final String FIREBASE_URL = "https://patech-xl-imdp-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        cancelButton = findViewById(R.id.cancel_dialog);

        //Set Screen Fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        firebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_URL);
        databaseReference = firebaseDatabase.getReference("data");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 1;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(i == dataSnapshot.getChildrenCount()){
                        ArduinoDataModel arduinoDataModel = dataSnapshot.getValue(ArduinoDataModel.class);
                        assert arduinoDataModel != null;
                        Float currTemp = arduinoDataModel.getTemp();
                        Float currRainDur = arduinoDataModel.getRain_dur();
                        Integer currHumidity = arduinoDataModel.getHumidity();
                        Integer currRainCond = arduinoDataModel.getRain_condition();
                        addNotification();

                    }
                    i++;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG", error.getMessage());
            }
        });


        //notification manager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("1", "1", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }


        //Initialize Location by Location Manager
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Home.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
        }

        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        String cityName = getCityName(location.getLongitude(), location.getLatitude());
        Bundle bundle = new Bundle();
        String latlong = location.getLatitude() + "," + location.getLongitude();
        bundle.putString("city", cityName);
        bundle.putString("latlong", latlong);
        weatherFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, homeFragment);
        fragmentTransaction.commit();

        dialog = new Dialog(Home.this);

        btmNavId = findViewById(R.id.bot_nav);
        frameLayout = findViewById(R.id.frame_layout);


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
//                    case R.id.userButton:
//                        fragmentTransaction.replace(R.id.frame_layout, new ProfileFragment());
//                        fragmentTransaction.commit();
//                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
    }

    public String getCityName(double longitude, double latitude){
        String cityName = "Not Found";
        Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude,longitude, 10);
            for (Address address: addresses){
                if(address != null){
                    String city = address.getLocality();
                    if(city!=null && !city.equals("")){
                        cityName = city;
                        Toast.makeText(Home.this, "User City Found!", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        }catch (IOException e){
                e.printStackTrace();
        }
        return cityName;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_CODE){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted...", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Please provide the permission", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    };

    private void addNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(Home.this, "1");
                        builder.setSmallIcon(R.drawable.logo_patech);
                        builder.setContentTitle("WASPADA ANTRAKNOSA");
                        builder.setContentText("Segera cek tanaman cabai Anda");
                        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
                        builder.setAutoCancel(true);

//
//        Intent notificationIntent = new Intent(this, MainActivity.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(contentIntent);

        // Add as notification
        // Add as notification
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(Home.this);
        notificationManagerCompat.notify(1, builder.build());
    }

    private void showDialog(){
        dialog.setContentView(R.layout.dialog_layout);
        dialog.getWindow().setBackgroundDrawable(null);
        dialog.getWindow().setDimAmount(0.0f);
        dialog.show();
    }


}