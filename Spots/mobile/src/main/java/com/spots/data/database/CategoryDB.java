package com.spots.data.database;

import com.spots.CategoryListAdapter;
import com.spots.MainActivity;
import com.spots.R;
import com.spots.data.model.Category;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class CategoryDB extends BaseDB {

	public static final String TABLE_NAME = "CATEGORY";

	protected static final String ID_FIELD_NAME = "_ID";
	private static final String NAME_FIELD_NAME = "NAME";
	private static final String LOGO_FIELD_NAME = "LOGO";

	private static final String ID_FIELD_TYPE = "INTEGER PRIMARY KEY AUTOINCREMENT";
	private static final String NAME_FIELD_TYPE = "TEXT";
	private static final String LOGO_FIELD_TYPE = "TEXT";

	private static final int NUM_COL_ID = 0;
	private static final int NUM_COL_NAME = 1;
	private static final int NUM_COL_LOGO = 2;

	public static final String CREATE_TABLE_STATEMENT = ID_FIELD_NAME + " " + ID_FIELD_TYPE
            + ", " + NAME_FIELD_NAME + " " + NAME_FIELD_TYPE
            + ", " + LOGO_FIELD_NAME + " " + LOGO_FIELD_TYPE;

	public static final String INSERT_TABLE_STATEMENT = NAME_FIELD_NAME
			+ ", " + LOGO_FIELD_NAME;

	public CategoryDB(Context context) {
		super(context);
		this.mDb = this.open();
	}

	public long insert(Category category) {
		ContentValues values = new ContentValues();
		values.put(NAME_FIELD_NAME, category.getName());
		values.put(LOGO_FIELD_NAME, category.getLogo());

		return mDb.insert(TABLE_NAME, null, values);
	}

	public int update(int id, Category category) {
		ContentValues values = new ContentValues();
		values.put(NAME_FIELD_NAME, category.getName());
		values.put(LOGO_FIELD_NAME, category.getLogo());

		return mDb.update(TABLE_NAME, values, ID_FIELD_NAME + " = " + id, null);
	}

	public int removeWithId(int id) {
        return mDb.delete(TABLE_NAME, ID_FIELD_NAME + " = " + id, null);
    }

    public Category getWithId(int id) {
        Cursor c = super.mDb.query(TABLE_NAME, null,
                ID_FIELD_NAME + "=" + id, null, null, null, null);
        return cursorToCategory(c);
    }

    public List<Category> getAll(){
        Cursor c = super.mDb.query(TABLE_NAME, null , null, null, null, null, null);
        return cursorToListCategory(c);
    }

    private List<Category> cursorToListCategory(Cursor c){
        if (c.getCount() == 0)
            return null;

        List<Category> listCategory = new ArrayList<Category>();
        listCategory.clear();

        if (c.moveToFirst()) {
            do {
                Category category = new Category();
                
                category.setId(c.getInt(NUM_COL_ID));
                category.setName(c.getString(NUM_COL_NAME));
                category.setLogo(c.getString(NUM_COL_LOGO));             

                listCategory.add(category);
            } while (c.moveToNext());
        }
        c.close();
        return listCategory;
    }
    
    private Category cursorToCategory(Cursor c) {
        if (c.getCount() == 0)
            return null;

        c.moveToFirst();
        Category category = new Category();

        category.setId(c.getInt(NUM_COL_ID));
        category.setName(c.getString(NUM_COL_NAME));
        category.setLogo(c.getString(NUM_COL_LOGO));

        c.close();

        return category;
    }



}