package android.com.avishkar;

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

public class UpcommingFragment extends Fragment {
    private FloatingActionButton addButton;
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.upcomming_fragment,container,false);
        ListView list = view.findViewById(R.id.upcomming_list);
        addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add aanything on float click
            }
        });
        return  view;
    }
}