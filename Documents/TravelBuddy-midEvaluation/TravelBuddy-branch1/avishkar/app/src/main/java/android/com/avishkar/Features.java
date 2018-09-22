package android.com.avishkar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class Features extends AppCompatActivity {
    public double latitude;
    MyReceiver myReceiver;
    public double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_features);
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WeatherService.MY_ACTION);
        registerReceiver(myReceiver, intentFilter);



        Intent returnIntent = getIntent();
        int returnFeatures = returnIntent.getExtras().getInt("feature");
        if(returnFeatures ==0){
            latitude = returnIntent.getExtras().getDouble("latitude");
            longitude = returnIntent.getExtras().getDouble("longitude");
            Toast.makeText(this,"ServiceStarted",Toast.LENGTH_LONG).show();
            Intent weatherServiceIntent = new Intent(this,WeatherService.class);
            weatherServiceIntent.putExtra("latitude",latitude);
            weatherServiceIntent.putExtra("longitude",longitude);
            startService(weatherServiceIntent);

        }
        else{

        }
    }

    private class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context arg0, Intent arg1) {

            String description = arg1.getExtras().getString("description");
            String temperature = arg1.getExtras().getString("temperature");
            String humidity = arg1.getExtras().getString("humidity");
            String w_speed = arg1.getExtras().getString("w_speed");
            String country = arg1.getExtras().getString("country");
            String city = arg1.getExtras().getString("city");
            Log.e("Feature",description);



        }

    }
}
