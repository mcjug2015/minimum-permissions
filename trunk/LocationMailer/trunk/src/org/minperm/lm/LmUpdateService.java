package org.minperm.lm;

import java.io.FileNotFoundException;

import org.minperm.lm.model.LmContainer;
import org.minperm.lm.model.SettingsDao;
import org.minperm.lm.model.action.MailAction;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

/**
 * Simple demo service that schedules a timer task to write something to the log
 * at regular intervals.
 * 
 * @author BMB
 * 
 */
public class LmUpdateService extends Service {
	/**
	 * Log tag for this service.
	 */
	private final String LOGTAG = "LmUpdateService";

	/**
	 * The function that runs in our worker thread
	 */
	Runnable mTask = new Runnable() {
		public void run() {
			if (LmContainer.getInstance().getLmStatus().isSendingUpdates()) {
				long curTime = System.currentTimeMillis();
				LmContainer.getInstance().getLmStatus().setLastUpdateDate(
						curTime);
				if (isConnectionAvailable() && isLocationAvailable()) {
					new MailAction(getBaseContext()).run();
					LmContainer.getInstance().getLmStatus()
							.setFailedLastUpdate(false);
				} else {
					LmContainer.getInstance().getLmStatus()
							.setFailedLastUpdate(true);
				}
				// if the failedlastupdate changed we gotta save the
				// settings
				try {
					SettingsDao.getInstance().saveLmStatus(
							LmContainer.getInstance().getLmStatus());
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			// Done with our work... stop the service!
			LmUpdateService.this.stopSelf();
		}
	};

	private boolean isLocationAvailable() {
		return true;
	}

	private boolean isConnectionAvailable() {
		return true;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(LOGTAG, "created");
		SettingsDao.getInstance().setContext(this);
		Thread thr = new Thread(null, mTask, "AlarmService_Service");
		thr.start();
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
}
