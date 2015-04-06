package com.test.cash.database;

import android.database.Cursor;

public class CashItem implements Comparable<CashItem>{

	private Long id;
	
	private Long denomination;
	
	private Long inventory;
	
	public CashItem (Cursor cursor) {
		this.id = cursor.getLong(0);
		this.denomination = cursor.getLong(1);
		this.inventory = cursor.getLong(2);
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDenomination() {
		return denomination;
	}

	public void setDenomination(Long denomination) {
		this.denomination = denomination;
	}

	public Long getInventory() {
		return inventory;
	}

	public void setInventory(Long inventory) {
		this.inventory = inventory;
	}
	
	@Override 
	public String toString() {
		return new StringBuffer("$")
				.append(denomination)
				.append(", ")
				.append(inventory).toString();
	}

	@Override
	public int compareTo(CashItem another) {
		if (this.denomination < another.denomination) {
			return -1;
		}
		if (this.denomination > another.denomination) {
			return 1;	
		}
		return 0;
	}
}
