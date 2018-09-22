package android.com.avishkar;

import android.app.Service;
import android.content.Intent;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

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
        latitude = intent.getExtras().getDouble("latitude");
        longitude = intent.getExtras().getDouble("longitude");
        Log.e("latitude", latitude + "");

        String url = "http://api.openweathermap.org/data/2.5/weather?lat="+latitude+"&lon="+longitude+"&appid=eac6fa72decf8f315716dffff09ec354" +
                "&units=metric";
        Log.e("Url",url);
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
            Log.e("Description",description+" "+temperature+" "+humidity+" "+w_speed+" "+country+" "+city);
            Intent sendIntent = new Intent(MY_ACTION);
            sendIntent.putExtra("description",description);
            sendIntent.putExtra("temperature",temperature);
            sendIntent.putExtra("humidity",humidity);
            sendIntent.putExtra("w_speed",w_speed);
            sendIntent.putExtra("country",country);
            sendIntent.putExtra("city",city);
            sendBroadcast(sendIntent);

        } catch (JSONException e) {
            e.printStackTrace();
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

}
