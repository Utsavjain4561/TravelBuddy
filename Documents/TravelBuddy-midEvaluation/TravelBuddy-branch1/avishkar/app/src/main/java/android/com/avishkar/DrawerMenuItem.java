package android.com.avishkar;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

//import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by lokesh on 1/7/18.
 */
@Layout(R.layout.drawer_item)


public class DrawerMenuItem {

    public static final int DRAWER_MENU_ITEM_PROFILE = 1;
    public static final int DRAWER_MENU_ITEM_TRAVEL = 2;
    public static final int DRAWER_MENU_ITEM_PROJECTS = 3;
    public static final int DRAWER_MENU_ITEM_SHARE = 4;
    public static final int DRAWER_MENU_ITEM_NOTIFICATIONS = 5;
    public static final int DRAWER_MENU_ITEM_SETTINGS = 6;
    public static final int DRAWER_MENU_ITEM_TERMS = 7;
    public static final int DRAWER_MENU_ITEM_LOGOUT = 8;

    private int mMenuPosition;
    private Context mContext;
    private DrawerCallBack mCallBack;

    @View(R.id.itemNameTxt)
    private TextView itemNameTxt;

    @View(R.id.itemIcon)
    private ImageView itemIcon;

    private String memail;
    private String mcity;

    public DrawerMenuItem(Context context, int menuPosition, String city) {
        if(city==null)
            city="Allahabad";
        mcity = city;
        mContext = context;
        mMenuPosition = menuPosition;
    }

    @Resolve
    private void onResolved() {
        switch (mMenuPosition) {
            case DRAWER_MENU_ITEM_PROFILE:
                itemNameTxt.setText("Profile");
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_email_black_24dp));
                break;
            case DRAWER_MENU_ITEM_TRAVEL:
                itemNameTxt.setText("Travel");
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_add_black_24dp));
                break;
            case DRAWER_MENU_ITEM_PROJECTS:
                itemNameTxt.setText("Projects");
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_edit_black_24dp));
                break;
            case DRAWER_MENU_ITEM_SHARE:
                itemNameTxt.setText("Share");
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_email_black_24dp));
                break;
            case DRAWER_MENU_ITEM_NOTIFICATIONS:
                itemNameTxt.setText("Notifications");
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_email_black_24dp));
                break;
            case DRAWER_MENU_ITEM_SETTINGS:
                itemNameTxt.setText("Settings");
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_add_black_24dp));
                break;
            case DRAWER_MENU_ITEM_TERMS:
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_email_black_24dp));
                itemNameTxt.setText("Developers");
                break;
            case DRAWER_MENU_ITEM_LOGOUT:
                itemIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_edit_black_24dp));
                itemNameTxt.setText("Logout");
                break;
        }
    }

    @Click(R.id.mainView)
    private void onMenuItemClick() {
        switch (mMenuPosition) {
            case DRAWER_MENU_ITEM_PROFILE:
                Intent profileIntent = new Intent(mContext, Profile.class);
                profileIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(profileIntent);
                if (mCallBack != null) mCallBack.onProfileMenuSelected();
                break;
            case DRAWER_MENU_ITEM_TRAVEL:
                Toast.makeText(mContext, "Travel", Toast.LENGTH_SHORT).show();
                Intent inten = new Intent(mContext, Features.class);
                Log.e("DrawerMenu", String.valueOf(mcity));
                inten.putExtra("city", mcity);
                inten.putExtra("feature", 1);
                inten.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(inten);
                if (mCallBack != null) mCallBack.onTravelMenuSelected();
                break;
            case DRAWER_MENU_ITEM_PROJECTS:

                break;
            case DRAWER_MENU_ITEM_SHARE:
                Toast.makeText(mContext, "Share", Toast.LENGTH_SHORT).show();
                if (mCallBack != null) mCallBack.onSHAREMenuSelected();
                break;
            case DRAWER_MENU_ITEM_NOTIFICATIONS:
                Toast.makeText(mContext, "Notifications", Toast.LENGTH_SHORT).show();
                if (mCallBack != null) mCallBack.onNotificationsMenuSelected();
                break;
            case DRAWER_MENU_ITEM_SETTINGS:
                Toast.makeText(mContext, "Settings", Toast.LENGTH_SHORT).show();
                if (mCallBack != null) mCallBack.onSettingsMenuSelected();
                break;
            case DRAWER_MENU_ITEM_TERMS:

                if (mCallBack != null) mCallBack.onTermsMenuSelected();
                break;
            case DRAWER_MENU_ITEM_LOGOUT:

                break;
        }
    }

    public void setDrawerCallBack(DrawerCallBack callBack) {
        mCallBack = callBack;
    }

    public interface DrawerCallBack {
        void onProfileMenuSelected();

        void onTravelMenuSelected();

        void onPROJECTSMenuSelected();

        void onSHAREMenuSelected();

        void onNotificationsMenuSelected();

        void onSettingsMenuSelected();

        void onTermsMenuSelected();

        void onLogoutMenuSelected();

    }
}
