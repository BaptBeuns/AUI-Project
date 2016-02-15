package com.spots;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
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
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.spots.data.database.CategoryDB;
import com.spots.data.model.Category;

import java.util.List;

public class HomeFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private String TAG = "HOME_FRAGMENT";
    private Context mCtx;
    private GoogleApiClient mGoogleApiClient;

    private LocationManager locationManager;
    private Location currentLocation;
    private Location markerLocation;
    String provider;

    private SupportMapFragment mapFragment;
    private GoogleMap mMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        view = inflater.inflate(R.layout.map, (ViewGroup) view.findViewById(R.id.map_layout), false);
        mapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Log.d(TAG,"aaaaaaaaaa");
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*
        FragmentManager fm = getChildFragmentManager();
        mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, mapFragment).commit();
        }
        */


        mCtx = getActivity();
        locationManager = (LocationManager) this.getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        markerLocation = new Location(provider);

        // Gere la carte

        // Crée une entité de Google API pour pouvoir faire des requêtes à Google Place
        mGoogleApiClient = new GoogleApiClient
                .Builder(this.getActivity())
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this.getActivity(), this)
                .build();
/*
        // EventListener pour le Place selector
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                fm.findFragmentById(R.id.place_autocomplete_fragment));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // On remplit l'EditView avec le nom du lieu
                TextView edit = (TextView) getView().findViewById(R.id.edit_spot_name);
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
*/
        // LocationListener
        // Acquire a reference to the system Location Manager
        if (ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        currentLocation = locationManager.getLastKnownLocation(provider);
        Log.d("LOCATION", currentLocation.toString());
        // Register the listener with the Location Manager to receive location updates
        // locationManager.requestLocationUpdates(provider, 60000, 200, locationListener);

        // Récupération des catégories à afficher
        CategoryDB categoryDB = new CategoryDB(mCtx);
        List<Category> catList = categoryDB.getAll();

        String categoryName = catList.get(0).getName();
        String categoryImage = catList.get(0).getLogo();
        ImageView imageView = (ImageView) getView().findViewById(R.id.category_image_1);
        int resID = getResources().getIdentifier(categoryImage, "drawable", MainActivity.PACKAGE_NAME);
        Drawable drawable = getActivity().getDrawable(resID);
        imageView.setImageDrawable(drawable);
        TextView txt = (TextView) getView().findViewById(R.id.cat1);
        txt.setText(categoryName);

        categoryName = catList.get(1).getName();
        txt = (TextView) getView().findViewById(R.id.cat2);
        txt.setText(categoryName);

        categoryName = catList.get(2).getName();
        txt = (TextView) getView().findViewById(R.id.cat3);
        txt.setText(categoryName);

        categoryName = catList.get(3).getName();
        txt = (TextView) getView().findViewById(R.id.cat4);
        txt.setText(categoryName);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMap == null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            return;
        }
        mMap = map;
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

    public void changeResource(View button) {
        button.setSelected(!button.isSelected());

        if (button.isSelected()) {
            button.setBackgroundResource(R.drawable.round_button_selected);
        } else {
            button.setBackgroundResource(R.drawable.round_button);
        }
    }

}
