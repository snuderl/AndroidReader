package org.snuderl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.snuderl.click.Click;
import org.snuderl.click.ClickCounter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class PersonalizedActivity extends ListActivity {
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
		parser.AddParameter("userId", userId);

		List<NewsMessage> list = parser.parse();
		adapter = new NewsAdapter(this, list);
		setListAdapter(adapter);

		lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
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

				AsyncTask<Click, Void, Void> report = new ClickCounter();
				report.execute(new Click(m.Id, userId));

				Intent details = new Intent(PersonalizedActivity.this,
						DetailsActivity.class);
				startActivity(details);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_personalized, menu);
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
			lv.clearTextFilter();
			if (checked > 0) {
				lv.setFilterText(filters[checked].toString());
			}
			return true;
		case R.id.settingsButton:
			Intent i = new Intent(PersonalizedActivity.this,
					SettingsActivity.class);
			PersonalizedActivity.this.startActivity(i);

			return true;
		case R.id.choseCategory:
			if (!all) {
				LinkedHashMap<String, Integer> map = adapter.GetCategories();
				List<String> tmp = new ArrayList<String>();
				for (String key : map.keySet()) {
					tmp.add(key + " (" + map.get(key) + ")");
				}
				items = new String[map.size()];
				filters = new String[map.size()];
				tmp.toArray(items);
				map.keySet().toArray(filters);
			} else {
				items = ApplicationState.GetApplicationState().Categories();
				filters = items;
			}
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Pick a color");
			builder.setSingleChoiceItems(items, checked, new OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					checked = which;
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
					checked=-1;
					getListView().clearTextFilter();
					Toast.makeText(PersonalizedActivity.this,
							"Filtering disabled", Toast.LENGTH_SHORT);
				}
			});
			AlertDialog alert = builder.create();
			alert.show();

			return true;

		case R.id.recreate:
			parser = new FeedParser("http://mobilniportalnovic.apphb.com/feed");
			parser.AddParameter("userId", userId);
			
			adapter.clear();
			adapter = new NewsAdapter(this, parser.parse());
			setListAdapter(adapter);
			lv.setAdapter(adapter);
			
			return true;
		case R.id.info:
			Dialog dialog = new Dialog(PersonalizedActivity.this);

			dialog.setContentView(R.layout.filters_dialog);
			dialog.setTitle("Custom Dialog");

			TextView text = (TextView) dialog.findViewById(R.id.filterInfo);
			StringBuilder sb = new StringBuilder();
			for(String s : parser.FilterInfo){
				sb.append(s+"\n");
			}
			text.setText(sb.toString());
			dialog.show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
