package com.spots.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.spots.data.database.CategoryDB;
import com.spots.data.database.SpotDB;

public class SpotsSQLiteDB extends SQLiteOpenHelper {

    public SpotsSQLiteDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Model
        db.execSQL("CREATE TABLE IF NOT EXISTS " + SpotDB.TABLE_NAME + " ("
                + SpotDB.CREATE_TABLE_STATEMENT + ")");
		db.execSQL("CREATE TABLE IF NOT EXISTS " + CategoryDB.TABLE_NAME + " ("
                + CategoryDB.CREATE_TABLE_STATEMENT + ")");
	}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Model
        db.execSQL("DROP TABLE " + SpotDB.TABLE_NAME + ";");
        db.execSQL("DROP TABLE " + CategoryDB.TABLE_NAME + ";");
        onCreate(db);
    }
}