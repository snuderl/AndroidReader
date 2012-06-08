package org.snuderl;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

public class DetailsActivity extends Activity{
	TextView title, pubDate, Category;
	WebView content;
	NewsMessage news = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details);
		
		news = State.getState().selected;
		title = (TextView)findViewById(R.id.detailsTitle);
		pubDate = (TextView)findViewById(R.id.detailsPubDate);
		Category = (TextView)findViewById(R.id.detailsCategory);
		content = (WebView)findViewById(R.id.detailsContent);
		
		title.setText(news.Title);
		pubDate.setText(news.Date);
		
		//Zamenjano. WTF
		Category.setText(news.Category);
		String body =  "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"+
			       "<html><head>"+
			       "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />"+
			       "<head><body>"
			       +news.Content+"</body></html>";
		content.loadData(body, "text/html; charset=utf-8", "UTF-8");
		
		
	}
}
