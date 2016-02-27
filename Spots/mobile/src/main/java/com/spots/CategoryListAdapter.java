package com.spots;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Jean on 18/02/16.
 */
public class CategoryListAdapter extends BaseAdapter {

    public int lastSelectedSpotIndex;
    private Activity activity;
    int [] categoryImages;
    String [] categoryNames;
    Context context;
    private static LayoutInflater inflater=null;

    public CategoryListAdapter(Activity _activity, Context savedPlaces, int[] categoryImgList, String[] categoryNameList) {
        activity = _activity;
        categoryImages=categoryImgList;
        categoryNames=categoryNameList;
        context=savedPlaces;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return categoryImages.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        TextView nameTextView;
        ImageView imgView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.category_listview, null);
        holder.nameTextView=(TextView) rowView.findViewById(R.id.categoryName);
        holder.imgView=(ImageView) rowView.findViewById(R.id.categoryImage);
        holder.nameTextView.setText(categoryNames[position]);
        holder.imgView.setImageResource(categoryImages[position]);
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                lastSelectedSpotIndex=position;
                Toast.makeText(context, "You Clicked "+categoryNames[position], Toast.LENGTH_LONG).show();
            }
        });
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(15, 0, 15, 0);
        rowView.setLayoutParams(params);
        return rowView;
    }

}