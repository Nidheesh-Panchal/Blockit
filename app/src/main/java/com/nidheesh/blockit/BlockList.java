package com.nidheesh.blockit;

import android.util.Log;

import java.util.List;

class BlockList {

	public static final String TAG = "BlockitLogs";
	private static BlockList mBlockList;

	private List<String> mList;

	public static BlockList getInstance() {
		if(mBlockList == null)
			mBlockList = new BlockList();
		return mBlockList;
	}

	private BlockList() {
		Log.d(TAG, "Inside BlockList constructor");
		FileHandler fileHandler = FileHandler.getInstance();
		mList = fileHandler.getList();
		Log.d(TAG, "BLock list size : " + mList.size());
	}

	public List<String> getList() {
		return mList;
	}

	public void addToList(String str) {
		Log.d(TAG, "Adding to block list. Writing it to file.");
		mList.add(str);
		FileHandler fileHandler = FileHandler.getInstance();
		fileHandler.putList(mList);
		Log.d(TAG, "List update and written to file.");
	}
}
