package com.spots;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.spots.data.database.CategoryDB;
import com.spots.data.database.SpotDB;
import com.spots.data.model.Category;
import com.spots.data.model.Spot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// http://developer.android.com/guide/topics/location/strategies.html
// http://developer.android.com/reference/android/location/LocationManager.html#requestLocationUpdates%28java.lang.String,%20long,%20float,%20android.app.PendingIntent%29
// http://webdesignergeeks.com/mobile/android/geting-current-location-in-android-application-using-gps/
// http://www.vogella.com/tutorials/AndroidLocationAPI/article.html#locationapi

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, OnConnectionFailedListener, LocationListener {

    private Context mCtx;
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private Location currentLocation;
    String provider;

    //private BottomToolbar toolbar;

    @Override
    public void onMapReady(GoogleMap map) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        Toast.makeText(mCtx, "Coordonnées : " + location.getLatitude() + " " +
                location.getLongitude(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCtx = this;

        // Gere la carte
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Crée une entité de Google API pour pouvoir faire des requêtes à Google Place
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        // EventListener pour le Place selector
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // On remplit l'EditView avec le nom du lieu
                TextView edit = (TextView) findViewById(R.id.edit_spot_name);
                edit.setText(place.getName());

                // On recentre la carte
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 17));

                // On place un marker
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

        // LocationListener
        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
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
        ImageView imageView = (ImageView)findViewById(R.id.category_image_1);
        int resID = getResources().getIdentifier(categoryImage , "drawable", getPackageName());
        Drawable drawable = getDrawable(resID );
        imageView.setImageDrawable(drawable);
        TextView txt = (TextView) findViewById(R.id.cat1);
        txt.setText(categoryName);

        categoryName = catList.get(1).getName();
        txt = (TextView) findViewById(R.id.cat2);
        txt.setText(categoryName);

        categoryName = catList.get(2).getName();
        txt = (TextView) findViewById(R.id.cat3);
        txt.setText(categoryName);

        categoryName = catList.get(3).getName();
        txt = (TextView) findViewById(R.id.cat4);
        txt.setText(categoryName);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    public void addTestSpots(View view) {
        // Fonctionne bien si le point a été donné par Google Place API
        // TODO : idem pour un point random qu'on a mis sans recherche de Google Place.
        Spot spot = new Spot();

        // On chope le nom du lieu dans la description
        TextView txt = (TextView) findViewById(R.id.edit_spot_name);
        String name = txt.getText().toString();
        Log.d("LOCATION NAME", name);
        if (name.matches("")) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            String textDate = dateFormat.format(date).toString();
            name = "Nouveau point ajouté au " + textDate;
        }
        spot.setName(name);

        // Les coordonnées GPS sont issues d'une requete android,
        // ou de la carte Google.
        if (currentLocation != null) {
            Log.d("LOCATION", "La position courante est bien connue par l'engin");
            spot.setLongitude(currentLocation.getLongitude());
            spot.setLatitude(currentLocation.getLatitude());
        }
        spot.setAddress("");

        SpotDB spotDB = new SpotDB(mCtx);
        spotDB.insert(spot);
        Toast.makeText(mCtx, "Le point " + name + " a bien été enregistré", Toast.LENGTH_SHORT).show();
    }

    public void goToSaveSpots(View view) {
        Intent intent = new Intent(this, SavedPlaces.class);
        startActivity(intent);
    }
/*
    public void startMap() {
        // INIT MAP
        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        ArrayList<LocationProvider> providers = new ArrayList<LocationProvider>();
        ArrayList<String> names = (ArrayList)locationManager.getProviders(true);

        for(String name : names)
            providers.add(locationManager.getProvider(name));

        Criteria critere = new Criteria();
        critere.setAccuracy(Criteria.ACCURACY_FINE);
        critere.setAltitudeRequired(false);
        critere.setBearingRequired(true);
        critere.setCostAllowed(false);
        critere.setPowerRequirement(Criteria.POWER_HIGH);
        critere.setSpeedRequired(false);

        String provider = locationManager.getBestProvider(critere, true);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d("chien", "ta mère");
            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }

            @Override
            public void onLocationChanged(Location location) {
                Log.d("GPS", "Latitude " + location.getLatitude() + " et longitude " + location.getLongitude());
            }
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 90, locationListener);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
        }

    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickButtonLeft(View view) {
        ActionBar bottomToolbar = getActionBar();

        /*
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);*/
    }

}
