package org.snuderl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Xml;

public class FeedParser {
	static final String PUB_DATE = "pubDate";
	static final String DESCRIPTION = "description";
	static final String LINK = "link";
	static final String TITLE = "title";
	static final String ITEM = "item";
	static final String ID = "guid";
	static final String CATEGORY = "category";
	private int page = 0;

	final String feedUrl;
	final String userId;

	protected FeedParser(String feedUrl, String userId) {
		this.feedUrl = feedUrl;
		this.userId = userId;
	}

	protected InputStream getInputStream(String uri) {
		try {
			return new URL(uri).openConnection().getInputStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public List<NewsMessage> more() {
		page = page + 1;
		String url = feedUrl + "?" + "page" + "=" + page + "&userId=" + userId;
		return parse(url);
	}

	public List<NewsMessage> parse() {
		return parse(feedUrl + "?" + "userId=" + userId);
	}

	public List<NewsMessage> parse(String URI) {

		final List<NewsMessage> messages = new ArrayList<NewsMessage>();
		final NewsMessage currentMessage = new NewsMessage();
		RootElement root = new RootElement("rss");
		Element channel = root.getChild("channel");
		Element item = channel.getChild(ITEM);
		item.setEndElementListener(new EndElementListener() {
			public void end() {
				messages.add(currentMessage.copy());
			}
		});
		item.getChild(ID).setEndTextElementListener(
				new EndTextElementListener() {
					public void end(String body) {
						currentMessage.Id = (body);
					}
				});
		item.getChild(TITLE).setEndTextElementListener(
				new EndTextElementListener() {
					public void end(String body) {
						currentMessage.Title = (body);
					}
				});
		item.getChild(LINK).setEndTextElementListener(
				new EndTextElementListener() {
					public void end(String body) {
						currentMessage.Link = (body);
					}
				});
		item.getChild(DESCRIPTION).setEndTextElementListener(
				new EndTextElementListener() {
					public void end(String body) {
						currentMessage.Short = (body);
					}
				});
		item.getChild(PUB_DATE).setEndTextElementListener(
				new EndTextElementListener() {
					public void end(String body) {
						currentMessage.Date = (body);
					}
				});
		item.getChild(CATEGORY).setEndTextElementListener(new EndTextElementListener() {
			
			public void end(String body) {
				currentMessage.Category=(body);				
			}
		});
		try {
			Xml.parse(this.getInputStream(URI), Xml.Encoding.UTF_8,
					root.getContentHandler());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return messages;
	}
}
