package com.spots;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.spots.data.database.SpotDB;
import com.spots.data.model.Spot;

import java.util.ArrayList;
import java.util.List;


public class SavedPlaces extends ActionBarActivity {

    Context mCtx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_places);
        mCtx = this;

        SpotDB spotDB = new SpotDB(mCtx);
        List<Spot> spotList = spotDB.getAll();
        List<String> spotNameList = new ArrayList<>();
        List<String> spotAddressList = new ArrayList<>();

        if (spotList != null) {
            for (Spot spot : spotList) {
                spotNameList.add(spot.getName());
                spotAddressList.add(spot.getAddress());
            }
            ListView listView = (ListView)findViewById(R.id.spotList);
            listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1 , spotNameList));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_saved_places, menu);
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
}
