package com.example.demoapp.infrastructure;

import java.util.ArrayList;

import com.example.demoapp.NewHomeScreen;
import com.example.demoapp.R;


import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MainListAdapter extends ArrayAdapter<ListItem> {

	private Context context;
	public static ArrayList<ListItem> items;

	/**
	 * Adapter for main list objects
	 * 
	 * @param context
	 * @param values
	 *        Array list of ListItem objects
	 */
	public MainListAdapter(Context context, ArrayList<ListItem> values) {
		super(context, R.layout.list_header_blue, values); // fix that
		this.context = context;
		this.items = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = null;

			rowView = inflater.inflate(R.layout.new_list_item, parent, false);
			
			// Set the profile picture
			ImageButton profilePicture = (ImageButton) rowView.findViewById(R.id.listProfileImage);
			Drawable profileImageAsDrawable = (profilePicture  != null) ? new BitmapDrawable(context.getResources(),
					items.get(position).profile_pic) : context.getResources().getDrawable(R.drawable.default_profile_pic);
			profilePicture.setImageDrawable(profileImageAsDrawable);
			
			// Set the contact name
			TextView contantName = (TextView) rowView.findViewById(R.id.listContactName);
			contantName.setText(items.get(position).contact_name);

			// Set the contact location
			TextView Location = (TextView) rowView.findViewById(R.id.listLocation);
			Location.setText(items.get(position).Location);

			// Set the contact icon (can add here some other options/icons)
			ImageButton searchIcon = (ImageButton) rowView.findViewById(R.id.listIconImage);
			Drawable IconImageAsDrawable = context.getResources().getDrawable(R.drawable.ic_icon_offline);
			
			// Set the position and ID for onClickListIcon method (have to fix the problem and change to switch)
			if (items.get(position).icon_status == "online"){
				IconImageAsDrawable = context.getResources().getDrawable(R.drawable.ic_icon_online);
				searchIcon.setTag(new Integer(position));
				searchIcon.setId(1);
			} else {
				if (items.get(position).icon_status == "request_sent"){
					IconImageAsDrawable = context.getResources().getDrawable(R.drawable.ic_request_sent);
					searchIcon.setTag(new Integer(position));
					searchIcon.setId(2);
				} else { 
					if (items.get(position).icon_status == "request_received"){
						IconImageAsDrawable = context.getResources().getDrawable(R.drawable.ic_request_received);
						searchIcon.setTag(new Integer(position));
						searchIcon.setId(3);
					} else { 
						if (items.get(position).icon_status == "answer_received"){
							IconImageAsDrawable = context.getResources().getDrawable(R.drawable.ic_answer_received);
							searchIcon.setTag(new Integer(position));
							searchIcon.setId(4);
						} else {
								IconImageAsDrawable = context.getResources().getDrawable(R.drawable.ic_icon_offline);
								searchIcon.setTag(new Integer(position));
								searchIcon.setId(5);
							}
						}
					}
				}
			searchIcon.setImageDrawable(IconImageAsDrawable);
		return rowView;
	}

}
