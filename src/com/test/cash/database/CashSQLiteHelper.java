package com.test.cash.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CashSQLiteHelper extends SQLiteOpenHelper {
	
	public static final String TABLE_CASH = "cash";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_DENOMINATION = "denomination";
	public static final String COLUMN_INVENTORY = "inventory";

	private static final String DATABASE_NAME = "cash.db";
	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_CREATE = "create table "
			+ TABLE_CASH + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_DENOMINATION
			+ " integer not null," + COLUMN_INVENTORY + " integer not null);";

	public CashSQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CASH);
		onCreate(db);
	}
	
	public void updateInventory(SQLiteDatabase db, long denomination, long inventory) {
		ContentValues values = new ContentValues();
		values.put(COLUMN_INVENTORY, inventory);
		String strFilter = COLUMN_DENOMINATION + "=" + denomination;
		db.update(TABLE_CASH, values, strFilter, null);
	}

}
