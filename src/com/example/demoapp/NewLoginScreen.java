package com.example.demoapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

public class NewLoginScreen extends Activity {

	/*
	enum States {connected, online, sent}
	States currentState;
	
	public void setState(States value) {
		currentState = value;
	}
	
	public States getState() {
		return currentState;
	}
	*/
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_login_screen);
	}
	
	
	// Login Button
	public void onClickLogin(View view){
		startActivity(new Intent(this, NewHomeScreen.class));
		
		Spinner mySpinner=(Spinner) findViewById(R.id.spinner);
		String text = mySpinner.getSelectedItem().toString();
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();	

	}
}
