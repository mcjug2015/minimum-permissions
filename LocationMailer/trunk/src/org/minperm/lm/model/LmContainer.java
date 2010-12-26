package org.minperm.lm.model;

import android.location.Location;

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

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getEmailPassword() {
		return emailPassword;
	}

	public void setEmailPassword(String emailPassword) {
		this.emailPassword = emailPassword;
	}
}
