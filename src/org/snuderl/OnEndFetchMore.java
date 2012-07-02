package org.snuderl;

import java.util.List;

import org.snuderl.mobilni.NewsMessage;
import org.snuderl.web.FeedParser;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

public class OnEndFetchMore implements OnScrollListener {
	FeedListener fl;
	private boolean hasMore = true;

	public OnEndFetchMore(FeedListener fl) {
		this.fl = fl;
	}

	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;
		if (loadMore && hasMore) {
			List<NewsMessage> list = fl.getParser().more();
			for (NewsMessage m : list) {
				fl.getAdapter().add(m);
			}
			fl.getAdapter().notifyDataSetChanged();

			if (list.size() == 0) {
				hasMore = false;
			}
			

		}

	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

}
