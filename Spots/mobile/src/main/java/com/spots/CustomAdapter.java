package com.spots;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomAdapter extends BaseAdapter {
    String [] titleResult;
    String [] detailResult;
    Context context;
    int [] imageId;
    private static LayoutInflater inflater=null;
    public CustomAdapter(SavedPlaces savedPlaces, String[] prgmTitleList, String[] prgmDetailList, int[] prgmImages) {
        // TODO Auto-generated constructor stub
        titleResult=prgmTitleList;
        detailResult=prgmDetailList;
        context=savedPlaces;
        imageId=prgmImages;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return titleResult.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView titleTextView;
        TextView detailTextView;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.activity_listview, null);
        holder.titleTextView=(TextView) rowView.findViewById(R.id.titleTextView);
        holder.detailTextView=(TextView) rowView.findViewById(R.id.detailTextView);
        holder.img=(ImageView) rowView.findViewById(R.id.imageView1);
        holder.titleTextView.setText(titleResult[position]);
        holder.detailTextView.setText(detailResult[position]);
        holder.img.setImageResource(imageId[position]);
        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked "+titleResult[position], Toast.LENGTH_LONG).show();
//                SavedPlaces savedPlacesObject=new SavedPlaces();
//                savedPlacesObject.displayGoogleMapsIntentForSpotAtIndex(position);

            }
        });
        return rowView;
    }

}