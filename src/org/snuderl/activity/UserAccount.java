package org.snuderl.activity;

import org.snuderl.ApplicationState;
import org.snuderl.R;
import org.snuderl.R.id;
import org.snuderl.R.layout;
import org.snuderl.web.PortalApi;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;

public class UserAccount extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		final EditText userName = (EditText) findViewById(R.id.username);
		final EditText password = (EditText) findViewById(R.id.password);
		ApplicationState state = ApplicationState.GetApplicationState();

		Button login = (Button) findViewById(R.id.login);
		login.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				PortalApi api = new PortalApi();
				String u = userName.getText().toString();
				String p = password.getText().toString();

				String response = api.Login(u, p);
				String text = "";
				if (response == "") {
					text = "Login failed";
				} else {
					text = "Login succesful.";
					ApplicationState.SetLoginToken(getApplicationContext(),
							response, u);
					notifyChange();
					UserAccount.this.finish();
				}
				Toast.makeText(getApplicationContext(), text,
						Toast.LENGTH_SHORT).show();
			}
		});
		Button register = (Button) findViewById(R.id.register);
		register.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				PortalApi api = new PortalApi();
				String u = userName.getText().toString();
				String p = password.getText().toString();

				String response = api.Register(u, p);
				String text = "";
				if (response == "Account succesffuly created" || response.equals("Account succesffuly created")) {
					text = "Registered";
					response = api.Login(u, p);
					ApplicationState.SetLoginToken(getApplicationContext(),
							response, u);
					notifyChange();
					UserAccount.this.finish();
				} else {
					text = "Failed.";
				}
				Toast.makeText(getApplicationContext(), text,
						Toast.LENGTH_SHORT).show();
			}
		});

	}

	public void notifyChange() {
		UserChanged c = ApplicationState.change;
		if (c != null) {
			c.onChange();
		}

	}
}
