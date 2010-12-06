package org.minperm.lm;

import java.util.Timer;
import java.util.TimerTask;

import org.minperm.lm.model.LmContainer;
import org.minperm.lm.model.action.MailAction;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
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
	 * Delay until first exeution of the Log task.
	 */
	private final long mDelay = 0;
	/**
	 * Period of the Log task.
	 */
	private final long mPeriod = 60 * 1000;
	/**
	 * Log tag for this service.
	 */
	private final String LOGTAG = "BootDemoService";
	/**
	 * Timer to schedule the service.
	 */
	private Timer mTimer;

	/**
	 * Implementation of the timer task.
	 */
	private class LogTask extends TimerTask implements Runnable {
		public void run() {
			LmContainer.getInstance().setLocation(getBestLocation());
			new MailAction().run();
		}
	}

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

	private LogTask mLogTask;

	@Override
	public IBinder onBind(final Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(LOGTAG, "created");
		mTimer = new Timer();
		mLogTask = new LogTask();
	}

	@Override
	public void onStart(final Intent intent, final int startId) {
		super.onStart(intent, startId);
		Log.i(LOGTAG, "started");
		mTimer.schedule(mLogTask, mDelay, mPeriod);
	}
}
