package org.snuderl.activity;

import java.util.ArrayList;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.snuderl.ApplicationState;
import org.snuderl.FeedActivity;
import org.snuderl.FeedListener;
import org.snuderl.NewsAdapter;
import org.snuderl.OnEndFetchMore;
import org.snuderl.R;
import org.snuderl.State;
import org.snuderl.click.Click;
import org.snuderl.click.ClickCounter;
import org.snuderl.mobilni.NewsMessage;
import org.snuderl.web.FeedParser;
import org.snuderl.web.PortalApi;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class PersonalizedActivity extends FeedActivity {
	Boolean all = false;
	int checked = -1;
	OnEndFetchMore f;

	Timer t;

	CharSequence[] items;
	CharSequence[] filters;

	Handler handler;

	ListView lv;
	OnEndFetchMore scrollListener;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ApplicationState.change = this;

		parser = new FeedParser("http://mobilniportalnovic.apphb.com/feed");
		List<NewsMessage> list;
		if (ApplicationState.GetLoginToken(getApplicationContext()) != null) {
			parser.AddParameter("token", ApplicationState.GetLoginToken(this
					.getApplicationContext()));

			list = parser.parse();
		} else {
			list = new ArrayList<NewsMessage>();
		}
		adapter = new NewsAdapter(this, list);
		adapter.Sort();
		setListAdapter(adapter);

		lv = getListView();
		scrollListener = new OnEndFetchMore(this);
		lv.setOnScrollListener(scrollListener);

		lv.setOnItemClickListener(this);
		t = new Timer();
		t.scheduleAtFixedRate(new FetchNew(), 5000, 5000);

		handler = new Handler() {
			public void handleMessage(Message msg) {
				GetNew();
			}
		};

	}

	@Override
	protected void onDestroy() {
		t.cancel();
		super.onDestroy();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_personalized, menu);
		return true;
	}

	public void GetNew() {
		List<NewsMessage> nm = parser.GetNew();
		adapter.AddRange(nm);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.settingsButton:
			Intent i = new Intent(PersonalizedActivity.this, UserAccount.class);
			PersonalizedActivity.this.startActivity(i);

			return true;

		case R.id.recreate:
			recreate();

			return true;
		case R.id.info:
			Dialog dialog = new Dialog(PersonalizedActivity.this);

			dialog.setContentView(R.layout.filters_dialog);
			dialog.setTitle("Personalization info");

			TextView user = (TextView) dialog.findViewById(R.id.user);
			user.setText("User: "
					+ ApplicationState.GetUsername(getApplicationContext()));

			TextView cat = (TextView) dialog.findViewById(R.id.categories);
			StringBuilder categories = new StringBuilder();
			categories.append("Displayed categories:\n");
			for (String s : parser.Categories) {
				categories.append(s + "\n");
			}
			cat.setText(categories.toString());

			TextView text = (TextView) dialog.findViewById(R.id.filterInfo);
			StringBuilder sb = new StringBuilder();
			sb.append("Used filters:\n");
			sb.append(parser.FilterInfo);
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
	}

	public void displayNotification(String msg) {
		NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.ic_launcher,
				msg, System.currentTimeMillis());

		// The PendingIntent will launch activity if the user selects this
		// notification
		Intent NotificationIntent = new Intent(this, TabWidget.class);
		NotificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TOP);
		NotificationIntent.setAction(Intent.ACTION_MAIN);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 1,
				NotificationIntent, 0);

		notification.setLatestEventInfo(this, "Novice",
				"Na vašem kanalu so na voljo nove novice.", contentIntent);

		manager.notify(2, notification);

	}

	public PendingIntent existAlarm(int id) {
		Intent intent = new Intent(this, TabWidget.class);
		intent.setAction(Intent.ACTION_VIEW);
		PendingIntent test = PendingIntent.getBroadcast(this, id + 0, intent,
				PendingIntent.FLAG_NO_CREATE);
		return test;
	}

	private class FetchNew extends TimerTask {

		public void run() {
			if (ApplicationState.GetLoginToken(getApplicationContext()) != null) {
				if (parser.HasNew() && existAlarm(1) == null) {
					if (ApplicationState.isActivityVisible() == false) {
						displayNotification("New news");
						handler.sendMessage(new Message());
					}
				}
			}

		}
	}
}
