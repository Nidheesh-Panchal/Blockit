package com.nidheesh.blockit;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class MyAccessibilityService extends AccessibilityService {

	public static final String TAG = "BlockitLogs";

	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {

		Log.d(TAG, "Accessibility event.");
		if (event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
			Log.d(TAG, "Event notification state changed");

			String packageName = (String) event.getPackageName();
			if(packageName.equalsIgnoreCase("com.whatsapp") ||
			packageName.equalsIgnoreCase("com.instagram.android"))
			{
				Log.d(TAG, "Start Notification Listener service.");
				Intent mIntent = new Intent(this, NotService.class);
				startService(mIntent);
			}
		}
	}

	@Override
	public void onInterrupt() {

	}
}
