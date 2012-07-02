package org.snuderl.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;
import org.snuderl.ApplicationState;
import org.snuderl.mobilni.NewsMessage;
import org.snuderl.web.RestClient.RequestMethod;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

public class PortalApi {
	public final String host = "http://mobilniportalnovic.apphb.com/feed";
	public final String Click = "Click";
	private final ApplicationState state = ApplicationState
			.GetApplicationState();

	public String ReportClick(org.snuderl.click.Click click) throws Exception {
		RestClient client = new RestClient(host + "/" + Click);
		client.AddParam("NewsId", click.id);
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		client.AddParam("ClickDate", dateFormat.format(date));
		client.AddParam("token", click.token);
		if (click.location != null) {
			client.AddParam("Longitude",
					Double.toString(click.location.getLongitude()));
			client.AddParam("Latitude",
					Double.toString(click.location.getLatitude()));
		}
		client.Execute(RestClient.RequestMethod.POST);
		return client.getResponse();
	}

	public String GetLocation() {
		Location l = state.GetLocation();
		String s;
		if (l == null) {
			s = "null";
		} else {
			s = l.getLongitude() + "|" + l.getLatitude();

		}

		Log.d("Location", s);
		return s;

	}

	public void LoadNews(NewsMessage news) {
		RestClient client = new RestClient(news.Link);
		try {
			client.Execute(RestClient.RequestMethod.GET);
		} catch (Exception e) {
			Log.e("LoadNews", "Error getting news Json");
			e.printStackTrace();
		}

		String response = client.getResponse();
		if (response != null) {
			try {

				JSONObject o = new JSONObject(response);

				news.Content = o.getString("Content");
				news.Category = o.getString("Category");
				// String date = o.getString("PubDate");
				// Date d = new Date(date);
				// SimpleDateFormat f = new SimpleDateFormat();
				// news.Date = f.format(d);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			Log.e("LoadNews", "EmptyResponse");
		}
	}

	public String Register(String username, String password) {
		StringBuilder sb = new StringBuilder();
		sb.append(host + "/register");
		RestClient client = new RestClient(sb.toString());
		client.AddParam("username", username);
		client.AddParam("password", password);
		try {
			client.Execute(RequestMethod.POST);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String response = client.getResponse();
		return response;
	}

	public String Login(String username, String password) {
		StringBuilder sb = new StringBuilder();
		sb.append(host + "/login");
		RestClient client = new RestClient(sb.toString());
		client.AddParam("username", username);
		client.AddParam("password", password);
		try {
			client.Execute(RequestMethod.POST);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String response = client.getResponse();
		return response;
	}
}
