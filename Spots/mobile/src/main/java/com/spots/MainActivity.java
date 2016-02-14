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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.spots.data.database.CategoryDB;
import com.spots.data.database.SpotDB;
import com.spots.data.model.Category;
import com.spots.data.model.Spot;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Context mCtx;
    //private BottomToolbar toolbar;

    @Override
    public void onMapReady(GoogleMap map) {
        LatLng sydney = new LatLng(-33.867, 151.206);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
            return;
        }

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));

        map.addMarker(new MarkerOptions()
                .title("Sydney")
                .snippet("Bonne chienne de Sydney.")
                .position(sydney));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCtx = this;

        //toolbar = (BottomToolbar)findViewById(R.id.toolbar_bottom);

        // Gere la carte
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        CategoryDB categoryDB = new CategoryDB(mCtx);
        List<Category> catList = categoryDB.getAll();

        String categoryName = catList.get(0).getName();
        String categoryImage = catList.get(0).getLogo();
        Log.d("Return from Database",categoryImage);
        ImageView imageView = (ImageView)findViewById(R.id.category_image_1);
        Log.d("Return from Database",getPackageName());
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

        ImageView imagev = (ImageView) findViewById(R.id.imageCategory);
        String mDrawableName = "handbag";
        resID = getResources().getIdentifier(mDrawableName , "drawable", getPackageName());
        imagev.setImageResource(resID);

    }

    public void addTestSpots(View view) {
        Spot duomo = new Spot();
        duomo.setName("Duomo");
        duomo.setLongitude(45.4640976);
        duomo.setLatitude(9.191926500000022);
        duomo.setAddress("Piazza del Duomo, Milano, Italy");

        SpotDB spotDB = new SpotDB(mCtx);
        spotDB.insert(duomo);
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
