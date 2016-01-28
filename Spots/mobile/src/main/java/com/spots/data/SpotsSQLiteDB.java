package com.spots.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.spots.data.database.CategoryDB;
import com.spots.data.database.SpotDB;
import com.spots.data.model.Category;

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

        // Fixtures
        db.execSQL("INSERT INTO " + CategoryDB.TABLE_NAME + "(" + CategoryDB.INSERT_TABLE_STATEMENT
                + ") values('Drink','GLASS_TULIP')");
        db.execSQL("INSERT INTO " + CategoryDB.TABLE_NAME + "(" + CategoryDB.INSERT_TABLE_STATEMENT
                + ") values('Eat','SILVERWARE_FORK')");
        db.execSQL("INSERT INTO " + CategoryDB.TABLE_NAME + "(" + CategoryDB.INSERT_TABLE_STATEMENT
                + ") values('Shop','SHOPPING')");
        db.execSQL("INSERT INTO " + CategoryDB.TABLE_NAME + "(" + CategoryDB.INSERT_TABLE_STATEMENT
                + ") values('Visit','CASTLE')");

        db.execSQL("INSERT INTO " + SpotDB.TABLE_NAME + "(" + SpotDB.INSERT_TABLE_STATEMENT
                + ") values(45.28476,9.14034,'Birrificio Lambrate','via Golgi, Milano')");
        db.execSQL("INSERT INTO " + SpotDB.TABLE_NAME + "(" + SpotDB.INSERT_TABLE_STATEMENT
                + ") values(45.47251790000001,9.23656619999997,'Jet Café','via Tajani, 11, Milano')");
        db.execSQL("INSERT INTO " + SpotDB.TABLE_NAME + "(" + SpotDB.INSERT_TABLE_STATEMENT
                + ") values(45.4640976,9.191926500000022,'Duomo','Piazza del Duomo, Milano, Italy')");
	}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Model
        db.execSQL("DROP TABLE " + SpotDB.TABLE_NAME + ";");
        db.execSQL("DROP TABLE " + CategoryDB.TABLE_NAME + ";");
        onCreate(db);
    }
}