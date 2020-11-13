package com.nidheesh.blockit;

import android.Manifest;
import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nidheesh.blockit.sms.SmsAdapter;
import com.nidheesh.blockit.sms.SmsBlockList;
import com.nidheesh.blockit.sms.SmsFileHandler;

import java.util.List;

public class MainActivity extends AppCompatActivity {

	private static final int PERMISSION_REQUEST_READ_PHONE_STATE = 0;
	public static final String TAG = "BlockitLogs";

	FileHandler mFileHandler = FileHandler.getInstance();
	BlockList mBlockList;

	SmsFileHandler mSmsFileHandler = SmsFileHandler.getInstance();
	SmsBlockList mSmsBlockList;

	FloatingActionButton fab;
	ImageView confirm;
	Toolbar toolbar;

	CheckBox selectAll;

	ImageView switchFragment;

	FragmentView mFragmentView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Log.d(TAG, "App started.");
		Log.d(TAG, "Set action bar.");

		toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		setupActionBar();

		Log.d(TAG, "Set Fragment View.");
		mFragmentView = FragmentView.getInstance();
		mFragmentView.setTag("BlockFragment");

		Log.d(TAG, "Set base directory for block list file.");
		mFileHandler.setBaseDir(getExternalFilesDir(null).toString());

		Log.d(TAG, "Check if file exists.");
		mFileHandler.checkFileCreated();
		mBlockList = BlockList.getInstance();

		Log.d(TAG, "Set base directory for SMS block list file.");
		mSmsFileHandler.setBaseDir(getExternalFilesDir(null).toString());

		Log.d(TAG, "Check if file exists.");
		mSmsFileHandler.checkFileCreated();
		mSmsBlockList = SmsBlockList.getInstance();

		if(!isAccessibilityServiceEnabled(this, MyAccessibilityService.class))
			Toast.makeText(this, "Enable Notification and Accessibility settings.",
					Toast.LENGTH_LONG).show();

		Log.d(TAG, "Set listener for action bar.");
		listener();

		Log.d(TAG, "Check for all required permissions.");
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
			if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED ||
					checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED ||
					checkSelfPermission(Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_DENIED ||
					checkSelfPermission(Manifest.permission.ANSWER_PHONE_CALLS) == PackageManager.PERMISSION_DENIED ||
					checkSelfPermission(Manifest.permission.MODIFY_PHONE_STATE) == PackageManager.PERMISSION_DENIED ||
					checkSelfPermission(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_DENIED) {
				String[] permissions = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE, Manifest.permission.READ_CALL_LOG,
						Manifest.permission.ANSWER_PHONE_CALLS, Manifest.permission.MODIFY_PHONE_STATE, Manifest.permission.RECEIVE_SMS};
				requestPermissions(permissions, PERMISSION_REQUEST_READ_PHONE_STATE);
			}
		}

		Log.d(TAG, "Add floating action button and its listener.");
		fab = findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if(mFragmentView.getTag().equalsIgnoreCase("BlockFragment")) {
					Log.d(TAG, "Add layout for adding number to block list.");
					LayoutInflater layoutinflater = getLayoutInflater();
					View view1 = layoutinflater.inflate(R.layout.addnumber, null);
					final EditText editText = (EditText)view1.findViewById(R.id.editText);

					Log.d(TAG, "Create and open alert dialog to input number.");
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.AddDialogTheme);
					alertDialog.setTitle("Block a number. NOW!");
					alertDialog.setMessage("Add a number you want to block. Add \"*\" at the end if you want to block all the numbers having that prefix");
					alertDialog.setView(view1);
					alertDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Log.d(TAG, "Adding number to the list : " + editText.getText());
							mBlockList.addToList(String.valueOf(editText.getText()));
						}
					});
					alertDialog.setNegativeButton("Cancel", null);
					alertDialog.show();
				}
				else {
					Log.d(TAG, "Add layout for adding number to block list.");
					LayoutInflater layoutinflater = getLayoutInflater();
					View view1 = layoutinflater.inflate(R.layout.add_sms_pattern, null);
					final EditText editText = (EditText)view1.findViewById(R.id.sms_edit_text);

					Log.d(TAG, "Create and open alert dialog to input number.");
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this, R.style.AddDialogTheme);
					alertDialog.setTitle("Block a text. NOW!");
					alertDialog.setMessage("Add a phrase of text you want to block. Do NOT use Enter key.");
					alertDialog.setView(view1);
					alertDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Log.d(TAG, "Adding text to the list : " + editText.getText());
							mSmsBlockList.addToList(String.valueOf(editText.getText()));
						}
					});
					alertDialog.setNegativeButton("Cancel", null);
					alertDialog.show();
				}

			}
		});
	}

	//TODO: Unable to prompt up the settings from here. Find something to do this.
	/*public void requestPermission() {
		Intent requestIntent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
		requestIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(requestIntent);
	}
	public void accessibility()
	{
		Intent access = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
		access.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(access);
	}*/

	public static boolean isAccessibilityServiceEnabled(Context context, Class<? extends AccessibilityService> service) {
		AccessibilityManager am = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
		List<AccessibilityServiceInfo> enabledServices = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK);

		for (AccessibilityServiceInfo enabledService : enabledServices) {
			ServiceInfo enabledServiceInfo = enabledService.getResolveInfo().serviceInfo;
			if (enabledServiceInfo.packageName.equals(context.getPackageName()) && enabledServiceInfo.name.equals(service.getName()))
				return true;
		}

		return false;
	}

	private void listener() {
		selectAll = findViewById(R.id.select_all_checkbox);
		selectAll.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mFragmentView.getTag().equalsIgnoreCase("BlockFragment")) {
					if(selectAll.isChecked()) {
						DeleteAdapter.getInstance().selectAll();
					}
					else if(!selectAll.isChecked()){
						DeleteAdapter.getInstance().unselectAll();
					}
				}
				else {
					if(selectAll.isChecked()) {
						SmsAdapter.getInstance().selectAll();
					}
					else if(!selectAll.isChecked()){
						SmsAdapter.getInstance().unselectAll();
					}
				}
			}
		});

		confirm = findViewById(R.id.delete_selected);
		confirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if(mFragmentView.getTag().equalsIgnoreCase("BlockFragment")) {
					DeleteAdapter.getInstance().deleteList();
					mFileHandler.putList(mBlockList.getList());
				}
				else {
					SmsAdapter.getInstance().deleteList();
					mSmsFileHandler.putList(mSmsBlockList.getList());
				}
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

		Log.d(TAG, "Getting layout for toolbar from layout resources.");
		Toolbar.LayoutParams lp1 = new Toolbar.LayoutParams(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.MATCH_PARENT);

		View customNav = LayoutInflater.from(this).inflate(R.layout.actionbar, null); // layout which contains your button.

		actionBar.setCustomView(customNav, lp1);
	}
}