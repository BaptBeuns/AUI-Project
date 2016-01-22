package com.spots;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.spots.data.database.BaseDB;
import com.spots.data.database.CategoryDB;
import com.spots.data.database.SpotDB;
import com.spots.data.model.Category;
import com.spots.data.model.Spot;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    private Context mCtx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCtx = this;

        CategoryDB categoryDB = new CategoryDB(mCtx);
        List<Category> catList = categoryDB.getAll();

        String categoryName = catList.get(0).getName();
        TextView txt = (TextView)findViewById(R.id.cat1);
        txt.setText(categoryName);
        categoryName = catList.get(1).getName();
        txt = (TextView)findViewById(R.id.cat2);
        txt.setText(categoryName);
        categoryName = catList.get(2).getName();
        txt = (TextView)findViewById(R.id.cat3);
        txt.setText(categoryName);
        categoryName = catList.get(3).getName();
        txt = (TextView)findViewById(R.id.cat4);
        txt.setText(categoryName);


        /*
        SpotDB spotDB = new SpotDB(mCtx);
        List<Spot> spotList = spotDB.getAll();
        List<String> spotNameList = new ArrayList<>();

        if (spotList != null) {
            for (Spot spot : spotList) {
                spotNameList.add(spot.getName());
            }
            ListView listView = (ListView)findViewById(R.id.list_category);
            listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1 , spotNameList));
        }
        */
    }

    public void addTestSpots(View view) {
        Spot duomo = new Spot();
        duomo.setName("Duomo");
        duomo.setLongitude(45.4640976);
        duomo.setLatitude(9.191926500000022);
        duomo.setAddress("Piazza del Duomo, Milano, Italy");

        SpotDB spotDB = new SpotDB(mCtx);
        spotDB.insert(duomo);
    }

    public void goToSaveSpots(View view) {
        Intent intent = new Intent(this, SavedPlaces.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
