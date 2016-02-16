package com.spots;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends Activity {

    private String TAG = "WATCH_MAIN";
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
            }
        });
    }

    public void changeResource(View button) {
        ViewGroup vg = (ViewGroup) findViewById(R.id.list_category);
        ViewGroup nextChild;
        // On assigne tous les enfants à pas selected
        for(int i=0; i<vg.getChildCount(); ++i) {
            nextChild = (ViewGroup) vg.getChildAt(i);
            nextChild.setSelected(false);
        }
        button.setSelected(true);
    }
}
