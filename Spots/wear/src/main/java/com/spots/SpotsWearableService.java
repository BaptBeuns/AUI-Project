package com.spots;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by Jean on 19/02/16.
 */
public class SpotsWearableService extends WearableListenerService {

    private String TAG = "SPOTS_WEARABLE_SERVICE";
    private static final String START_ACTIVITY = "/main_activity";
    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d(TAG,"onMessageReceived");
        if(messageEvent.getPath().equalsIgnoreCase(START_ACTIVITY)) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            super.onMessageReceived(messageEvent);
        }
    }

}
