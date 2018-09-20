package android.com.avishkar;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class PresentTrip extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private DaysAdapter mAdapter;
    private ArrayList<Days> mDays;
    private String title,from,to,days,budget,email;
    Date startDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_present_trip);

        final Context context=this;
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //mDays=new ArrayList<>();
        final TextView tit,source,dest,bud,tripdays,sdate;
        tit=(TextView)findViewById(R.id.title);
        source=(TextView)findViewById(R.id.source);
        dest=(TextView)findViewById(R.id.destination);
        bud=(TextView)findViewById(R.id.budget);
        tripdays=(TextView)findViewById(R.id.maxDays);
        sdate=(TextView)findViewById(R.id.startdate);

        //set current storage on adapter
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference myRef=database.getReference();
        email=getIntent().getExtras().getString("email");
        myRef.child("users").child(email).child("ongoingTrip").child("current").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CurrentTour currtour=dataSnapshot.getValue(CurrentTour.class);
                title=currtour.title.trim();
                from=currtour.source.trim();
                to="To: "+currtour.destination.trim();
                days=currtour.tripDuration+"";
                budget="Budget: "+currtour.budget;
                startDate=currtour.startDate;
                tit.setText(title);
                source.setText(from);
                dest.setText(to);
                bud.setText(budget);
                tripdays.setText(days);
                sdate.setText(startDate+"");
                mDays=currtour.days;
                if(mDays!=null){
                    mAdapter=new DaysAdapter(context,mDays,email);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        FloatingActionButton add=(FloatingActionButton)findViewById(R.id.addDay);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Days day=new Days();
                mDays.add(day);
                mAdapter=new DaysAdapter(context,mDays,email);
                mRecyclerView.setAdapter(mAdapter);
            }
        });
    }
}
