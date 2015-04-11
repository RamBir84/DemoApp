package com.example.demoapp;

import java.util.ArrayList;

import com.example.demoapp.infrastructure.ListTagItem;
import com.example.demoapp.infrastructure.TagByLocation;
import com.example.demoapp.infrastructure.TagListAdapter;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.location.Location;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class TagsScreen extends Activity {
	private ListView mainTagContainer;
	public static boolean isOnline = false;


	static Location userLocation;


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tags_screen);
		
		if (!isMyServiceRunning(geofencingService.class)) {
			startService(new Intent(getBaseContext(), geofencingService.class));
		}
		userLocation = geofencingService.userLocation;

		// Fake tags data
		double[][] locationValues = {{32.164573732085216, 34.846692737191916},{32.164941560481445, 34.84806066378951}
			,{32.16795678912813, 34.83754640445113}, {32.16464638966391, 34.84786754474044}, {32.164582814285744, 34.847985561937094}};
		ArrayList<TagByLocation> fakeTags = new ArrayList<TagByLocation>();
		for (int i = 0; i < locationValues.length; i++) {
			Location loc = new Location("i");
			loc.setLatitude(locationValues[i][0]);
			loc.setLongitude(locationValues[i][1]);
			fakeTags.add(new TagByLocation("this is the " + i + " tag",loc));
		}
		float radius = userLocation.getAccuracy();
		Location tagLocation;
		ArrayList<ListTagItem> tagsInUserLocation = new ArrayList<ListTagItem>();

		for (int i = 0; i < fakeTags.size(); i++) {
			tagLocation = fakeTags.get(i).tag_location;
			if ((userLocation.distanceTo(tagLocation) - tagLocation.getAccuracy()) <= (radius + 50)){
				tagsInUserLocation.add(new ListTagItem(fakeTags.get(i).tag + " Accuracy: " + radius + "  Distance to tag: " + userLocation.distanceTo(tagLocation)));
			}
		}
		mainTagContainer = (ListView)findViewById(R.id.mainTagContainer);
		ListAdapter listAdapter = new TagListAdapter(this, tagsInUserLocation);
		mainTagContainer.setAdapter(listAdapter);

	}

	// Menu Button
	public void onClickTagMenu(final View view) {
		startActivity(new Intent(this, NewHomeScreen.class));
		Toast.makeText(this, "Open menu(Tag)", Toast.LENGTH_SHORT).show();	
	}


	// Search Button
	public void onClickAdd(final View view) {
		Toast.makeText(this, "Open search(Tag)", Toast.LENGTH_SHORT).show();	
	}


	// Tag Item
	public void onClickItem(final View view) {
		int position = (Integer)view.getTag();
		Toast.makeText(this, "Choose this tag" + position, Toast.LENGTH_SHORT).show();	
	}


	// check if the geofencingService is running
	private boolean isMyServiceRunning(Class<geofencingService> serviceClass) {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceClass.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}	
}

