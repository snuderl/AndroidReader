package org.snuderl.activity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.snuderl.ApplicationState;
import org.snuderl.NewsAdapter;
import org.snuderl.OnEndFetchMore;
import org.snuderl.R;
import org.snuderl.State;
import org.snuderl.R.id;
import org.snuderl.R.layout;
import org.snuderl.R.menu;
import org.snuderl.click.Click;
import org.snuderl.click.ClickCounter;
import org.snuderl.mobilni.NewsMessage;
import org.snuderl.web.FeedParser;
import org.snuderl.web.PortalApi;

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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class PersonalizedActivity extends ListActivity implements UserChanged, OnScrollListener {
	FeedParser parser = null;
	final PortalApi api = new PortalApi();
	NewsAdapter adapter = null;
	Boolean all = false;
	int checked = -1;
	OnEndFetchMore f ;

	CharSequence[] items;
	CharSequence[] filters;

	ListView lv;
	OnEndFetchMore scrollListener;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ApplicationState.change =  this;

		parser = new FeedParser("http://mobilniportalnovic.apphb.com/feed");
		parser.AddParameter("token",
				ApplicationState.GetLoginToken(this.getApplicationContext()));

		List<NewsMessage> list = parser.parse();
		adapter = new NewsAdapter(this, list);
		adapter.Sort();
		setListAdapter(adapter);

		lv = getListView();
		scrollListener=new OnEndFetchMore(adapter, parser);
		lv.setOnScrollListener(this);
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				NewsMessage m = adapter.getItem(position);
				// /If content is loaded already, there is no need to load it
				// again, or to report it as a click
				if (m.Content == null) {
					api.LoadNews(m);

					AsyncTask<Click, Void, Void> report = new ClickCounter();
					report.execute(new Click(m.Id, ApplicationState
							.GetLoginToken(getApplicationContext())));
				}

				State.getState().selected = m;

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
		case R.id.settingsButton:
			Intent i = new Intent(PersonalizedActivity.this,
					UserAccount.class);
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
					checked = -1;
					getListView().clearTextFilter();
					Toast.makeText(PersonalizedActivity.this,
							"Filtering disabled", Toast.LENGTH_SHORT);
				}
			});
			AlertDialog alert = builder.create();
			alert.show();

			return true;

		case R.id.recreate:
			recreate();

			return true;
		case R.id.info:
			Dialog dialog = new Dialog(PersonalizedActivity.this);

			dialog.setContentView(R.layout.filters_dialog);
			dialog.setTitle("Personalization info");

			TextView user = (TextView) dialog.findViewById(R.id.user);
			user.setText(ApplicationState.GetUsername(getApplicationContext()));

			TextView cat = (TextView) dialog.findViewById(R.id.categories);
			StringBuilder categories = new StringBuilder();

			TextView text = (TextView) dialog.findViewById(R.id.filterInfo);
			StringBuilder sb = new StringBuilder();
			for (String s : parser.FilterInfo) {
				sb.append(s + "\n");
			}
			text.setText(sb.toString());
			dialog.show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void recreate() {
		parser = new FeedParser("http://mobilniportalnovic.apphb.com/feed");
		parser.AddParameter("token",
				ApplicationState.GetLoginToken(getApplicationContext()));

		adapter.clear();
		adapter = new NewsAdapter(this, parser.parse());
		
		setListAdapter(adapter);
		lv.setAdapter(adapter);
		scrollListener=new OnEndFetchMore(adapter, parser);
		lv.setOnScrollListener(this);
	}

	public void onChange() {
		recreate();
	}

	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		scrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);

		lv.clearTextFilter();
		if (checked > 0) {
			lv.setFilterText(filters[checked].toString());
		}
		
	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}
}
