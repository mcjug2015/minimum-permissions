package org.minperm.lm;

import org.minperm.lm.ui.SettingsView;

import android.app.Activity;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Base code blantantly ripped off of
 * http://hejp.co.uk/android/android-gps-example/
 * 
 * @author faldureon
 * 
 */
public class LocationMailer extends Activity {

	/*
	 * this class implements LocationListener, which listens for both changes in
	 * the location of the device and changes in the status of the GPS system.
	 */

	static final String tag = "Main"; // for Log
	LocationManager lm;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new SettingsView(this));
	}

	@Override
	protected void onStop() {
		finish();
		super.onStop();
	}
}