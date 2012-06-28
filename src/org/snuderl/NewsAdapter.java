package org.snuderl;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

import org.snuderl.mobilni.NewsMessage;

import android.content.Context;
import android.net.ParseException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NewsAdapter extends ArrayAdapter<NewsMessage> {
	private final Context context;
	private final List<NewsMessage> values;

	public NewsAdapter(Context context, List<NewsMessage> values) {
		super(context, R.layout.row, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.row, parent, false);
		NewsMessage item = getItem(position);
		try {
			TextView textView = (TextView) rowView.findViewById(R.id.Title);
			textView.setText(item.Title);
			textView = (TextView) rowView.findViewById(R.id.Short);
			textView.setText(item.Short);
			textView = (TextView) rowView.findViewById(R.id.Date);
			textView.setText(item.Date);
		} catch (Exception e) {

			Log.e("ListView", "Title " + item);
		}

		return rowView;
	}

	public LinkedHashMap<String, Integer> GetCategories() {
		LinkedHashMap<String, Integer> map = new LinkedHashMap<String, Integer>();
		for (NewsMessage nm : this.values) {
			if (map.containsKey(nm.toString())) {
				map.put(nm.toString(), map.get(nm.toString()) + 1);
			} else {
				map.put(nm.toString(), 1);
			}
		}
		return map;
	}

	@Override
	public void add(NewsMessage object) {
		super.add(object);
	}

	public void Sort() {
		this.sort(new Comparator<NewsMessage>() {

			public int compare(NewsMessage lhs, NewsMessage rhs) {
				long ld = toMiliSeconds(lhs.Date);
				long rd = toMiliSeconds(rhs.Date);
				if (ld > rd)
					return 1;
				if (rd < ld)
					return -1;
				return 0;
			}
		});
	}

	public static Long toMiliSeconds(final String iso8601string) {
		String s = iso8601string.substring(0, iso8601string.length() - 2);
		Date date = null;
		try {
			date = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss").parse(s);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date.getTime();
	}

	public void AddRange(List<NewsMessage> list) {
		for (NewsMessage m : list) {
			this.add(m);
		}
		Sort();
		notifyDataSetChanged();
	}
}
