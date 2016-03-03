package com.spots;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
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
    private ImageView lastSelectedView;

    private static LayoutInflater inflater=null;

    public CategoryListAdapter(Activity _activity, Context savedPlaces, int[] categoryImgList, String[] categoryNameList) {
        activity = _activity;
        categoryImages=categoryImgList;
        categoryNames=categoryNameList;
        context=savedPlaces;
        lastSelectedSpotIndex = -1;
        lastSelectedView=new ImageView(context);
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

    private void updateSelectedView(ImageView newSelectedView) {
        if (lastSelectedView!=null){
            lastSelectedView.setSelected(false);
            lastSelectedView.setBackgroundResource(R.drawable.round_button);
        }
        newSelectedView.setSelected(true);
        newSelectedView.setBackgroundResource(R.drawable.round_button_selected);
        lastSelectedView=newSelectedView;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        // TODO Auto-generated method stub
        final Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.category_listview, null);
        holder.nameTextView=(TextView) rowView.findViewById(R.id.categoryName);
        holder.imgView=(ImageView) rowView.findViewById(R.id.categoryImage);
        holder.nameTextView.setText(categoryNames[position]);
        holder.imgView.setImageResource(categoryImages[position]);
        holder.imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                lastSelectedSpotIndex = position;
                context.getApplicationContext();
                updateSelectedView((ImageView) v);
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