package com.spots;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.spots.data.database.CategoryDB;
import com.spots.data.database.SpotDB;
import com.spots.data.model.Category;
import com.spots.data.model.Spot;

import java.util.List;

public class SavedSpotsActivity extends AppCompatActivity {

    private static String TAG = "SAVED_SPOTS_ACTIVITY";
    private FragmentPagerAdapter adapterViewPager;
    private SavedSpotsFragment savedSpotsFragment;
    private Context mCtx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_spots);
        mCtx = this;

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            savedSpotsFragment = new SavedSpotsFragment();
            transaction.replace(R.id.sample_content_fragment, savedSpotsFragment);
            transaction.commit();
        }
    }

    public void openBottomSheet(View v, final String elementTitle, final int id,final double elementLatitude, final double elementLongitude) {
        View view = getLayoutInflater().inflate (R.layout.bottom_sheet, null);
        TextView txtBackup = (TextView)view.findViewById( R.id.txt_backup);
        TextView txtDetail = (TextView)view.findViewById( R.id.txt_detail);
        TextView txtOpen = (TextView)view.findViewById( R.id.txt_open);

        final Dialog mBottomSheetDialog = new Dialog(this,
                R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow ().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow ().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();


        txtBackup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Clicked Open in Google Maps", Toast.LENGTH_SHORT).show();
                mBottomSheetDialog.dismiss();
                String stringLongitude = Double.toString(elementLongitude);
                String stringLatitude = Double.toString(elementLatitude);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:"+stringLatitude+","+stringLongitude+"?q="+stringLatitude+","+stringLongitude+"("+elementTitle+")"));
                intent.setPackage("com.google.android.apps.maps");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        txtDetail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Clicked Open in Citymapper", Toast.LENGTH_SHORT).show();
                String locationString="https://citymapper.com/directions?endcoord="+Double.toString(elementLatitude)+"%2C"+Double.toString(elementLongitude)+"&endname="+elementTitle;
                Log.d("Location :", locationString);
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(locationString));
                startActivity(intent);

//                intent.setPackage("com.citymapper.app.release");
//                if (intent.resolveActivity(getPackageManager()) != null) {
//                    mBottomSheetDialog.dismiss();
//                }
            }
        });

        txtOpen.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Clicked Delete Spot", Toast.LENGTH_SHORT).show();
                SpotDB spotDatabase = new SpotDB(mCtx);
                spotDatabase.removeWithId(id);
                savedSpotsFragment.updateListView();
                //Log.d("database updated:", spotDatabase.getAll().toString());
                mBottomSheetDialog.dismiss();
            }
        });

    }

}
