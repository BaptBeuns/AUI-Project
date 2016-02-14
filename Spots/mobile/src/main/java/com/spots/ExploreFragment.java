package com.spots;

import android.support.v4.app.Fragment;

public class ExploreFragment extends Fragment {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_explore_layout);
    }

}
