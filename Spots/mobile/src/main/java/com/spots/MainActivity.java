package com.spots;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

// http://developer.android.com/guide/topics/location/strategies.html
// http://developer.android.com/reference/android/location/LocationManager.html#requestLocationUpdates%28java.lang.String,%20long,%20float,%20android.app.PendingIntent%29
// http://webdesignergeeks.com/mobile/android/geting-current-location-in-android-application-using-gps/
// http://www.vogella.com/tutorials/AndroidLocationAPI/article.html#locationapi

public class MainActivity extends FragmentActivity {

    public static String PACKAGE_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PACKAGE_NAME = getApplicationContext().getPackageName();

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            SlidingTabFragment fragment = new SlidingTabFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }
    }
}

    //private BottomToolbar toolbar;
/*
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
    }
*/

/*
        mCtx = this;
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        markerLocation = new Location(provider);

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

        // LocationListener
        // Acquire a reference to the system Location Manager
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
        ImageView imageView = (ImageView) findViewById(R.id.category_image_1);
        int resID = getResources().getIdentifier(categoryImage, "drawable", getPackageName());
        Drawable drawable = getDrawable(resID);
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
        }*/
/*
    @Override
    protected void onResume() {
        super.onResume();
        // locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    public void changeResource(View button) {
        button.setSelected(!button.isSelected());

        if (button.isSelected()) {
            button.setBackgroundResource(R.drawable.round_button_selected);
        } else {
            button.setBackgroundResource(R.drawable.round_button);
        }
    }

    public void addTestSpots(View view) {
        // Fonctionne bien si le point a été donné par Google Place API
        // TODO : idem pour un point random qu'on a mis sans recherche de Google Place.
        Spot spot = new Spot();

        // On chope le nom du lieu dans la description
        TextView txt = (TextView) findViewById(R.id.edit_spot_name);
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

    public void goToSaveSpots(View view) {
        Intent intent = new Intent(this, SavedPlaces.class);
        startActivity(intent);
    }
}
*/
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
>>>>>>> develop
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==0) {
            Log.d("MAIN ACTIVITY","OnOptionsItemsSelected");
        }
        return super.onOptionsItemSelected(item);
    }

}
*/
