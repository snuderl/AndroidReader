package org.snuderl;

import java.util.List;

import android.app.*;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ListActivity {
	FeedParser parser = new FeedParser("http://mobilniportalnovic.apphb.com/feed");
	final PortalApi api = new PortalApi();
	NewsAdapter adapter= null;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		final SharedPreferences settings  = getSharedPreferences("org.snuderl.settings", 2);
		settings.edit().commit();
		
		List<NewsMessage> list = parser.parse();
		adapter = new NewsAdapter(this, list);
		
		setListAdapter(adapter);
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				String result = "";
				try {
					result = api.ReportClick(adapter.get(position).id, settings.getString("userId", "1"));
				} catch (Exception e) {
					result = "error reporting click";
				}
				// When clicked, show a toast with the TextView text
				Toast.makeText(MainActivity.this,
						result,
						Toast.LENGTH_SHORT).show();
			}
		});
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
	        case R.id.moreButton:
	        	for (NewsMessage nm	 : parser.more()) {
					adapter.add(nm);
				}
	            return true;
	        case R.id.settingsButton:
	        	Intent i = new Intent(MainActivity.this, SettingsActivity.class);
	        	MainActivity.this.startActivity(i);
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}