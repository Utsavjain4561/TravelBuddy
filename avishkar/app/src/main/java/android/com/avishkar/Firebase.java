package android.com.avishkar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

public class Firebase extends AppCompatActivity {

    EditText et1,et2;
    Button bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        et1=(EditText)findViewById(R.id.et1);
        et2=(EditText)findViewById(R.id.et2);
        bt =(Button)findViewById(R.id.bt);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String etinp1=et1.getText().toString();
                final String etinp2=et2.getText().toString();
                Toast.makeText(Firebase.this, etinp1+" "+etinp2, Toast.LENGTH_SHORT).show();
                database.getReference("users").child(etinp1).push().setValue(etinp2);
                Intent intent = new Intent (Firebase.this,LoginActivity.class);
            }
        });
        //myref.setValue("message");
//        myref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                String val = (String) dataSnapshot.getValue();
//                Toast.makeText(Firebase.this,val,Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
    }
}
