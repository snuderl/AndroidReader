package org.snuderl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.snuderl.click.Click;
import org.snuderl.click.ClickCounter;

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

	ListView lv;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		userId = getIntent().getExtras().getString("userId");
		all = getIntent().getExtras().getBoolean("all");

		parser = new FeedParser("http://mobilniportalnovic.apphb.com/feed");

		List<NewsMessage> list = parser.parse();
		adapter = new NewsAdapter(this, list);
		setListAdapter(adapter);

		this.getListView().setOnItemClickListener(GetItemClickListener());
	}

	public OnItemClickListener GetItemClickListener() {
		return new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				NewsMessage m = adapter.getItem(position);
				///If content is loaded already, there is no need to load it again, or to report it as a click
				if (m.Content == null) {
					api.LoadNews(m);

					AsyncTask<Click, Void, Void> report = new ClickCounter();
					report.execute(new Click(m.Id, userId));
				}

				State.getState().selected = m;
				Intent details = new Intent(MainActivity.this,
						DetailsActivity.class);
				startActivity(details);
			}
		};
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
			adapter.notifyDataSetChanged();
			return true;
		case R.id.settingsButton:
			Intent i = new Intent(MainActivity.this, SettingsActivity.class);
			MainActivity.this.startActivity(i);

			return true;
		case R.id.choseCategory:

			items = ApplicationState.GetApplicationState().Categories();
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Choose category:");
			builder.setSingleChoiceItems(items, checked, new OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					checked = which;
				}
			});
			builder.setPositiveButton("Show category", new OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					int CategoryId = ApplicationState.GetApplicationState().Categories[checked].Id;
					parser = new FeedParser(
							"http://mobilniportalnovic.apphb.com/Feed/CategoryView/"
									+ CategoryId);
					adapter.clear();
					for (NewsMessage nm : parser.parse()) {
						adapter.add(nm);
					}
				}
			});
			builder.setNegativeButton("Show all", new OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					parser = new FeedParser(
							"http://mobilniportalnovic.apphb.com/feed");
					adapter.clear();
					for (NewsMessage nm : parser.parse()) {
						adapter.add(nm);
					}
					Toast.makeText(MainActivity.this, "Displaying all",
							Toast.LENGTH_SHORT);
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
