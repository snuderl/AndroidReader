package org.snuderl.click;

import org.snuderl.PortalApi;

import android.os.AsyncTask;
import android.util.Log;


public class ClickCounter extends AsyncTask<Click, Void, Void> {
	@Override
	protected Void doInBackground(Click... params) {
		PortalApi api = new PortalApi();
		try {
			api.ReportClick(params[0].id, params[0].userId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("Report click", "Failed");
			e.printStackTrace();
		}
		return null;
	}

}