package com.spots;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.spots.data.database.CategoryDB;
import com.spots.data.model.Category;

import java.util.List;

public class HomeFragment extends Fragment {

    private String TAG = "HOME_FRAGMENT";
    private Context mCtx;
    private String title;
    private int page;

    // newInstance constructor for creating fragment with arguments
    public static HomeFragment newInstance(Context ctx, int page, String title) {
        HomeFragment fragmentFirst = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.top_layout);
        View autocomplete = inflater.inflate(R.layout.places_layout, rl, true);

        rl = (RelativeLayout) view.findViewById(R.id.map_layout);
        View map = inflater.inflate(R.layout.map, rl, true);

        // Récupération des catégories à afficher
        displayCategories();

        return view;
    }


    private void displayCategories() {
        CategoryDB categoryDB = new CategoryDB(mCtx);
        List<Category> catList = categoryDB.getAll();
        String categoryName;
        String categoryImage;
        ImageView imageView;
        int resID;
        Drawable drawable;
        TextView txt;

        Map<int, List<Drawable>> dict;
        dict.put(0, [R.id.category1, R.id.cat1]);
        dict.put(1, [R.id.category2, R.id.cat2]);
        dict.put(2, [R.id.category3, R.id.cat3]);
        dict.put(3, [R.id.category4, R.id.cat4]);

        for (Map.Entry<String, String> entry : dict.entrySet())
        {
            // On récupère le logo de la catégorie
            categoryImage = catList.get(entry.getKey()).getLogo();
            drawable = getDrawable(resID);
            resID = getResources().getIdentifier(categoryImage , "drawable", getPackageName());
            // On l'affiche dans l'ImageView qui correspond
            imageView = (ImageView)findViewById(entry.getValue()[0]);
            imageView.setImageDrawable(drawable);
            // On affiche aussi le nom de la catégorie en dessous
            txt = (TextView) findViewById(entry.getValue()[0]);
            categoryName = catList.get(entry.getKey()).getName();
            txt.setText(categoryName);
        }

        // categoryImage = catList.get(0).getLogo();
        // drawable = getDrawable(resID);
        // resID = getResources().getIdentifier(categoryImage , "drawable", getPackageName());
        // imageView = (ImageView)findViewById(R.id.category_image_1);
        // imageView.setImageDrawable(drawable);
        // txt = (TextView) findViewById(R.id.cat1);
        // categoryName = catList.get(0).getName();
        // txt.setText(categoryName);

        // categoryImage = catList.get(1).getLogo();
        // drawable = getDrawable(resID);
        // resID = getResources().getIdentifier(categoryImage , "drawable", getPackageName());
        // imageView = (ImageView)findViewById(R.id.category_image_2);
        // imageView.setImageDrawable(drawable);
        // txt = (TextView) findViewById(R.id.cat2);
        // categoryName = catList.get(1).getName();
        // txt.setText(categoryName);

        // categoryImage = catList.get(2).getLogo();
        // drawable = getDrawable(resID);
        // resID = getResources().getIdentifier(categoryImage , "drawable", getPackageName());
        // imageView = (ImageView)findViewById(R.id.category_image_3);
        // imageView.setImageDrawable(drawable);
        // txt = (TextView) findViewById(R.id.cat3);
        // categoryName = catList.get(2).getName();
        // txt.setText(categoryName);

        // categoryImage = catList.get(3).getLogo();
        // drawable = getDrawable(resID);
        // resID = getResources().getIdentifier(categoryImage , "drawable", getPackageName());
        // imageView = (ImageView)findViewById(R.id.category_image_4);
        // imageView.setImageDrawable(drawable);
        // txt = (TextView) findViewById(R.id.cat4);
        // categoryName = catList.get(3).getName();
        // txt.setText(categoryName);
    }

    // Fonction appelée par le clic sur le bouton ADD SPOT
    public void addSpot(View view) {
        Spot spot = new Spot();

        // On chope le nom du lieu dans la description
        TextView txt = (TextView) findViewById(R.id.edit_spot_name);
        String name = txt.getText().toString();
        if (name.matches("")) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            String textDate = dateFormat.format(date).toString();
            name = "Nouveau point ajouté au " + textDate;
        }
        spot.setName(name);

        // Les coordonnées GPS sont issues d'une requete android,
        // ou de la carte Google.
        if (markerLocation != null) {
            Log.d("LOCATION", "Localisation issue de Google");
            spot.setLongitude(markerLocation.getLongitude());
            spot.setLatitude(markerLocation.getLatitude());
        } else if (currentLocation != null) {
            Log.d("LOCATION", "Localisation issue du GPS");
            spot.setLongitude(currentLocation.getLongitude());
            spot.setLatitude(currentLocation.getLatitude());
        }
        spot.setAddress("");

        SpotDB spotDB = new SpotDB(mCtx);
        spotDB.insert(spot);
        Toast.makeText(mCtx, "Le point " + name + " a bien été enregistré", Toast.LENGTH_SHORT).show();
    }

    // Fonction appelée par le clic sur une des catégories
    public void changeResource(View button) {
        ViewGroup vg = (ViewGroup) findViewById(R.id.layout_images);
        // On assigne tous les enfants à pas selected
        for(int i=0; i<vg.getChildCount(); ++i) {
            nextChild = vg.getChildAt(i).getChildAt(0).setBackgroundResource(R.drawable.round_button);
            nextChild.setSelected(false);
        }
        button.setSelected(true);
        button.setBackgroundResource(R.drawable.round_button_selected);
    }
}
