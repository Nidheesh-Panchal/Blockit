package com.nidheesh.blockit.sms;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nidheesh.blockit.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SmsAdapter extends RecyclerView.Adapter<SmsAdapter.ViewHolder>{

	public static final String TAG = "BlockitLogs";
	private static SmsAdapter sSmsAdapter;

	public static SmsAdapter getInstance() {
		if(sSmsAdapter == null) {
			sSmsAdapter = new SmsAdapter();
		}
		return sSmsAdapter;
	}

	private List<String> smsList = new ArrayList<>();
	private Map<String, Boolean> map = new HashMap<>();
	private Boolean isSelectedAll = false;
	private Boolean allUnselect = false;

	/*
	This is and adapter for the RecyclerView.
	The blocklist passed here somehow references to the real Block list instance allowing
	to make changes here and reflect it in the actual list.
	 */

	public void setBlockList(List<String> smsList) {
		this.smsList = smsList;
	}

	public void deleteList() {

		for(String key:map.keySet()) {
			if(map.get(key)) {
				Log.d(TAG,"Removing from block list : " + key);
				smsList.remove(key);
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
	public SmsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		int layout = R.layout.sms_list_layout;
		View v = LayoutInflater
				.from(parent.getContext())
				.inflate(layout, parent, false);
		return new ViewHolder(v);
	}

	/*
	onBindViewHolder called when there is notifyDataSetChanged() or any update.
	 */

	@Override
	public void onBindViewHolder(@NonNull SmsAdapter.ViewHolder holder, int position) {
		String blockedSms = smsList.get(position);
		holder.showBlockedDetails(blockedSms);

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
		return smsList.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {

		private TextView blockSmsTextView;
		private CheckBox mCheckBox;

		/*
		For each item in RecyclerView there are 2 things which we track.
		 */

		public ViewHolder(@NonNull View itemView) {
			super(itemView);

			blockSmsTextView =(TextView)itemView.findViewById(R.id.textdelView);
			mCheckBox = (CheckBox)itemView.findViewById(R.id.checkBox);
			mCheckBox.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(mCheckBox.isChecked()) {
						Log.d(TAG, "Selected : " + blockSmsTextView.getText());
						map.put(String.valueOf(blockSmsTextView.getText()),true);
					}
					else if(!mCheckBox.isChecked()) {
						Log.d(TAG, "Unselect : " + blockSmsTextView.getText());
						map.put(String.valueOf(blockSmsTextView.getText()),false);
					}
				}
			});
		}

		public void showBlockedDetails(String blockedSms) {
			blockSmsTextView.setText(blockedSms);
		}
	}
}
