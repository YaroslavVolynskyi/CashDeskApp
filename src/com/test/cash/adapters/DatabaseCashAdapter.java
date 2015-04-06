package com.test.cash.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.test.cash.R;
import com.test.cash.database.CashDataSource;
import com.test.cash.database.CashItem;

public class DatabaseCashAdapter extends ArrayAdapter<CashItem> {

	private Context context;
	private CashDataSource source;

	public DatabaseCashAdapter(Context context, CashDataSource source) {
		super(context, 0);
		this.context = context;
		this.source = source;
	}

	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.cashdesk_list_item, parent, false);
		}

		TextView text = (TextView) convertView.findViewById(R.id.item_textview);
		if (source.getItems() != null) {
			text.setText(source.getItems().get(position).toString());
		}

		return convertView;
	}

	@Override
	public int getCount() {
		if (this.source.getItems() != null) {
			return this.source.getItems().size();
		}
		return 0;
	}

}
