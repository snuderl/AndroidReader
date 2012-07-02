package org.snuderl.activity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.snuderl.ApplicationState;
import org.snuderl.FeedActivity;
import org.snuderl.FeedListener;
import org.snuderl.NewsAdapter;
import org.snuderl.OnEndFetchMore;
import org.snuderl.R;
import org.snuderl.State;
import org.snuderl.R.id;
import org.snuderl.R.menu;
import org.snuderl.click.Click;
import org.snuderl.click.ClickCounter;
import org.snuderl.mobilni.NewsMessage;
import org.snuderl.web.FeedParser;
import org.snuderl.web.PortalApi;

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
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

public class MainActivity extends FeedActivity {
	int checked = -1;

	CharSequence[] items;
	CharSequence[] filters;

	ListView lv;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		parser = new FeedParser("http://mobilniportalnovic.apphb.com/feed");

		List<NewsMessage> list = parser.parse();
		adapter = new NewsAdapter(this, list);
		setListAdapter(adapter);

		lv = this.getListView();
		lv.setOnItemClickListener(this);
		lv.setOnScrollListener(new OnEndFetchMore(this));
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
		case R.id.settingsButton:
			Intent i = new Intent(MainActivity.this, UserAccount.class);
			MainActivity.this.startActivity(i);
			return true;
		case R.id.choseCategory:

			items = ApplicationState.Categories();
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
					adapter.notifyDataSetChanged();
					lv.setSelectionAfterHeaderView();
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
					adapter.notifyDataSetChanged();
					lv.setSelectionAfterHeaderView();
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
