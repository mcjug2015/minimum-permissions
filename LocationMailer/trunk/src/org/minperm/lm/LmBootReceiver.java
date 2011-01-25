package org.minperm.lm;

import org.minperm.lm.model.LmContainer;
import org.minperm.lm.model.SettingsDao;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

/**
 * Simple receiver that will handle the boot completed intent and send the
 * intent to launch the LmUpdateService.
 * 
 * @author BMB
 * 
 */
public class LmBootReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(final Context context, final Intent intent) {
		if (intent.getAction().equals(
				android.net.ConnectivityManager.CONNECTIVITY_ACTION)) {
			boolean noConnection = intent.getBooleanExtra(
					ConnectivityManager.EXTRA_NO_CONNECTIVITY, true);
			if (!noConnection) {
				SettingsDao.getInstance().setContext(context);
				if (LmContainer.getInstance().getLmStatus().isSendingUpdates()
						&& LmContainer.getInstance().getLmStatus()
								.isFailedLastUpdate()) {
					Intent sendOnce = new Intent(context, LmUpdateService.class);
					context.startService(sendOnce);
				}
			}
		} else {
			doBootWork(context);
		}
	}

	private void doBootWork(Context context) {
		SettingsDao.getInstance().setContext(context);
		if (LmContainer.getInstance().getLmStatus().isSendingUpdates()) {
			PendingIntent mAlarmSender = PendingIntent.getService(context, 0,
					new Intent(context, LmUpdateService.class), 0);
			// Schedule the alarm!
			AlarmManager am = (AlarmManager) context
					.getSystemService(Activity.ALARM_SERVICE);
			am
					.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0,
							LmContainer.getInstance().getUpdateInterval(),
							mAlarmSender);
		}
	}
}
