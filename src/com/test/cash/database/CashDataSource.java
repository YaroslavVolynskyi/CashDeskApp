package com.test.cash.database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class CashDataSource {

	private SQLiteDatabase database;

	private CashSQLiteHelper dbHelper;

	private List<CashItem> items;

	private String[] ALL_COLUMNS = { CashSQLiteHelper.COLUMN_ID,
			CashSQLiteHelper.COLUMN_DENOMINATION,
			CashSQLiteHelper.COLUMN_INVENTORY };

	public CashDataSource(Context context) {
		dbHelper = new CashSQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
		initController();
	}

	public void close() {
		dbHelper.close();
	}

	public void restock(int oldVersion, int newVersion) {
		dbHelper.onUpgrade(database, oldVersion, newVersion);
		initController();
	}

	public void initController() {
		items = getAllCashItems();
		if (items.size() == 0) {
			items.add(createCashItem(1, 10));
			items.add(createCashItem(5, 10));
			items.add(createCashItem(10, 10));
			items.add(createCashItem(20, 10));
			items.add(createCashItem(100, 10));
		}
	}

	public List<CashItem> getItems() {
		return this.items;
	}

	public boolean getCash(long amount) {
		database.beginTransaction();
		long denomination;
		long inventory;
		Collections.sort(items, Collections.reverseOrder());
		for (CashItem item : items) {
			denomination = item.getDenomination();
			inventory = item.getInventory();
			long required = amount / denomination;
			if (required > 0) {
				long newAmount = inventory - required;
				if (newAmount >= 0) {
					amount %= denomination;
				} else {
					amount -= denomination * inventory;
					newAmount = 0;
				}
				dbHelper.updateInventory(database, denomination, newAmount);
			}
		}
		if (amount > 0) {
			database.endTransaction();
			return false;
		}
		database.setTransactionSuccessful();
		database.endTransaction();
		items = getAllCashItems();
		return true;
	}

	public CashItem createCashItem(long denomination, long inventory) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(CashSQLiteHelper.COLUMN_DENOMINATION, denomination);
		contentValues.put(CashSQLiteHelper.COLUMN_INVENTORY, inventory);

		long insertId = database.insert(CashSQLiteHelper.TABLE_CASH, null,
				contentValues);
		Cursor cursor = database.query(true, CashSQLiteHelper.TABLE_CASH,
				ALL_COLUMNS, CashSQLiteHelper.COLUMN_ID + " = " + insertId,
				null, null, null, null, null, null);

		cursor.moveToFirst();

		CashItem cashItem = new CashItem(cursor);
		cursor.close();
		return cashItem;
	}

	public List<CashItem> getAllCashItems() {
		List<CashItem> items = new ArrayList<>();
		Cursor cursor = database.query(true, CashSQLiteHelper.TABLE_CASH,
				ALL_COLUMNS, null, null, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			items.add(new CashItem(cursor));
			cursor.moveToNext();
		}
		cursor.close();
		return items;
	}
}
