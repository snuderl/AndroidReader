package org.snuderl;

import org.snuderl.activity.DetailsActivity;
import org.snuderl.activity.UserChanged;
import org.snuderl.click.Click;
import org.snuderl.click.ClickCounter;
import org.snuderl.mobilni.NewsMessage;
import org.snuderl.web.FeedParser;
import org.snuderl.web.PortalApi;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class FeedActivity extends ListActivity implements OnItemClickListener,
		UserChanged {
	class LoadNewsTask extends AsyncTask<NewsMessage, Void, NewsMessage> {

		@Override
		protected NewsMessage doInBackground(NewsMessage... params) {
			api.LoadNews(params[0]);
			return params[0];
		}

		@Override
		protected void onPostExecute(NewsMessage m) {
			ShowNews(m);
			super.onPostExecute(m);
		}

	}
	public NewsAdapter adapter = null;
	public FeedParser parser = null;

	public PortalApi api = new PortalApi();

	public void onChange() {
		recreate();
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		NewsMessage m = adapter.getItem(position);
		// /If content is loaded already, there is no need to load it
		// again, or to report it as a click
		if (m.Content == null) {
			new LoadNewsTask().execute(m);

			AsyncTask<Click, Void, Void> report = new ClickCounter();
			report.execute(new Click(m.Id, ApplicationState
					.GetLoginToken(getApplicationContext()),
					ApplicationState.GetLocation()));
		} else {
			ShowNews(m);
		}
	}

	public void recreate() {
	}

	public void ShowNews(NewsMessage m) {
		State.getState().selected = m;

		Intent details = new Intent(FeedActivity.this, DetailsActivity.class);
		startActivity(details);
	}
}
