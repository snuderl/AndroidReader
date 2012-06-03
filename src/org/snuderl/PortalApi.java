package org.snuderl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.snuderl.RestClient.RequestMethod;

import android.util.Log;

public class PortalApi {
	public final String host = "http://mobilniportalnovic.apphb.com/feed";
	public final String Click = "Click";

	public String ReportClick(String NewsId, String userId) throws Exception {
		RestClient client = new RestClient(host + "/" + Click);
		client.AddParam("NewsId", NewsId);
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		client.AddParam("ClickDate", dateFormat.format(date));
		client.AddParam("UserId", userId);
		client.AddParam("Location", "null");
		client.Execute(RequestMethod.POST);
		return client.getResponse();
	}

	public void LoadNews(NewsMessage news) {
		RestClient client = new RestClient(news.link);
		String response = client.getResponse();
		try {
			JSONObject object = new JSONObject(response);
			news.Category=object.getString("category");
			news.Content = object.getString("content");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
