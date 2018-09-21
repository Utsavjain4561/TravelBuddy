package android.com.avishkar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by UTSAV JAIN on 9/20/2018.
 */

public class TripListAdapter extends ArrayAdapter {

    public TripListAdapter(@NonNull Context context, int resource, ArrayList<CurrentTour> currentTours) {
        super(context, 0,currentTours);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem ==null){
            listItem = LayoutInflater.from(getContext()).inflate(
                    R.layout.trip_list_item, parent, false);
        }
        CurrentTour tour =(CurrentTour) getItem(position);
        TextView titleView = (TextView)listItem.findViewById(R.id.titlelist);
        titleView.setText(tour.title);
        TextView sourceView = (TextView)listItem.findViewById(R.id.sourcelist);
        sourceView.setText(tour.source);

        TextView destinationView = (TextView)listItem.findViewById(R.id.destinationlist);
        destinationView.setText(tour.destination);

        TextView dateview=(TextView)listItem.findViewById(R.id.datelist);
        dateview.setText(tour.startDate+"");
        return  listItem;

    }
}
