package org.snuderl;

import org.snuderl.web.FeedParser;

import android.widget.ListView;

public interface FeedListener {
	public FeedParser getParser();
	public NewsAdapter getAdapter();
	public ListView getLV();
}
