package com.spots;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SavedSpotsActivity extends AppCompatActivity {

    private static String TAG = "SAVED_SPOTS_ACTIVITY";
    private FragmentPagerAdapter adapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_spots);

        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        adapterViewPager = new SpotsPagerAdapter(getSupportFragmentManager(), this);
        vpPager.setAdapter(adapterViewPager);

        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
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


    public static class SpotsPagerAdapter extends FragmentPagerAdapter {

        private static int NUM_ITEMS = 2;
        private Context context;
        SavedSpotsFragment SavedSpotsFragment;

        public SpotsPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Page number :"+position;
        }

        @Override
        public Fragment getItem(int position) {
            return SavedSpotsFragment.newInstance(position);
        }

    }
}
