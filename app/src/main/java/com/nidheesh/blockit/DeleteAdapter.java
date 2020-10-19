package com.nidheesh.blockit;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DeleteAdapter extends RecyclerView.Adapter<DeleteAdapter.ViewHolder>{

	public static final String TAG = "BlockitLogs";
	private static DeleteAdapter sDeleteAdapter;

	public static DeleteAdapter getInstance() {
		if(sDeleteAdapter == null) {
			sDeleteAdapter = new DeleteAdapter();
		}
		return sDeleteAdapter;
	}

	private List<String> blockList = new ArrayList<>();
	private Map<String, Boolean> map = new HashMap<>();
	private Boolean isSelectedAll = false;
	private Boolean allUnselect = false;

	/*
	This is and adapter for the RecyclerView.
	The blocklist passed here somehow references to the real Block list instance allowing
	to make changes here and reflect it in the actual list.
	 */

	public void setBlockList(List<String> blockList) {
		this.blockList = blockList;
	}

	public void deleteList() {

		for(String key:map.keySet()) {
			if(map.get(key)) {
				Log.d(TAG,"Removing from block list : " + key);
				blockList.remove(key);
			}
		}
		Log.d(TAG,"Updating view.");
		map.clear();
		unselectAll();
	}

	public void selectAll() {
		Log.d(TAG,"Select All");
		isSelectedAll=true;
		notifyDataSetChanged();
	}

	public void unselectAll(){
		Log.d(TAG,"Unselect All");
		allUnselect=true;
		notifyDataSetChanged();
	}

	/*
	Add layout of a single item you want in the RecyclerView.
	 */

	@NonNull
	@Override
	public DeleteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		int layout = R.layout.delelistlayout;
		View v = LayoutInflater
				.from(parent.getContext())
				.inflate(layout, parent, false);
		return new ViewHolder(v);
	}

	/*
	onBindViewHolder called when there is notifyDataSetChanged() or any update.
	 */

	@Override
	public void onBindViewHolder(@NonNull DeleteAdapter.ViewHolder holder, int position) {
		String blockedCall = blockList.get(position);
		holder.showBlockedDetails(blockedCall);

		if (allUnselect)
			holder.mCheckBox.setChecked(false);
		else if(isSelectedAll)
			holder.mCheckBox.setChecked(true);
		if(position == getItemCount() - 1) {
			isSelectedAll = false;
			allUnselect = false;
		}
	}

	@Override
	public int getItemCount() {
		return blockList.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {

		private TextView blockCallTextView;
		private CheckBox mCheckBox;

		/*
		For each item in RecyclerView there are 2 things which we track.
		 */

		public ViewHolder(@NonNull View itemView) {
			super(itemView);

			blockCallTextView=(TextView)itemView.findViewById(R.id.textdelView);
			mCheckBox = (CheckBox)itemView.findViewById(R.id.checkBox);
			mCheckBox.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(mCheckBox.isChecked()) {
						Log.d(TAG, "Selected : " + blockCallTextView.getText());
						map.put(String.valueOf(blockCallTextView.getText()),true);
					}
					else if(!mCheckBox.isChecked()) {
						Log.d(TAG, "Unselect : " + blockCallTextView.getText());
						map.put(String.valueOf(blockCallTextView.getText()),false);
					}
				}
			});
		}

		public void showBlockedDetails(String blockedCall) {
			blockCallTextView.setText(blockedCall);
		}
	}
}
