package android.com.avishkar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Temp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        Button saveDetails=(Button)findViewById(R.id.presentTourDetails);
        final String email=getIntent().getExtras().getString("email");
        Toast.makeText(Temp.this,email,Toast.LENGTH_LONG).show();
        saveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Days>days1=new ArrayList<>();
                CurrentTour currentTour=new CurrentTour("trip","Allahabad","Banaras",false,days1,10,5000);
                FirebaseDatabase database=FirebaseDatabase.getInstance();
                DatabaseReference myRef=database.getReference();
                //myRef.child("users").child(email).child("ongoingTrip").setValue(currentTour);
                Intent temp=new Intent(Temp.this,PresentTrip.class);
                temp.putExtra("email",email);
                startActivity(temp);
                finish();
            }
        });
    }
}
