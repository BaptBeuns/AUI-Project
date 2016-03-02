package com.spots;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.spots.data.database.CategoryDB;


public class MainActivity extends Activity implements DataApi.DataListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    // GLOBAL VARIABLES
    private String TAG = "WEAR_MAIN_ACTIVITY";
    public static String PACKAGE_NAME;
    private Context mCtx;

    // LAYOUT VARIABLES
    private TextView mTextView;
    public LinearLayout categoryLayout;
    private Button addSpotButton;

    // LOCATION VARIABLES
    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    // HANDLED COMMUNICATION
    private static final String ADD_SPOT_PATH = "/add_spot";
    private static final String REQUEST_CATEGORIES_PATH = "/request_categories";

    // MODEL VARIABLES
    private String[]categoryName = {"Eat","Drink"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PACKAGE_NAME = getPackageName();
        final Activity mainActivity = this;

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
                categoryLayout = (LinearLayout) stub.findViewById(R.id.categoryLayout);
                addSpotButton = (Button) stub.findViewById(R.id.add_spot_button);
                addSpotButton.setEnabled(false);
                mCtx = getApplicationContext();
                // We fill the list of categories with all our categories
                CategoryDB catDB = new CategoryDB(mCtx);
                catDB.fillCategoryList(mainActivity, mCtx, categoryLayout);
                catDB.close();
            }
        });

        initHandledReceiver();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "on connected");
        Wearable.DataApi.addListener(mGoogleApiClient, this);
        // Create the LocationRequest object
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        locationRequest.setSmallestDisplacement(2);

        // Register listener using the LocationRequest object
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(mCtx, "Geolocation Disabled on device", Toast.LENGTH_LONG).show();
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);

        new SenderThread(REQUEST_CATEGORIES_PATH,categoryName.toString());
    }

    @Override
    protected void onPause() {
        Log.d(TAG,"on pause");
        super.onPause();
        Wearable.DataApi.removeListener(mGoogleApiClient, this);
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG,"on connection suspended");
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG,"on connection failed");
    }


    /*
    ***************************************************************************************************
                                            LOCATION MANAGEMENT
    ***************************************************************************************************
     */

    @Override
    public void onLocationChanged(Location location) {
        mTextView.setText("Location Detected!");
        addSpotButton.setEnabled(true);
        mCurrentLocation = location;
    }


    /*
    ***************************************************************************************************
                                            ADD SPOT
    ***************************************************************************************************
     */

    public void addSpot(View view) {
        if(mCurrentLocation != null) {
            String message = "Add Spot Lat:"+mCurrentLocation.getLatitude()+":Long:"+mCurrentLocation.getLongitude();
            new SenderThread(ADD_SPOT_PATH,message).start();
        }
    }


    public class SenderThread extends Thread {

        String path;
        String message;

        // Constructor to send a message to the data layer
        public SenderThread(String p, String msg) {
            path = p;
            message = msg;
        }

        public void run() {
            NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
            for (Node node : nodes.getNodes()) {
                MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), path, message.getBytes()).await();
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

    /*
    ***************************************************************************************************
                                        HANDLED
    ***************************************************************************************************
    */

    private void initHandledReceiver() {
        IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
        MessageReceiver messageReceiver = new MessageReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, messageFilter);
    }

    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            // Display message in UI
            Toast.makeText(context, "Mobile Sent : "+message, Toast.LENGTH_LONG).show();
            String delims = "[,]";
            String[] params = message.split(delims);
            Log.d(TAG,params[0]);
        }
    }

}
