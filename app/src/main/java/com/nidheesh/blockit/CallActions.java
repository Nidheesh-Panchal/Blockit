package com.nidheesh.blockit;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;

class CallActions {

	public void endCall(Context context) {
		/*try {
			Log.d("BlockitLogs", "Sending key event");
			Runtime.getRuntime().exec(Integer.toString(KeyEvent.KEYCODE_MEDIA_PLAY));
			Log.d("BlockitLogs", "cut call successful");
		}
		catch(Exception e) {

			String enforcedPerm = "android.permission.CALL_PRIVILEGED";
			Intent btnDown = new Intent(Intent.ACTION_MEDIA_BUTTON).putExtra(
					Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN,
							KeyEvent.KEYCODE_HEADSETHOOK));
			Intent btnUp = new Intent(Intent.ACTION_MEDIA_BUTTON).putExtra(
					Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_UP,
							KeyEvent.KEYCODE_HEADSETHOOK));

			Log.d("BlockitLogs", context.toString());

			context.sendOrderedBroadcast(btnDown, enforcedPerm);
			context.sendOrderedBroadcast(btnUp, enforcedPerm);

			Log.d("BlockitLogs", "Cant cut call" + e.getMessage());
		}*/
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
			TelecomManager telecomManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);

			try {
				telecomManager.endCall();
				Log.d("BlockitLogs", "Invoked 'endCall' on TelecomManager");
			} catch (Exception e) {
				Log.e("BlockitLogs", "Couldn't end call with TelecomManager", e);
			}
		}
		else {
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			try {
				Method m = tm.getClass().getDeclaredMethod("getITelephony");
				m.setAccessible(true);

				ITelephony telephony = (ITelephony) m.invoke(tm);

				telephony.endCall();
			} catch (Exception e) {
				Log.e("BlockitLogs", "Couldn't end call with TelephonyManager", e);
			}
		}
	}

	public void showNotifications(Context context, String number) {

		if (Build.VERSION.SDK_INT >= 26) {
			NotificationManager notificationManager =  (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			NotificationChannel channel = new NotificationChannel(
					"default", context.getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT
			);
			channel.setDescription("Call Blocked");
			notificationManager.createNotificationChannel(channel);
		}

		Notification notify = new NotificationCompat.Builder(context, "M_CH_ID")
				.setSmallIcon(R.drawable.ic_stat_name)
				.setContentTitle("Call Blocked")
				.setContentText(number != null ? number : "Private Number")
				.setCategory(NotificationCompat.CATEGORY_CALL)
				.setShowWhen(true)
				.setAutoCancel(true)
				.setChannelId("default")
				.build();

		String tag = number != null ? number : "Private";
		NotificationManagerCompat.from(context).notify(tag, 1234, notify);
	}
}
