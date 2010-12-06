package org.minperm.lm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Simple receiver that will handle the boot completed intent and send the
 * intent to launch the BootDemoService.
 * 
 * @author BMB
 * 
 */
public class BootDemoReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(final Context context, final Intent bootintent) {
		Intent mServiceIntent = new Intent();
		mServiceIntent.setAction("org.minperm.lm.BootDemoService");
		context.startService(mServiceIntent);
	}
}
