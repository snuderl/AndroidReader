package org.snuderl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NewsAdapter extends ArrayAdapter<NewsMessage> {
	private final Context context;
	private final List<NewsMessage> values;
	private boolean[] allowedCategories;
	private String[] categories;
	
	private List<NewsMessage> news;

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
		try{
		TextView textView = (TextView) rowView.findViewById(R.id.Title);
		textView.setText(item.Title);
		textView = (TextView) rowView.findViewById(R.id.Short);
		textView.setText(item.Short);
		textView = (TextView) rowView.findViewById(R.id.Date);
		textView.setText(item.Date);
		}catch(Exception e){
			
		Log.e("ListView", "Title "+ item);
		}

		return rowView;
	}
	
	public NewsMessage get(int position){
		return values.get(position);
	}
}
