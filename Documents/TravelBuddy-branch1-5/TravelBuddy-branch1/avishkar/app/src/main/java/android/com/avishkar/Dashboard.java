package android.com.avishkar;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Dashboard extends AppCompatActivity  {


    private android.support.v7.widget.Toolbar mToolBar;

    public static String signinemail;
    private DrawerLayout mDrawerLayout;

    private NavigationView mNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        signinemail=getIntent().getExtras().getString("email");
        mToolBar =  findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = findViewById(R.id.navigation_view);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout,mToolBar,
                R.string.navigation_draw_open, R.string.navigation_draw_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame, new ViewPagerFragment()).commit();

        }





    }


    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            finish();
            System.exit(0);
        }

    }


    public void tripClick(View view) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame,new ViewPagerFragment()).commit();
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    public void profileClick(View view) {
        //getSupportFragmentManager().beginTransaction().replace(R.id.frame,new Profile()).commit();
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    public void exploreClick(View view) {
        Toast.makeText(Dashboard.this,"Explore",Toast.LENGTH_SHORT).show();
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    public void homeClick(View view) {
        Toast.makeText(Dashboard.this,"Home",Toast.LENGTH_SHORT).show();
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    public void shareClick(View view) {
        Toast.makeText(Dashboard.this,"Share",Toast.LENGTH_SHORT).show();
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    public void settingsClick(View view) {
        Toast.makeText(Dashboard.this,"Settings",Toast.LENGTH_SHORT).show();
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }
}