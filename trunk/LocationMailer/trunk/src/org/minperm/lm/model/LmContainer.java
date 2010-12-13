package org.minperm.lm.model;

import android.location.Location;

public class LmContainer {
	private long lastUpdateDate;

	private long updateInterval;

	private String updateEmailAddress;

	private Location location;

	private static LmContainer instance;

	LmContainer() {

	}

	public static LmContainer getInstance() {
		if (instance == null) {
			instance = SettingsDao.getInstance().getLmContainer();
			instance.setUpdateEmailAddress("victor.semenov@gmail.com");
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
}
