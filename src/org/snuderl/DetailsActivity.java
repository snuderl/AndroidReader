package org.snuderl;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

public class DetailsActivity extends Activity{
	TextView title, pubDate, Category, content;
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
		content = (TextView)findViewById(R.id.detailsContent);
		
		title.setText(news.Title);
		pubDate.setText(news.Date);
		
		//Zamenjano. WTF
		Category.setText(news.Content);
		content.setText(news.Category);
		
		
	}
}
