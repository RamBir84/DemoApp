package com.example.demoapp;

import java.util.ArrayList;

import com.example.demoapp.infrastructure.ListTagItem;
import com.example.demoapp.infrastructure.TagListAdapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
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
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tags_screen);
	
		// Some data
		ArrayList<ListTagItem> fakeData = new ArrayList<ListTagItem>();
		fakeData.add(new ListTagItem("This is the first tag"));
		fakeData.add(new ListTagItem("This is the second tag"));
		fakeData.add(new ListTagItem("The next tag is empty"));
		//fakeData.add(new ListTagItem(""));
		fakeData.add(new ListTagItem("This is the third tag and he is very long"));
		//fakeData.add(new ListTagItem("This is the last tag"));
		
		
		mainTagContainer = (ListView)findViewById(R.id.mainTagContainer);
		ListAdapter listAdapter = new TagListAdapter(this, fakeData);
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
	
}

