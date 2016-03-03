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
                + ") values('Breakfast','pankake')");
        db.execSQL("INSERT INTO " + CategoryDB.TABLE_NAME + "(" + CategoryDB.INSERT_TABLE_STATEMENT
                + ") values('Lunch','burger')");
        db.execSQL("INSERT INTO " + CategoryDB.TABLE_NAME + "(" + CategoryDB.INSERT_TABLE_STATEMENT
                + ") values('Diner','waiter')");
        db.execSQL("INSERT INTO " + CategoryDB.TABLE_NAME + "(" + CategoryDB.INSERT_TABLE_STATEMENT
                + ") values('Nightlife','dj')");
        db.execSQL("INSERT INTO " + CategoryDB.TABLE_NAME + "(" + CategoryDB.INSERT_TABLE_STATEMENT
                + ") values('Bar','cocktail')");
        db.execSQL("INSERT INTO " + CategoryDB.TABLE_NAME + "(" + CategoryDB.INSERT_TABLE_STATEMENT
                + ") values('Coffee','coffee')");
        db.execSQL("INSERT INTO " + CategoryDB.TABLE_NAME + "(" + CategoryDB.INSERT_TABLE_STATEMENT
                + ") values('Shopping','shopping')");
        db.execSQL("INSERT INTO " + CategoryDB.TABLE_NAME + "(" + CategoryDB.INSERT_TABLE_STATEMENT
                + ") values('Visit','monument')");
        db.execSQL("INSERT INTO " + CategoryDB.TABLE_NAME + "(" + CategoryDB.INSERT_TABLE_STATEMENT
                + ") values('Leisures','run')");
        db.execSQL("INSERT INTO " + CategoryDB.TABLE_NAME + "(" + CategoryDB.INSERT_TABLE_STATEMENT
                + ") values('Other','star')");

        db.execSQL("INSERT INTO " + SpotDB.TABLE_NAME + "(" + SpotDB.INSERT_TABLE_STATEMENT
                + ") values(3,45.28476,9.14034,'Birrificio Lambrate','via Golgi, Milano')");
        db.execSQL("INSERT INTO " + SpotDB.TABLE_NAME + "(" + SpotDB.INSERT_TABLE_STATEMENT
                + ") values(4,45.47251790000001,9.23656619999997,'Jet Café','via Tajani, 11, Milan')");
        db.execSQL("INSERT INTO " + SpotDB.TABLE_NAME + "(" + SpotDB.INSERT_TABLE_STATEMENT
                + ") values(7,45.4640976,9.191926500000022,'Duomo','Piazza del Duomo, Milan, Italy')");
        db.execSQL("INSERT INTO " + SpotDB.TABLE_NAME + "(" + SpotDB.INSERT_TABLE_STATEMENT
                + ") values(2,45.4640976,9.191926500000022,'Pizzeria Maruzzella','12 Porta Venezia, Milan, Italy')");
        db.execSQL("INSERT INTO " + SpotDB.TABLE_NAME + "(" + SpotDB.INSERT_TABLE_STATEMENT
                + ") values(0,45.47920781803847,9.234260574491206,'Pavè','Via Felice Casati, Milan, Italy')");
        db.execSQL("INSERT INTO " + SpotDB.TABLE_NAME + "(" + SpotDB.INSERT_TABLE_STATEMENT
                + ") values(8,45.46248070000001,9.187817099999961,'GetFit','Via Falcone, 5, 20123 Milan, Italy')");
        db.execSQL("INSERT INTO " + SpotDB.TABLE_NAME + "(" + SpotDB.INSERT_TABLE_STATEMENT
                + ") values(6,45.4565288,9.180842799999937,'VANS Store','Corso di Porta Ticinese, 75, 20123 Milano, Italy')");
        db.execSQL("INSERT INTO " + SpotDB.TABLE_NAME + "(" + SpotDB.INSERT_TABLE_STATEMENT
                + ") values(5,45.4661378,9.191872999999987,'Juice Bar','Via Agnello, 18, 20122 Milano, Italy')");
        db.execSQL("INSERT INTO " + SpotDB.TABLE_NAME + "(" + SpotDB.INSERT_TABLE_STATEMENT
                + ") values(1,45.4819147,9.20079659999999,'Mama Burger','Via Vittor Pisani, 14, 20124 Milano, Italy')");
        db.execSQL("INSERT INTO " + SpotDB.TABLE_NAME + "(" + SpotDB.INSERT_TABLE_STATEMENT
                + ") values(9,45.4887396,9.225336500000026,'Italiano Parruchieri','Via Casoretto, 6, 20131 Milano, Italy')");
//        db.execSQL("INSERT INTO " + SpotDB.TABLE_NAME + "(" + SpotDB.INSERT_TABLE_STATEMENT
//                + ") values(3,45.438387,9.229969299999993,'Dude Club','Via Carlo Boncompagni, 44, Milano, Italy')");
        db.execSQL("INSERT INTO " + SpotDB.TABLE_NAME + "(" + SpotDB.INSERT_TABLE_STATEMENT
                + ") values(7,45.4443237,9.205654900000013,'Fondazione Prada','Largo Isarco, 20139 Milano, Italy')");
//        db.execSQL("INSERT INTO " + SpotDB.TABLE_NAME + "(" + SpotDB.INSERT_TABLE_STATEMENT
//                + ") values(2,45.4764654,9.20597459999999,'Ristorante Da Oscar','Via Lazzaro Palazzi, 4, 20124 Milano, Italy')");



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Model
        db.execSQL("DROP TABLE " + SpotDB.TABLE_NAME + ";");
        db.execSQL("DROP TABLE " + CategoryDB.TABLE_NAME + ";");
        onCreate(db);
    }
}