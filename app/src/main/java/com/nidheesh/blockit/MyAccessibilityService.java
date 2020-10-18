package com.nidheesh.blockit;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

public class MyAccessibilityService extends AccessibilityService {
	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {

		if (event.getEventType() == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
			Log.d("BlockitLogs", "Event notification state changed");
			Log.d("BlockitLogs", String.valueOf(event.getText()));

			startService(new Intent(this, NotService.class));
		}
	}

	@Override
	public void onInterrupt() {

	}
}
