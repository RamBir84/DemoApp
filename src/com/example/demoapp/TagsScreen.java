package com.example.demoapp;

import java.util.ArrayList;

import com.example.demoapp.infrastructure.ListTagItem;
import com.example.demoapp.infrastructure.MainListAdapter;
import com.example.demoapp.infrastructure.TagByLocation;
import com.example.demoapp.infrastructure.TagListAdapter;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


public class TagsScreen extends Activity {
	private ListView mainTagContainer;
	public static boolean isOnline = false;
	ImageButton btnClosePopup, btnSendTag;	
	private PopupWindow pwindo;
	FrameLayout blur_layout;
	static Location userLocation;
	ArrayList<TagByLocation> fakeTags;


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tags_screen);
		blur_layout = (FrameLayout) findViewById(R.id.tagScreenFrame);
		blur_layout.getForeground().setAlpha(0);
		;
		if (!isMyServiceRunning(geofencingService.class)) {
			startService(new Intent(getBaseContext(), geofencingService.class));
		}
		userLocation = geofencingService.userLocation;

		// Fake tags data
		ArrayList<double[]> locationValues = new ArrayList<double[]>();
		locationValues.add(new double[]{32.164573732085216, 34.846692737191916});
		locationValues.add(new double[]{32.164941560481445, 34.84806066378951});
		locationValues.add(new double[]{32.16795678912813, 34.83754640445113});
		locationValues.add(new double[]{32.16464638966391, 34.84786754474044});
		locationValues.add(new double[]{32.164582814285744, 34.847985561937094});
		fakeTags = new ArrayList<TagByLocation>();
		for (int i = 0; i < locationValues.size(); i++) {
			Location loc = new Location("i");
			loc.setLatitude(locationValues.get(i)[0]);
			loc.setLongitude(locationValues.get(i)[1]);
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
		
		Toast.makeText(this, "Open menu(Tag)", Toast.LENGTH_SHORT).show();	
	}


	// Search Button
	public void onClickAdd(final View view) {
		initiatePopupWindow();
	}


	// Tag Item
	public void onClickItem(final View view) {
		startActivity(new Intent(this, NewHomeScreen.class));
		Toast.makeText(this, "Tag was sent", Toast.LENGTH_SHORT).show();	
	}

	// Add tag popup
	private void initiatePopupWindow() {

		// We need to get the instance of the LayoutInflater
		LayoutInflater inflater = (LayoutInflater) TagsScreen.this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.activity_add_tag_popup,
				(ViewGroup) findViewById(R.id.add_tag_popup));

		pwindo = new PopupWindow(layout, 700, 500, false);
		pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);
		btnClosePopup = (ImageButton) layout.findViewById(R.id.btn_close_add_Tag);
		btnClosePopup.setOnClickListener(cancel_add_tag_click_listener);
		btnSendTag = (ImageButton) layout.findViewById(R.id.btn_send_tag);
		btnSendTag.setOnClickListener(send_tag_listener);
		// blur background and disable layout
		blur_layout.getForeground().setAlpha(190);
		pwindo.setFocusable(true);
		pwindo.update();
	}
	private OnClickListener cancel_add_tag_click_listener = new OnClickListener() {
		public void onClick(View v) {
			// restore blur and enable layout
			blur_layout.getForeground().setAlpha(0); 
			pwindo.dismiss();
		}
	};	
	
	private OnClickListener send_tag_listener = new OnClickListener() {
		public void onClick(View v) {
			Toast.makeText(TagsScreen.this, "Tag Was Added", Toast.LENGTH_SHORT).show();	
			blur_layout.getForeground().setAlpha(0); 
			pwindo.dismiss();
			userLocation = geofencingService.userLocation;
			String tag = "test";
			fakeTags.add(new TagByLocation(tag,userLocation));
			
			for (int i = 0; i < fakeTags.size(); i++) {
				System.out.println("tag: " + fakeTags.get(i).tag);
				System.out.println("tag location: " + fakeTags.get(i).tag_location.getLatitude() + ", " + fakeTags.get(i).tag_location.getLongitude());
			}
		}
	};	
	
	
	
	
	
	
	

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

