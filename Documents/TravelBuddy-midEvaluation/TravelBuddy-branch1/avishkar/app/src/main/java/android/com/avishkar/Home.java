package android.com.avishkar;

import android.*;
import android.app.ActionBar;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mindorks.placeholderview.PlaceHolderView;

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

public class Home extends AppCompatActivity implements Serializable, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static transient GoogleMap mMap;
    public static Marker marker;
    static LocationManager locationManager;
    ActionBar actionBar;
    final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 3;
    private String email;
    static String city;
    static String lala;
    public static Tours tour;
    int id, j;
    boolean done = false;
    double latitude, longitude;
    public LatLng latLng;
    View fragment;
    public String name;
    //Navigation Drawer
    private PlaceHolderView mDrawerView;
    private DrawerLayout mDrawer;
    private Toolbar mToolbar;
    private PlaceHolderView mGalleryView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        email = getIntent().getExtras().getString("email");
        fragment = findViewById(R.id.map_in);
        id = getIntent().getExtras().getInt("id");

        mDrawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerView = (PlaceHolderView) findViewById(R.id.drawerView);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        Button searchView = mToolbar.findViewById(R.id.search_city);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(Home.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                }
            }
        });
        mGalleryView = (PlaceHolderView) findViewById(R.id.galleryView);
        setupDrawer();


        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(Home.this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Home.this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                name = place.getName().toString();
                latLng = place.getLatLng();
                int lat, lng;
                lat = (int) latLng.latitude;
                lng = (int) latLng.longitude;
                mMap.clear();
                drawMarker(latLng);
                TextView placeview = fragment.findViewById(R.id.place);
                placeview.setText(name);
                TextView address = fragment.findViewById(R.id.address);
                address.setText(place.getAddress());
                TextView latView = fragment.findViewById(R.id.lat);
                latView.setText("Latitude " + String.valueOf(lat) + "°N");
                TextView lonView = fragment.findViewById(R.id.lon);
                lonView.setText("Longitude " + String.valueOf(lng) + "°S");
                setupDrawer();
                Log.d("Name", name + "\n" + lat + "\n" + lng);
            }

        }
    }

    public void getdetails(LatLng latLng) {
        Toast.makeText(Home.this, "service", Toast.LENGTH_LONG).show();
        double latitude = latLng.latitude;
        double longitude = latLng.longitude;
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> adress = geocoder.getFromLocation(latitude, longitude, 1);
            city = adress.get(0).getLocality().toLowerCase();
            lala = "";
            lala = lala + adress.get(0).getAddressLine(0);
            System.out.print(city);
            Toast.makeText(Home.this, city + lala + latitude + longitude, Toast.LENGTH_LONG).show();
            tour = new Tours(city, lala, latitude, longitude);
//            TextView place = fragment.findViewById(R.id.place);
//            place.setText(city);
//            TextView address = fragment.findViewById(R.id.address);
//            address.setText(lala);
//            TextView lat = fragment.findViewById(R.id.lat);
//            lat.setText(String.valueOf(latitude));
//            TextView lon = fragment.findViewById(R.id.lon);
//            lon.setText(String.valueOf(longitude));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String url = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + city +
                "+Tourist&language=en&key=AIzaSyCLAkq9FBr_0tfE4HvRGpe_g7I5i8rXYTU";
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


            for (int i = 0; i < tours.length(); i++) {
                JSONArray place = tours.getJSONObject(i).getJSONArray("types");
                System.out.println(place.length());
                for (j = 0; j < place.length(); j++) {
                    if (place.getString(j).equals("museum") || place.getString(j).equals("establishment")) {
                        break;
                    }
                }
                if (j != place.length() && Double.parseDouble(tours.getJSONObject(i).getString("rating")) >= 2.5) {
                    String placeid = tours.getJSONObject(i).getString("place_id");
                    String address = tours.getJSONObject(i).getString("formatted_address");
                    String link = tours.getJSONObject(i).getString("icon");
                    String name = tours.getJSONObject(i).getString("name");
                    double rating = Double.parseDouble(tours.getJSONObject(i).getString("rating"));
                    String types = "Tourism";
                    Tours tours1 = new Tours(placeid, address, link, name, rating, types, true);
                    //list.add(tours1);
                }
                //if (list.size()==3)
                //break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Tours tour=new Tours("Allahabad","weather","Rainy",37,99,550);
        //list.add(tour);
//            ArrayList<HashMap<String,String>> arrayList= new ArrayList<>();
//            if(list!=null) {
//                for (int i = 0; i < list.size(); i++) {
//                    HashMap<String, String> hm = new HashMap<>();
//                    hm.put("name", list.get(i).address);
//                    arrayList.add(hm);
//                }
//            }
        String[] s = {"name"};
        int[] t = {R.id.tv};
//            if (list!=null)
//            {
//                mAdapter=new CardAdapter(getApplicationContext(),list);
//                mRecyclerView.setAdapter(mAdapter);
//            }
    }

    public static String buildStringIOutils(InputStream is) {
        try {
            return IOUtils.toString(is, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public void drawMarker(LatLng point) {
        // Creating an instance of MarkerOptions
        // Toast.makeText(Home.this,"drawn",Toast.LENGTH_LONG).show();
        MarkerOptions markerOptions = new MarkerOptions();

        // Setting latitude and longitude for the marker
        markerOptions.position(point);

        // Adding marker on the Google Map
        marker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 16.0f));


    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Intent placePickerIntent = new Intent(Home.this, MyMapLocation.class);
        placePickerIntent.putExtra("lat", latitude);
        placePickerIntent.putExtra("lng", longitude);
        startActivity(placePickerIntent);
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latlng1 = new LatLng(25.4918881, 81.86750959);
        drawMarker(latlng1);
        mMap.setOnMarkerClickListener(Home.this);
    }

    public Tours getTours() {
        if (tour != null)
            return tour;
        return new Tours("city", "ad", 0.00, 0.00);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mybutton) {
            // do something here
            try {
                Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(this);
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            }

        }
        return super.onOptionsItemSelected(item);
    }


    private void setupDrawer() {
        //Toast.makeText(getApplicationContext(),user+"this"+cour,Toast.LENGTH_SHORT).show();
        mDrawerView.removeAllViews();

        mDrawerView
                .addView(new Drawerheader(this.getApplicationContext(), latLng))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_PROFILE, name))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_TRAVEL, name))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_PROJECTS, email))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_SHARE, email))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_TERMS, email))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_SETTINGS, email))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_LOGOUT, email));

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar, R.string.open_drawer, R.string.close_drawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

}
