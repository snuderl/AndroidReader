package org.snuderl.click;

import android.location.Location;
import android.location.LocationListener;

public class Click {
	public String id;
	public String token;
	public Location location;

	public Click(String id, String token, Location location) {
		this.id = id;
		this.token = token;
		this.location=location;
	}
}