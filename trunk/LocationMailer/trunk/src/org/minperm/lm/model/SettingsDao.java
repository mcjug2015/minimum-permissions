package org.minperm.lm.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import android.content.Context;

public class SettingsDao {
	private String FILENAME = "settings_file";
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
			lmStatus.setEmailAddress(properties.getProperty("email",
					"victor.semenov@gmail.com"));
			lmStatus.setEmailPassword(properties.getProperty("password", ""));
			lmStatus.setUpdateInterval(Long.valueOf(properties.getProperty(
					"interval", "300000")));
		} catch (IOException e) {
			lmStatus.setEmailAddress("victor.semenov@gmail.com");
			lmStatus.setEmailPassword("");
			lmStatus.setUpdateInterval(5 * 60 * 1000);
		}

		return lmStatus;
	}

	public void saveLmStatus(LmStatus lmStatus) throws FileNotFoundException {
		FileOutputStream os = context.openFileOutput(FILENAME,
				Context.MODE_WORLD_WRITEABLE);
		Properties properties = new Properties();
		properties.put("email", lmStatus.getEmailAddress());
		properties.put("password", lmStatus.getEmailPassword());
		properties
				.put("interval", String.valueOf(lmStatus.getUpdateInterval()));
		properties.save(os, "Location Mailer Properties from "
				+ System.currentTimeMillis());

	}

	public void setContext(Context context) {
		this.context = context;
	}

}
