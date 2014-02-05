package org.minperm.lm.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import android.content.Context;
import android.util.Log;

public class SettingsDao {
	private String FILENAME = "settings_file";
	private static String emailPropertyName = "email";
	private static String passwordPropertyName = "password";
	private static String lastUpdateDatePropertyName = "last_update_date";
	private static String updateIntervalPropertyName = "interval";
	private static String failedLastUpdatePropertyName = "failed_last_update";
	private static String sendingUpdatesPropertyName = "sending_updates";
	private static SettingsDao instance;
	Context context;

	private SettingsDao() {

	}

	public static SettingsDao getInstance() {
		if (instance == null) {
			instance = new SettingsDao();
		}
		return instance;
	}

	public LmStatus getLmStatus() {
		LmStatus lmStatus = new LmStatus();

		try {
			FileInputStream in = context.openFileInput(FILENAME);
			Properties properties = new Properties();
			properties.load(in);
			lmStatus.setEmailAddress(properties.getProperty(emailPropertyName,
					"victor.semenov@gmail.com"));
			lmStatus.setEmailPassword(properties.getProperty(
					passwordPropertyName, ""));
			lmStatus.setUpdateInterval(Long.valueOf(properties.getProperty(
					updateIntervalPropertyName, "300000")));
			lmStatus.setLastUpdateDate(Long.valueOf(properties.getProperty(
					lastUpdateDatePropertyName, "0")));
			lmStatus.setFailedLastUpdate(Boolean.valueOf(properties
					.getProperty(failedLastUpdatePropertyName, "false")));
			lmStatus.setSendingUpdates(Boolean.valueOf(properties.getProperty(
					sendingUpdatesPropertyName, "false")));
		} catch (IOException e) {
			lmStatus.setEmailAddress("victor.semenov@gmail.com");
			lmStatus.setEmailPassword("");
			lmStatus.setUpdateInterval(5 * 60 * 1000);
			lmStatus.setLastUpdateDate(0);
			lmStatus.setFailedLastUpdate(false);
			lmStatus.setSendingUpdates(false);
		}

		return lmStatus;
	}

	public void saveLmStatus(LmStatus lmStatus) throws FileNotFoundException {
		FileOutputStream os = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
		Properties properties = new Properties();
		properties.put(emailPropertyName, lmStatus.getEmailAddress());
		properties.put(passwordPropertyName, lmStatus.getEmailPassword());
		properties.put(updateIntervalPropertyName, String.valueOf(lmStatus
				.getUpdateInterval()));
		properties.put(lastUpdateDatePropertyName, String.valueOf(lmStatus
				.getLastUpdateDate()));
		properties.put(failedLastUpdatePropertyName, String.valueOf(lmStatus
				.isFailedLastUpdate()));
		properties.put(sendingUpdatesPropertyName, String.valueOf(lmStatus
				.isSendingUpdates()));
		
		try {
			properties.store(os, "Location Mailer Properties from "
					+ System.currentTimeMillis());
		} catch (IOException e) {
			Log.e("LM main activity: ", "Error saving settings "
					+ e.getMessage() + e.getStackTrace().toString());
		}

	}

	public void setContext(Context context) {
		this.context = context;
	}

}
