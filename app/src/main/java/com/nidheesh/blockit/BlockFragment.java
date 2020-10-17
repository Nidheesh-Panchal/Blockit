package com.nidheesh.blockit;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
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
		// Inflate the layout for this fragment


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

		/*view.findViewById(R.id.action_delete).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				NavHostFragment.findNavController(BlockFragment.this)
						.navigate(R.id.action_FirstFragment_to_SecondFragment);
			}
		});*/

	}

	private void init(){
//		FileHandler fl = FileHandler.getInstance();
//		Log.d("BlockitLogs", "Base Dir : " + fl.getBaseDir());
//
//		blockList=new ArrayList<String>();
//
//		// Initiating Adapter
//		System.out.println("------------Context : " + getContext());
//
//
//		// Adding some demo data(Call Objects).
//		// You can get them from your data server
//		blockList.add(new String("John"));
//		blockList.add(new String("Rob"));
		BlockList blockList = BlockList.getInstance();
		mList = blockList.getList();

		mBlockAdapter=DeleteAdapter.getInstance();
		// Set items to adapter
		mBlockAdapter.setBlockList(mList);

		mRecyclerView.setAdapter(mBlockAdapter);
		mBlockAdapter.notifyDataSetChanged();
	}
}
