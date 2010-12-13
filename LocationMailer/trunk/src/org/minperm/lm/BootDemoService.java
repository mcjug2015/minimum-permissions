package org.minperm.lm;

import org.minperm.lm.model.LmContainer;
import org.minperm.lm.model.SettingsDao;
import org.minperm.lm.model.action.MailAction;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
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
public class BootDemoService extends Service {
	/**
	 * Log tag for this service.
	 */
	private final String LOGTAG = "BootDemoService";

	/**
	 * The function that runs in our worker thread
	 */
	Runnable mTask = new Runnable() {
		public void run() {
			LmContainer.getInstance().setLocation(getBestLocation());
			new MailAction().run();
			// Done with our work... stop the service!
			BootDemoService.this.stopSelf();
		}
	};

	public Location getBestLocation() {
		Location retval = null;
		LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
		if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			retval = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		} else if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			retval = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		} else if (lm.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
			retval = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
		}

		return retval;
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
