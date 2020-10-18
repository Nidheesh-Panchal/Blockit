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
				Log.d("BlockitLogs", "File created");
			}
			else
			{
				Log.d("BlockitLogs", "File already exists");
			}

		} catch (IOException e) {
			e.printStackTrace();
			Log.println(Log.INFO,"notsave","unable to create file");
		}
		return flag;
	}

	public List<String> getList() {
		String filePath = baseDir + File.separator;
		File f = new File(filePath,fileName);

		List<String> list = new ArrayList<>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line;

			while ((line = br.readLine()) != null) {
				list.add(line);
			}
			br.close();
		}
		catch (IOException e) {
			Log.e("BlockitLogs", e.getMessage());
		}
		return list;
	}

	public void putList(List<String> list) {
		String filePath = baseDir + File.separator;
		File f = new File(filePath,fileName);

		try {
			PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(f)));

			for(String line : list) {
				pw.println(line);
			}
			pw.close();
		}
		catch (IOException e) {
			Log.e("BlockitLogs", e.getMessage());
		}
	}
}
