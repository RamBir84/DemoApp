package com.example.demoapp.infrastructure;

import android.graphics.Bitmap;


// This object represent a contacts list
public class ListItem {

	public String contact_name;
	public String Location;
	public Bitmap profile_pic;
	public String icon_status; //online, request_sent, request_received, answer_received, offline.
	public String header = "";
	public boolean isHeader = false;

	//Constructor
	public ListItem(String contact_name, String Location, Bitmap profile_pic, String icon_status, String ID) {
		this.contact_name = contact_name;
		this.Location = Location;
		this.profile_pic = profile_pic;
		this.icon_status = icon_status;
	}
}
// General information - name, picture, ID
// personal relationship - icon_status, location