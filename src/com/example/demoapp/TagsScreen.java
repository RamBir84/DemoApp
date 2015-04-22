package com.example.demoapp;

import java.util.ArrayList;

import com.example.demoapp.infrastructure.ListTagItem;
import com.example.demoapp.infrastructure.TagListAdapter;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;


public class TagsScreen extends Activity {
	private ListView mainTagContainer;
	public static boolean isOnline = false;
	ImageButton btnClosePopup, btnSendTag;	
	private PopupWindow pwindo;
	FrameLayout blur_layout;
	static Location userLocation;
	ArrayList<ListTagItem> fakeTags;
	String newTag;
	EditText tagEdit;


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tags_screen);
		blur_layout = (FrameLayout) findViewById(R.id.tagScreenFrame);
		blur_layout.getForeground().setAlpha(0);
		
		// Start geofencing service
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
		fakeTags = new ArrayList<ListTagItem>();
		for (int i = 0; i < locationValues.size(); i++) {
			Location loc = new Location("i");
			loc.setLatitude(locationValues.get(i)[0]);
			loc.setLongitude(locationValues.get(i)[1]);
			fakeTags.add(new ListTagItem("", loc));
		}
		fakeTags.get(0).tag = "In the library - first floor";
		fakeTags.get(1).tag = "In the cafeteria";
		fakeTags.get(2).tag = "In class L101";
		fakeTags.get(3).tag = "In the main entrance";
		fakeTags.get(4).tag = "In the miLAb class";
		
		float radius = userLocation.getAccuracy();
		Location tagLocation;
		ArrayList<ListTagItem> tagsInUserLocation = new ArrayList<ListTagItem>();

		for (int i = 0; i < fakeTags.size(); i++) {
			tagLocation = fakeTags.get(i).tag_location;
			if ((userLocation.distanceTo(tagLocation) - tagLocation.getAccuracy()) <= (radius + 1000)){
				tagsInUserLocation.add(new ListTagItem(fakeTags.get(i).tag , tagLocation));
			}
		}
		mainTagContainer = (ListView)findViewById(R.id.mainTagContainer);
		ListAdapter listAdapter = new TagListAdapter(this, tagsInUserLocation);
		mainTagContainer.setAdapter(listAdapter);
		
		
		for (int i = 0; i < fakeTags.size(); i++) {
			System.out.println("tag: " + fakeTags.get(i).tag);
			System.out.println("tag location: " + fakeTags.get(i).tag_location.getLatitude() + ", " + fakeTags.get(i).tag_location.getLongitude());
		}

	}

	// Menu Button
	public void onClickTagMenu(final View view) {
		triggerNotification();
		//Toast.makeText(this, "Open menu(Tag)", Toast.LENGTH_SHORT).show();	
	}

	// Search Button
	public void onClickAdd(final View view) {
		initiatePopupWindow();
	}


	// Tag Item
	public void onClickItem(final View view) {
		// **Have to Add - change the data to: data.icon_status = "online"
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
		tagEdit = (EditText) layout.findViewById(R.id.searchBoxAdd);

		
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
			// close popup and reset blur
			blur_layout.getForeground().setAlpha(0); 
			pwindo.dismiss(); 
			// Take tag string
			String tag = (tagEdit.getText().toString());
			Toast.makeText(TagsScreen.this, "Tag Was sent: " + tag, Toast.LENGTH_SHORT).show();
			// update the tag details
			userLocation = geofencingService.userLocation;
			fakeTags.add(new ListTagItem(tag,userLocation));
			startActivity(new Intent(TagsScreen.this, NewHomeScreen.class));
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
	
	private void triggerNotification() {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
		mBuilder.setSmallIcon(R.drawable.ic_notification);
		mBuilder.setContentTitle("Waldo Notification!");
		mBuilder.setContentText("Hi, This is a Test Notification");
		mBuilder.setDefaults(Notification.DEFAULT_ALL);

		Intent resultIntent = new Intent(this, TagsScreen.class);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(TagsScreen.class);

		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);

		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// notificationID allows you to update the notification later on.
		mNotificationManager.notify(123, mBuilder.build());
	}
}

