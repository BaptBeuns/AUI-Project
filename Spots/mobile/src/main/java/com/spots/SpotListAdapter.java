package com.spots;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SpotListAdapter extends BaseAdapter {

    public int lastSelectedSpotIndex;
    private Activity activity;
    String [] titleResult;
    String [] detailResult;
    Context context;
    int [] imageId;
    private static LayoutInflater inflater=null;

    public SpotListAdapter(Activity _activity, Context savedPlaces, String[] prgmTitleList, String[] prgmDetailList, int[] prgmImages) {
        // TODO Auto-generated constructor stub
        activity = _activity;
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
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.spots_listview, null);
        holder.titleTextView=(TextView) rowView.findViewById(R.id.titleTextView);
        holder.detailTextView=(TextView) rowView.findViewById(R.id.detailTextView);
        holder.img=(ImageView) rowView.findViewById(R.id.imageView1);
        holder.titleTextView.setText(titleResult[position]);
        holder.detailTextView.setText(detailResult[position]);
        holder.img.setImageResource(imageId[position]);
        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                lastSelectedSpotIndex=position;
                //Toast.makeText(context, "You Clicked "+titleResult[position], Toast.LENGTH_LONG).show();
            }
        });
        return rowView;
    }

}