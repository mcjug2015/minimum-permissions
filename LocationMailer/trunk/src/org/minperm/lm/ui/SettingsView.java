package org.minperm.lm.ui;

import org.minperm.lm.R;
import org.minperm.lm.model.LmContainer;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class SettingsView extends LinearLayout {

	private TextView emailTextView;
	private RadioGroup updateIntervalRg;
	private RadioButton weekly;
	private RadioButton daily;
	private RadioButton twelveHours;
	private Button setSettingsButton;

	public SettingsView(Context context) {
		super(context);
		initUi();
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

		twelveHours = new RadioButton(getContext());
		twelveHours.setText(R.string.sv_12_hours_rb_text);
		daily = new RadioButton(getContext());
		daily.setText(R.string.sv_daily_rb_text);
		weekly = new RadioButton(getContext());
		weekly.setText(R.string.sv_weekly_rb_text);
		updateIntervalRg = new RadioGroup(getContext());
		updateIntervalRg.addView(twelveHours);
		updateIntervalRg.addView(daily);
		updateIntervalRg.addView(weekly);

		setSettingsButton = new Button(getContext());
		setSettingsButton.setText(R.string.sv_set_current_button_text);
		setSettingsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setCurrentSettings();
			}
		});

		addView(emailLabel);
		addView(emailTextView);
		addView(updateIntervalLabel);
		addView(updateIntervalRg);
		addView(setSettingsButton);
	}

	public void setCurrentSettings() {
		LmContainer.getInstance().setUpdateEmailAddress(
				emailTextView.getText().toString());

		if (twelveHours.isChecked()) {
			LmContainer.getInstance().setUpdateInterval(12 * 60 * 60 * 1000l);
		} else if (daily.isChecked()) {
			LmContainer.getInstance().setUpdateInterval(24 * 60 * 60 * 1000l);
		} else if (weekly.isChecked()) {
			LmContainer.getInstance().setUpdateInterval(
					7 * 24 * 60 * 60 * 1000l);
		}
	}
}
