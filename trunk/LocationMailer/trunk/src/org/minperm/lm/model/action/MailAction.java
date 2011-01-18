package org.minperm.lm.model.action;

import java.util.List;

import org.minperm.lm.model.LmContainer;
import org.minperm.lm.model.Mail;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

public class MailAction implements Runnable {
	private Context context;

	public MailAction(Context context) {
		this.context = context;
	}

	@Override
	public void run() {
		Location location = null;
		String error = "";
		try {
			location = LmContainer.getInstance().getLocation(
					context.getSystemService(Context.LOCATION_SERVICE));
		} catch (Exception e) {
			error += "Failed to retrieve phone location due to: "
					+ e.toString() + " " + e.getStackTrace().toString();
		}

		Mail m = new Mail(LmContainer.getInstance().getUpdateEmailAddress(),
				LmContainer.getInstance().getEmailPassword());

		String[] toArr = { LmContainer.getInstance().getUpdateEmailAddress() };
		m.setTo(toArr);
		m.setFrom("victor.semenov@gmail.com");
		m.setSubject("Location Mailer Update. " + System.currentTimeMillis());
		m.setBody(error + "\n" + getEmailBody(location));
		try {
			m.send();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("MailApp", "Could not send email to "
					+ LmContainer.getInstance().getUpdateEmailAddress() + " / "
					+ LmContainer.getInstance().getEmailPassword(), e);
		}
	}

	private String getEmailBody(Location location) {
		if (location == null) {
			return getNullLocationEmailBody();
		}

		StringBuilder bodySb = new StringBuilder();
		bodySb.append("Phone's current location is: \n");
		bodySb.append("Latitude: " + location.getLatitude() + "\n");
		bodySb.append("Longtitude: " + location.getLongitude() + "\n");
		bodySb.append("Altitude: " + location.getAltitude() + "\n");
		bodySb.append("Google maps url: http://maps.google.com/?daddr="
				+ location.getLatitude() + "," + location.getLongitude() + "\n");
		bodySb.append("Location accuracy: " + location.getAccuracy() + "\n");
		Geocoder gc = new Geocoder(context);
		try {
			List<Address> addresses = gc.getFromLocation(
					location.getLatitude(), location.getLongitude(), 3);
			for (Address address : addresses) {
				bodySb.append("Approx Address: " + address.toString() + "\n");
			}
		} catch (Exception e1) {
			bodySb.append("Could not convert location to an address\n");
		}
		return bodySb.toString();
	}

	private String getNullLocationEmailBody() {
		StringBuilder bodySb = new StringBuilder();
		bodySb.append("Phone's location could not be determined, maybe all "
				+ "the location providers are disabled?\n");
		return bodySb.toString();
	}

}
