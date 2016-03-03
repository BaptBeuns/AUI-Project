package com.spots;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

/**
 * Created by Jean on 18/02/16.
 */
public class BottomToolbar extends Toolbar {

    private Context mCtx;
    private Button addActivityButton;
    private Button savedSpotsButton;
    //private Button exploreButton;

    public BottomToolbar(Context context) {
        super(context);
        mCtx = context;
    }


    public BottomToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mCtx = context;
    }

    public BottomToolbar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mCtx = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        addActivityButton = (Button) findViewById(R.id.go_to_add_activity_button);
        savedSpotsButton = (Button) findViewById(R.id.go_to_saved_spots_activity_button);
        //exploreButton = (Button) findViewById(R.id.go_to_explore_activity_button);
        addListenerOnButton();

        // SET STATES
        if (mCtx instanceof MainActivity) {
            addActivityButton.setSelected(true);
            savedSpotsButton.setSelected(false);
            //exploreButton.setSelected(false);
        } else if (mCtx instanceof SavedSpotsActivity) {
            addActivityButton.setSelected(false);
            savedSpotsButton.setSelected(true);
            //exploreButton.setSelected(false);
        } else {
            addActivityButton.setSelected(false);
            savedSpotsButton.setSelected(false);
            //exploreButton.setSelected(true);
        }
    }



    public void addListenerOnButton() {

        addActivityButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(mCtx instanceof MainActivity)) {
                    Intent intent =
                            new Intent(mCtx,MainActivity.class);
                    mCtx.startActivity(intent);
                }
            }

        });

        savedSpotsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(mCtx instanceof SavedSpotsActivity)) {
                    Intent intent =
                            new Intent(mCtx,SavedSpotsActivity.class);
                    mCtx.startActivity(intent);
                }
            }

        });
/*
        exploreButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(mCtx instanceof ExploreActivity)) {
                    Intent intent =
                            new Intent(mCtx,ExploreActivity.class);
                    mCtx.startActivity(intent);
                }
            }

        });
*/
    }
}
