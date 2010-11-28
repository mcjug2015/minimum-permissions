package org.minperm.lm.model;

public class LmContainer {
	private long lastUpdateDate;
	
	private long updateInterval;
	
	private String updateEmailAddress;
	
	private static LmContainer instance;
	
	private LmContainer(){
		
	}
	
	public static LmContainer getInstance(){
		if (instance == null){
			instance = new LmContainer();
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
}
