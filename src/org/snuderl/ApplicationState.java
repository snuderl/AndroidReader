package org.snuderl;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.snuderl.activity.UserChanged;
import org.snuderl.mobilni.Category;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class ApplicationState extends Application {
	public static Category[] Categories;
	private static Map<String, String> ChildToParent = null;
	public static Location Location = null;
	
	public static Location GetLocation(){
		if(Location==null || (Location.getLatitude()==0 && Location.getLongitude()==0))
			return null;
		return Location;
	}

	public static UserChanged change = null;

	public static boolean isActivityVisible() {
		return activityVisible;
	}

	public static void activityResumed() {
		activityVisible = true;
	}

	public static void activityPaused() {
		activityVisible = false;
	}

	private static boolean activityVisible;

	public static CharSequence[] Categories() {
		CharSequence[] sequence = new CharSequence[Categories.length];
		for (int i = 0; i < Categories.length; i++) {
			sequence[i] = Categories[i].Name;
		}
		return sequence;
	}

	private static ApplicationState singleton = null;

	public ApplicationState() {
	}

	public static ApplicationState GetApplicationState() {
		if (singleton == null)
			singleton = new ApplicationState();
		return singleton;
	}

	public String GetParent(String child) {
		if (ChildToParent == null) {
			ChildToParent = new LinkedHashMap<String, String>();
			for (Category p : Categories) {
				for (Category c : p.Children) {
					ChildToParent.put(c.Name, p.Name);
				}
			}
		}

		return ChildToParent.get(child);
	}

	public static String GetLoginToken(Context c) {

		SharedPreferences settings = c.getSharedPreferences(
				"org.snuderl.settings", 2);
		String token = settings.getString("token", null);
		return token;
	}

	public static String GetUsername(Context c) {

		SharedPreferences settings = c.getSharedPreferences(
				"org.snuderl.settings", 2);
		String token = settings.getString("username", null);
		return token;
	}

	public static void SetLoginToken(Context c, String token, String username) {

		SharedPreferences settings = c.getSharedPreferences(
				"org.snuderl.settings", 2);
		Editor t = settings.edit();
		t.putString("token", token);
		t.putString("username", username);
		t.commit();
	}

}
