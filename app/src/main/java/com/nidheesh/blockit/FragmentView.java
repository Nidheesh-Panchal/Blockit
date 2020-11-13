package com.nidheesh.blockit;

import androidx.fragment.app.Fragment;

public class FragmentView {

	private String tag;

	static FragmentView mFragmentView = null;

	public static FragmentView getInstance() {
		if(mFragmentView == null)
			mFragmentView = new FragmentView();
		return mFragmentView;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
}
