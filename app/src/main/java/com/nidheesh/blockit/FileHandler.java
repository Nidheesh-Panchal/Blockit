package com.nidheesh.blockit;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

class FileHandler {
	public static final String TAG = "BlockitLogs";
	String baseDir;

	String fileName = "Block List.txt";

	private static FileHandler mList;

	public static FileHandler getInstance() {
		if(mList == null) {
			mList = new FileHandler();

		}

		return mList;
	}

	public void setBaseDir(String dir) {
		baseDir = dir;
	}

	public boolean checkFileCreated() {
		String filePath = baseDir + File.separator;
		File f = new File(filePath,fileName);
		boolean flag = false;
		try {
			if(f.createNewFile())
			{
				flag = true;
				Log.d(TAG, "File created");
			}
			else
			{
				Log.d(TAG, "File already exists");
			}

		} catch (IOException e) {
			e.printStackTrace();
			Log.e(TAG,"Exception : ",e);
		}
		return flag;
	}

	public List<String> getList() {
		String filePath = baseDir + File.separator;
		File f = new File(filePath,fileName);

		List<String> list = new ArrayList<>();
		try {

			Log.d(TAG, "Reading from file.");

			BufferedReader br = new BufferedReader(new FileReader(f));
			String line;

			while ((line = br.readLine()) != null) {
				list.add(line);
			}
			br.close();
		}
		catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
		}
		return list;
	}

	public void putList(List<String> list) {
		String filePath = baseDir + File.separator;
		File f = new File(filePath,fileName);

		try {
			Log.d(TAG, "Writing to file.");

			PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(f)));

			for(String line : list) {
				pw.println(line);
			}
			pw.close();
		}
		catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
		}
	}
}
