package com.example.demoapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class NewLoginScreen extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_login_screen);
	}
	
	
	// Login Button
	public void onClickLogin(View view){
		Toast.makeText(this, "Open main screen", Toast.LENGTH_SHORT).show();	
		startActivity(new Intent(this, NewHomeScreen.class));
	}
}
