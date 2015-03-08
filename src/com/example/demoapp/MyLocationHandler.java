package com.example.demoapp;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.demoapp.helpers.ServerAsyncParent;
import com.example.demoapp.helpers.ServerCommunicator;
import com.google.android.gms.location.GeofencingEvent;


import android.app.IntentService;
import android.content.Intent;
//import android.content.SharedPreferences;
import android.widget.Toast;


public class MyLocationHandler extends IntentService implements ServerAsyncParent{

	//SharedPreferences settings = getSharedPreferences("UserInfo", 0);
	private boolean onCampus = true;
	
	
	public MyLocationHandler() {
		super("");
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
		geofencingService.geoStatus = geofencingEvent.getGeofenceTransition();		
		
		
		if ((geofencingService.geoStatus == 1) || (geofencingService.geoStatus == 4)) {
			this.sendCheckInToServer(geofencingService.settings.getInt("uid", -5), onCampus);
			Toast.makeText(this, "AUTO : You are inside the IDC", Toast.LENGTH_SHORT).show();
		} else if (geofencingService.geoStatus == 2) {
			this.sendCheckInToServer(geofencingService.settings.getInt("uid", -5), !onCampus);
			Toast.makeText(this, "AUTO : You are outside the IDC", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "AUTO : geoStatus NULL", Toast.LENGTH_SHORT).show();
		}
	}
	
	
	
	public void sendCheckInToServer(int userId, boolean onCampus ) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userId", Integer.toString(userId)));
		params.add(new BasicNameValuePair("onCampus", Boolean.toString(onCampus)));
		new ServerCommunicator(this, params, ServerCommunicator.METHOD_POST)
				.execute("http://ram.milab.idc.ac.il/app_send_chekin.php");
	}

	@Override
	public void doOnPostExecute(JSONObject jObj) {
		try {
			if (jObj.getInt("success") == 1){
				Toast.makeText(this,"status was updated successfuly", Toast.LENGTH_LONG).show();
			}else {
				Toast.makeText(this,"status FAILED to updated", Toast.LENGTH_LONG).show();
			}
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(this,"status FAILED to updated", Toast.LENGTH_LONG).show();
		}
	
	}
}
