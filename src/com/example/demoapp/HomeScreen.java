package com.example.demoapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.demoapp.helpers.ServerAsyncParent;
import com.example.demoapp.helpers.ServerCommunicator;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class HomeScreen extends Activity implements ServerAsyncParent {

	private ListView mainList;
	SharedPreferences settings = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_screen);

		mainList = (ListView) findViewById(R.id.mainContainer);

//		Intent intent = getIntent();

		// start the geofencingService if necessary
		if (!isMyServiceRunning(geofencingService.class)) {
			//Toast.makeText(this, "before", Toast.LENGTH_SHORT).show();
			startService(new Intent(getBaseContext(), geofencingService.class));
			//Toast.makeText(this, "after", Toast.LENGTH_SHORT).show();
		}
		
		
//		int UserId = intent.getIntExtra("UserId", -3);
		settings = getSharedPreferences("UserInfo", 0);
		
		int UserId = settings.getInt("uid", -3);
		getDataFromServer(UserId);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	// Method to start the service
	/*
	 * public void startService(View view) { startService(new
	 * Intent(getBaseContext(), geofencingService.class)); }
	 */

	// Method to stop the service
	public void stopService(View view) {
		stopService(new Intent(getBaseContext(), geofencingService.class));
	}

	public void getDataFromServer(int UserId) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userId", Integer.toString(UserId)));
		new ServerCommunicator(this, params, ServerCommunicator.METHOD_GET)
				.execute("http://ram.milab.idc.ac.il/app_get_users.php");
	}

	@Override
	public void doOnPostExecute(JSONObject jObj) {
		try {
			setDataFromServer(jObj.getJSONArray("users"));
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public void setDataFromServer(JSONArray users) {
		try {

			// create the grid item mapping
			String[] from = new String[] { "UserId", "Name", "OnCampus" };
			int[] to = new int[] { R.id.UserId, R.id.Name, R.id.OnCampus };

			// looping through All Users prepare the list of all records
			List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
			for (int i = 0; i < users.length(); i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				JSONObject row = users.getJSONObject(i);
				map.put("UserId", row.getString("UserId"));
				map.put("Name", row.getString("Name"));
				map.put("OnCampus", row.getString("OnCampus"));
				fillMaps.add(map);
			}

			// fill in the grid_item layout
			SimpleAdapter adapter = new SimpleAdapter(this, fillMaps, R.layout.list_item, from, to);
			mainList.setAdapter(adapter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*public void sendCheckInToServer(int userId, boolean onCampus) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userId", Integer.toString(userId)));
		params.add(new BasicNameValuePair("onCampus", Boolean.toString(onCampus)));
		new ServerCommunicator(this, params, ServerCommunicator.METHOD_POST)
				.execute("http://ram.milab.idc.ac.il/app_get_users.php");
	}*/

	// Handler the geofence button
	public void getGeofenceStatus(View view) {

		if ((geofencingService.geoStatus == 1) || (geofencingService.geoStatus == 4)) {
			Toast.makeText(this, "MANUAL : You are inside the IDC", Toast.LENGTH_SHORT).show();
		} else if (geofencingService.geoStatus == 2) {
			Toast.makeText(this, "MANUAL : You are outside the IDC", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "geoStatus NULL", Toast.LENGTH_SHORT).show();
		}
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
