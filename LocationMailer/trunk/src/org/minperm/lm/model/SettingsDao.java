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

	public LmContainer getLmContainer() {
		LmContainer lmContainer = new LmContainer();

		try {
			FileInputStream in = context.openFileInput(FILENAME);
			Properties properties = new Properties();
			properties.load(in);
			lmContainer.setUpdateEmailAddress(properties.getProperty("email",
					"victor.semenov@gmail.com"));
			lmContainer
					.setEmailPassword(properties.getProperty("password", ""));
			lmContainer.setUpdateInterval(Long.valueOf(properties.getProperty(
					"interval", "300000")));
		} catch (IOException e) {
			lmContainer.setUpdateEmailAddress("victor.semenov@gmail.com");
			lmContainer.setEmailPassword("");
			lmContainer.setUpdateInterval(5 * 60 * 1000);
		}

		return lmContainer;
	}

	public void saveLmContainer(LmContainer lmContainer)
			throws FileNotFoundException {
		FileOutputStream os = context.openFileOutput(FILENAME,
				Context.MODE_WORLD_WRITEABLE);
		Properties properties = new Properties();
		properties.put("email", lmContainer.getUpdateEmailAddress());
		properties.put("password", lmContainer.getEmailPassword());
		properties.put("interval", String.valueOf(lmContainer
				.getUpdateInterval()));
		properties.save(os, "Location Mailer Properties from "
				+ System.currentTimeMillis());

	}

	public void setContext(Context context) {
		this.context = context;
	}

}
