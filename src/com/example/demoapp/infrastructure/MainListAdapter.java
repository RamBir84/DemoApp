package com.example.demoapp.infrastructure;

import java.util.ArrayList;

import com.example.demoapp.NewHomeScreen;
import com.example.demoapp.R;
import com.example.demoapp.R.color;


import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
		super(context, R.layout.new_list_item, values); // fix that
		this.context = context;
		this.items = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = null;

			rowView = inflater.inflate(R.layout.new_list_item, parent, false);
			
			// Set the profile picture(picture, position and status)
			ImageButton profilePicture = (ImageButton) rowView.findViewById(R.id.listProfileImage);
			Drawable profileImageAsDrawable = new BitmapDrawable(context.getResources(), items.get(position).profile_pic);
			profilePicture.setImageDrawable(profileImageAsDrawable);
			profilePicture.setTag(new Integer(position));
			if (items.get(position).icon_status == "offline"){
				profilePicture.setId(5);
			} else {
				profilePicture.setId(1);
			}
			
			// Set the contact name
			TextView contactName = (TextView) rowView.findViewById(R.id.listContactName);
			contactName.setText(items.get(position).contact_name);
			
			// Set the contact location
			TextView Location = (TextView) rowView.findViewById(R.id.listLocation);
			Location.setText(items.get(position).Location);

			// Set the contact icon
			ImageButton searchIcon = (ImageButton) rowView.findViewById(R.id.listIconImage);
			Drawable IconImageAsDrawable = context.getResources().getDrawable(R.drawable.ic_icon_offline);
			searchIcon.setTag(new Integer(position));
				// Set the position and ID for onClickListIcon method
			if (items.get(position).icon_status == "online"){
				IconImageAsDrawable = context.getResources().getDrawable(R.drawable.ic_icon_online_bg);
				searchIcon.setId(1);
			} else {
				if (items.get(position).icon_status == "request_sent"){
					IconImageAsDrawable = context.getResources().getDrawable(R.drawable.ic_request_sent);
					searchIcon.setId(2);
				} else { 
					if (items.get(position).icon_status == "request_received"){
						IconImageAsDrawable = context.getResources().getDrawable(R.drawable.ic_icon_request_received_bg);
						searchIcon.setId(3);
					} else { 
						if (items.get(position).icon_status == "answer_received"){
							IconImageAsDrawable = context.getResources().getDrawable(R.drawable.ic_icon_answer_received_bg);
							searchIcon.setId(4);
						} else {
								IconImageAsDrawable = context.getResources().getDrawable(R.drawable.ic_icon_offline);
								searchIcon.setId(5);
								contactName.setTextColor(color.medium_grey);
								Location.setText(null);
							}
						}
					}
				}
			searchIcon.setImageDrawable(IconImageAsDrawable);
			
		return rowView;
	}
}
