package android.com.avishkar;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by lokesh on 22/9/18.
 */

public class WeatherService extends Service {
    private double latitude;
    private double longitude;
    final static String MY_ACTION= "My_Action";

    @Override
    public void onCreate() {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("Weather", "Service");
        int type = intent.getExtras().getInt("type");
        switch(type)
        {
            case 0:
                    parseWeather(intent);
                    break;
            case 1:
                    parseTravel(intent);
                    break;
    }
        return START_STICKY;

    }
    public static String buildStringIOutils(InputStream is){
        try {
            return IOUtils.toString(is,"UTF-8");
        }
        catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }
    public void parseTravel(Intent intent){
        ArrayList<Travel> travels = new ArrayList<>();
        String city = intent.getExtras().getString("city");
        Log.e("ServiceCity",city);
        String place = intent.getExtras().getString("place");
        String museumurl = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=museum%20"+city+
                "&inputtype=textquery&&fields=formatted_address,name,rating,opening_hours&key=AIzaSyCLAkq9FBr_0tfE4HvRGpe_g7I5i8rXYTU";
        HttpResponse mus_response = null;
        HttpGet mus_request;
        JSONObject mus_result = null;
        DefaultHttpClient mus_client = new DefaultHttpClient();
        mus_request = new HttpGet(museumurl);
        try {
            mus_response = mus_client.execute(mus_request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream mus_source = null;
        try {
            mus_source = mus_response.getEntity().getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String mus_returnValue = buildStringIOutils(mus_source);
        try {
            mus_result = new JSONObject(mus_returnValue);
            JSONArray cand = mus_result.getJSONArray("candidates");
            String form_add,p_name,rating,open;
            for (int i =0 ;i<cand.length();i++)
            {
                JSONObject object = cand.getJSONObject(i);
                form_add=object.getString("formatted_address");
                p_name= object.getString("name");
                rating = String.valueOf(object.getDouble("rating"));
                if(object.length()<=3)
                    open="Not Found";
                else
                    open= String.valueOf(object.getJSONObject("opening_hours").getBoolean("open_now"));
                travels.add(new Travel(form_add,p_name,rating,open));
                Log.e("answer",form_add+" "+p_name+" "+rating+" "+open);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent inte = new Intent(MY_ACTION);
        Bundle bundle = new Bundle();
        bundle.putSerializable("list",travels);
        inte.putExtra("type",1);
        inte.putExtra("bundle",bundle);
        sendBroadcast(inte);
    }
    public void parseWeather(Intent intent){
        latitude = intent.getExtras().getDouble("latitude");
        longitude = intent.getExtras().getDouble("longitude");
        Log.e("latitude", latitude + "");

        String url =  "http://api.openweathermap.org/data/2.5/weather?&lat="+String.valueOf(latitude) + "&lon=" + String.valueOf(longitude) +
                "&appid=eac6fa72decf8f315716dffff09ec354&units=metric";
        Log.e("Url", url);
        HttpResponse response = null;
        HttpGet request;
        JSONObject result = null;
        DefaultHttpClient client = new DefaultHttpClient();
        request = new HttpGet(url);
        try {
            response = client.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream source = null;
        try {
            source = response.getEntity().getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String returnValue = buildStringIOutils(source);
        try {
            result = new JSONObject(returnValue);
            String description = result.getJSONArray("weather").getJSONObject(0).getString("description");
            String temperature = result.getJSONObject("main").getString("temp");
            String humidity = result.getJSONObject("main").getString("humidity");
            String w_speed = result.getJSONObject("wind").getString("speed");
            String country = result.getJSONObject("sys").getString("country");
            String city = result.getString("name");
            Log.e("Description", description + " " + temperature + " " + humidity + " " + w_speed + " " + country + " " + city);
            Intent sendIntent = new Intent(MY_ACTION);
            sendIntent.putExtra("description", description);
            sendIntent.putExtra("temperature", temperature);
            sendIntent.putExtra("humidity", humidity);
            sendIntent.putExtra("w_speed", w_speed);
            sendIntent.putExtra("country", country);
            sendIntent.putExtra("city", city);
            sendIntent.putExtra("type",0);
            sendBroadcast(sendIntent);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
