package org.snuderl;

import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.Toast;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.*;
import android.content.*;

public class TabWidget extends TabActivity {

	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null;
	}

	public void RegisterGpsUpdates() {
		// Acquire a reference to the system Location Manager
		LocationManager locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);

		// Define a listener that responds to location updates
		LocationListener locationListener = new LocationListener() {
			public void onLocationChanged(Location l) {
				ApplicationState.GetApplicationState().Location = l;
				
			}

			public void onProviderEnabled(String provider) {
			}

			public void onProviderDisabled(String provider) {
			}

			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub

			}
		};

		// Register the listener with the Location Manager to receive location
		// updates
		locationManager.requestLocationUpdates(
				LocationManager.GPS_PROVIDER, 0, 0, locationListener);
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_host);
		if (!isNetworkAvailable()) {
			Toast.makeText(
					getApplicationContext(),
					"Internet connecton is required for this application to work.",
					Toast.LENGTH_LONG).show();

			TabWidget.this.finish();
		}

		else {

			final SharedPreferences settings = getSharedPreferences(
					"org.snuderl.settings", 2);
			final ApplicationState state = ApplicationState
					.GetApplicationState();
			
			RegisterGpsUpdates();
			
			try {
				state.Categories = Category.GetCategories();
			} catch (Exception e) {
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
			spec = tabHost.newTabSpec("personalized")
					.setIndicator("Personalized").setContent(intent);
			tabHost.addTab(spec);

			// Do the same for the other tabs
			intent = new Intent().setClass(this, MainActivity.class);
			intent.putExtra("userId", "1");
			intent.putExtra("all", true);
			spec = tabHost.newTabSpec("all").setIndicator("All")
					.setContent(intent);
			tabHost.addTab(spec);

			tabHost.setCurrentTab(0);
		}
	}
}
