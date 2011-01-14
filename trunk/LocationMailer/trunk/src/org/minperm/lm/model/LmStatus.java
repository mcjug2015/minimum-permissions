package org.minperm.lm.model;

public class LmStatus {

	private long updateInterval;
	private long lastUpdateDate;
	private String emailAddress;
	private String emailPassword;
	private boolean failedLastUpdate;
	private boolean sendingUpdates;

	public LmStatus(long updateInterval, long lastUpdateDate,
			String emailAddress, String emailPassword, boolean failedLastUpdate) {
		this.updateInterval = updateInterval;
		this.lastUpdateDate = lastUpdateDate;
		this.emailAddress = emailAddress;
		this.emailPassword = emailPassword;
		this.failedLastUpdate = failedLastUpdate;
	}

	public LmStatus() {

	}

	public long getUpdateInterval() {
		return updateInterval;
	}

	public void setUpdateInterval(long updateInterval) {
		this.updateInterval = updateInterval;
	}

	public long getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(long lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getEmailPassword() {
		return emailPassword;
	}

	public void setEmailPassword(String emailPassword) {
		this.emailPassword = emailPassword;
	}

	public boolean isFailedLastUpdate() {
		return failedLastUpdate;
	}

	public void setFailedLastUpdate(boolean failedLastUpdate) {
		this.failedLastUpdate = failedLastUpdate;
	}

	public boolean isSendingUpdates() {
		return sendingUpdates;
	}

	public void setSendingUpdates(boolean sendingUpdates) {
		this.sendingUpdates = sendingUpdates;
	}

	
}
