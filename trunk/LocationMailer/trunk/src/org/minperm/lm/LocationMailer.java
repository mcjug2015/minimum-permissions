package org.minperm.lm;

import java.io.FileNotFoundException;

import org.minperm.lm.model.SettingsDao;
import org.minperm.lm.ui.SettingsView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Base code blantantly ripped off of
 * http://hejp.co.uk/android/android-gps-example/
 * 
 * @author faldureon
 * 
 */
public class LocationMailer extends Activity {
	SettingsView settingsView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SettingsDao.getInstance().setContext(this);
		settingsView = new SettingsView(this);
		setContentView(settingsView);
	}

	@Override
	protected void onPause() {
		super.onPause();
		try {
			settingsView.saveCurrentToContainer();
		} catch (FileNotFoundException e) {
			Log.e("LM main activity: ", "Error saving settings "
					+ e.getMessage() + e.getStackTrace().toString());
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		settingsView.updateFromLmContainer();
	}

	@Override
	protected void onStop() {
		finish();
		super.onStop();
	}
}