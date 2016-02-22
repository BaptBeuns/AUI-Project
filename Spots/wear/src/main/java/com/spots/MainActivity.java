package com.spots;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.Wearable;
import com.spots.data.database.CategoryDB;
import com.spots.data.model.Category;

import java.util.List;

public class MainActivity extends Activity implements DataApi.DataListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private String TAG = "MAIN_ACTIVITY";
    private TextView mTextView;
    LinearLayout categoryLayout;
    private Context mCtx;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
                categoryLayout = (LinearLayout) stub.findViewById(R.id.categoryLayout);
                mCtx = getApplicationContext();
                // We fill the list of categories with all our categories
                fillCategoryList();
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
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
        Wearable.DataApi.addListener(mGoogleApiClient, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Wearable.DataApi.removeListener(mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void fillCategoryList() {

        Category category;
        CategoryDB categoryDB = new CategoryDB(mCtx);
        List<Category> categoryList = categoryDB.getAll();
        String[] namesArray = new String[categoryList.size()];
        int[] imagesArray = new int[categoryList.size()];

        for (int i = 0; i < categoryList.size(); i++) {
            category = categoryList.get(i);
            namesArray[i] = category.getName();
            imagesArray[i] = getResources().getIdentifier(category.getLogo(),"drawable",getPackageName());
        }

        CategoryListAdapter adapter = new CategoryListAdapter(this, mCtx, imagesArray, namesArray);
        final int adapterCount = adapter.getCount();
        for (int i = 0; i < adapterCount; i++) {
            View item = adapter.getView(i, null, null);
            categoryLayout.addView(item);
        }
    }


}
