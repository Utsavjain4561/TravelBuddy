package android.com.avishkar;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;

import static com.google.android.gms.internal.zzbgp.NULL;

/**
 * Created by lokesh on 18/9/18.
 */

public class CurrentTour {
    public Date startDate;
    public String title,source,destination;
    public String tripDuration,budget;
    public boolean state;
    ArrayList<Days> days;

    CurrentTour(){
        startDate=new Date();
        title=destination=source=NULL;
        tripDuration=budget=NULL;
        state=false;
        days=new ArrayList<>();
    }
    CurrentTour(String title,String source,String destination,boolean state,ArrayList<Days>days1,String tripDuration,String budget){
        this.title=title;
        this.source=source;
        this.destination=destination;
        this.state=state;
        this.days=days1;
        this.tripDuration=tripDuration;
        this.budget=budget;
        this.startDate=new Date();
    }
}