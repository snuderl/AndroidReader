package org.snuderl.click;

import org.snuderl.web.PortalApi;

import android.os.AsyncTask;
import android.util.Log;


public class ClickCounter extends AsyncTask<Click, Void, Void> {
	@Override
	protected Void doInBackground(Click... params) {
		PortalApi api = new PortalApi();
		try {
			String response = api.ReportClick(params[0]);
			Log.d("Click response", response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("Report click", "Failed");
		}
		return null;
	}

}