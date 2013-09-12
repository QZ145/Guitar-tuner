package com.guitartuner;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity{
	
	public final static String PREFS_KEY_SHOW_FREQBAR = "prefs_key_show_freqbar";
	public final static String PREFS_KEY_SHOW_FREQ = "prefs_key_show_freq";
	public final static String PREFS_KEY_SHOW_MIC_LEVEL = "prefs_key_show_mic_level";
	public final static String PREFS_KEY_SET_BGCOLOR = "prefs_key_set_bgcolor";
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
	
}
