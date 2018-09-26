package android.com.avishkar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import static android.app.Activity.RESULT_OK;

public class SaveTrip extends Fragment {
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE_SOURCE =1 ;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE_DESTINATION =2 ;

    private TextView mSource;
    private TextView mDestination;
    private EditText mDays;
    private EditText mItirenary;
    private CalendarView mCalendar;
    public String source=null,destination=null;
    private Button mButton;
    public int days=-1;
    private long date;
    private String actual_date;
    private String email;
    private String itirenary;
    private double sourceLat;
    private double sourceLng;
    private double destinationLat;
    private double destinationLng;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE_SOURCE){
            if(resultCode == RESULT_OK){
                Place place= PlaceAutocomplete.getPlace(getActivity(),data);
                source = place.getName().toString();
                sourceLat = place.getLatLng().latitude;
                sourceLng = place.getLatLng().longitude;
                mSource.setText(source);

            }
        }
        else if(requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE_DESTINATION){
            if(resultCode == RESULT_OK){
                Place place= PlaceAutocomplete.getPlace(getActivity(),data);
                destination = place.getName().toString();
                destinationLat = place.getLatLng().latitude;
                destinationLng = place.getLatLng().longitude;
                mDestination.setText(destination);

            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.save_trip,container,false);
        mSource = view.findViewById(R.id.source);
        mDestination = view.findViewById(R.id.destination);
        mDays = view.findViewById(R.id.days_of_journey);
        mCalendar = view.findViewById(R.id.date_of_journey);
        mButton = view.findViewById(R.id.saveBuuton);
        mItirenary = view.findViewById(R.id.itirenary_save_list);
        mSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(getActivity());
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE_SOURCE);
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                }
            }
        });
        mDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(getActivity());
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE_DESTINATION);
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                }
            }

        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                days = Integer.parseInt(mDays.getText().toString());
                date = mCalendar.getDate();
                itirenary = mItirenary.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                actual_date=sdf.format(new Date(date));
                email =((Dashboard)getActivity()).getEmail();
                    Trips trips = new Trips(source, destination, actual_date, days,itirenary,sourceLat,sourceLng,destinationLat,
                            destinationLng,1);
                    Log.e("trip", trips + "");
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    final UserLogin[] userLogin = new UserLogin[1];
                    final DatabaseReference ref = firebaseDatabase.getReference().child("users").child(email).child("profile");
                    firebaseDatabase.getReference().child("users").child(email).child("trips").push().setValue(trips);
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            userLogin[0] = dataSnapshot.getValue(UserLogin.class);
                            userLogin[0].future += 1;
                            int f = userLogin[0].future;
                            f += 1;
                            ref.child("future").setValue(f);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
              getFragmentManager().beginTransaction().replace(R.id.frame,new ViewPagerFragment()).commit();
            }
        });
        return view;

    }
}
