package com.nidheesh.blockit;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

	private static final int PERMISSION_REQUEST_READ_PHONE_STATE = 0;
	FileHandler mFileHandler = FileHandler.getInstance();
	BlockList mBlockList;
	FloatingActionButton fab;
	ImageView confirm;
	Toolbar toolbar;

	CheckBox selectAll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		setupActionBar();

		mFileHandler.setBaseDir(getExternalFilesDir(null).toString());

		mFileHandler.checkFileCreated();
		mBlockList = BlockList.getInstance();

		listener();

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
			if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED ||
					checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED ||
					checkSelfPermission(Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_DENIED ||
					checkSelfPermission(Manifest.permission.ANSWER_PHONE_CALLS) == PackageManager.PERMISSION_DENIED ||
					checkSelfPermission(Manifest.permission.MODIFY_PHONE_STATE) == PackageManager.PERMISSION_DENIED) {
				String[] permissions = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE, Manifest.permission.READ_CALL_LOG,
						Manifest.permission.ANSWER_PHONE_CALLS, Manifest.permission.MODIFY_PHONE_STATE};
				requestPermissions(permissions, PERMISSION_REQUEST_READ_PHONE_STATE);
			}
		}

		fab = findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
//				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//						.setAction("Action", null).show();
//				Toast.makeText(getApplicationContext(), "Add block number", Toast.LENGTH_SHORT).show();
//				final EditText editText = new EditText(MainActivity.this);
				LayoutInflater layoutinflater = getLayoutInflater();
				View view1 = layoutinflater.inflate(R.layout.addnumber, null);
				final EditText editText = (EditText)view1.findViewById(R.id.editText);

				AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.AddDialogTheme);
				alertDialog.setTitle("Block a number. NOW!");
				alertDialog.setMessage("Add a number you want to block. Add \"*\" at the end if you want to block all the numbers having that prefix");
				alertDialog.setView(view1);
				alertDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						mBlockList.addToList(String.valueOf(editText.getText()));
					}
				});
				alertDialog.setNegativeButton("Cancel", null);
				alertDialog.show();
			}
		});
	}

	private void listener() {
		selectAll = findViewById(R.id.select_all_checkbox);
		selectAll.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(selectAll.isChecked()) {
					DeleteAdapter.getInstance().selectAll();
				}
				else if(!selectAll.isChecked()){
					DeleteAdapter.getInstance().unselectAll();
				}
			}
		});

		confirm = findViewById(R.id.delete_selected);
		confirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DeleteAdapter.getInstance().deleteList();
				mFileHandler.putList(mBlockList.getList());
			}
		});
	}

	private void setupActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);

		Toolbar.LayoutParams lp1 = new Toolbar.LayoutParams(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.MATCH_PARENT);

//		ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
		View customNav = LayoutInflater.from(this).inflate(R.layout.actionbar, null); // layout which contains your button.

		actionBar.setCustomView(customNav, lp1);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		switch (requestCode) {
			case PERMISSION_REQUEST_READ_PHONE_STATE: {
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					Toast.makeText(this, "Permission granted: " + PERMISSION_REQUEST_READ_PHONE_STATE, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(this, "Permission NOT granted: " + PERMISSION_REQUEST_READ_PHONE_STATE, Toast.LENGTH_SHORT).show();
				}

				return;
			}
		}
	}
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
			NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);
			navController.navigate(R.id.action_SecondFragment_to_BlockFragment);
			fab.show();
			toolbar.removeView(confirm);
			return true;
		}

		if (id == R.id.action_delete) {
			Toast.makeText(getApplicationContext(), "Select items to be deleted", Toast.LENGTH_SHORT).show();
			*//*BlockFragment fragment =  (BlockFragment) getSupportFragmentManager().findFragmentById(R.id.BlockFragment);

			Log.d("BlockitLogs", "fragment : " + fragment);
			if(fragment == null)
				fragment.changeView();*//*
			fab.hide();
			toolbar.addView(confirm);
			NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);
			navController.navigate(R.id.action_BlockFragment_to_SecondFragment);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}*/

}