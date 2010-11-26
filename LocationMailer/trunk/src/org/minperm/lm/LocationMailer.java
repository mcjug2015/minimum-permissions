package org.minperm.lm;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Base code blantantly ripped off of
 * http://hejp.co.uk/android/android-gps-example/
 * 
 * @author faldureon
 * 
 */
public class LocationMailer extends Activity implements LocationListener {

	/*
	 * this class implements LocationListener, which listens for both changes in
	 * the location of the device and changes in the status of the GPS system.
	 */

	static final String tag = "Main"; // for Log

	TextView txtInfo;
	LocationManager lm;
	StringBuilder sb;
	int noOfFixes = 0;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/* get TextView to display the GPS data */
		txtInfo = new TextView(this);

		/*
		 * the location manager is the most vital part it allows access to
		 * location and GPS status services
		 */
		lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		setContentView(txtInfo);
		sendEmail();
	}

	@Override
	protected void onResume() {
		/*
		 * onResume is is always called after onStart, even if the app hasn't
		 * been paused
		 * 
		 * add location listener and request updates every 1000ms or 10m
		 */
		lm
				.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
						10f, this);
		super.onResume();
	}

	@Override
	protected void onPause() {
		/* GPS, as it turns out, consumes battery like crazy */
		lm.removeUpdates(this);
		super.onResume();
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.v(tag, "Location Changed");

		sb = new StringBuilder(512);

		noOfFixes++;

		/* display some of the data in the TextView */

		sb.append("No. of Fixes: ");
		sb.append(noOfFixes);
		sb.append('\n');
		sb.append('\n');

		sb.append("Londitude: ");
		sb.append(location.getLongitude());
		sb.append('\n');

		sb.append("Latitude: ");
		sb.append(location.getLatitude());
		sb.append('\n');

		sb.append("Altitiude: ");
		sb.append(location.getAltitude());
		sb.append('\n');

		sb.append("Accuracy: ");
		sb.append(location.getAccuracy());
		sb.append('\n');

		sb.append("Timestamp: ");
		sb.append(location.getTime());
		sb.append('\n');

		txtInfo.setText(sb.toString());
	}

	@Override
	public void onProviderDisabled(String provider) {
		/* this is called if/when the GPS is disabled in settings */
		Log.v(tag, "Disabled");

		/* bring up the GPS settings */
		Intent intent = new Intent(
				android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivity(intent);
	}

	@Override
	public void onProviderEnabled(String provider) {
		Log.v(tag, "Enabled");
		Toast.makeText(this, "GPS Enabled", Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		/* This is called when the GPS status alters */
		switch (status) {
		case LocationProvider.OUT_OF_SERVICE:
			Log.v(tag, "Status Changed: Out of Service");
			Toast.makeText(this, "Status Changed: Out of Service",
					Toast.LENGTH_SHORT).show();
			break;
		case LocationProvider.TEMPORARILY_UNAVAILABLE:
			Log.v(tag, "Status Changed: Temporarily Unavailable");
			Toast.makeText(this, "Status Changed: Temporarily Unavailable",
					Toast.LENGTH_SHORT).show();
			break;
		case LocationProvider.AVAILABLE:
			Log.v(tag, "Status Changed: Available");
			Toast.makeText(this, "Status Changed: Available",
					Toast.LENGTH_SHORT).show();
			break;
		}
	}

	@Override
	protected void onStop() {
		/*
		 * may as well just finish since saving the state is not important for
		 * this toy app
		 */
		finish();
		super.onStop();
	}

	public void sendEmail() {
		Mail m = new Mail("victor.semenov@gmail.com", "3rsheam5");

		String[] toArr = { "victor.semenov@gmail.com" };
		m.setTo(toArr);
		m.setFrom("victor.semenov@gmail.com");
		m.setSubject("Location Mailer Update.");
		m.setBody("Phone's current location is: ");

		try {

			if (m.send()) {
				Toast.makeText(this, "Email was sent successfully.",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(this, "Email was not sent.", Toast.LENGTH_LONG)
						.show();
			}
		} catch (Exception e) {
			// Toast.makeText(MailApp.this,
			// "There was a problem sending the email.",
			// Toast.LENGTH_LONG).show();
			Log.e("MailApp", "Could not send email", e);
		}
	}
}