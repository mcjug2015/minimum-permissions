package org.minperm.lm.model;

import android.location.Location;
import android.location.LocationManager;

public class LmContainer {

	private LmStatus lmStatus;

	private static LmContainer instance;

	private LmContainer() {

	}

	public static LmContainer getInstance() {
		if (instance == null) {
			instance = new LmContainer();
			instance.lmStatus = SettingsDao.getInstance().getLmStatus();
		}

		return instance;
	}

	public long getLastUpdateDate() {
		return lmStatus.getLastUpdateDate();
	}

	public void setLastUpdateDate(long lastUpdateDate) {
		lmStatus.setLastUpdateDate(lastUpdateDate);
	}

	public long getUpdateInterval() {
		return lmStatus.getUpdateInterval();
	}

	public void setUpdateInterval(long updateInterval) {
		lmStatus.setUpdateInterval(updateInterval);
	}

	public String getUpdateEmailAddress() {
		return lmStatus.getEmailAddress();
	}

	public void setUpdateEmailAddress(String updateEmailAddress) {
		lmStatus.setEmailAddress(updateEmailAddress);
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
		return lmStatus.getEmailPassword();
	}

	public void setEmailPassword(String emailPassword) {
		lmStatus.setEmailPassword(emailPassword);
	}

	public LmStatus getLmStatus() {
		return lmStatus;
	}
	
}
