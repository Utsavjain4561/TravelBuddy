package android.com.avishkar;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MapInfo extends Fragment {
    JSONObject weather;
    public String city;
    public String cityAdd;
    public double latitude;
    public double longitude;
    public TextView cityView;
    public TextView addView;
    public TextView latView;
    public TextView lonView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.map_info,container,false);
        Tours tour = ((Home)getActivity()).getTours();
        city = tour.city;
        cityAdd = tour.address;
        latitude = tour.latitude;
        longitude = tour.longitude;
        cityView  = view.findViewById(R.id.place);
        addView  = view.findViewById(R.id.address);
        latView  = view.findViewById(R.id.lat);
        lonView  = view.findViewById(R.id.lon);
        return view;
    }

/*  @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        city = "Allahabad";
//        try {
//            pareseWeather(weather);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        Weather placeWeather = new Weather(city,type,description,temperature,humidity,wind);
//        Toast.makeText(getContext(),placeWeather.description,Toast.LENGTH_SHORT).show();
    }

    public void pareseWeather(JSONObject weather) throws JSONException {

        type = weather.getJSONArray("weather").getJSONObject(0).getString("main");
        description = weather.getJSONArray("weather").getJSONObject(0).getString("description");
        temperature = weather.getJSONObject("main").getDouble("temp");
        humidity = weather.getJSONObject("main").getDouble("humidity");
        wind = weather.getJSONObject("wind").getDouble("speed");
    }
    public void getWeather(final String city) {

        new AsyncTask<Void, Void, Void>() {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q="+city+"&APPID=ea574594b9d36ab688642d5fbeab847e");

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    BufferedReader reader =
                            new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    StringBuffer json = new StringBuffer(1024);
                    String tmp = "";

                    while((tmp = reader.readLine()) != null)
                        json.append(tmp).append("\n");
                    reader.close();

                    weather = new JSONObject(json.toString());

                    if(weather.getInt("cod") != 200) {
                        System.out.println("Cancelled");
                        return null;
                    }


                } catch (Exception e) {

                    System.out.println("Exception "+ e.getMessage());
                    return null;
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void Void) {
                if(weather!=null){
                    Log.d("my weather received",weather.toString());
                }

            }
        }.execute();

    }*/
}
