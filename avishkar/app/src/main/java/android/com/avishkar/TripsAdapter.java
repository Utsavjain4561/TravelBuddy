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
/**
 * Created by UTSAV JAIN on 9/18/2018.
 */

public class TripsAdapter extends ArrayAdapter {
    int resource;
    Context context;
    public TripsAdapter(@NonNull Context context,int res, ArrayList<Trips> Trips) {
        super(context,res,Trips);
        this.context=context;
        this.resource=res;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem==null){
            LayoutInflater li;
            li = LayoutInflater.from(context);
            listItem=li.inflate(resource,null);
        }
        Trips trips =(Trips)getItem(position);
        TextView source = (TextView) listItem.findViewById(R.id.source_tv);
        source.setText(trips.getSource());
        TextView destination = (TextView) listItem.findViewById(R.id.destination_tv);
        destination.setText(trips.getDestination());
        TextView days = (TextView) listItem.findViewById(R.id.days);
        days.setText(String.valueOf(trips.getDays()));
        TextView itirenary = (TextView) listItem.findViewById(R.id.itirenary_list);
        itirenary.setText(trips.getItirenrary());
        TextView date = (TextView) listItem.findViewById(R.id.date);
        date.setText(trips.getDate());
        return listItem;
    }
}