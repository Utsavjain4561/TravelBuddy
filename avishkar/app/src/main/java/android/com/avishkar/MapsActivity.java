package android.com.avishkar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements Serializable,OnMapReadyCallback,LocationListener,GoogleMap.OnMarkerClickListener {

    private transient GoogleMap mMap;
    public Marker marker;
    ListView lv;
    LocationManager locationManager;
    double latitude,longitude;
    String city;
    int j;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this,"xyz",Toast.LENGTH_LONG).show();
            String[] permission =new String[1];
            permission[0]=Manifest.permission.ACCESS_FINE_LOCATION;

            ActivityCompat.requestPermissions(MapsActivity.this,permission,1);
            return;
        }
        else{



        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                2000,
                1, locationListenerGPS);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 1,
                locationListenerGPS);
        lv = (ListView) findViewById(R.id.lv);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().addToBackStack(null);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }
    LocationListener locationListenerGPS = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            mMap.clear();
            drawMarker(new LatLng(latitude,longitude));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude),16.0f));
            Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
            try {
                List<Address>adress=geocoder.getFromLocation(latitude,longitude,1);
                city=adress.get(0).getLocality().toLowerCase();
              //  Toast.makeText(MapsActivity.this,city,Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
         //   Log.e("city",city);



            String url=
                    "https://maps.googleapis.com/maps/api/place/textsearch/json?query="+city+"+Tourist&language=en&key=AIzaSyCLAkq9FBr_0tfE4HvRGpe_g7I5i8rXYTU";
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
                JSONArray tours = result.getJSONArray("results");
                ArrayList<Tours> list = new ArrayList<>();
                for (int i=0;i<tours.length();i++)
                {
                    JSONArray place = tours.getJSONObject(i).getJSONArray("types");
                   // System.out.println(place.length());
                    for ( j=0;j<place.length();j++)
                    {
                        if (place.getString(j).equals("museum") || place.getString(j).equals("establishment"))
                        {
                            break;
                        }
                    }
                    if (j!=place.length()&&Double.parseDouble(tours.getJSONObject(i).getString("rating"))>=2.5){
                        String placeid= tours.getJSONObject(i).getString("place_id");
                        String address=tours.getJSONObject(i).getString("formatted_address");
                        String link=tours.getJSONObject(i).getString("icon");
                        String name=tours.getJSONObject(i).getString("name");
                        double rating = Double.parseDouble(tours.getJSONObject(i).getString("rating"));
                        String types="Tourism";
                        Tours tours1 =new Tours(placeid,address,link,name,rating,types);
                        list.add(tours1);}
                    if (list.size()==3)
                        break;
                }
                ArrayList<HashMap<String,String>> arrayList= new ArrayList<>();
                for (int i=0;i<list.size();i++)
                {
                    HashMap<String, String> hm = new HashMap<>();
                    hm.put("name",list.get(i).address);
                    arrayList.add(hm);
                }
                String[]  s={"name"};
                int [] t={R.id.tv};
                if (!list.isEmpty())
                {
                    SimpleAdapter simpleAdapter = new SimpleAdapter(MapsActivity.this,arrayList,R.layout.activity_layout,s,t);
                    lv.setAdapter(simpleAdapter);
                }
                else
                    Toast.makeText(MapsActivity.this,"nahi",Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };
    private String buildStringIOutils(InputStream is) {
        try {
            return IOUtils.toString(is, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
    }


    public  void drawMarker(LatLng point){
        // Creating an instance of MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions();

        // Setting latitude and longitude for the marker
        markerOptions.position(point);

        // Adding marker on the Google Map
        marker = mMap.addMarker(markerOptions);


    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude= location.getLongitude();
        LatLng latLng = new LatLng(latitude,longitude);
        drawMarker(latLng);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

            Intent placePickerIntent = new Intent(MapsActivity.this, MyMapLocation.class);
            startActivity(placePickerIntent);
            return false;


    }
}
