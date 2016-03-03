package com.spots;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by Jean on 19/02/16.
 */
public class HandledListenerService extends WearableListenerService {

    private String TAG = "SPOTS_LISTENER_SERVICE_WEAR_SIDE";
    private static final String START_ACTIVITY_PATH = "/main_activity";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d(TAG,"onMessageReceived");
        if(messageEvent.getPath().equalsIgnoreCase(START_ACTIVITY_PATH)) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            super.onMessageReceived(messageEvent);
        }
    }

}
