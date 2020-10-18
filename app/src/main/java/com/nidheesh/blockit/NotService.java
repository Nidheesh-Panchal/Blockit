package com.nidheesh.blockit;

import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NotService extends NotificationListenerService {

	String filePath;
	String fileName;
	File f;
	String baseDir;

	@Override
	public void onCreate()
	{
		super.onCreate();
		baseDir = getExternalFilesDir(null)+File.separator+"NotSaving";
		Log.d("BlockitLogs","created service");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		Log.d("BlockitLogs","Timer started onStartCommand");
		return START_STICKY;
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		Log.d("BlockitLogs","onDestroy");
	}

	@Override
	public void onNotificationPosted(StatusBarNotification sbn)
	{
		Log.d("BlockitLogs","got notification");
		String pakagename=sbn.getPackageName();
		Log.d("BlockitLogs",sbn.getPackageName());

		if(pakagename.equals("com.whatsapp"))
		{
			filePath = baseDir + File.separator + "WhatsApp";
			File createFolder=new File(filePath);
			createFolder.mkdirs();
			Log.d("BlockitLogs","creating folders");
			String temp= DateFormat.getDateTimeInstance().format(new Date()).substring(0,11);
			fileName = temp+".txt";
			filePath += File.separator;
			f = new File(filePath,fileName);
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				Log.println(Log.INFO,"BlockitLogs","unable to create file");
			}

			Bundle extras=sbn.getNotification().extras;

			String title=extras.getCharSequence("android.title").toString();
			String text=extras.getCharSequence("android.text").toString();

			if(title.length() > 8 && title.substring(0,8).equals("WhatsApp"))
			{
				return;
			}

			if(text.matches(".* messages from .* chats"))
			{
				return;
			}

			if(title.equalsIgnoreCase("WhatsApp") &&
					text.matches("Checking for new messages"))
			{
				return;
			}

			long millis = sbn.getNotification().when;

			if(System.currentTimeMillis() - millis > 3000 ){
				return;
			}

			Date date=new Date(millis);

			SimpleDateFormat format=new SimpleDateFormat("hh:mm a");
			String time = format.format(date);

			Log.d("BlockitLogs","Time : " + time);

			f=new File(filePath,fileName);
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(f,true)));
				pw.println("");
				pw.println("Title : " + title);
				pw.println("Text : " + text);
				pw.println("Time : " + time);
				pw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Log.d("BlockitLogs","saving into file");
		}

		else if(pakagename.equals("com.instagram.android"))
		{
			filePath = baseDir + File.separator + "Instagram";
			File createFolder=new File(filePath);
			createFolder.mkdirs();
			Log.d("BlockitLogs","creating folders");
			String temp= DateFormat.getDateTimeInstance().format(new Date()).substring(0,11);
			fileName = temp+".txt";
			filePath += File.separator;
			f = new File(filePath,fileName);
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				Log.println(Log.INFO,"BlockitLogs","unable to create file");
			}

			Bundle extras=sbn.getNotification().extras;
			String title=extras.getCharSequence("android.title").toString();
			String text=extras.getCharSequence("android.text").toString();

			if(text.endsWith("liked your post."))
			{
				return;
			}

			long millis = sbn.getNotification().when;
			Date date=new Date(millis);

			SimpleDateFormat format=new SimpleDateFormat("hh:mm a");
			String time = format.format(date);

			Log.d("BlockitLogs","Time : " + time);

			f=new File(filePath,fileName);
			try {
				PrintWriter pw=new PrintWriter(new BufferedWriter(new FileWriter(f,true)));
				pw.println("");
				pw.println("Title : " + title);
				pw.println("Text : " + text);
				pw.println("Time : " + time);
				pw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			Log.d("BlockitLogs","saving into file");
		}
	}

	@Override
	public void onNotificationRemoved(StatusBarNotification sbn)
	{
		Log.d("BlockitLogs","notification removed");
	}
}
