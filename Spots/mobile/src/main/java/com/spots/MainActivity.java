package com.spots;

import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.NotificationCompat;
import android.app.Notification;
import android.widget.Button;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

// http://developer.android.com/guide/topics/location/strategies.html
// http://developer.android.com/reference/android/location/LocationManager.html#requestLocationUpdates%28java.lang.String,%20long,%20float,%20android.app.PendingIntent%29
// http://webdesignergeeks.com/mobile/android/geting-current-location-in-android-application-using-gps/
// http://www.vogella.com/tutorials/AndroidLocationAPI/article.html#locationapi

public class MainActivity extends AppCompatActivity {

    public static String PACKAGE_NAME;
    private static String TAG = "MAIN_ACTIVITY";

    private FragmentPagerAdapter adapterViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_base);
        PACKAGE_NAME = getApplicationContext().getPackageName();

        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        adapterViewPager = new SpotsPagerAdapter(getSupportFragmentManager(), MainActivity.this);
        vpPager.setAdapter(adapterViewPager);

        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                Toast.makeText(MainActivity.this,
                        "Selected page position: " + position, Toast.LENGTH_SHORT).show();
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }
    // HOME FRAGMENT

    // Fonction appelée par le clic sur une des catégories
    public void changeResource(View button) {
        ViewGroup vg = (ViewGroup) findViewById(R.id.layout_images);
        ViewGroup nextChild;
        View view;
        // On assigne tous les enfants à pas selected
        Log.d(TAG, Double.toString(vg.getChildCount()));
        for(int i=0; i<vg.getChildCount(); ++i) {
            nextChild = (ViewGroup) vg.getChildAt(i);
            view = nextChild.getChildAt(0);
            view.setBackgroundResource(R.drawable.round_button);
            view.setSelected(false);
        }
        button.setSelected(true);
        button.setBackgroundResource(R.drawable.round_button_selected);
    }



    // SAVED PLACES FRAGMENT

    // EXPLORE FRAGMENT

    // NAVIGATION


    public static class SpotsPagerAdapter extends FragmentPagerAdapter {

        private static int NUM_ITEMS = 2;
        private Context context;
        SavedPlacesFragment savedPlacesFragment;
        //ExploreFragment exploreFragment;
        HomeFragment homeFragment;

        public SpotsPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
            savedPlacesFragment = SavedPlacesFragment.newInstance(context,1,"Saved Places");
            //exploreFragment = ExploreFragment.newInstance(mCtx,2,"Explore Fragment");
            homeFragment = HomeFragment.newInstance(context,0,"Home");
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 1:
                    return new String("SAVED PLACES");
                default:
                    return new String("ADD");
            }
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 1:
                    return savedPlacesFragment;
                //case 2:
                //    return exploreFragment;
                default:
                    return homeFragment;
            }
        }

    }
}