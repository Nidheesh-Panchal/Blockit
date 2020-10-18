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

	private RecyclerView mRecyclerView;
	private LinearLayoutManager mLinearLayoutManager;

	private List<String> mList;
	private DeleteAdapter mBlockAdapter;

	@Override
	public View onCreateView(
			LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState
	) {

		View rootView = inflater.inflate(R.layout.block_fragment, container, false);

		Log.d("BlockitLogs", "Getting layout manager");
		mLinearLayoutManager = new LinearLayoutManager(getActivity());

		mRecyclerView = rootView.findViewById(R.id.recyclerView);
		mRecyclerView.setLayoutManager(mLinearLayoutManager);
		Log.d("BlockitLogs", "Calling init");
		init();

		return rootView;
	}

	public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

	}

	private void init(){
		BlockList blockList = BlockList.getInstance();
		mList = blockList.getList();

		mBlockAdapter=DeleteAdapter.getInstance();
		mBlockAdapter.setBlockList(mList);

		mRecyclerView.setAdapter(mBlockAdapter);
		mBlockAdapter.notifyDataSetChanged();
	}
}
