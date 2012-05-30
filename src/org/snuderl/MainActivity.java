package org.snuderl;

import java.util.List;

import android.app.*;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ListActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		NewsMessage m = new NewsMessage();
//		m.Title = "a";
//		m.Short = "b";
//		m.Date="12.1.2012";		
//		NewsMessage[] n = new NewsMessage[] { m, m.copy() };
		
		FeedParser parser = new FeedParser("http://mobilniportalnovic.apphb.com/feed");
		List<NewsMessage> list = parser.parse();
		
		final NewsAdapter adapter = new NewsAdapter(this, list);
		setListAdapter(adapter);
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// When clicked, show a toast with the TextView text
				Toast.makeText(MainActivity.this,
						adapter.get(position).Short,
						Toast.LENGTH_SHORT).show();
			}
		});
	}
}