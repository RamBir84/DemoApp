package com.example.demoapp.infrastructure;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.demoapp.R;
import com.example.demoapp.TagsScreen;

public class TagListAdapter extends ArrayAdapter<ListTagItem> {

	private Context context;
	private ArrayList<ListTagItem> items;

	/**
	 * Adapter for main list objects
	 * 
	 * @param context
	 * @param values
	 *        Array list of ListTagItem objects
	 */
	public TagListAdapter(Context context, ArrayList<ListTagItem> values) {
		super(context, R.layout.list_header_blue, values); // fix that
		this.context = context;
		this.items = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = null;

			rowView = inflater.inflate(R.layout.list_tag_item, parent, false);

			// Set the tag  content
			TextView album = (TextView) rowView.findViewById(R.id.listTagContent);
			album.setText(items.get(position).tag_content);
			
			// Set the Item position
			LinearLayout listItem = (LinearLayout) rowView.findViewById(R.id.tag_list_item);
			listItem.setTag(new Integer(position));
			
		return rowView;
	}

}
