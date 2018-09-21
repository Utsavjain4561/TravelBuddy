package android.com.avishkar;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by UTSAV JAIN on 9/16/2018.
 */

public class UpcommingFragment extends Fragment {
    private FloatingActionButton addButton;
    public  String email;
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.upcomming_fragment,container,false);
        final ListView list = view.findViewById(R.id.upcomming_list);
        addButton = view.findViewById(R.id.addButton);
        email = ((Dashboard)getActivity()).getEmail();
        final ArrayList<Trips> tripDataList = new ArrayList<>();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        Query query = firebaseDatabase.getReference().child("users")
                .child(email).child("trips");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    for(DataSnapshot data: dataSnapshot.getChildren()){
                        Trips trip = data.getValue(Trips.class);
                        tripDataList.add(trip);
                    }
                    TripsAdapter adapter = new TripsAdapter(getContext(),R.layout.list_item,tripDataList);
                    list.setAdapter(adapter);
            }


        }
                                                 @Override
                                                 public void onCancelled(DatabaseError databaseError) {

                                                 }});

        System.out.println(tripDataList);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frame,new SaveTrip()).commit();
            }
        });
        return  view;
    }
}
