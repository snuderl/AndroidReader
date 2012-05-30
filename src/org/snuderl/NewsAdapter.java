package org.snuderl;

import java.util.List;

import android.content.Context;
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
		TextView textView = (TextView) rowView.findViewById(R.id.Title);
		textView.setText(values.get(position).Title);
		textView = (TextView) rowView.findViewById(R.id.Short);
		textView.setText(values.get(position).Short);
		textView = (TextView) rowView.findViewById(R.id.Date);
		textView.setText(values.get(position).Date);

		return rowView;
	}
	
	public NewsMessage get(int position){
		return values.get(position);
	}
}
