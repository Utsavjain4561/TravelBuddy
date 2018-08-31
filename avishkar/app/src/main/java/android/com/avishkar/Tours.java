package android.com.avishkar;

import java.util.ArrayList;

public class Tours {
    public String place_id;
    public String address;
    public String url;
    public String nam;
    public double rating;
    public String types;
    Tours(String place_id,String address,String url,String nam,double rating,String types)
    {
        this.address=address;
        this.nam=nam;
        this.place_id=place_id;
        this.rating=rating;
        this.types=types;
        this.url=url;
    }
}
