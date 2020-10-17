package com.nidheesh.blockit;

import android.util.Log;

import java.util.List;

class BlockList {

	private static BlockList mBlockList;

	private List<String> mList;

	public static BlockList getInstance() {
		if(mBlockList == null)
			mBlockList = new BlockList();
		return mBlockList;
	}

	private BlockList() {
		Log.d("BlockitLogs", "inside contructor");
		FileHandler fileHandler = FileHandler.getInstance();
		mList = fileHandler.getList();
		Log.d("BlockitLogs", "inside contructor" + mList.get(0));
	}

	public void setList() {
		FileHandler fileHandler = FileHandler.getInstance();
		mList = fileHandler.getList();
	}

	public List<String> getList() {
		return mList;
	}

	public void addToList(String str) {
		mList.add(str);
		FileHandler fileHandler = FileHandler.getInstance();
		fileHandler.putList(mList);
	}
}
