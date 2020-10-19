package com.nidheesh.blockit;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class MyAccessibilityService extends AccessibilityService {

	public static final String TAG = "BlockitLogs";

	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {

		if (event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
			Log.d(TAG, "Event notification state changed");

			Log.d(TAG, "Start Notification Listener service.");
			startService(new Intent(this, NotService.class));
		}
	}

	@Override
	public void onInterrupt() {

	}
}
