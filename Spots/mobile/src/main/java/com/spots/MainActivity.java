package com.spots;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.spots.data.database.BaseDB;
import com.spots.data.database.SpotDB;
import com.spots.data.model.Spot;

import java.util.List;


public class MainActivity extends Activity {

    private Context mCtx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCtx = this;

        Spot bar = new Spot();
        bar.setName("Birrificio Lambrate");

        SpotDB spotDB = new SpotDB(mCtx);
        spotDB.insert(bar);

        List<Spot> barList = spotDB.getAll();
        int size = barList.size();
        Log.d("MainActivity : TEST SIZE ",Integer.toString(size));
    }

    public void clickFunction(View view) {
        String textToPrint = "Sale pute";

        TextView t=(TextView)findViewById(R.id.text_test);
        t.setText(textToPrint);
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
