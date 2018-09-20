package android.com.avishkar;

/**
 * Created by lokesh on 19/9/18.
 */

public class UserLogin {
    public String email,mob;
    public int present,past,future;
    UserLogin(){

    }

    UserLogin(String email,String mob,int p,int pa,int f){
        this.email=email;
        this.mob=mob;
        this.present=p;
        this.past=pa;
        this.future=f;
    }
}
