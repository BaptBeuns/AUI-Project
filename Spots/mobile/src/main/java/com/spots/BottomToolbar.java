package com.spots;

import android.app.ActivityManager;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;

public class BottomToolbar extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_toolbar);
    }

    protected void onClickButtonLeft(View view) {
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.AppTask> taskInfo = am.getAppTasks();
        Log.d("topActivity", "CURRENT Activity ::" + taskInfo.get(0).getTaskInfo().description);

        /*
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);*/
    }


}
