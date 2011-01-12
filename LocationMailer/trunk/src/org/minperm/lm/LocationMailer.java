package org.minperm.lm;

import java.io.FileNotFoundException;

import org.minperm.lm.model.LmContainer;
import org.minperm.lm.model.SettingsDao;
import org.minperm.lm.ui.SettingsView;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ScrollView;

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
		settingsView = new SettingsView(this);
		ScrollView scrollView = new ScrollView(this);
		scrollView.addView(settingsView);
		setContentView(scrollView);
	}

	@Override
	protected void onPause() {
		super.onPause();
		SettingsDao.getInstance().setContext(this);
		try {
			settingsView.saveCurrentToContainer();
			SettingsDao.getInstance().saveLmStatus(
					LmContainer.getInstance().getLmStatus());
		} catch (FileNotFoundException e) {
			Log.e("LM main activity: ", "Error saving settings "
					+ e.getMessage() + e.getStackTrace().toString());
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		SettingsDao.getInstance().setContext(this);
		settingsView.updateFromLmContainer();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
}