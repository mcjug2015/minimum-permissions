package org.minperm.lm.ui;

import java.io.FileNotFoundException;

import org.minperm.lm.LmService;
import org.minperm.lm.LmUpdateService;
import org.minperm.lm.R;
import org.minperm.lm.model.LmContainer;
import org.minperm.lm.model.SettingsDao;
import org.minperm.lm.model.action.MailAction;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsView extends LinearLayout {

	@SuppressWarnings("unused")
	private PendingIntent mAlarmSender;
	private TextView emailTextView;
	private TextView passwordTextView;
	private RadioGroup updateIntervalRg;
	private RadioButton fiveMinutes;
	private RadioButton weekly;
	private RadioButton daily;
	private RadioButton twelveHours;
	private Button setSettingsButton;
	private CheckBox sendingUpdatesCb;

	public SettingsView(Context context) {
		super(context);
		mAlarmSender = PendingIntent.getService(getContext(), 0, new Intent(
				getContext(), LmUpdateService.class), 0);
		initUi();
	}

	private void initUi() {
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		setOrientation(LinearLayout.VERTICAL);

		TextView emailLabel = new TextView(getContext());
		emailLabel.setText(R.string.sv_email_label_text);

		emailTextView = new EditText(getContext());
		passwordTextView = new EditText(getContext());
		passwordTextView.setTransformationMethod(PasswordTransformationMethod
				.getInstance());

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
				saveCurrentToContainer();
				try {
					SettingsDao.getInstance().saveLmStatus(
							LmContainer.getInstance().getLmStatus());
				} catch (FileNotFoundException e) {
					Log.e("LM settingsview: ", "Error saving settings "
							+ e.getMessage() + e.getStackTrace().toString());
					e.printStackTrace();
				}
			}
		});

		addView(emailLabel);
		addView(emailTextView);
		addView(passwordTextView);
		addView(updateIntervalLabel);
		addView(updateIntervalRg);
		addView(setSettingsButton);

		LinearLayout ll = new LinearLayout(getContext());
		ll.setOrientation(HORIZONTAL);

		sendingUpdatesCb = new CheckBox(getContext());
		sendingUpdatesCb.setText("Send Updates");
		sendingUpdatesCb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CheckBox currentSUCb = (CheckBox) v;
				saveCurrentToContainer();
				LmContainer.getInstance().getLmStatus().setSendingUpdates(
						currentSUCb.isChecked());
				try {
					SettingsDao.getInstance().saveLmStatus(
							LmContainer.getInstance().getLmStatus());
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (currentSUCb.isChecked()) {

					Intent intent = new Intent(getContext(), LmService.class);
					getContext().startService(intent);
				} else {
					PendingIntent mAlarmSender = PendingIntent.getService(
							getContext(), 0, new Intent(getContext(),
									LmUpdateService.class), 0);
					AlarmManager am = (AlarmManager) getContext()
							.getSystemService(Activity.ALARM_SERVICE);
					am.cancel(mAlarmSender);
				}
			}
		});
		ll.addView(sendingUpdatesCb);

		Button button = new Button(getContext());
		button.setText("Send Location");
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					saveCurrentToContainer();
					new MailAction(getContext()).run();
				} catch (Exception e) {
					Toast.makeText(
							getContext(),
							"Error! " + e.toString() + " "
									+ e.getStackTrace()[0].toString(),
							Toast.LENGTH_LONG).show();
				}

			}
		});
		ll.addView(button);
		addView(ll);
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
		passwordTextView.setText(LmContainer.getInstance().getEmailPassword());

		sendingUpdatesCb.setChecked(LmContainer.getInstance().getLmStatus()
				.isSendingUpdates());
	}

	public void saveCurrentToContainer() {
		LmContainer.getInstance().setUpdateEmailAddress(
				emailTextView.getText().toString());
		LmContainer.getInstance().setEmailPassword(
				passwordTextView.getText().toString());

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
	}
}
