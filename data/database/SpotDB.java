package com.spots.data.database;

import com.spots.data.model.Spot;

import android.content.ContentValues;
import android.content.Context;

public class SpotDB extends BaseDB {

	public static final String TABLE_NAME = "SPOT";

	private static final String ID_FIELD_NAME = "_ID";
	private static final String LONGITUDE_FIELD_NAME = "LONGITUDE";
	private static final String LATITUDE_FIELD_NAME = "LATITUDE";
	private static final String NAME_FIELD_NAME = "NAME";
	private static final String ADDRESS_FIELD_NAME = "ADDRESS";
	private static final String FILTER_WEEK_FIELD_NAME = "FILTERWEEK";
	private static final String FILTER_WEEKEND_FIELD_NAME = "FILTERWEEKEND";
	private static final String FILTER_EVENING_FIELD_NAME = "FILTEREVENING";

	private static final String ID_FIELD_TYPE = "INTEGER PRIMARY KEY AUTOINCREMENT";
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

	static final String CREATE_TABLE_STATEMENT = ID_FIELD_NAME + " " + ID_FIELD_TYPE
            + ", " + LONGITUDE_FIELD_NAME + " " + LONGITUDE_FIELD_TYPE
            + ", " + LATITUDE_FIELD_NAME + " " + LATITUDE_FIELD_TYPE
            + ", " + NAME_FIELD_NAME + " " + NAME_FIELD_TYPE
            + ", " + ADDRESS_FIELD_NAME + " " + ADDRESS_FIELD_TYPE
            + ", " + FILTER_WEEK_FIELD_NAME + " " + FILTER_WEEK_FIELD_TYPE
            + ", " + FILTER_WEEKEND_FIELD_NAME + " " + FILTER_WEEKEND_FIELD_TYPE
            + ", " + FILTER_EVENING_FIELD_NAME + " " + FILTER_EVENING_FIELD_TYPE;

	public CategoryDB(Context context) {
		super(context);
		this.mDb = this.open();
	}

	public long insert(Spot spot) {
		ContentValues values = new ContentValues();
		values.put(LONGITUDE_FIELD_NAME, spot.longitude);
		values.put(LATITUDE_FIELD_NAME, spot.latitude);
		values.put(NAME_FIELD_NAME, spot.name);
		values.put(ADDRESS_FIELD_NAME, spot.address);
		values.put(FILTER_WEEK_FIELD_NAME, spot.filterNotifyWeek);
		values.put(FILTER_WEEKEND_FIELD_NAME, spot.filterNotifyWeekEnd);
		values.put(FILTER_EVENING_FIELD_NAME, spot.filterNotifyEvening);

		return mDb.insert(TABLE_NAME, null, values);
	}

	public int update(int id, Spot spot) {
		ContentValues values = new ContentValues();
		values.put(LONGITUDE_FIELD_NAME, spot.longitude);
		values.put(LATITUDE_FIELD_TYPE, spot.latitude);
		values.put(NAME_FIELD_NAME, spot.name);
		values.put(ADDRESS_FIELD_NAME, spot.address);
		values.put(FILTER_WEEK_FIELD_NAME, spot.filterNotifyWeek);
		values.put(FILTER_WEEKEND_FIELD_NAME, spot.filterNotifyWeekEnd);
		values.put(FILTER_EVENING_FIELD_NAME, spot.filterNotifyEvening);

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

    private Spot cursorToSpot(Cursor c) {
        if (c.getCount() == 0)
            return null;

        c.moveToFirst();
        Spot spot = new Spot();

        spot.setId(c.getInt(NUM_COL_ID));
        spot.setLongitude(c.getDouble(NUM_COL_LONGITUDE));
        spot.setLatitude(c.getDouble(NUM_COL_LATITUDE))
        spot.setName(c.getString(NUM_COL_NAME));
        spot.setAddress(c.getString(NUM_COL_ADDRESS));
        spot.setFilterWeek(c.getBoolean(NUM_COL_FILTER_WEEK));
        spot.setFilterWeekEnd(c.getBoolean(NUM_COL_FILTER_WEEKEND));
        spot.setFilterEvening(c.getBoolean(NUM_COL_FILTER_EVENING));

        c.close();

        return spot;
    }

}