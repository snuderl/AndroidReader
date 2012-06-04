package org.snuderl;

import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends Activity {
	
	EditText et = null;
	SharedPreferences settings;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		
		
		settings  = getSharedPreferences("org.snuderl.settings", 0);
		
		et = (EditText)findViewById(R.id.userIdTextField);
		et.setText(settings.getString("userId", "1"));
		
		Button save = (Button)findViewById(R.id.saveSettings);
		save.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Editor e = settings.edit();
				e.putString("userId", et.getText().toString());
				e.commit();
				
			}
		});
		

	}
}
