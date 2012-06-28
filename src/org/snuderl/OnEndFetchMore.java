package org.snuderl;

import java.util.List;

import org.snuderl.mobilni.NewsMessage;
import org.snuderl.web.FeedParser;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

public class OnEndFetchMore implements OnScrollListener{
	public NewsAdapter adapter;
	public FeedParser parser;
	private boolean hasMore = true;
	
	public OnEndFetchMore(NewsAdapter a, FeedParser p){
		this.adapter=a;
		this.parser=p;
	}

	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
        boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;
        if(loadMore&&hasMore) {
        	List<NewsMessage> list = parser.more();
            for(NewsMessage m : list){
            	adapter.add(m);
            }
			adapter.notifyDataSetChanged();
			
            if(list.size()==0){
            	hasMore=false;
            }
            
        }
		
	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {

		
	}

}
