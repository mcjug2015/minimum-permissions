package org.minperm.lm.ui;

import java.io.FileNotFoundException;

import org.minperm.lm.BootDemoService;
import org.minperm.lm.R;
import org.minperm.lm.model.LmContainer;
import org.minperm.lm.model.SettingsDao;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsView extends LinearLayout {

	private PendingIntent mAlarmSender;
	private TextView emailTextView;
	private RadioGroup updateIntervalRg;
	private RadioButton fiveMinutes;
	private RadioButton weekly;
	private RadioButton daily;
	private RadioButton twelveHours;
	private Button setSettingsButton;

	public SettingsView(Context context) {
		super(context);
		mAlarmSender = PendingIntent.getService(getContext(), 0, new Intent(
				getContext(), BootDemoService.class), 0);
		initUi();
		updateFromLmContainer();
	}

	private void initUi() {
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		setOrientation(LinearLayout.VERTICAL);

		TextView emailLabel = new TextView(getContext());
		emailLabel.setText(R.string.sv_email_label_text);

		emailTextView = new EditText(getContext());
		emailTextView
				.setText(LmContainer.getInstance().getUpdateEmailAddress());

		TextView updateIntervalLabel = new TextView(getContext());
		updateIntervalLabel.setText(R.string.sv_update_interval_label_text);

		fiveMinutes = new RadioButton(getContext());
		fiveMinutes.setText("5 minutes");
		twelveHours = new RadioButton(getContext());
		twelveHours.setText(R.string.sv_12_hours_rb_text);
		daily = new RadioButton(getContext());
		daily.setText(R.string.sv_daily_rb_text);
		weekly = new RadioButton(getContext());
		weekly.setText(R.string.sv_weekly_rb_text);
		updateIntervalRg = new RadioGroup(getContext());
		updateIntervalRg.addView(fiveMinutes);
		updateIntervalRg.addView(twelveHours);
		updateIntervalRg.addView(daily);
		updateIntervalRg.addView(weekly);

		setSettingsButton = new Button(getContext());
		setSettingsButton.setText(R.string.sv_set_current_button_text);
		setSettingsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					saveCurrentToContainer();
				} catch (FileNotFoundException e) {
					Log.e("SettingsView ", "Error saving settings "
							+ e.getMessage() + e.getStackTrace().toString());
				}
			}
		});

		addView(emailLabel);
		addView(emailTextView);
		addView(updateIntervalLabel);
		addView(updateIntervalRg);
		addView(setSettingsButton);

		Button button = new Button(getContext());
		button.setText("Start Alarm");
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startRepeatingAlarm();
			}
		});
		addView(button);

		button = new Button(getContext());
		button.setText("Stop alarm");
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				stopRepeatingAlarm();
			}
		});
		addView(button);
	}

	public void startRepeatingAlarm() {
		long firstTime = SystemClock.elapsedRealtime();

		// Schedule the alarm!
		AlarmManager am = (AlarmManager) getContext().getSystemService(
				Activity.ALARM_SERVICE);
		am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime,
				60 * 1000, mAlarmSender);

		// Tell the user about what we did.
		Toast.makeText(getContext(), "Began sending location updates",
				Toast.LENGTH_LONG).show();
	}

	public void stopRepeatingAlarm() {
		// And cancel the alarm.
		AlarmManager am = (AlarmManager) getContext().getSystemService(
				Activity.ALARM_SERVICE);
		am.cancel(mAlarmSender);

		// Tell the user about what we did.
		Toast.makeText(getContext(), "Stopped sending location updates",
				Toast.LENGTH_LONG).show();
	}

	public void updateFromLmContainer() {
		long interval = LmContainer.getInstance().getUpdateInterval();
		if (interval == 12 * 60 * 60 * 1000) {
			twelveHours.setChecked(true);
		} else if (interval == 24 * 60 * 60 * 1000) {
			daily.setChecked(true);
		} else if (interval == 7 * 24 * 60 * 60 * 1000) {
			weekly.setChecked(true);
		} else if (interval == 5 * 60 * 1000) {
			fiveMinutes.setChecked(true);
		}

		emailTextView
				.setText(LmContainer.getInstance().getUpdateEmailAddress());
	}

	public void saveCurrentToContainer() throws FileNotFoundException {
		LmContainer.getInstance().setUpdateEmailAddress(
				emailTextView.getText().toString());

		if (twelveHours.isChecked()) {
			LmContainer.getInstance().setUpdateInterval(12 * 60 * 60 * 1000);
		} else if (daily.isChecked()) {
			LmContainer.getInstance().setUpdateInterval(24 * 60 * 60 * 1000);
		} else if (weekly.isChecked()) {
			LmContainer.getInstance()
					.setUpdateInterval(7 * 24 * 60 * 60 * 1000);
		} else if (fiveMinutes.isChecked()) {
			LmContainer.getInstance().setUpdateInterval(5 * 60 * 1000);
		}

		SettingsDao.getInstance().saveLmContainer(LmContainer.getInstance());
	}
}
