package com.spots;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by Jean on 19/02/16.
 */
public class HandledListenerService extends WearableListenerService {

    private String TAG = "SPOTS_LISTENER_SERVICE_WEAR_SIDE";
    private static final String CATEGORY_LIST_PATH = "/category_list";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.v(TAG, "Message path received on watch is: " + messageEvent.getPath());
        if (messageEvent.getPath().equals(CATEGORY_LIST_PATH)) {
            final String message = new String(messageEvent.getData());
            Log.v(TAG, "Message received on watch is: " + message);

            Intent messageIntent = new Intent();
            messageIntent.setAction(Intent.ACTION_SEND);
            messageIntent.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
        }
        else {
            super.onMessageReceived(messageEvent);
        }
    }

}
