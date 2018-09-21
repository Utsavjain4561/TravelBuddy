package android.com.avishkar;

public class Trips {
    public String source;
    public String destination;
    public String date;
    public int days;
    public int status;
    //1 for upcoming, 2 for past and 3 for current trips
    Trips()
    {

    }
    Trips(String source,String destination,String date,int days,int status)
    {
        this.date=date;
        this.days=days;
        this.destination=destination;
        this.source=source;
        this.status=status;
    }
    public String getSource(){
        return source;
    }
    public String getDestination(){
        return destination;
    }
    public String getDate(){
        return date;
    }

    public int getDays(){
        return days;
    }
    public int getStatus(){
        return status;
    }
}
