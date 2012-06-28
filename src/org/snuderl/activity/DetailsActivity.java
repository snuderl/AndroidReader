package org.snuderl.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.snuderl.R;
import org.snuderl.State;
import org.snuderl.R.id;
import org.snuderl.R.layout;
import org.snuderl.mobilni.NewsMessage;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

public class DetailsActivity extends Activity {
	TextView title, pubDate, Category;
	WebView content;
	NewsMessage news = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details);

		news = State.getState().selected;
		title = (TextView) findViewById(R.id.detailsTitle);
		pubDate = (TextView) findViewById(R.id.detailsPubDate);
		Category = (TextView) findViewById(R.id.detailsCategory);
		content = (WebView) findViewById(R.id.detailsContent);

		title.setText(news.Title);
		pubDate.setText(news.Date);

		// Zamenjano. WTF
		Category.setText(news.Category);
		String body = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
				+ "<html><head>"
				+ "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />"
				+ "</head><body>" + news.Content + "</body></html>";

		float maxAllowed = 250;
		StringBuilder sb = new StringBuilder();

		while (body.indexOf("<img") > 0) {
			int i = body.indexOf("<img");
			sb.append(body.substring(0, i));
			body = body.substring(i);

			try {
				int h = body.indexOf("height");
				int w = body.indexOf("width");
				if (h > 0 && w > 0) {
					String hs = body.substring(h + 8, h + 11);
					String ws = body.substring(w + 7, w + 10);
					int height = Integer.parseInt(hs);
					int width = Integer.parseInt(ws);
					int max = Math.max(height, width);
					if (max > maxAllowed) {
						float ratio = maxAllowed / max;
						int nheight = (int) (ratio * height);
						int nwidth = (int) (ratio * width);
						
						sb.append("<img src=\"");
						int srcStart = body.indexOf("src=\"");
						String tmp = body.substring(srcStart+5);
						sb.append(tmp.subSequence(0, tmp.indexOf("\"")));
						sb.append("\" ");
						
						sb.append("width=\""+nwidth+"\"" );
						sb.append("height=\""+nheight+"\"" );
						sb.append(" alt ");
						
						sb.append(">");
					}
				}

				else {
					break;
				}
			} catch (Exception e) {
			}
			int end = body.indexOf(">");
			body = body.substring(end + 1);
		}
		sb.append(body);
		body = sb.toString();

		content.loadData(body, "text/html; charset=utf-8", "UTF-8");

	}
}
