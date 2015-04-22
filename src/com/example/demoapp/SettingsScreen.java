package com.example.demoapp;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

public class SettingsScreen extends PreferenceActivity   {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// add the xml resource 
		getFragmentManager().beginTransaction().replace(android.R.id.content, new MySettingsFragment()).commit();



	}


	public static class MySettingsFragment extends PreferenceFragment
	{
		@Override
		public void onCreate(final Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.settings);
		}
	}


} 

