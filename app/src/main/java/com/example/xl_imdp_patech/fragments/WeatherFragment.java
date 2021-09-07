package com.example.xl_imdp_patech.fragments;

import android.annotation.SuppressLint;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.xl_imdp_patech.R;
import com.example.xl_imdp_patech.adapter.WeatherAdapter;
import com.example.xl_imdp_patech.main.Home;
import com.example.xl_imdp_patech.model.WeatherModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    WeatherAdapter weatherAdapter;
    public ArrayList<WeatherModel> weatherModel;
    int x;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherModel = new ArrayList<>();
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_weather, container, false);

        Bundle bundle = getArguments();
        assert bundle != null;
        String citynames = bundle.getString("city");

        TextView tvCityProvince = v.findViewById(R.id.cityprovince_text);
        TextView tvTempDay = v.findViewById(R.id.suhu_today);
        TextView tvSkyCondition = v.findViewById(R.id.kondisi_awan);

        String API_KEY = "43372368b1f347cba9f115910210109";
        String BASE_URL = "http://api.weatherapi.com/v1/forecast.json?key=" + API_KEY + "&q=" + citynames + "&days=1&aqi=no&alerts=no";

        RecyclerView recyclerView = v.findViewById(R.id.forecast_recyclerview);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, BASE_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                weatherModel.clear();
                try {
                    String region = response.getJSONObject("location").getString("region");
                    Double temp = response.getJSONObject("current").getDouble("temp_c");
                    String skyCondition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                    Integer code = response.getJSONObject("current").getJSONObject("condition").getInt("code");
                    Integer humidity = response.getJSONObject("current").getInt("humidity");

                    String cityregion = "Cuaca Hari Ini\n" + citynames + ", " + region + ".";
                    tvCityProvince.setText(cityregion);
                    tvTempDay.setText(temp.toString());
                    tvSkyCondition.setText(skyCondition);

                    JSONObject forecast = response.getJSONObject("forecast");
                    JSONObject forecastday = forecast.getJSONArray("forecastday").getJSONObject(0);
                    JSONArray hourForecast = forecastday.getJSONArray("hour");
                    x = hourForecast.length();
                    for (int i = 0; i < hourForecast.length(); i++) {
                        JSONObject hourItem = hourForecast.getJSONObject(i);
                        double temphour = hourItem.getDouble("temp_c");
                        String time = hourItem.getString("time");
                        Integer hum = hourItem.getInt("humidity");
                        String skyHourCondition = hourItem.getJSONObject("condition").getString("text");
                        WeatherModel model = new WeatherModel(time, skyCondition, hum, (float) temphour);
                        weatherModel.add(model);
                    }
                    recyclerView.setAdapter(new WeatherAdapter(weatherModel));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(request);
        return v;
    }

    public void getWeatherInfo(String cityName, ArrayList<WeatherModel> weatherModels) {
        String API_KEY = "43372368b1f347cba9f115910210109";
        String BASE_URL = "http://api.weatherapi.com/v1/forecast.json?key=" + API_KEY + "&q=" + cityName + "&days=1&aqi=no&alerts=no";

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, BASE_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String region = response.getJSONObject("location").getString("region");
                    double temp = response.getJSONObject("current").getDouble("temp_c");
                    String skyCondition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                    Integer code = response.getJSONObject("current").getJSONObject("condition").getInt("code");
                    Integer humidity = response.getJSONObject("current").getInt("humidity");

                    JSONObject forecast = response.getJSONObject("forecast");
                    JSONObject forecastday = forecast.getJSONArray("forecastday").getJSONObject(0);
                    JSONArray hourForecast = forecastday.getJSONArray("hour");

                    for (int i = 0; i < hourForecast.length(); i++) {
                        JSONObject hourItem = hourForecast.getJSONObject(i);
                        double temphour = hourItem.getDouble("temp_c");
                        String time = hourItem.getString("time");
                        Integer hum = hourItem.getInt("humidity");
//                        Log.d("TAG", time);
                        String skyHourCondition = hourItem.getJSONObject("condition").getString("text");
                        weatherModels.add(new WeatherModel(time, skyHourCondition, hum, (float) temphour));
                    }
//                    weatherAdapter.notifyDataSetChanged();
                    for (int i = 0; i < hourForecast.length(); i++) {
                        String model = weatherModels.get(i).getHour() + weatherModels.get(i).getSkyCondition() + weatherModels.get(i).getTemp() + weatherModels.get(i).getHumidity();
                        Log.d("TAG", model);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);
    }
}