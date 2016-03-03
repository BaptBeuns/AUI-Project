package com.spots.data.database;

import com.spots.data.model.Spot;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SpotDB extends BaseDB {

	public static final String TABLE_NAME = "SPOT";

	private static final String ID_FIELD_NAME = "_ID";
    private static final String CATEGORY_ID_FIELD_NAME = "CATEGORY_ID";
    private static final String LONGITUDE_FIELD_NAME = "LONGITUDE";
	private static final String LATITUDE_FIELD_NAME = "LATITUDE";
	private static final String NAME_FIELD_NAME = "NAME";
	private static final String ADDRESS_FIELD_NAME = "ADDRESS";
	private static final String FILTER_WEEK_FIELD_NAME = "FILTERWEEK";
	private static final String FILTER_WEEKEND_FIELD_NAME = "FILTERWEEKEND";
	private static final String FILTER_EVENING_FIELD_NAME = "FILTEREVENING";

	private static final String ID_FIELD_TYPE = "INTEGER PRIMARY KEY AUTOINCREMENT";
    private static final String CATEGORY_ID_FIELD_TYPE = "INTEGER";
    private static final String LONGITUDE_FIELD_TYPE = "REAL";
	private static final String LATITUDE_FIELD_TYPE = "REAL";
	private static final String NAME_FIELD_TYPE = "TEXT";
	private static final String ADDRESS_FIELD_TYPE = "TEXT";
	private static final String FILTER_WEEK_FIELD_TYPE = "INTEGER";
	private static final String FILTER_WEEKEND_FIELD_TYPE = "INTEGER";
	private static final String FILTER_EVENING_FIELD_TYPE = "INTEGER";

	private static final int NUM_COL_ID = 0;
    private static final int NUM_COL_LONGITUDE = 1;
	private static final int NUM_COL_LATITUDE = 2;
	private static final int NUM_COL_NAME = 3;
	private static final int NUM_COL_ADDRESS = 4;
	private static final int NUM_COL_FILTER_WEEK = 5;
	private static final int NUM_COL_FILTER_WEEKEND = 6;
	private static final int NUM_COL_FILTER_EVENING = 7;
    private static final int NUM_COL_CATEGORY_ID = 8;


    public static final String CREATE_TABLE_STATEMENT = ID_FIELD_NAME + " " + ID_FIELD_TYPE
            + ", " + LONGITUDE_FIELD_NAME + " " + LONGITUDE_FIELD_TYPE
            + ", " + LATITUDE_FIELD_NAME + " " + LATITUDE_FIELD_TYPE
            + ", " + NAME_FIELD_NAME + " " + NAME_FIELD_TYPE
            + ", " + ADDRESS_FIELD_NAME + " " + ADDRESS_FIELD_TYPE
            + ", " + FILTER_WEEK_FIELD_NAME + " " + FILTER_WEEK_FIELD_TYPE
            + ", " + FILTER_WEEKEND_FIELD_NAME + " " + FILTER_WEEKEND_FIELD_TYPE
            + ", " + FILTER_EVENING_FIELD_NAME + " " + FILTER_EVENING_FIELD_TYPE
            + ", " + CATEGORY_ID_FIELD_NAME + " " + CATEGORY_ID_FIELD_TYPE
            + ", FOREIGN KEY (" + CATEGORY_ID_FIELD_NAME + ") REFERENCES " +CategoryDB.TABLE_NAME+ "("+CategoryDB.ID_FIELD_NAME+")";

	public static final String INSERT_TABLE_STATEMENT = CATEGORY_ID_FIELD_NAME
            + ", " + LONGITUDE_FIELD_NAME
            + ", " + LATITUDE_FIELD_NAME
            + ", " + NAME_FIELD_NAME
            + ", " + ADDRESS_FIELD_NAME;

	public SpotDB(Context context) {
		super(context);
		this.mDb = this.open();
	}

	public long insert(Spot spot) {
		ContentValues values = new ContentValues();
        values.put(CATEGORY_ID_FIELD_NAME, spot.getCategoryId());
        values.put(LONGITUDE_FIELD_NAME, spot.getLongitude());
		values.put(LATITUDE_FIELD_NAME, spot.getLatitude());
		values.put(NAME_FIELD_NAME, spot.getName());
		values.put(ADDRESS_FIELD_NAME, spot.getAddress());
		values.put(FILTER_WEEK_FIELD_NAME, spot.getFilterWeek());
		values.put(FILTER_WEEKEND_FIELD_NAME, spot.getFilterWeekEnd());
		values.put(FILTER_EVENING_FIELD_NAME, spot.getFilterEvening());

		return mDb.insert(TABLE_NAME, null, values);
	}

	public int update(int id, Spot spot) {
		ContentValues values = new ContentValues();
		values.put(LONGITUDE_FIELD_NAME, spot.getLongitude());
		values.put(LATITUDE_FIELD_TYPE, spot.getLatitude());
		values.put(NAME_FIELD_NAME, spot.getName());
		values.put(ADDRESS_FIELD_NAME, spot.getAddress());
		values.put(FILTER_WEEK_FIELD_NAME, spot.getFilterWeek());
		values.put(FILTER_WEEKEND_FIELD_NAME, spot.getFilterWeekEnd());
		values.put(FILTER_EVENING_FIELD_NAME, spot.getFilterEvening());
        values.put(CATEGORY_ID_FIELD_NAME, spot.getCategoryId());

        return mDb.update(TABLE_NAME, values, ID_FIELD_NAME + " = " + id, null);
	}

	public int removeWithId(int id) {
        return mDb.delete(TABLE_NAME, ID_FIELD_NAME + " = " + id, null);
    }

    public Spot getWithId(int id) {
        Cursor c = super.mDb.query(TABLE_NAME, null,
                ID_FIELD_NAME + "=" + id, null, null, null, null);
        return cursorToSpot(c);
    }

    public List<Spot> getAll(){
        Cursor c = super.mDb.query(TABLE_NAME, null , null, null, null, null, null);
        return cursorToListSpot(c);
    }

    private List<Spot> cursorToListSpot(Cursor c){
        if (c.getCount() == 0)
            return null;

        Log.d("DB", c.toString());
        List<Spot> listSpot = new ArrayList<Spot>();
        listSpot.clear();

        if (c.moveToFirst()) {
            do {
                Spot spot = new Spot();

                spot.setId(c.getInt(NUM_COL_ID));
                spot.setLongitude(c.getDouble(NUM_COL_LONGITUDE));
                spot.setLatitude(c.getDouble(NUM_COL_LATITUDE));
                spot.setName(c.getString(NUM_COL_NAME));
                spot.setAddress(c.getString(NUM_COL_ADDRESS));
                spot.setFilterWeek(c.getInt(NUM_COL_FILTER_WEEK) > 0);
                spot.setFilterWeekEnd(c.getInt(NUM_COL_FILTER_WEEKEND) > 0);
                spot.setFilterEvening(c.getInt(NUM_COL_FILTER_EVENING) > 0);
                spot.setCategoryId(c.getInt(NUM_COL_CATEGORY_ID));

                listSpot.add(spot);
            } while (c.moveToNext());
        }
        c.close();
        return listSpot;
    }

    private Spot cursorToSpot(Cursor c) {
        if (c.getCount() == 0)
            return null;

        c.moveToFirst();
        Spot spot = new Spot();

        spot.setId(c.getInt(NUM_COL_ID));
        spot.setLongitude(c.getDouble(NUM_COL_LONGITUDE));
        spot.setLatitude(c.getDouble(NUM_COL_LATITUDE));
        spot.setName(c.getString(NUM_COL_NAME));
        spot.setAddress(c.getString(NUM_COL_ADDRESS));
        spot.setFilterWeek(c.getInt(NUM_COL_FILTER_WEEK) > 0);
        spot.setFilterWeekEnd(c.getInt(NUM_COL_FILTER_WEEKEND) > 0);
        spot.setFilterEvening(c.getInt(NUM_COL_FILTER_EVENING) > 0);
        spot.setCategoryId(NUM_COL_CATEGORY_ID);

        c.close();

        return spot;
    }

}