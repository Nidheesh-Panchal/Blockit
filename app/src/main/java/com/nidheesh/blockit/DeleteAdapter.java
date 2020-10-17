package com.nidheesh.blockit;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DeleteAdapter extends RecyclerView.Adapter<DeleteAdapter.ViewHolder>{

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

	public void setBlockList(List<String> blockList) {
		this.blockList = blockList;
	}

	public void deleteList() {
		for(String key:map.keySet()) {
			if(map.get(key)) {
				blockList.remove(key);
			}
		}
		map.clear();
		unselectAll();
	}

	public void selectAll() {
		Log.d("BlockitLogs","Select All");
		isSelectedAll=true;
		notifyDataSetChanged();
	}

	public void unselectAll(){
		Log.d("BlockitLogs","Unselect All");
		allUnselect=true;
		notifyDataSetChanged();
	}

	@NonNull
	@Override
	public DeleteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		int layout = R.layout.delelistlayout;
		View v = LayoutInflater
				.from(parent.getContext())
				.inflate(layout, parent, false);
		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull DeleteAdapter.ViewHolder holder, int position) {
		String blockedCall = blockList.get(position);
		holder.showBlockedDetails(blockedCall);
		Log.d("BlockitLogs", "changed checkbox : " + blockedCall + " " + isSelectedAll + allUnselect);
		if (allUnselect)
			holder.mCheckBox.setChecked(false);
		else if(isSelectedAll)
			holder.mCheckBox.setChecked(true);
		if(position == getItemCount() - 1) {
			isSelectedAll = false;
			allUnselect = false;
		}

//		if(holder.mCheckBox.isChecked()) {
//			Log.d("BlockitLogs", "Selected : " + blockedCall);
//			map.put(blockedCall, true);
//		}
//		else if(holder.mCheckBox.isChecked()){
//			map.put(blockedCall, false);
//		}
	}

	@Override
	public int getItemCount() {
		return blockList.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {

		private TextView blockCallTextView;
		private CheckBox mCheckBox;

		public ViewHolder(@NonNull View itemView) {
			super(itemView);

			blockCallTextView=(TextView)itemView.findViewById(R.id.textdelView);
			mCheckBox = (CheckBox)itemView.findViewById(R.id.checkBox);
			mCheckBox.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(mCheckBox.isChecked()) {
						Log.d("BlockitLogs", "Selected : " + blockCallTextView.getText());
						map.put(String.valueOf(blockCallTextView.getText()),true);
					}
					else if(!mCheckBox.isChecked()) {
						Log.d("BlockitLogs", "Unselect : " + blockCallTextView.getText());
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
