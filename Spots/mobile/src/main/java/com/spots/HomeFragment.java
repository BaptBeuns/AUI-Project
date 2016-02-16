package com.spots;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.spots.data.database.CategoryDB;
import com.spots.data.database.SpotDB;
import com.spots.data.model.Category;
import com.spots.data.model.Spot;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private String TAG = "HOME_FRAGMENT";
    private Context mCtx;
    private String title;
    private int page;
    private View mView;
    private SupportMapFragment mMapFragment;
    private GoogleMap mMap;
    private CategoryDB categoryDB;

    // newInstance constructor for creating fragment with arguments
    public static HomeFragment newInstance(Context ctx, int page, String title) {
        HomeFragment fragmentFirst = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.top_layout);
        View autocomplete = inflater.inflate(R.layout.places_layout, rl, true);

        rl = (RelativeLayout) view.findViewById(R.id.map_layout);
        View map = inflater.inflate(R.layout.map, rl, true);

        mView = view;

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        mMapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        if (mMapFragment == null) {
            mMapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, mMapFragment).commit();
        }
    }

    @Override
    public void onAttach(Context context){
        super.onAttach (context);
        mCtx = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMap == null) {
            mMapFragment.getMapAsync(this);
        }

        displayCategories();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            return;
        }
        mMap = map;
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)));
    }


    private void displayCategories() {
        try {
            categoryDB = new CategoryDB(mCtx);
        } catch (NullPointerException e) {
            Log.d(TAG,e.getMessage());
        }

        List<Category> catList = categoryDB.getAll();
        String categoryName;
        String categoryImage;
        ImageView imageView;
        int resID;
        Drawable drawable;
        TextView txt;

        categoryImage = catList.get(0).getLogo();
        resID = getResources().getIdentifier(categoryImage , "drawable", MainActivity.PACKAGE_NAME);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = getResources().getDrawable(resID, mCtx.getTheme());
        } else {
            drawable = getResources().getDrawable(resID);
        }
        drawable = getResources().getDrawable(resID);
        imageView = (ImageView) mView.findViewById(R.id.category_image_1);
        imageView.setImageResource(resID);
        txt = (TextView) getActivity().findViewById(R.id.cat1);
        categoryName = catList.get(0).getName();
        txt.setText(categoryName);


        categoryImage = catList.get(1).getLogo();
        resID = getResources().getIdentifier(categoryImage , "drawable", MainActivity.PACKAGE_NAME);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = getResources().getDrawable(resID, mCtx.getTheme());
        } else {
            drawable = getResources().getDrawable(resID);
        }
        drawable = getResources().getDrawable(resID);
        imageView = (ImageView) mView.findViewById(R.id.category_image_2);
        imageView.setImageResource(resID);
        txt = (TextView) getActivity().findViewById(R.id.cat2);
        categoryName = catList.get(1).getName();
        txt.setText(categoryName);


        categoryImage = catList.get(2).getLogo();
        resID = getResources().getIdentifier(categoryImage , "drawable", MainActivity.PACKAGE_NAME);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = getResources().getDrawable(resID, mCtx.getTheme());
        } else {
            drawable = getResources().getDrawable(resID);
        }
        drawable = getResources().getDrawable(resID);
        imageView = (ImageView) mView.findViewById(R.id.category_image_3);
        imageView.setImageResource(resID);
        txt = (TextView) getActivity().findViewById(R.id.cat3);
        categoryName = catList.get(2).getName();
        txt.setText(categoryName);


        categoryImage = catList.get(3).getLogo();
        resID = getResources().getIdentifier(categoryImage , "drawable", MainActivity.PACKAGE_NAME);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = getResources().getDrawable(resID, mCtx.getTheme());
        } else {
            drawable = getResources().getDrawable(resID);
        }
        drawable = getResources().getDrawable(resID);
        imageView = (ImageView) mView.findViewById(R.id.category_image_4);
        imageView.setImageResource(resID);
        txt = (TextView) getActivity().findViewById(R.id.cat4);
        categoryName = catList.get(3).getName();
        txt.setText(categoryName);

    }

    // Fonction appelée par le clic sur le bouton ADD SPOT
    public void addSpot(View view) {
        Spot spot = new Spot();

        // On chope le nom du lieu dans la description
        TextView txt = (TextView) getActivity().findViewById(R.id.edit_spot_name);
        String name = txt.getText().toString();
        if (name.matches("")) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            String textDate = dateFormat.format(date).toString();
            name = "Nouveau point ajouté au " + textDate;
        }
        spot.setName(name);

        // Les coordonnées GPS sont issues d'une requete android,
        // ou de la carte Google.
        /*if (markerLocation != null) {
            Log.d("LOCATION", "Localisation issue de Google");
            spot.setLongitude(markerLocation.getLongitude());
            spot.setLatitude(markerLocation.getLatitude());
        } else if (currentLocation != null) {
            Log.d("LOCATION", "Localisation issue du GPS");
            spot.setLongitude(currentLocation.getLongitude());
            spot.setLatitude(currentLocation.getLatitude());
        }*/
        spot.setAddress("");

        SpotDB spotDB = new SpotDB(mCtx);
        spotDB.insert(spot);
        Toast.makeText(mCtx, "Le point " + name + " a bien été enregistré", Toast.LENGTH_SHORT).show();
    }

}
