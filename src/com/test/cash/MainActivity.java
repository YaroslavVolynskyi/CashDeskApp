package com.test.cash;

import com.test.cash.adapters.DatabaseCashAdapter;
import com.test.cash.database.CashDataSource;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private ListView cashDeskListView;

	private DatabaseCashAdapter adapter;
	
	private CashDataSource cashDataSource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initDatabase();
		initViews();
	}
	
	private void initDatabase() {
		cashDataSource = new CashDataSource(this);
		cashDataSource.open();
		
		adapter = new DatabaseCashAdapter(this, cashDataSource);
		cashDeskListView = (ListView) findViewById(R.id.cash_desk_listview);
		cashDeskListView.setAdapter(adapter);
	}
	
	private void initViews() {
		final EditText inputEditText = (EditText) findViewById(R.id.cash_desk_edittext);
		Button getCashButton = (Button) findViewById(R.id.get_cash_button);
		getCashButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getCash(inputEditText);
			}
		});
		
		Button restockButton = (Button) findViewById(R.id.restock_button);
		restockButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cashDataSource.restock(1, 1);
				adapter.notifyDataSetChanged();
			}
		});
	}
	
	private void getCash(EditText input) {
		int amount;
		try {
			amount = Integer.parseInt(input.getText().toString());
		} catch (NumberFormatException ex) {
			Toast.makeText(this, getResources().getString(R.string.invalid_input), Toast.LENGTH_SHORT).show();
			input.setText("");
			return;
		}
		boolean isEnough = cashDataSource.getCash(amount);
		if (isEnough) {
			adapter.notifyDataSetChanged();
		} else {
			Toast.makeText(this, getResources().getString(R.string.invalid_amount), Toast.LENGTH_SHORT).show();
		}
		input.setText("");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
