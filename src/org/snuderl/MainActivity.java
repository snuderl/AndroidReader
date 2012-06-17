package org.snuderl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

public class MainActivity extends ListActivity {
	FeedParser parser = null;
	final PortalApi api = new PortalApi();
	NewsAdapter adapter = null;
	String userId = "";
	Boolean all = false;
	int checked = -1;

	CharSequence[] items;
	CharSequence[] filters;
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		userId = getIntent().getExtras().getString("userId");
		all = getIntent().getExtras().getBoolean("all");

		if (!all) {
			parser = new FeedParser("http://mobilniportalnovic.apphb.com/feed",
					userId);
		} else {
			parser = new FeedParser("http://mobilniportalnovic.apphb.com/feed",
					"0");
		}

		List<NewsMessage> list = parser.parse();
		adapter = new NewsAdapter(this, list);

		setListAdapter(adapter);
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				NewsMessage m = adapter.getItem(position);
				api.LoadNews(m);

				State.getState().selected = m;

				AsyncTask<Click, Void, Void> report = new ClickCounter();
				report.execute(new Click(m.Id, userId));

				Intent details = new Intent(MainActivity.this,
						DetailsActivity.class);
				startActivity(details);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.moreButton:
			for (NewsMessage nm : parser.more()) {
				adapter.add(nm);
			}
			return true;
		case R.id.settingsButton:
			Intent i = new Intent(MainActivity.this, SettingsActivity.class);
			MainActivity.this.startActivity(i);

			return true;
		case R.id.choseCategory:
			if(!all){
				LinkedHashMap<String, Integer> map= adapter.GetCategories();
				List<String> tmp = new ArrayList<String>();
				for(String key: map.keySet()){
					tmp.add(key + " ("+map.get(key)+")");
				}
				items = new String[map.size()];
				filters = new String[map.size()];
				tmp.toArray(items);
				map.keySet().toArray(filters);
			}
			else{
				items = ApplicationState.GetApplicationState()
						.Categories();
				filters=items;
			}
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Pick a color");
			builder.setSingleChoiceItems(items, checked, new OnClickListener() {

				public void onClick(DialogInterface dialog, int which){
					checked=which;
				}
			});
			builder.setPositiveButton("Filter", new OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					
					dialog.cancel();
					getListView().setFilterText(filters[checked].toString());
					getListView().setTextFilterEnabled(true);
				}
			});
			builder.setNegativeButton("Disable filter", new OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					getListView().clearTextFilter();
					Toast.makeText(MainActivity.this, "Filtering disabled", Toast.LENGTH_SHORT);
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
			
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}

class Click {
	String id;
	String userId;

	public Click(String id, String userId) {
		this.id = id;
		this.userId = userId;
	}
}

class ClickCounter extends AsyncTask<Click, Void, Void> {
	@Override
	protected Void doInBackground(Click... params) {
		PortalApi api = new PortalApi();
		try {
			api.ReportClick(params[0].id, params[0].userId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("Report click", "Failed");
			e.printStackTrace();
		}
		return null;
	}

}