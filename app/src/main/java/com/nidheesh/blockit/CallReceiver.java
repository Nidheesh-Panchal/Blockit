package com.nidheesh.blockit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallReceiver extends BroadcastReceiver {
	String number;

	@Override
	public void onReceive(Context context, Intent intent) {

		try {
			String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
			number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);

			if(state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)){

				Log.d("BlockitLogs", "state ringing");

				try {
					if (number!=null) {
						if(shouldBlock())
						{
							CallActions callActions = new CallActions();
							callActions.endCall(context);
							callActions.showNotifications(context,number);
						}
						Log.d("BlockitLogs","Call from : " + number);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if(state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_IDLE)){
				Log.d("BlockitLogs", "state idle");
			}

		} catch (Exception e) {
			e.printStackTrace();
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
