package com.spots.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.spots.data.SpotsSQLiteDB;

public abstract class BaseDB {
    protected final static int VERSION_BDD = 1;
    protected final static String NOM_BDD = "spots.db";

    protected SQLiteDatabase mDb = null;
    protected SpotsSQLiteDB mDbHelper = null;
    protected Context mCtx;

    public BaseDB(Context pContext) {
        this.mDbHelper = new SpotsSQLiteDB(pContext, NOM_BDD, null, VERSION_BDD);
        this.mCtx = pContext;
    }

    public SQLiteDatabase open() {
        mDb = mDbHelper.getWritableDatabase();
        return mDb;
    }

    public void close() {
        mDb.close();
    }

    public SQLiteDatabase getDb() {
        return mDb;
    }
}
