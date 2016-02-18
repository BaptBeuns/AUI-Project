package com.spots;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

// http://developer.android.com/guide/topics/location/strategies.html
// http://developer.android.com/reference/android/location/LocationManager.html#requestLocationUpdates%28java.lang.String,%20long,%20float,%20android.app.PendingIntent%29
// http://webdesignergeeks.com/mobile/android/geting-current-location-in-android-application-using-gps/
// http://www.vogella.com/tutorials/AndroidLocationAPI/article.html#locationapi

public class MainActivity extends AppCompatActivity {

    public static String PACKAGE_NAME;
    private static String TAG = "MAIN_ACTIVITY";

    private FragmentPagerAdapter adapterViewPager;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PACKAGE_NAME = getApplicationContext().getPackageName();

        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        adapterViewPager = new SpotsPagerAdapter(getSupportFragmentManager(), MainActivity.this);
        vpPager.setAdapter(adapterViewPager);

        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                String title = (String) adapterViewPager.getPageTitle(position);
                if (title.equals("SAVED PLACES")) {
                    Log.d(TAG, "on Update List");
                    ((SavedPlacesFragment) adapterViewPager.getItem(position)).updateListView();
                }
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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.spots/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }
    // HOME FRAGMENT

    // Fonction appelée par le clic sur une des catégories
    public void changeResource(View button) {
        ViewGroup vg = (ViewGroup) findViewById(R.id.layout_images);
        ViewGroup nextChild;
        View view;
        // On assigne tous les enfants à pas selected
        Log.d(TAG, Double.toString(vg.getChildCount()));
        for (int i = 0; i < vg.getChildCount(); ++i) {
            nextChild = (ViewGroup) vg.getChildAt(i);
            if (nextChild.getChildCount() > 0) {
                view = nextChild.getChildAt(0);
                view.setBackgroundResource(R.drawable.round_button);
                view.setSelected(false);
            }
        }
        button.setSelected(true);
        button.setBackgroundResource(R.drawable.round_button_selected);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.spots/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


    // SAVED PLACES FRAGMENT

    // EXPLORE FRAGMENT

    // NAVIGATION


    public static class SpotsPagerAdapter extends FragmentPagerAdapter {

        private static int NUM_ITEMS = 2;
        private Context context;
        SavedPlacesFragment savedPlacesFragment;
        ExploreFragment exploreFragment;
        HomeFragment homeFragment;

        public SpotsPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
            savedPlacesFragment = SavedPlacesFragment.newInstance(context, 1, "Saved Places");
            exploreFragment = ExploreFragment.newInstance(context, 2, "Explore Fragment");
            homeFragment = HomeFragment.newInstance(context, 0, "Home");
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 2:
                    return exploreFragment.FRAGMENT_TITLE;
                case 1:
                    return savedPlacesFragment.FRAGMENT_TITLE;
                default:
                    return homeFragment.FRAGMENT_TITLE;
            }
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 2:
                    return exploreFragment;
                case 1:
                    return savedPlacesFragment;
                default:
                    return homeFragment;
            }
        }

    }
}