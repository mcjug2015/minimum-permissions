package org.minperm.lm.model;

import android.location.Location;
import android.location.LocationManager;

public class LmContainer {
	private long lastUpdateDate;

	private long updateInterval;

	private String updateEmailAddress;

	private String emailPassword;

	private Location location;

	private static LmContainer instance;

	LmContainer() {

	}

	public static LmContainer getInstance() {
		if (instance == null) {
			instance = SettingsDao.getInstance().getLmContainer();
		}

		return instance;
	}

	public long getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(long lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public long getUpdateInterval() {
		return updateInterval;
	}

	public void setUpdateInterval(long updateInterval) {
		this.updateInterval = updateInterval;
	}

	public String getUpdateEmailAddress() {
		return updateEmailAddress;
	}

	public void setUpdateEmailAddress(String updateEmailAddress) {
		this.updateEmailAddress = updateEmailAddress;
	}

	public Location getLocation(Object locationManager) {
		Location retval = null;
		LocationManager lm = (LocationManager) locationManager;
		if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			retval = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		} else if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			retval = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		} else if (lm.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
			retval = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
		}

		return retval;
	}

	public String getEmailPassword() {
		return emailPassword;
	}

	public void setEmailPassword(String emailPassword) {
		this.emailPassword = emailPassword;
	}
}
