package com.nidheesh.blockit;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BlockFragment extends Fragment {

	public static final String TAG = "BlockitLogs";
	private RecyclerView mRecyclerView;
	private LinearLayoutManager mLinearLayoutManager;

	private List<String> mList;
	private DeleteAdapter mBlockAdapter;

	@Override
	public View onCreateView(
			LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState
	) {

		/*
		When this view is called this view is created first.
		So first we need to get the root view and inflate it with the fragment we want.

		Then add the linear layout to recyclerview and then set Adapter to it so that the
		list items are taken care of automatically.
		*/

		View rootView = inflater.inflate(R.layout.block_fragment, container, false);

		Log.d(TAG, "Getting layout manager");
		mLinearLayoutManager = new LinearLayoutManager(getActivity());

		mRecyclerView = rootView.findViewById(R.id.recyclerView);
		mRecyclerView.setLayoutManager(mLinearLayoutManager);
		Log.d(TAG, "Calling init");
		init();

		/*
		Always remember to return this rootView.
		 */
		return rootView;
	}

	public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

	}

	private void init(){
		Log.d(TAG, "Getting the locally saved list.");
		BlockList blockList = BlockList.getInstance();
		mList = blockList.getList();

		Log.d(TAG, "Get DeleteAdapter and set it to the recyclerview.");
		mBlockAdapter=DeleteAdapter.getInstance();
		mBlockAdapter.setBlockList(mList);

		mRecyclerView.setAdapter(mBlockAdapter);
		mBlockAdapter.notifyDataSetChanged();
		Log.d(TAG, "Adapter added.");
	}
}
