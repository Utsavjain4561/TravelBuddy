package android.com.avishkar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

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
import java.util.List;
import java.util.Map;

/**
 * Created by UTSAV JAIN on 8/30/2018.
 */

public class MyMapLocation extends FragmentActivity implements OnMapReadyCallback {
    private static final int  PLACE_PICKER_REQUEST = 1;
    public Marker marker;
    public GoogleMap map;
    double lat,lng;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_map_location);





        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                LatLng query = place.getLatLng();
                lat = query.latitude;
                lng = query.longitude;
                LatLng latLng=new LatLng(lat,lng);
                drawMarker(latLng);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,16.0f));
                LatLng latLng1=new LatLng(25.431160,81.829420);
                drawMarker(latLng1);
                String url=
                        "http://maps.googleapis.com/maps/api/directions/json?origin="
                                + lat + "," + lng +"&destination="
                                + 25.431160 + "," + 81.829420 + "&sensor=false";
                makeLoacation(url);


            }

        }
    }
    public  void drawMarker(LatLng point){
        // Creating an instance of MarkerOptions

        MarkerOptions markerOptions = new MarkerOptions();

        // Setting latitude and longitude for the marker
        markerOptions.position(point);

        // Adding marker on the Google Map
        marker = map.addMarker(markerOptions);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(point,16.0f));;



    }

    public JSONObject makeLoacation(String url){
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
        System.out.println(returnValue);
        try {
            result = new JSONObject(returnValue);
            makePolyLine(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
    public void makePolyLine(JSONObject result){
        try{
            JSONArray routes = result.getJSONArray("routes");
            long distanceForSegment = routes.getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONObject("distance").getInt("value");
            JSONArray steps = routes.getJSONObject(0).getJSONArray("legs")
                    .getJSONObject(0).getJSONArray("steps");
            List<LatLng> lines = new ArrayList<LatLng>();
            for(int i=0; i < steps.length(); i++) {
                String polyline = steps.getJSONObject(i).getJSONObject("polyline").getString("points");

                for(LatLng p : decodePolyline(polyline)) {
                    lines.add(p);
                }
            }
            map.addPolyline(new PolylineOptions().addAll(lines).width(3).color(Color.RED));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private List<LatLng> decodePolyline(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();

        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(p);
        }

        return poly;
    }
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
        map = googleMap;
    }
}

