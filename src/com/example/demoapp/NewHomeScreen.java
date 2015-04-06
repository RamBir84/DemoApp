package com.example.demoapp;


import java.util.ArrayList;

import com.example.demoapp.infrastructure.ListItem;
import com.example.demoapp.infrastructure.MainListAdapter;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;


public class NewHomeScreen extends Activity {
	private ListView mainContainer;
	Button btnClosePopup;	
	private PopupWindow pwindo;
	private int position;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_home_screen);



		// Some data
		ArrayList<ListItem> fakeData = new ArrayList<ListItem>();
		fakeData.add(new ListItem("Or Bokobza", "I am in the library", BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.profile_photo), "answer_received"));
		fakeData.add(new ListItem("", "Empty name", BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.profile_photo), "request_sent"));
		fakeData.add(new ListItem("Empty message", "", BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.profile_photo), "request_received"));
		fakeData.add(new ListItem("Or Bokobza 3", "Check without image",null, "online"));
		fakeData.add(new ListItem("Or Bokobza4", "I am in the cafeteria", BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.profile_photo), "online"));
		fakeData.add(new ListItem("Or Bokobza", "I am in the library", BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.profile_photo), "offline"));
		fakeData.add(new ListItem("Or Bokobza", "I am in the library", BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.profile_photo), "offline"));
		fakeData.add(new ListItem("Or Bokobza", "I am in the library", BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.profile_photo), "offline"));
		fakeData.add(new ListItem("Or Bokobza", "I am in the library", BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.profile_photo), "offline"));
		fakeData.add(new ListItem("Or Bokobza", "I am in the library", BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.profile_photo), "offline"));

		mainContainer = (ListView)findViewById(R.id.mainContainer);
		ListAdapter listAdapter = new MainListAdapter(this, fakeData);
		mainContainer.setAdapter(listAdapter);
	}


	// Menu Button
	public void onClickMenu(View view){
		Toast.makeText(this, "Open menu", Toast.LENGTH_SHORT).show();	
	}

	// Search Button
	public void onClickSearch(View view) {
		Toast.makeText(this, "Open search", Toast.LENGTH_SHORT).show();	
	}


	// ListICon Button
	public void onClickListIcon(View view) {
		position = (Integer)view.getTag();
		if (view.getId() == 1){
			Toast.makeText(this, "Location request was send to: *__* " + position, Toast.LENGTH_SHORT).show(); // Online
		}
		if (view.getId() == 2){
			Toast.makeText(this, "Location request was already sent" + position, Toast.LENGTH_SHORT).show(); // Request_sent
		}
		if (view.getId() == 3){
			startActivity(new Intent(this, TagsScreen.class));
			Toast.makeText(this, "*Open the tags screen*" + position, Toast.LENGTH_SHORT).show(); // Request_received
		}
		if (view.getId() == 4){
			initiatePopupWindow(view);
			Toast.makeText(this, "*Open the answer popup*: " + position, Toast.LENGTH_SHORT).show(); // Answer_received

		}
		if (view.getId() == 5){
			Toast.makeText(this, "The user is currently not on campus" + position, Toast.LENGTH_SHORT).show(); // Offline
		}
		//MainListAdapter.items.get(position).contact_name;
	}

	// ListProfile Button
	public void onClickListProfile(View view) {
		Toast.makeText(this, "Open contact profile", Toast.LENGTH_SHORT).show();	
	}



	private void initiatePopupWindow(View view) {

		// We need to get the instance of the LayoutInflater
		LayoutInflater inflater = (LayoutInflater) NewHomeScreen.this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.activity_answer_popup,
				(ViewGroup) findViewById(R.id.popup_element));

		pwindo = new PopupWindow(layout, 700, 500, false);
		pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);
		btnClosePopup = (Button) layout.findViewById(R.id.btn_close_popup);
		btnClosePopup.setOnClickListener(cancel_button_click_listener);


		// Set the contact name
		TextView contactName = (TextView) layout.findViewById(R.id.answer_contact_name);
		contactName.setText(MainListAdapter.items.get(position).contact_name);

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
			pwindo.dismiss();
		}
	};	

}
