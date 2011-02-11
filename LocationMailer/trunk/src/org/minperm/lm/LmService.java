package org.minperm.lm;

import org.minperm.lm.model.LmContainer;
import org.minperm.lm.model.SettingsDao;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

public class LmService extends Service {

	/**
	 * Log tag for this service.
	 */
	private final String LOGTAG = "LmService";

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(LOGTAG, "created");
		SettingsDao.getInstance().setContext(this);
		startUpdates();
	}

	private void startUpdates() {
		if (LmContainer.getInstance().getLmStatus().isSendingUpdates()) {
			PendingIntent mAlarmSender = PendingIntent.getService(
					LmService.this, 0, new Intent(LmService.this,
							LmUpdateService.class), 0);
			// Schedule the alarm!
			AlarmManager am = (AlarmManager) LmService.this
					.getSystemService(Activity.ALARM_SERVICE);
			am
					.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0,
							LmContainer.getInstance().getUpdateInterval(),
							mAlarmSender);
		}
	}

	/**
	 * This is the object that receives interactions from clients. See
	 * RemoteService for a more complete example.
	 */
	private final IBinder mBinder = new Binder() {
		@Override
		protected boolean onTransact(int code, Parcel data, Parcel reply,
				int flags) throws RemoteException {
			return super.onTransact(code, data, reply, flags);
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
}
