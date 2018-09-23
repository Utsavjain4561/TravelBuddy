package android.com.avishkar;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class Dashboard extends AppCompatActivity  {
    public static String signinemail;
    private android.support.v7.widget.Toolbar mToolBar;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mToolBar =  findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        signinemail = getIntent().getExtras().getString("email");
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
    public String getEmail()
    {
        return signinemail;
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
        Intent intent = new Intent(getApplicationContext(),Profile.class);
        startActivity(intent);
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    public void exploreClick(View view) {
        Toast.makeText(Dashboard.this,"Explore",Toast.LENGTH_SHORT).show();
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    public void homeClick(View view) {
        Toast.makeText(Dashboard.this,"Home",Toast.LENGTH_SHORT).show();
        Intent homeIntent = new Intent(Dashboard.this,Home.class);
        homeIntent.putExtra("id",1);
        homeIntent.putExtra("email",signinemail);
        mDrawerLayout.closeDrawer(GravityCompat.START);
        startActivity(homeIntent);
    }

    public void shareClick(View view) {
        Toast.makeText(Dashboard.this,"Share",Toast.LENGTH_SHORT).show();
        String shareBody = "Here is the share content body";
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "xzy"));
        mDrawerLayout.closeDrawer(GravityCompat.START);

    }

    public void settingsClick(View view) {
        Toast.makeText(Dashboard.this,"Settings",Toast.LENGTH_SHORT).show();
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }
}
