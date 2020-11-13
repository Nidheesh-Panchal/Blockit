package com.nidheesh.blockit.sms;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SMSBroadcastReceiver extends BroadcastReceiver {
	private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	public static final String TAG = "BlockitLogs";

	SmsBlockList mSmsBlockList = SmsBlockList.getInstance();

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction() == SMS_RECEIVED) {

			Bundle bundle = intent.getExtras();

			if (bundle != null) {

				Object[] pdus = (Object[])bundle.get("pdus");

				for (int i = 0; i < pdus.length; i++)
				{
					SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
					String senderNum = currentMessage.getDisplayOriginatingAddress();
					String message = currentMessage.getDisplayMessageBody();

					Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message);

					markMessageRead(context, senderNum, message);
				}
			}
		}
	}

	private void markMessageRead(Context context, String number, String body) {

		Uri uri = Uri.parse("content://sms/inbox");
		Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
		try{

			while (cursor.moveToNext()) {
				if ((cursor.getString(cursor.getColumnIndex("address")).equals(number)) && (cursor.getInt(cursor.getColumnIndex("read")) == 0)) {
					if (cursor.getString(cursor.getColumnIndex("body")).contains(body)) {
						String SmsMessageId = cursor.getString(cursor.getColumnIndex("_id"));
						ContentValues values = new ContentValues();
						values.put("read", true);
						context.getContentResolver().update(Uri.parse("content://sms/inbox"), values, "_id=" + SmsMessageId, null);
						return;
					}
				}
			}
		}catch(Exception e)
		{
			Log.d(TAG, "Error in Read: "+e.toString());
		}
	}
}
