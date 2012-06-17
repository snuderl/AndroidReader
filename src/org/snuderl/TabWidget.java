package org.snuderl;

import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class TabWidget extends TabActivity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_host);
		
		final SharedPreferences settings = getSharedPreferences(
				"org.snuderl.settings", 2);
		final ApplicationState state = ApplicationState.GetApplicationState();
		try{
		state.Categories = Category.GetCategories();
		}
		catch(Exception e){
			e.printStackTrace();
		}

		settings.edit().putString("userId", "0");
		settings.edit().commit();

		Resources res = getResources(); // Resource object to get Drawables
		TabHost tabHost = getTabHost(); // The activity TabHost
		TabHost.TabSpec spec; // Resusable TabSpec for each tab
		Intent intent; // Reusable Intent for each tab

		// Create an Intent to launch an Activity for the tab (to be reused)
		intent = new Intent().setClass(this, PersonalizedActivity.class);
		intent.putExtra("userId", "1");
		intent.putExtra("all", false);

		// Initialize a TabSpec for each tab and add it to the TabHost
		spec = tabHost.newTabSpec("personalized").setIndicator("Personalized")
				.setContent(intent);
		tabHost.addTab(spec);

		// Do the same for the other tabs
		intent = new Intent().setClass(this, MainActivity.class);
		intent.putExtra("userId", "1");
		intent.putExtra("all", true);
		spec = tabHost.newTabSpec("all").setIndicator("All").setContent(intent);
		tabHost.addTab(spec);

		tabHost.setCurrentTab(0);
	}
}
