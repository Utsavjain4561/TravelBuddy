package android.com.avishkar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    int PLACE_PICKER_REQUEST = 1;
    double lat, lng;
    LocationManager locationManager;
    LatLng la;
    //  Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                2000,
                1, locationListenerGPS);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 1, locationListenerGPS);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
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
    LocationListener locationListenerGPS= new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Toast.makeText(MapsActivity.this,"onlocation",Toast.LENGTH_LONG).show();
             la= new LatLng(location.getLatitude(),location.getLongitude());
            drawMarker(la);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            Toast.makeText(MapsActivity.this,"onstatus",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProviderEnabled(String s) {
            Toast.makeText(MapsActivity.this,"enabled",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProviderDisabled(String s) {
            Toast.makeText(MapsActivity.this,"disabled",Toast.LENGTH_LONG).show();
        }
    };
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
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
                LatLng latLng = new LatLng(lat, lng);
                drawMarker(latLng);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));
                String url=
                        "http://maps.googleapis.com/maps/api/directions/json?origin="
                                + lat + "," + lng +"&destination="
                                + la.latitude + "," + la.longitude + "&sensor=false";

                HttpResponse response = null;
                HttpGet request;
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
   //             System.out.println(returnValue);
                try {
                    JSONObject result = new JSONObject(returnValue);
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
                     mMap.addPolyline(new PolylineOptions().addAll(lines).width(15).color(Color.BLUE));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

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

    public  void drawMarker(LatLng point){
        // Creating an instance of MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions();

        // Setting latitude and longitude for the marker
        markerOptions.position(point);

        // Adding marker on the Google Map
        mMap.addMarker(markerOptions);


    }
}
