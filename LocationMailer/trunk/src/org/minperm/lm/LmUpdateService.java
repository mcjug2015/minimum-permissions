package org.minperm.lm;

import org.minperm.lm.model.LmContainer;
import org.minperm.lm.model.SettingsDao;
import org.minperm.lm.model.action.MailAction;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
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

	private LocationManager locationManager;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(LOGTAG, "created");
		SettingsDao.getInstance().setContext(this);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, locationListener);
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
	}

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

	private LocationListener locationListener = new LocationListener() {

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onLocationChanged(Location location) {
			new MailAction(LmUpdateService.this, location).run();
			LmContainer.getInstance().getLmStatus().setFailedLastUpdate(false);
			LmContainer.getInstance().getLmStatus().setLastUpdateDate(
					System.currentTimeMillis());
			locationManager.removeUpdates(this);
		}
	};
}
