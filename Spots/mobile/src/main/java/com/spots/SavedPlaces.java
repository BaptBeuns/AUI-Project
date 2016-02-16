package com.spots;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewDebug;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.spots.data.database.CategoryDB;
import com.spots.data.database.SpotDB;
import com.spots.data.model.Category;
import com.spots.data.model.Spot;

import java.util.List;

public class SavedPlaces extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_places);

        SpotDB spotDatabase = new SpotDB(this);
        List<Spot> spotList=spotDatabase.getAll();
        CategoryDB categoryDatabase = new CategoryDB(this);
        List<Category> categoryList=categoryDatabase.getAll();

        System.out.println(spotList);
        if (spotList.size()>0) {

            String[] titlesArray = new String[spotList.size()];
            String[] adressesArray = new String[spotList.size()];
            int[] imagesArray = new int[spotList.size()];

            for (int i=0;i<titlesArray.length;i++) {

                Spot spot=spotList.get(i);
                Category category= categoryList.get(spot.getCategoryId());

                titlesArray[i]=spot.getName();
                adressesArray[i]=spot.getAddress();
                imagesArray[i]=getResources().getIdentifier(category.getLogo(), "drawable", getPackageName());

                Log.d("category is", Integer.toString(spot.getCategoryId()));
            }

            CustomAdapter adapter = new CustomAdapter(this, this, titlesArray,adressesArray,imagesArray);
            final ListView listView = (ListView) findViewById(R.id.savedSpotsList);
            listView.setAdapter(adapter);
            listView.setItemsCanFocus(true);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Object listItem = listView.getItemAtPosition(position);
                    Log.d("Nique ta mere", "t'auras des freres");
                }
            });
        }
    }

    public void modelInfo(int pos) {
        Log.i("modelInfo", "=" + pos);
    }

    public void openBottomSheet (View v) {

        View view = getLayoutInflater ().inflate (R.layout.bottom_sheet, null);
        TextView txtBackup = (TextView)view.findViewById( R.id.txt_backup);
        TextView txtDetail = (TextView)view.findViewById( R.id.txt_detail);
        TextView txtOpen = (TextView)view.findViewById( R.id.txt_open);
//        final TextView txtUninstall = (TextView)view.findViewById( R.id.txt_uninstall);

        final Dialog mBottomSheetDialog = new Dialog(SavedPlaces.this,
                R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView (view);
        mBottomSheetDialog.setCancelable (true);
        mBottomSheetDialog.getWindow ().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow ().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show ();


        txtBackup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(SavedPlaces.this, "Clicked Open in Google Maps", Toast.LENGTH_SHORT).show();
                mBottomSheetDialog.dismiss();
                Uri gmmIntentUri = Uri.parse("geo:37.7749,-122.4194");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });

        txtDetail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(SavedPlaces.this,"Clicked Open in Citymapper",Toast.LENGTH_SHORT).show();
                mBottomSheetDialog.dismiss();
            }
        });

        txtOpen.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(SavedPlaces.this, "Clicked Delete Spot", Toast.LENGTH_SHORT).show();
                mBottomSheetDialog.dismiss();
            }
        });


    }

    public void displayGoogleMapsIntentForSpotAtIndex(int spotIndex){

//        //Get Spot
//        SpotDB database = new SpotDB(this);
//        List<Spot> spotList=database.getAll();
//        Spot selectedSpot=spotList.get(spotIndex);
//
//        //Build location String
//        String latitude=Double.toString(selectedSpot.getLatitude());
//        String longitude=Double.toString(selectedSpot.getLongitude());
//        String locationString = "geo"+latitude+","+longitude;
//        Uri gmmIntentUri = Uri.parse(locationString);

        //Call GoogleMaps Intent
        Uri gmmIntentUri = Uri.parse("geo:37.7749,-122.4194");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }


}