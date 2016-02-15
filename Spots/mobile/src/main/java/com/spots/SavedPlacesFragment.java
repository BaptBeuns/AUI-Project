package com.spots;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SavedPlacesFragment extends Fragment {
    // Store instance variables
    static final String LOG_TAG = "SavedPlacesFragment";
    private Context mCtx;
    private String title;
    private int page;

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

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved_places, container, false);
        TextView tvLabel = (TextView) view.findViewById(R.id.text);
        tvLabel.setText(page + " -- " + title);
        return view;
    }
}
