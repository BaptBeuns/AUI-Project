package com.spots;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.spots.data.database.CategoryDB;
import com.spots.data.database.SpotDB;
import com.spots.data.model.Category;
import com.spots.data.model.Spot;

import java.util.List;

public class SavedPlacesFragment extends Fragment {
    // Store instance variables
    static final String LOG_TAG = "SavedPlacesFragment";
    protected final String FRAGMENT_TITLE = "SAVED PLACES";
    private Context mCtx;
    private View mView;
    private String title;
    private int page;
    private List<Spot> spotList;
    public List<Category> categoryList;
    public ListView listView;

    // newInstance constructor for creating fragment with arguments
    public static SavedPlacesFragment newInstance(Context ctx, int page, String title) {
        SavedPlacesFragment fragmentFirst = new SavedPlacesFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCtx = context;
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved_places, container, false);
        mView = view;
        updateListView();
        return view;
    }

    protected void updateListView() {
        SpotDB spotDatabase = new SpotDB(mCtx);
        spotList = spotDatabase.getAll();
        CategoryDB categoryDatabase = new CategoryDB(mCtx);
        categoryList = categoryDatabase.getAll();

        if (spotList.size() > 0) {
            String[] titlesArray = new String[spotList.size()];
            String[] adressesArray = new String[spotList.size()];
            int[] imagesArray = new int[spotList.size()];

            for (int i = 0; i < titlesArray.length; i++) {

                Spot spot = spotList.get(i);
                if (spot.getCategoryId() < 0 || spot.getCategoryId() > 5) {
                    spot.setCategoryId(5);
                }
                Category category = categoryList.get(spot.getCategoryId());

                titlesArray[i] = spot.getName();
                adressesArray[i] = spot.getAddress();
                imagesArray[i] = getResources().getIdentifier(category.getLogo(), "drawable", MainActivity.PACKAGE_NAME);

            }

            SpotListAdapter adapter = new SpotListAdapter(getActivity(), mCtx, titlesArray, adressesArray, imagesArray);
            listView = (ListView) mView.findViewById(R.id.savedSpotsList);
            listView.setAdapter(adapter);
        }

    }
}

