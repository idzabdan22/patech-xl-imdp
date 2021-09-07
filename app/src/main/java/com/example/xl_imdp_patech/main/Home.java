package com.example.xl_imdp_patech.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.xl_imdp_patech.fragments.HomeFragment;
import com.example.xl_imdp_patech.fragments.ProfileFragment;
import com.example.xl_imdp_patech.fragments.WeatherFragment;
import com.example.xl_imdp_patech.R;
import com.example.xl_imdp_patech.api.ApiServiceCurrent;
import com.example.xl_imdp_patech.model.Current;
import com.example.xl_imdp_patech.model.HourForecast;
import com.example.xl_imdp_patech.model.LocationWeather;
import com.example.xl_imdp_patech.model.WeatherApiModels;
import com.example.xl_imdp_patech.model.WeatherModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Home extends AppCompatActivity {
    BottomNavigationView btmNavId;
    HomeFragment homeFragment = new HomeFragment();
    WeatherFragment weatherFragment = new WeatherFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    FrameLayout frameLayout;
    LocationManager locationManager;
    private final int PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Set Screen Fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Initialize Location by Location Manager
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(Home.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
        }

        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        String cityName = getCityName(location.getLongitude(), location.getLatitude());
        Bundle bundle = new Bundle();
        bundle.putString("city", cityName);
        weatherFragment.setArguments(bundle);

        btmNavId = findViewById(R.id.bot_nav);
        frameLayout = findViewById(R.id.frame_layout);



        btmNavId.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                switch (item.getItemId()){
                    case R.id.homeButton:
                        fragmentTransaction.replace(R.id.frame_layout, homeFragment);
                        fragmentTransaction.commit();
                        break;
                    case R.id.weatherButton:
                        fragmentTransaction.replace(R.id.frame_layout, weatherFragment);
                        fragmentTransaction.commit();
                        break;
                    case R.id.userButton:
                        fragmentTransaction.replace(R.id.frame_layout, new ProfileFragment());
                        fragmentTransaction.commit();
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
    }

    public void getFirebaseDatabase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://patech-xl-imdp-default-rtdb.asia-southeast1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("data");
        myRef.child("rain_dur").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()){
                }
                else{
                }
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



//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        ApiServiceCurrent service = retrofit.create(ApiServiceCurrent.class);
//        Call<WeatherApiModels> call = service.getCurrentWeather(API_KEY, cityName, 1, "no", "no");
//        call.enqueue(new Callback<WeatherApiModels>() {
//            @Override
//            public void onResponse(Call<WeatherApiModels> call, Response<WeatherApiModels> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<WeatherApiModels> call, Throwable t) {
//
//            }
//        });
////        call.enqueue(new Callback<WeatherApiModels>() {
////            @Override
////            public void onResponse(Call<WeatherApiModels> call, Response<WeatherApiModels> response) {
////                if (response.code() == 200){
////                    ArrayList<WeatherApiModels.Forecast.Forecastday> = new ArrayList<>(response.body().getForecasts().getForecastdays().)
////                    LocationWeather locationWeather = weatherApiModels.getLocation();
////                    Current current = weatherApiModels.getCurrent();
////                    String regionName = locationWeather.getRegionName();
////                    String lastUpdate = current.getLastUpdate();
////                    Float tempInCelcius = current.getTempCelcius();
////                    Integer humidity = current.getHumidity();
////                    String skyCondition = current.getCondition().getSkyCondition();
////                    Integer code = current.getCondition().getCode();
////                    bundleForFragments(cityName, regionName, lastUpdate, tempInCelcius, humidity, skyCondition, code);
////                }
////            }
////
////            @Override
////            public void onFailure(Call<WeatherApiModels> call, Throwable t) {
////
////            }
////        });
//     }

//     public void bundleForFragments(String cityName, String regionName, Float temp, Integer humidity, String skyCondition, Integer code){
//            Bundle bundle = new Bundle();
//            bundle.putString("region", regionName);
//            bundle.putFloat("temp", temp);
//            bundle.putInt("hum", humidity);
//            bundle.putString("sky", skyCondition);
//            bundle.putInt("code", code);
//            bundle.putString("city", cityName);
//            weatherFragment.setArguments(bundle);
//     }

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
}