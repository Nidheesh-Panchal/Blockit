package com.nidheesh.blockit;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BlockFragment extends Fragment {

	public static final String TAG = "BlockitLogs";
	private RecyclerView mRecyclerView;
	private LinearLayoutManager mLinearLayoutManager;
	private Button switchFragment;

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

		switchFragment = rootView.findViewById(R.id.switch_to_sms);
		switchFragment.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				navigate();
			}
		});

		TextView tv = (TextView)getActivity().findViewById(R.id.fragment_view);
		tv.setText("Blocked Calls");

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
		FragmentView.getInstance().setTag("BlockFragment");
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

	public void navigate() {
		NavHostFragment.findNavController(BlockFragment.this)
				.navigate(R.id.action_BlockFragment_to_SmsFragment);
	}
}
