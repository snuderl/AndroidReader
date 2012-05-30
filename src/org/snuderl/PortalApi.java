package org.snuderl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.snuderl.RestClient.RequestMethod;

import android.util.Log;

public class PortalApi {
	public final String host="http://mobilniportalnovic.apphb.com/feed";
	public final String Click = "Click"; 
	public final String userId;

	public PortalApi(String userId) {
		this.userId = userId;
	}
	
	public String ReportClick(String NewsId) throws Exception{
		RestClient client = new RestClient(host+"/"+Click);
		client.AddParam("NewsId", NewsId);
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		client.AddParam("ClickDate", dateFormat.format(date));
		client.AddParam("UserId", userId);
		client.AddParam("Location", "null");
		client.Execute(RequestMethod.POST);
		return client.getResponse();
	}
	
	
}
