package com.nidheesh.blockit.sms;

import android.util.Log;

import java.util.List;

public class SmsBlockList {

	public static final String TAG = "BlockitLogs";
	private static SmsBlockList sSmsBlockList;

	private List<String> mList;

	public static SmsBlockList getInstance() {
		if(sSmsBlockList == null)
			sSmsBlockList = new SmsBlockList();
		return sSmsBlockList;
	}

	private SmsBlockList() {
		Log.d(TAG, "Inside BlockList constructor");
		SmsFileHandler smsFileHandler = SmsFileHandler.getInstance();
		mList = smsFileHandler.getList();
		Log.d(TAG, "BLock list size : " + mList.size());
	}

	public List<String> getList() {
		return mList;
	}

	public void addToList(String str) {
		Log.d(TAG, "Adding to block list. Writing it to file.");
		mList.add(str);
		SmsFileHandler smsFileHandler = SmsFileHandler.getInstance();
		smsFileHandler.putList(mList);
		Log.d(TAG, "List update and written to file.");
	}
}
