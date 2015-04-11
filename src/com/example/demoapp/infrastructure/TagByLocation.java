package com.example.demoapp.infrastructure;

import android.location.Location;

public class TagByLocation {

	public String tag;
	public Location tag_location;
	
	public TagByLocation(String tag, Location tag_location){
		this.tag = tag;
		this.tag_location = tag_location;
	}
}

