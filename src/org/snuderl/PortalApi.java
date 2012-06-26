package org.snuderl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

public class PortalApi {
	public final String host = "http://mobilniportalnovic.apphb.com/feed";
	public final String Click = "Click";
	private final ApplicationState state = ApplicationState
			.GetApplicationState();

	public String ReportClick(String NewsId, String userId) throws Exception {
		RestClient client = new RestClient(host + "/" + Click);
		client.AddParam("NewsId", NewsId);
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		client.AddParam("ClickDate", dateFormat.format(date));
		client.AddParam("UserId", userId);
		client.AddParam("Location", GetLocation());
		client.Execute(RestClient.RequestMethod.POST);
		return client.getResponse();
	}

	public String GetLocation() {
		Location l = state.Location;
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

}
