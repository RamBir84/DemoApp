package com.example.demoapp;


import java.util.ArrayList;

import com.example.demoapp.infrastructure.ListItem;
import com.example.demoapp.infrastructure.MainListAdapter;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class NewHomeScreen extends Activity   {
	// Design fields
	private ListView mainContainer;
	ImageButton btnClosePopup;	
	private PopupWindow pwindo;
	private int position, textLength;
	FrameLayout blur_layout;
	ArrayList<ListItem> fakeData, array_sort;
	LinearLayout searchBoxLayout;
	EditText searchBox;
	ListAdapter listAdapter;


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_home_screen);
		blur_layout = (FrameLayout) findViewById(R.id.newScreenFrame);
		blur_layout.getForeground().setAlpha(0);
		searchBoxLayout = (LinearLayout) findViewById(R.id.topBarMain);

		// Some data
		fakeData = new ArrayList<ListItem>();
		fakeData.add(new ListItem("Or Bokobza", "Last seen in the library", BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.profile_photo), "answer_received", "ID", "10.04.15,  10:04"));
		fakeData.add(new ListItem("Barr Solnik", "Last seen in the cafeteria", BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.profile_photo), "request_sent", "ID", "10.04.15,  12:00"));
		fakeData.add(new ListItem("Ram Birbrayer", "Last seen in class L101", BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.profile_photo), "request_received", "ID", "10.04.15,  13:08"));
		fakeData.add(new ListItem("Jesse Ritz", "Last seen in the main entrance",BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.profile_photo), "online", "ID", "10.04.15,  14:04"));
		fakeData.add(new ListItem("Clara Lutzky", "", BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.profile_photo), "online", "ID", ""));
		fakeData.add(new ListItem("Maya Klein", "Last seen in some place", BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.profile_photo), "offline", "ID", "09.04.15,  17:52"));
		fakeData.add(new ListItem("Dotan Jakoby", "Last seen in some place", BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.profile_photo), "offline", "ID", "09.04.15,  14:34"));
		fakeData.add(new ListItem("Irina Afanasyeva", "Last seen in some place", BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.profile_photo), "offline", "ID", "08.04.15,  20:40"));
		fakeData.add(new ListItem("Hagar Bass", "Last seen in some place", BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.profile_photo), "offline", "ID", "09.04.15,  10:04"));
		fakeData.add(new ListItem("Ariel Ben Moshe", "Last seen in some place", BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.profile_photo), "offline", "ID", "08.04.15,  10:04"));
		mainContainer = (ListView)findViewById(R.id.mainContainer);
		listAdapter = new MainListAdapter(this, fakeData);
		//*End of design part*
		if (!isMyServiceRunning(geofencingService.class)) {
			startService(new Intent(getBaseContext(), geofencingService.class));
		}

		array_sort = new ArrayList<ListItem>(fakeData);

		final MainListAdapter adapter=new MainListAdapter(this, array_sort); 
		mainContainer.setAdapter(adapter);

		textLength = 0;
		searchBox = (EditText) findViewById(R.id.searchBox);
		searchBox.addTextChangedListener(new TextWatcher()
		{
			public void afterTextChanged(Editable s)
			{
			}
			public void beforeTextChanged(CharSequence s,
					int start, int count, int after)
			{
			}
			public void onTextChanged(CharSequence s,
					int start, int before, int count)
			{

				//get the text in the EditText
				String searchString=searchBox.getText().toString();
				textLength=searchString.length();
				//clear the initial data set
				array_sort.clear();
				for(int i=0;i < fakeData.size();i++)
				{
					String name=fakeData.get(i).contact_name;
					if(textLength<=name.length()){
						//compare the String in EditText with Names in the ArrayList
						if(searchString.equalsIgnoreCase((String)name.substring(0,textLength))){
							array_sort.add(fakeData.get(i));
						}
					}
				}
				adapter.notifyDataSetChanged();
			}
		});
	}

	// Menu Button
	public void onClickMenu(View view){
		Toast.makeText(this, "Open menu", Toast.LENGTH_SHORT).show();	
	}

	// Search Button
	public void onClickSearch(View view) {
		searchBoxLayout.setVisibility(View.INVISIBLE);
		EditText yourEditText= (EditText) findViewById(R.id.searchBox);
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(yourEditText, InputMethodManager.SHOW_IMPLICIT);
	}

	// Exit search button
	public void onClickExitSearch(View view){
		searchBoxLayout.setVisibility(View.VISIBLE);
	}

	public void onClickListIcon(View view) {
		position = (Integer)view.getTag();


		if (view.getId() == 1){ // Online
			view.setSelected(true);
			Toast.makeText(this, "Location request was sent to: " + MainListAdapter.items.get(position).contact_name, Toast.LENGTH_SHORT).show(); 
			view.setId(2);
			MainListAdapter.items.get(position).icon_status = "request_sent";
		} else { 
			if (view.getId() == 2){ // Request_sent
				Toast.makeText(this, "Location request was already sent", Toast.LENGTH_SHORT).show(); 
			}
		}
		if (view.getId() == 3){ // Request_received
			startActivity(new Intent(this, TagsScreen.class));
			view.setSelected(true);
			view.setId(1);
			MainListAdapter.items.get(position).icon_status = "online";
		}
		if (view.getId() == 4){ // Answer_received
			view.setSelected(true);
			view.setId(1);
			initiatePopupWindow(view);
			MainListAdapter.items.get(position).icon_status = "online";
		}
		if (view.getId() == 5){ // Offline
			Toast.makeText(this, "The user is currently not on campus", Toast.LENGTH_SHORT).show(); 
		}
	}


	// ListProfile Button
	public void onClickListProfile(View view) {
		position = (Integer)view.getTag();
		initiatePopupWindow(view);
	}

	public void onClickMenuButton(View view) {

	}


	// Profile popup
	private void initiatePopupWindow(View view) {

		// We need to get the instance of the LayoutInflater
		LayoutInflater inflater = (LayoutInflater) NewHomeScreen.this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.activity_answer_popup,
				(ViewGroup) findViewById(R.id.popup_element));

		pwindo = new PopupWindow(layout, 700, 500, false);
		pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);
		btnClosePopup = (ImageButton) layout.findViewById(R.id.btn_close_popup);
		btnClosePopup.setOnClickListener(cancel_button_click_listener);
		// blur background and disable layout
		blur_layout.getForeground().setAlpha(190);
		pwindo.setFocusable(true);
		pwindo.update();

		// Set the contact name
		TextView contactName = (TextView) layout.findViewById(R.id.answer_contact_name);
		contactName.setText(MainListAdapter.items.get(position).contact_name);

		// Set date and date
		TextView contactDate = (TextView) layout.findViewById(R.id.answer_location_time);
		contactDate.setText(MainListAdapter.items.get(position).tagDateTime);

		// Set status (online/offline) message
		TextView contactStatus = (TextView) layout.findViewById(R.id.answer_status);
		if (view.getId() == 5){
			contactStatus.setText("User is currently Offline");
		} else {
			contactStatus.setText("User is currently Online");
		}


		// Set the contact location
		TextView Location = (TextView) layout.findViewById(R.id.answer_location);
		Location.setText(MainListAdapter.items.get(position).Location);

		// Set the profile picture
		ImageView profilePicture = (ImageView) layout.findViewById(R.id.answer_profile_picture);
		Drawable profileImageAsDrawable = new BitmapDrawable(NewHomeScreen.this.getResources(),
				MainListAdapter.items.get(position).profile_pic);
		profilePicture.setImageDrawable(profileImageAsDrawable);

	}
	private OnClickListener cancel_button_click_listener = new OnClickListener() {
		public void onClick(View v) {
			// restore blur and enable layout
			blur_layout.getForeground().setAlpha(0); 
			pwindo.dismiss();
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
