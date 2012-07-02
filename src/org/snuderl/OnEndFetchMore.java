package org.snuderl;

import java.util.List;

import org.snuderl.mobilni.NewsMessage;
import org.snuderl.web.FeedParser;

import android.os.AsyncTask;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

public class OnEndFetchMore implements OnScrollListener {
	FeedActivity fl;
	private boolean hasMore = true;
	private boolean loading = false;

	public OnEndFetchMore(FeedActivity fl) {
		this.fl = fl;
	}

	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;
		if (loadMore && hasMore) {
			if (loading == false) {
				loading = true;
				new LoadMoreTask().execute();
			}
		}

	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	class LoadMoreTask extends AsyncTask<Void, Void, List<NewsMessage>> {

		@Override
		protected List<NewsMessage> doInBackground(Void... params) {
			return fl.parser.more();
		}

		@Override
		protected void onPostExecute(List<NewsMessage> news) {
			for (NewsMessage m : news) {
				fl.adapter.add(m);
			}
			fl.adapter.notifyDataSetChanged();

			if (news.size() == 0) {
				hasMore = false;
			}
			loading = false;
			super.onPostExecute(news);
		}

	}

}
