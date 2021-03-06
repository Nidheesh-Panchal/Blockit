package com.nidheesh.blockit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallReceiver extends BroadcastReceiver {
	public static final String TAG = "BlockitLogs";
	String number;

	@Override
	public void onReceive(Context context, Intent intent) {

		/*
		Because of the permissions the Receiver get the state of the phone.
		When a call is received this function is called.
		 */

		try {
			String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
			number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);

			if(state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)){

				Log.d(TAG, "State ringing");

				try {
					if (number!=null) {
						Log.d(TAG, "Call from : " + number);
						if(shouldBlock())
						{
							Log.d(TAG, "Blocking number.");
							CallActions callActions = new CallActions();
							callActions.endCall(context);
							callActions.showNotifications(context,number);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					Log.e(TAG, "Exception : ", e);
				}
			}

			if(state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_IDLE)){
				Log.d(TAG, "State idle");
			}

		} catch (Exception e) {
			Log.e(TAG, "Exception : ", e);
		}
	}

	private Boolean shouldBlock() {
		BlockList blockList = BlockList.getInstance();

		for(String line:blockList.getList()) {
			if(line.endsWith("*") && number.startsWith(line.substring(0, line.length() - 2))) {
				return true;
			}
			else if(number.equalsIgnoreCase(line)){
				return true;
			}
		}
		return false;
	}
}
