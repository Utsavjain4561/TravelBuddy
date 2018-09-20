package android.com.avishkar;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by UTSAV JAIN on 9/16/2018.
 */

public class MyTripFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.trip_fragment,container,false);
        ListView list = view.findViewById(R.id.trip_list);
        final String reducedemail=Dashboard.signinemail;

        FloatingActionButton saveCurrentTour=(FloatingActionButton)view.findViewById(R.id.saveCurrentTour);
        saveCurrentTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent totemp=new Intent(getActivity(),Temp.class);
                totemp.putExtra("email",reducedemail);
                startActivity(totemp);
            }
        });
        return view;
    }
}