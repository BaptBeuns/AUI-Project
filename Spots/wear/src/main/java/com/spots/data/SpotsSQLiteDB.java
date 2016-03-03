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
        /*
        db.execSQL("CREATE TABLE IF NOT EXISTS " + SpotDB.TABLE_NAME + " ("
                + SpotDB.CREATE_TABLE_STATEMENT + ")");
                */
		db.execSQL("CREATE TABLE IF NOT EXISTS " + CategoryDB.TABLE_NAME + " ("
                + CategoryDB.CREATE_TABLE_STATEMENT + ")");

        // Fixtures
        db.execSQL("INSERT INTO " + CategoryDB.TABLE_NAME + "(" + CategoryDB.INSERT_TABLE_STATEMENT
                + ") values('Drink','rounded_beer')");
        db.execSQL("INSERT INTO " + CategoryDB.TABLE_NAME + "(" + CategoryDB.INSERT_TABLE_STATEMENT
                + ") values('Eat','rounded_waiter')");
        db.execSQL("INSERT INTO " + CategoryDB.TABLE_NAME + "(" + CategoryDB.INSERT_TABLE_STATEMENT
                + ") values('Shop','rounded_shopping_bag')");
        db.execSQL("INSERT INTO " + CategoryDB.TABLE_NAME + "(" + CategoryDB.INSERT_TABLE_STATEMENT
                + ") values('Visit','rounded_sagrada_familia')");
        db.execSQL("INSERT INTO " + CategoryDB.TABLE_NAME + "(" + CategoryDB.INSERT_TABLE_STATEMENT
                + ") values('Party','rounded_dj')");
        db.execSQL("INSERT INTO " + CategoryDB.TABLE_NAME + "(" + CategoryDB.INSERT_TABLE_STATEMENT
                + ") values('Sport','rounded_sport')");

        /*
        db.execSQL("INSERT INTO " + SpotDB.TABLE_NAME + "(" + SpotDB.INSERT_TABLE_STATEMENT
                + ") values(4,45.28476,9.14034,'Birrificio Lambrate','via Golgi, Milano')");
        db.execSQL("INSERT INTO " + SpotDB.TABLE_NAME + "(" + SpotDB.INSERT_TABLE_STATEMENT
                + ") values(0,45.47251790000001,9.23656619999997,'Jet Café','via Tajani, 11, Milano')");
        db.execSQL("INSERT INTO " + SpotDB.TABLE_NAME + "(" + SpotDB.INSERT_TABLE_STATEMENT
                + ") values(3,45.4640976,9.191926500000022,'Duomo','Piazza del Duomo, Milano, Italy')");
        db.execSQL("INSERT INTO " + SpotDB.TABLE_NAME + "(" + SpotDB.INSERT_TABLE_STATEMENT
                + ") values(1,45.4640976,9.191926500000022,'Pizzeria Maruzzella','12 Porta Venezia, Milano, Italy')");
    */
	}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Model
        //db.execSQL("DROP TABLE " + SpotDB.TABLE_NAME + ";");
        db.execSQL("DROP TABLE " + CategoryDB.TABLE_NAME + ";");
        onCreate(db);
    }
}