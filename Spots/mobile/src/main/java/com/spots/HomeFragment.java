package com.spots;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
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

public class HomeFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private String TAG = "HOME_FRAGMENT";
    private Context mCtx;
    private View mView;
    private SupportMapFragment mMapFragment;
    public SupportPlaceAutocompleteFragment mPlaceAutoFragment;
    private GoogleMap mMap;
    private CategoryDB categoryDB;
    private GoogleApiClient mGoogleApiClient;
    private LocationManager locationManager;
    private Location currentLocation;
    private Location markerLocation;
    String provider;

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

        View btn = (Button) view.findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addSpot();
            }
        });

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

        locationManager = (LocationManager) mCtx.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        markerLocation = new Location(provider);

        // Crée une entité de Google API pour pouvoir faire des requêtes à Google Place
        mGoogleApiClient = new GoogleApiClient
                .Builder(mCtx)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this.getActivity(), this)
                .build();

        // EventListener pour le Place selector
        FragmentManager fm2 = getChildFragmentManager();
        mPlaceAutoFragment = (SupportPlaceAutocompleteFragment) fm2.findFragmentById(R.id.place_autocomplete_fragment);
        if (mPlaceAutoFragment == null) {
            Log.d(TAG, "On est en train d'en remettre un...");
            mPlaceAutoFragment = new SupportPlaceAutocompleteFragment();
            fm2.beginTransaction().replace(R.id.place_autocomplete_fragment, mPlaceAutoFragment).commit();
        }
        Log.d(TAG, mPlaceAutoFragment.toString());
        mPlaceAutoFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.i(TAG, "Place: " + place.getName());

                // On remplit l'EditView avec le nom du lieu
                TextView edit = (TextView) mView.findViewById(R.id.edit_spot_name);
                edit.setText(place.getName());

                // On recentre la carte
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 17));

                // On place un marker
                Log.d("LOCATION MARKER", Double.toString(place.getLatLng().latitude));

                markerLocation.setLatitude(place.getLatLng().latitude);
                markerLocation.setLongitude(place.getLatLng().longitude);
                Marker newMarker = mMap.addMarker(new MarkerOptions()
                        .title((String) place.getName())
                        .snippet((String) place.getAddress())
                        .position(place.getLatLng()));
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("GOOGLE PLACES", "An error occurred: " + status);
            }
        });

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

        if (ActivityCompat.checkSelfPermission(mCtx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mCtx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        currentLocation = locationManager.getLastKnownLocation(provider);
        Log.d("LOCATION", currentLocation.toString());

        // On recentre la carte
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 17));

        // On place un marker
        markerLocation.setLatitude(currentLocation.getLatitude());
        markerLocation.setLongitude(currentLocation.getLongitude());
        Marker newMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())));
    }

    public void onConnectionFailed(ConnectionResult result) {
        Log.d("MAIN ACTIVITY", "Connetion Failed");
    }

    @Override
    public void onLocationChanged(Location location) {
        // Called when a new location is found by the network location provider.
        currentLocation = location;
        Log.d("ONLOCATIONCHANGED", currentLocation.toString());
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
    public void addSpot() {
        Spot spot = new Spot();

        // On chope le nom du lieu dans la description
        TextView txt = (TextView) mView.findViewById(R.id.edit_spot_name);
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
        if (markerLocation != null) {
            Log.d("LOCATION", "Localisation issue de Google");
            spot.setLongitude(markerLocation.getLongitude());
            spot.setLatitude(markerLocation.getLatitude());
        } else if (currentLocation != null) {
            Log.d("LOCATION", "Localisation issue du GPS");
            spot.setLongitude(currentLocation.getLongitude());
            spot.setLatitude(currentLocation.getLatitude());
        }
        spot.setAddress("");

        SpotDB spotDB = new SpotDB(mCtx);
        spotDB.insert(spot);
        Toast.makeText(mCtx, "Le point " + name + " a bien été enregistré", Toast.LENGTH_SHORT).show();
    }

}
