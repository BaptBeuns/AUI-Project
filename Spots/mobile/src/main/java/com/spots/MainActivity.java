package com.spots;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.AddPlaceRequest;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.spots.data.database.CategoryDB;
import com.spots.data.database.SpotDB;
import com.spots.data.model.Category;
import com.spots.data.model.Spot;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/*

FILE ORGANISATION

1. VARIABLES
2. ACTIVITY LIFECYCLE
3. RETRIEVE SETTINGS
4. BUILD CONNECT AND DISCONNECT GOOGLE API CLIENTS
5. LOCATION REQUEST
6. LOCATION MANAGEMENT
7. MAP
8. GEOCODER
9. GOOGLE PLACES
10. PLACE PICKER
11. STORE SETTINGS
12. WEAR

*/

public class MainActivity extends FragmentActivity implements OnMapReadyCallback,
        ConnectionCallbacks, OnConnectionFailedListener,
        LocationListener, LocationSource, ResultCallback<LocationSettingsResult> {

    //TODO : GET 10 OR 20 MARKERS AROUNDS TO MAKE A FITBOUNDS ON MAP OPENING
    // + DISPLAY CURRENT ADDRESS
    // + COMMUNICATION WEAR

    // GLOBAL ACTIVITY's VARIABLES
    private String TAG = "MOBILE_MAIN_ACTIVITY";
    private static final String START_ACTIVITY = "/main_activity";
    private static final String WEAR_MESSAGE_PATH = "/message";
    public static String PACKAGE_NAME;

    // LOCATION AND MAP RELATED VARIABLES
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    private GoogleApiClient mWearableGoogleApiClient;
    private GoogleApiClient mLocationGoogleApiClient;
    private GoogleApiClient mGooglePlaceApiClient;
    private GoogleApiClient indexAPIClient;
    protected Boolean mRequestingLocationUpdates;
    private LocationRequest mLocationRequest;
    private OnLocationChangedListener mListener;
    private LocationSettingsRequest mLocationSettingsRequest;
    private Location mCurrentLocation;
    private AddressResultReceiver mAddressResultReceiver;
    private String mAddressOutput;
    private boolean alreadySetLocationAddress;
    private GoogleMap mMap;
    private Marker markerLocation;

    // KEYS FOR STORING ACTIVITY STATE IN THE BUNDLE
    protected final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    protected final static String KEY_LOCATION = "location";

    // LAYOUT ACTIVITY's VARIABLES
    private LinearLayout    categoryLayout;
    private TextView        descriptionTextView;
    private Button          addSpotButton;
    protected static int    TopBarHeight;

    // CATEGORIES VARIABLES
    private CategoryListAdapter adapter;
    private int categoryNumber;
    private List<Category> inMemoryCategoryList;
    private List<Category> wearCategoryList;

    // GOOGLE PLACES VARIABLES
    private int firstUpdated = 0;
    private List<LatLng> placesNearby;
    private Bitmap nearbyPinBitmap;
    private boolean placeSelected = false;

    // WEAR COMMUNICATION VARIABLES
    private static final String CATEGORY_LIST_PATH = "/category_list";
    private static final String SPOT_ADD_SUCCESS_PATH = "/spot_add_success";

  /*
    ***************************************************************************************************
                                        ACTIVITY LIFECYCLE
    ***************************************************************************************************
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Context mCtx = getApplicationContext();
        PACKAGE_NAME = mCtx.getPackageName();

        // UPDATE SETTINGS FROM BUNDLE
        updateValuesFromBundle(savedInstanceState);

        // BUILD, CONNECT AND DISCONNECT GOOGLE API CLIENTS
        mRequestingLocationUpdates = false;
        buildGoogleApiClient();

        // SET AND START LOCATION REQUESTS
        setupLocationRequest();
        buildLocationSettingsRequest();
        checkLocationSettings();
        mAddressOutput = "";
        alreadySetLocationAddress = false;

        // HANDLE AND UPDATE LAYOUT
        setUpGooglePlaces();
        categoryLayout = (LinearLayout) findViewById(R.id.categoryLayout);
        CategoryDB categoryDB = new CategoryDB(mCtx);
        inMemoryCategoryList = categoryDB.getAll();
        categoryDB.close();
        wearCategoryList = new ArrayList<>();
        categoryNumber = inMemoryCategoryList.size();

        this.fillCategoryList(this, mCtx, categoryLayout);
        descriptionTextView = (TextView) findViewById(R.id.edit_spot_name);
        addSpotButton = (Button) findViewById(R.id.add_spot_button);
        addSpotButton.setEnabled(false);

        placesNearby = new ArrayList<LatLng>();
        setNearbyPin();
        initWear();
    }

    public void addSpotClicked(View view) {
        int categoryId = 9;
        double latitude = 0, longitude = 0;
        String name, address = "";
        // On chope le nom du lieu dans la description
        TextView txt = (TextView) findViewById(R.id.edit_spot_name);
        name = txt.getText().toString();

        if (placeSelected) {
            Log.d("LOCATION", "Localisation issue de Google");
            longitude = markerLocation.getPosition().longitude;
            latitude = markerLocation.getPosition().latitude;
        } else if (mCurrentLocation != null) {
            Log.d("LOCATION", "Localisation issue du GPS");
            longitude = mCurrentLocation.getLongitude();
            latitude = mCurrentLocation.getLatitude();
        }
        if (adapter.lastSelectedSpotIndex != -1)
            categoryId = adapter.lastSelectedSpotIndex;
        if (mAddressOutput != null) {
            address = mAddressOutput;
        }
        Location location = new Location("no use");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        addSpot(location, address, name, categoryId);
    }


    public void addSpot(Location location, String name, String address, int categoryID) {
        Spot spot = new Spot();
        SpotDB spotDB = new SpotDB(getApplicationContext());

        if (name.matches("")) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            String textDate = dateFormat.format(date).toString();
            name = "Spot added on " + textDate;
        }
        spot.setName(name);
        if(location != null) {
            spot.setLatitude(location.getLatitude());
            spot.setLongitude(location.getLongitude());
        } else
            return;
        if(address != null) {
            spot.setAddress(address);
        } else
            spot.setAddress("");
        if(categoryID>=0 && categoryID<categoryNumber) {
            spot.setCategoryId(categoryID);
        } else
            spot.setCategoryId(0);
        spotDB.insert(spot);

        addSpotButton.setText("SPOT ADDED!");
        addSpotButton.setBackgroundResource(R.drawable.spot_added_button);
        addSpotButton.setEnabled(false);
        Runnable hide = new Runnable() {
            @Override
            public void run() {
                addSpotButton.setText("ADD SPOT");
                addSpotButton.setBackgroundResource(R.drawable.add_button_selector);
                addSpotButton.setEnabled(true);
            }
        };
        addSpotButton.postDelayed(hide,3000);
    }


    public void fillCategoryList(Activity act, Context mCtx, LinearLayout linearLayout) {
        Category category;
        CategoryDB categoryDB = new CategoryDB(mCtx);
        List<Category> categoryList = categoryDB.getAll();
        String[] namesArray = new String[categoryList.size()];
        int[] imagesArray = new int[categoryList.size()];

        for (int i = 0; i < categoryList.size(); i++) {
            category = categoryList.get(i);
            namesArray[i] = category.getName();
            imagesArray[i] = mCtx.getResources().getIdentifier(category.getLogo(), "drawable", MainActivity.PACKAGE_NAME);
        }

        adapter = new CategoryListAdapter(act, mCtx, imagesArray, namesArray);

        final int adapterCount = adapter.getCount();
        for (int i = 0; i < adapterCount; i++) {
            View item = adapter.getView(i, null, null);
            linearLayout.addView(item);
        }
        categoryDB.close();
    }




    @Override
    protected void onStart() {
        super.onStart();
        mLocationGoogleApiClient.connect();
    }


    @Override
    public void onResume() {
        super.onResume();
        setUpMap();
        if (mLocationGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mLocationGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }


    @Override
    protected void onStop() {
        mLocationGoogleApiClient.disconnect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.spots/http/host/path")
        );
        AppIndex.AppIndexApi.end(indexAPIClient, viewAction);
        indexAPIClient.disconnect();
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



    /*
    ***************************************************************************************************
                                        RETRIEVE SETTINGS
    ***************************************************************************************************
     */

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
            // the Start Updates and Stop Updates buttons are correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(KEY_REQUESTING_LOCATION_UPDATES)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        KEY_REQUESTING_LOCATION_UPDATES);
            }
            // Update the value of mCurrentLocation from the Bundle and update the UI to show the
            // correct latitude and longitude.
            if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
                // Since KEY_LOCATION was found in the Bundle, we can be sure that mCurrentLocation
                // is not null.
                mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            }
            updateMapLayout(mCurrentLocation);
        }
    }


    /*
    ***************************************************************************************************
                                BUILD CONNECT AND DISCONNECT GOOGLE API CLIENTS
    ***************************************************************************************************
     */

    private void buildGoogleApiClient() {
        mWearableGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        mWearableGoogleApiClient.connect();

        // GOOGLE PLACES API CLIENT INIT
        mGooglePlaceApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
        mGooglePlaceApiClient.connect();

        // LOCATION API CLIENT INIT
        if (mLocationGoogleApiClient == null) {
            mLocationGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        // INDEX API CLIENT INIT
        indexAPIClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        indexAPIClient.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.spots/http/host/path")
        );
        AppIndex.AppIndexApi.start(indexAPIClient, viewAction);
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (mCurrentLocation == null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                buildLocationSettingsRequest();
                return;
            }
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mLocationGoogleApiClient);
            startLocationUpdates();
        }
    }


    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }


    /*
    ***************************************************************************************************
                                            LOCATION REQUEST
    ***************************************************************************************************
     */

    protected void checkLocationSettings() {
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mLocationGoogleApiClient,
                        mLocationSettingsRequest
                );
        result.setResultCallback(this);
    }


    protected void setupLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }


    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                Log.i(TAG, "All location settings are satisfied.");
                startLocationUpdates();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to" +
                        "upgrade location settings ");
                try {
                    status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    Log.i(TAG, "PendingIntent unable to execute request.");
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog " +
                        "not created.");
                break;
        }
    }

    // Triggered when user's comes back from settings : after startResolutionForResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes.");
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(TAG, "User chose not to make required location settings changes.");
                        break;
                }
                break;
        }
    }


    /*
    ***************************************************************************************************
                                                 LOCATION MANAGEMENT
    ***************************************************************************************************
     */

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            buildLocationSettingsRequest();
            return;
        }
        LocationServices.FusedLocationApi
                .requestLocationUpdates(mLocationGoogleApiClient, mLocationRequest, this)
                .setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                mRequestingLocationUpdates = true;
            }
        });
        addSpotButton.setEnabled(true);
    }


    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        if (mListener != null) {
            mListener.onLocationChanged(location);
        }
        if(mLocationGoogleApiClient.isConnected() && mCurrentLocation != null) {
            startIntentService();
        }

        if(firstUpdated == 0) {
            StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            sb.append("location="+mCurrentLocation.getLatitude()+","+mCurrentLocation.getLongitude());
            sb.append("&radius=80");
            sb.append("&sensor=true");
            sb.append("&key=AIzaSyBAhBPWq4wUOxKSPbG9mEOb2nplEARqS6M");

            // Creating a new non-ui thread task to download json data
            PlacesTask placesTask = new PlacesTask();
            placesTask.execute(sb.toString());
        } else
            updateMapLayout(location);
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
    }

    @Override
    public void deactivate() {
        mListener = null;
    }


    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mLocationGoogleApiClient,
                (com.google.android.gms.location.LocationListener) this
        ).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                mRequestingLocationUpdates = false;
            }
        });
    }


    /*
    ***************************************************************************************************
                                               MAP
    ***************************************************************************************************
     */

    private void setUpMap() {
        if (mMap == null) {
            MapFragment mapFragment = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }


    // Triggered by getMapAsync
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            buildLocationSettingsRequest();
            return;
        }
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setMyLocationEnabled(true);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                placeSelected = true;
                markerLocation = marker;
                String delim  = "[:]";
                String[] params = marker.getTitle().split(delim);
                descriptionTextView.setText(params[0]);
                return true;
            }
        });
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener(){
            @Override
            public boolean onMyLocationButtonClick() {
                descriptionTextView.setText(mAddressOutput);
                placeSelected = false;
                return false;
            }
        });
    }


    public void updateMapLayout(Location location) {
        if (location != null) {
            if (placesNearby != null) {
                if(placesNearby.size() > 0 && firstUpdated == 0) {
                    LatLngBounds.Builder bounds = new LatLngBounds.Builder();
                    for (LatLng latLng : placesNearby) {
                        bounds.include(latLng);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 10));
                    }
                    firstUpdated = 1;
                }
            } else {
                LatLngBounds bounds = this.mMap.getProjection().getVisibleRegion().latLngBounds;
                if (!bounds.contains(new LatLng(location.getLatitude(), location.getLongitude()))) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                }
            }
        }
    }

    /*
    ***************************************************************************************************
                                   GEOCODER : FROM LOCATION TO ADDRESS
    ***************************************************************************************************
     */

    protected void startIntentService() {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        mAddressResultReceiver = new AddressResultReceiver(new Handler());
        intent.putExtra(FetchAddressIntentService.Constants.RECEIVER, mAddressResultReceiver);
        intent.putExtra(FetchAddressIntentService.Constants.LOCATION_DATA_EXTRA, mCurrentLocation);
        startService(intent);
    }

    @SuppressLint("ParcelCreator")
    class AddressResultReceiver extends ResultReceiver {

        private AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultCode == FetchAddressIntentService.Constants.SUCCESS_RESULT) {
                mAddressOutput = resultData.getString(FetchAddressIntentService.Constants.RESULT_DATA_KEY);
            } else { mAddressOutput = "Address not found"; }
            if (!placeSelected && !alreadySetLocationAddress) {
                descriptionTextView.setText(mAddressOutput);
                alreadySetLocationAddress = true;
            }
        }
    }



    /*
    ***************************************************************************************************
                                               GOOGLE PLACES
    ***************************************************************************************************
     */

    private void setUpGooglePlaces() {
        // eventListener for the Place selector
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // We fill the description with the name of the place
                placeSelected = true;
                descriptionTextView.setText(place.getName());
                mAddressOutput = (String)place.getAddress();
                // We center the map on the selected place
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 17));

                // We put a marker on it
                markerLocation = mMap.addMarker(new MarkerOptions()
                        .title((String) place.getName())
                        .snippet((String) place.getAddress())
                        .position(place.getLatLng())
                        .icon(BitmapDescriptorFactory.fromBitmap(nearbyPinBitmap)));
            }

            @Override
            public void onError(Status status) {
                Log.i("GOOGLE PLACES", "An error occurred: " + status);
            }
        });
    }

    /*
    ***************************************************************************************************
                                        PLACE PICKER
    ***************************************************************************************************
     */

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }
            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception dwnld url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }

        return data;
    }

    /** A class, to download Google Places */
    private class PlacesTask extends AsyncTask<String, Integer, String> {

        String data = null;

        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(String... url) {
            try{
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String result){
            ParserTask parserTask = new ParserTask();

            // Start parsing the Google places in JSON format
            // Invokes the "doInBackground()" method of the class ParseTask
            parserTask.execute(result);
        }

    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{

        JSONObject jObject;

        // Invoked by execute() method of this object
        @Override
        protected List<HashMap<String,String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;
            PlaceJSONParser placeJsonParser = new PlaceJSONParser();
            try{
                jObject = new JSONObject(jsonData[0]);

                /** Getting the parsed data as a List construct */
                places = placeJsonParser.parse(jObject);

            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            return places;
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(List<HashMap<String,String>> list){

            // Clears all the existing markers
            //mMap.clear();
            if(list == null) {
                Log.d(TAG,"Null places list");
                return;
            }
            if(placesNearby != null) {
                placesNearby.clear();
            }
            for(int i=0;i<list.size();i++){
                MarkerOptions markerOptions = new MarkerOptions();
                HashMap<String, String> hmPlace = list.get(i);

                double lat = Double.parseDouble(hmPlace.get("lat"));
                double lng = Double.parseDouble(hmPlace.get("lng"));
                String name = hmPlace.get("place_name");
                String vicinity = hmPlace.get("vicinity");
                LatLng latLng = new LatLng(lat, lng);
                float[] distance = new float[2];
                Location.distanceBetween(mCurrentLocation.getLatitude(),mCurrentLocation.getLongitude(),lat,lng,distance);
                    if(distance[0] < 100) {
                        placesNearby.add(latLng);
                        markerOptions.position(latLng);
                        markerOptions.title(name + " : " + vicinity);
                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(nearbyPinBitmap));
                        mMap.addMarker(markerOptions);
                    }
            }
            updateMapLayout(mCurrentLocation);
        }
    }


    public void setNearbyPin() {
        int width = getResources().getDimensionPixelSize(R.dimen.pin_width);
        int height = getResources().getDimensionPixelSize(R.dimen.pin_height);
        nearbyPinBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(nearbyPinBitmap);
        Drawable shape = ContextCompat.getDrawable(this,R.drawable.pin);
        shape.setBounds(0, 0, nearbyPinBitmap.getWidth(), nearbyPinBitmap.getHeight());
        shape.draw(canvas);
    }

    /*
    ***************************************************************************************************
                                        STORE SETTINGS
    ***************************************************************************************************
     */

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(KEY_REQUESTING_LOCATION_UPDATES, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(KEY_LOCATION, mCurrentLocation);
        super.onSaveInstanceState(savedInstanceState);
    }



    /*
    ***************************************************************************************************
                                        WEAR
    ***************************************************************************************************
    */

    private void initWear() {
        IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
        MessageReceiver messageReceiver = new MessageReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, messageFilter);
    }

    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            String path = intent.getStringExtra("path");
            // Display message in UI
            Toast.makeText(context, "Wear Sent : "+message, Toast.LENGTH_LONG).show();

            if(path.equals("/add_spot")) {
                String delims = "[:]";
                String[] params = message.split(delims);
                if(params.length > 3) {
                    double latitude=Double.parseDouble(params[1]);
                    double longitude=Double.parseDouble(params[3]);
                    Location location = new Location("no use");
                    addSpot(location,"Added by Wear","",0);
                    new SenderThread(SPOT_ADD_SUCCESS_PATH,"");
                }
                else
                    Log.d(TAG,"Error in location parse");
            }
            // WATCH SENT ITS CATEGORIES
            /*
            else if (path.equals("/category_list")) {
                Log.d(TAG,"debug1");
                wearCategoryList.clear();
                String delim = "[;]";
                String[] categories = message.split(delim);
                // RETRIEVE WEAR'S DATA
                for(String category : categories) {
                    Log.d(TAG,"debug2");
                    String delim2 = "[,]";
                    String[] args = category.split(delim2);
                    Category cat = new Category();
                    cat.setName(args[0]);
                    cat.setLogo(args[1]);
                    wearCategoryList.add(cat);
                }
                // COMPARE IT WITH HANDLED'S ONE
                int i = 0;
                for(Category handledCat : inMemoryCategoryList) {
                    Log.d(TAG,"debug3");
                    if (wearCategoryList.get(i) != null) {
                        Category wearCat = (Category)wearCategoryList.get(i);
                        if(!wearCat.getName().equals(handledCat.getName()) || !wearCat.getLogo().equals(handledCat.getLogo())) {
                            updateWearCategories();
                            return;
                        }
                    } else {
                        updateWearCategories();
                        return;
                    }
                    i++;
                }
                new SenderThread(CATEGORY_LIST_PATH,"UTD");
            }*/
        }
    }

    private void updateWearCategories() {
        String categories = "";
        for(Category cat:inMemoryCategoryList) {
            categories = categories + cat.getName() + "," + cat.getLogo() + ";";
        }
        new SenderThread(CATEGORY_LIST_PATH,categories);
    }

    public class SenderThread extends Thread {

        String path;
        String message;

        public SenderThread(String p, String msg) {
            path = p;
            message = msg;
        }

        public void run() {
            Log.d(TAG,"Sender Thread for msg : "+message);
            NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mWearableGoogleApiClient).await();
            for (Node node : nodes.getNodes()) {
                MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(mWearableGoogleApiClient, node.getId(), path, message.getBytes()).await();
                if (result.getStatus().isSuccess()) {
                    Log.v(TAG, "Message: {" + message + "} sent to: " + node.getDisplayName());
                }
                else {
                    // Log an error
                    Log.v(TAG, "ERROR: failed to send Message");
                }
            }
        }
    }

}
