package com.spots;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.spots.data.database.CategoryDB;
import com.spots.data.database.SpotDB;
import com.spots.data.model.Category;
import com.spots.data.model.Spot;
import com.spots.slider.SlidingTabLayout;

import java.util.List;


public class SavedSpotsFragment extends Fragment {

    static final String TAG = "SAVED SPOTS FRAGMENTS";
    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;
    private Context mCtx;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.saved_spots_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new SamplePagerAdapter());

        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCtx = context;
    }

    public void updateListView() {
        CategoryDB categoryDatabase = new CategoryDB(mCtx);
        List<Category> categoryList = categoryDatabase.getAll();
    }

    class SamplePagerAdapter extends PagerAdapter {
        List<Category> categoryList;
        List<Spot> spotList;

        public SamplePagerAdapter() {
            CategoryDB catDB = new CategoryDB(mCtx);
            categoryList = catDB.getAll();
            SpotDB spotDB = new SpotDB(mCtx);
            spotList = spotDB.getAll();
        }

        @Override
        public int getCount() {
            return categoryList.size() + 1;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "ALL";
            }
            return categoryList.get(position - 1).getName();
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int numberOfSpots = 0;
            int fillArrays = 0;
            int spotInList = 0;
            ListView listView;
            LinearLayout noSpot;
            // Inflate a new layout from our resources
            View view = getActivity().getLayoutInflater().inflate(R.layout.saved_spots_pager,
                    container, false);
            // Add the newly created View to the ViewPager
            container.addView(view);

            // COUNT NUMBER OF CORRESPONDING SPOTS
            if(position == 0) { // "ALL" CATEGORY
                numberOfSpots = spotList.size();
            } else {            // OTHERS CATEGORIES
                for (Spot spot: spotList) {
                    if (spot.getCategoryId() == position - 1) {
                        numberOfSpots++;
                    }
                }
            }

            String[] titlesArray    = new String[numberOfSpots];
            String[] addressesArray = new String[numberOfSpots];
            int[] idsArray = new int[numberOfSpots];
            double[] latitudesArray = new double[numberOfSpots];
            double[] longitudesArray = new double[numberOfSpots];
            int[] imagesArray  = new int[numberOfSpots];

            while((fillArrays < numberOfSpots)&&(spotInList < spotList.size())) {
                Spot spot = spotList.get(spotInList);
                int categoryId = spot.getCategoryId();

                if ((categoryId == position - 1)||(position == 0)) {
                    titlesArray[fillArrays] = spot.getName();
                    addressesArray[fillArrays] = spot.getAddress();
                    idsArray[fillArrays] = spot.getId();
                    latitudesArray[fillArrays] = spot.getLatitude();
                    longitudesArray[fillArrays] = spot.getLongitude();
                    if (!((categoryId<=categoryList.size())&&(categoryId>=0))) {
                        categoryId = 0;
                    }
                    Category category = categoryList.get(categoryId);
                    imagesArray[fillArrays] = getResources().getIdentifier(category.getLogo(), "drawable", MainActivity.PACKAGE_NAME);
                    fillArrays++;
                }
                spotInList++;
            }

            listView = (ListView) view.findViewById(R.id.savedSpotsList);
            noSpot = (LinearLayout) view.findViewById(R.id.noSpotLayout);
            if(numberOfSpots == 0) {
                listView.setVisibility(View.INVISIBLE);
                noSpot.setVisibility(View.VISIBLE);
            } else {
                listView.setVisibility(View.VISIBLE);
                noSpot.setVisibility(View.INVISIBLE);
            }

            SpotListAdapter adapter = new SpotListAdapter(getActivity(), mCtx, titlesArray, addressesArray, idsArray, latitudesArray, longitudesArray, imagesArray);

            listView.setAdapter(adapter);

            return view;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            Log.i(TAG, "destroyItem() [position: " + position + "]");
        }

    }
}
