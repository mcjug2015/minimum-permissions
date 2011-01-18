package org.minperm.lm;

import org.minperm.lm.model.LmContainer;
import org.minperm.lm.model.SettingsDao;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Simple receiver that will handle the boot completed intent and send the
 * intent to launch the LmUpdateService.
 * 
 * @author BMB
 * 
 */
public class LmBootReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(final Context context, final Intent bootintent) {
		SettingsDao.getInstance().setContext(context);
		if (LmContainer.getInstance().getLmStatus().isSendingUpdates()) {
			PendingIntent mAlarmSender = PendingIntent.getService(context, 0,
					new Intent(context, LmUpdateService.class), 0);
			long firstTime = LmContainer.getInstance().getLmStatus()
					.getLastUpdateDate()
					+ LmContainer.getInstance().getUpdateInterval();

			// Schedule the alarm!
			AlarmManager am = (AlarmManager) context
					.getSystemService(Activity.ALARM_SERVICE);
			am
					.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
							firstTime, LmContainer.getInstance()
									.getUpdateInterval(), mAlarmSender);
			
			if (LmContainer.getInstance().getLmStatus().isFailedLastUpdate()) {
				Intent mServiceIntent = new Intent();
				mServiceIntent.setAction("org.minperm.lm.LmUpdateService");
				context.startService(mServiceIntent);
			}
		}
	}
}
