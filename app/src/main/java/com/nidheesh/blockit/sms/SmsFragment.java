package com.nidheesh.blockit.sms;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nidheesh.blockit.FragmentView;
import com.nidheesh.blockit.R;

import java.util.List;

public class SmsFragment extends Fragment {
	public static final String TAG = "BlockitLogs";
	private RecyclerView mRecyclerView;
	private LinearLayoutManager mLinearLayoutManager;
	private Button switchFragment;

	private List<String> mList;
	private SmsAdapter mSmsAdapter;

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

		View rootView = inflater.inflate(R.layout.sms_fragment, container, false);

		switchFragment = rootView.findViewById(R.id.switch_to_block);
		switchFragment.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				navigate();
			}
		});

		TextView tv = (TextView)getActivity().findViewById(R.id.fragment_view);
		tv.setText("Blocked SMS");

		Log.d(TAG, "Getting layout manager");
		mLinearLayoutManager = new LinearLayoutManager(getActivity());

		mRecyclerView = rootView.findViewById(R.id.sms_recycler_view);
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

		FragmentView.getInstance().setTag("SmsFragment");
	}

	private void init(){
		Log.d(TAG, "Getting the locally saved list.");
		SmsBlockList smsBlockList = SmsBlockList.getInstance();
		mList = smsBlockList.getList();

		Log.d(TAG, "Get SmsAdapter and set it to the recyclerview.");
		mSmsAdapter = SmsAdapter.getInstance();
		mSmsAdapter.setBlockList(mList);

		mRecyclerView.setAdapter(mSmsAdapter);
		mSmsAdapter.notifyDataSetChanged();
		Log.d(TAG, "Adapter added.");
	}

	public void navigate() {
		NavHostFragment.findNavController(SmsFragment.this)
			.navigate(R.id.action_SmsFragment_to_BlockFragment);
	}
}