package com.spots;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.spots.data.database.CategoryDB;
import com.spots.data.database.SpotDB;
import com.spots.data.model.Category;
import com.spots.data.model.Spot;

import java.util.List;

public class SavedPlaces extends AppCompatActivity {

    //Database

    //List Input Data
//    String[] titlesArray = {"Rock'n Roll Bar","Atomic","Duomo Bar","Jet Café"};
//    String[] detailsArray = {"2.7 km away","300 meters away","6 km away"};
//    int [] imagesArray={R.drawable.rounded_dj,R.drawable.rounded_beer,R.drawable.rounded_sport,R.drawable.rounded_shopping_bag};

    public SavedPlaces(){
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_places);

        SpotDB spotDatabase = new SpotDB(this);
        List<Spot> spotList=spotDatabase.getAll();
        CategoryDB categoryDatabase = new CategoryDB(this);
        List<Category> categoryList=categoryDatabase.getAll();
        Log.d("ufy",spotList.toString());


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
                imagesArray[i]=getResources().getIdentifier(category.getLogo() , "drawable", getPackageName());

                Log.d("category is", Integer.toString(spot.getCategoryId()));
            }

            CustomAdapter adapter = new CustomAdapter(this, titlesArray,adressesArray,imagesArray);
            ListView listView = (ListView) findViewById(R.id.savedSpotsList);
            listView.setAdapter(adapter);
        }
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