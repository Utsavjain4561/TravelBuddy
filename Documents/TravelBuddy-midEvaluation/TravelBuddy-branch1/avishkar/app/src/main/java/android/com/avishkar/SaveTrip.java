package android.com.avishkar;

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

public class SaveTrip extends Fragment {
    private EditText mSource;
    private EditText mDestination;
    private EditText mDays;
    private CalendarView mCalendar;
    public String source,destination;
    private Button mButton;
    public int days;
    private long date;
    private String actual_date;
    private String email;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.save_trip,container,false);
        mSource = view.findViewById(R.id.source);
        mDestination = view.findViewById(R.id.destination);
        mDays = view.findViewById(R.id.days_of_journey);
        mCalendar = view.findViewById(R.id.date_of_journey);
        mButton = view.findViewById(R.id.saveBuuton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                source = mSource.getText().toString();
                destination = mDestination.getText().toString();
                days = Integer.parseInt(mDays.getText().toString());
                date = mCalendar.getDate();
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                actual_date=sdf.format(new Date(date));
                email =((Dashboard)getActivity()).getEmail();
                Trips trips = new Trips(source,destination,actual_date,days,1);
                Log.e("trip",trips+"");
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                final UserLogin[] userLogin = new UserLogin[1];
                final DatabaseReference ref=firebaseDatabase.getReference().child("users").child(email).child("profile");
                firebaseDatabase.getReference().child("users").child(email).child("trips").push().setValue(trips);
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        userLogin[0] = dataSnapshot.getValue(UserLogin.class);
                        userLogin[0].future+=1;
                        int f = userLogin[0].future;
                        f+=1;
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
