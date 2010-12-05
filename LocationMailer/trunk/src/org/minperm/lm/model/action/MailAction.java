package org.minperm.lm.model.action;

import java.io.IOException;
import java.util.List;

import org.minperm.lm.model.LmContainer;
import org.minperm.lm.model.Mail;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

public class MailAction implements Runnable {
	private Context context;

	public MailAction(Context context) {
		this.context = context;
	}

	@Override
	public void run() {
		Location location = LmContainer.getInstance().getLocation();
		Mail m = new Mail("victor.semenov@gmail.com", "3rsheam5");

		String[] toArr = { LmContainer.getInstance().getUpdateEmailAddress() };
		m.setTo(toArr);
		m.setFrom("victor.semenov@gmail.com");
		m.setSubject("Location Mailer Update.");
		m.setBody(getEmailBody(location));
		try {

			if (m.send()) {
				Toast.makeText(context, "Email was sent successfully.",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(context, "Email was not sent.",
						Toast.LENGTH_LONG).show();
			}
		} catch (Exception e) {
			// Toast.makeText(MailApp.this,
			// "There was a problem sending the email.",
			// Toast.LENGTH_LONG).show();
			Log.e("MailApp", "Could not send email", e);
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
		bodySb.append("Location accuracy: " + location.getAccuracy() + "\n");
		Geocoder gc = new Geocoder(context);
		try {
			List<Address> addresses = gc.getFromLocation(
					location.getLatitude(), location.getLongitude(), 3);
			for (Address address : addresses) {
				bodySb.append("Approx Address: " + address.toString() + "\n");
			}
		} catch (IOException e1) {
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
