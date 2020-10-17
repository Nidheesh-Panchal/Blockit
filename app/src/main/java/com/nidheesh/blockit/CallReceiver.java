package com.nidheesh.blockit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.navigation.fragment.NavHostFragment;

import com.android.internal.telephony.ITelephony;

import java.io.IOException;
import java.lang.reflect.Method;

public class CallReceiver extends BroadcastReceiver {
	String number;

	@Override
	public void onReceive(Context context, Intent intent) {
		/*TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		PhoneCallStateListener customPhoneListener = new PhoneCallStateListener(context);
		telephony.listen(customPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);*/

		ITelephony telephonyService;
		try {
			String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
			 number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);

			if(state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_RINGING)){
				Log.d("BlockitLogs", "state ringing");

				TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
				try {
					/*Class clazz = Class.forName(tm.getClass().getName());
					Method m = clazz.getDeclaredMethod("getITelephony");

					m.setAccessible(true);
					telephonyService = (ITelephony) m.invoke(tm);*/

					if (number!=null) {
//						telephonyService.endCall();
						if(shouldBlock())
						{
							new CallActions().endCall(context);
						}
						Log.d("BlockitLogs","Call from : " + number);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				Toast.makeText(context, "Ring " + number, Toast.LENGTH_SHORT).show();

			}
			if(state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_OFFHOOK)){
				Toast.makeText(context, "Answered " + number, Toast.LENGTH_SHORT).show();
			}
			if(state.equalsIgnoreCase(TelephonyManager.EXTRA_STATE_IDLE)){
				Toast.makeText(context, "Idle "+ number, Toast.LENGTH_SHORT).show();
				Log.d("BlockitLogs", "state idle");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void cutCall() {
		try {
			Log.d("BlockitLogs", "Sending key event");
			Runtime.getRuntime().exec(Integer.toString(KeyEvent.KEYCODE_HEADSETHOOK));
			Log.d("BlockitLogs", "cut call successful");
		} catch (IOException e) {
			// Runtime.exec(String) had an I/O problem, try to fall back
			/*String enforcedPerm = "android.permission.CALL_PRIVILEGED";
			Intent btnDown = new Intent(Intent.ACTION_MEDIA_BUTTON).putExtra(
					Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN,
							KeyEvent.KEYCODE_HEADSETHOOK));
			Intent btnUp = new Intent(Intent.ACTION_MEDIA_BUTTON).putExtra(
					Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP,
							KeyEvent.KEYCODE_HEADSETHOOK));

			mContext.sendOrderedBroadcast(btnDown, enforcedPerm);
			mContext.sendOrderedBroadcast(btnUp, enforcedPerm);*/

			Log.d("BlockitLogs", "Cant cut call" + e.getMessage());
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
