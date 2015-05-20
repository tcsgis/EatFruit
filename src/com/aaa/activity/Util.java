package com.aaa.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.changhong.util.CHLogger;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Util {
	
	private static SharedPreferences sp = null;
	private static SharedPreferences.Editor editor = null;
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	public static int getOffset(Date date, int offset2) {
		Calendar c = Calendar.getInstance();
		int today = c.get(Calendar.DAY_OF_YEAR);
		c.setTime(date);
		int deadline = c.get(Calendar.DAY_OF_YEAR);
		int offset = deadline - today;

		return offset + offset2;
	}
	
	public static int[] getAwakeTime(Context contxet){
		sp = getSP(contxet);
		editor = getSP(contxet).edit();
		
		int hour = 20;
		int minute = 0;
		
		if(sp.contains("hour")){
			hour = sp.getInt("hour", 20);
		}else{
			editor.putInt("hour", 20);
			editor.commit();
		}
		
		if(sp.contains("minute")){
			minute = sp.getInt("minute", 0);
		}else{
			editor.putInt("minute", 0);
			editor.commit();
		}
		
		CHLogger.d("Util.getAwakeTime", "hour " + hour + " / minute " + minute);
		
		int[] result = new int[]{hour, minute};
		return result;
	}
	
	public static void setAwakeTime(Context contxet, int hour, int minute){
		CHLogger.d("Util.setAwakeTime", "hour " + hour + " / minute " + minute);
		sp = getSP(contxet);
		editor = getSP(contxet).edit();
		
		editor.putInt("hour", hour);
		editor.putInt("minute", minute);
		editor.commit();
	}
	
	public static boolean needNotifyEat(Context context){
		sp = getSP(context);
		editor = getSP(context).edit();
		
		boolean notify = sp.getBoolean(MainActivity.NOTIFY, true);
		CHLogger.d("Util.needNotify", "" + notify);
		
		return notify;
	}
	
	public static void setNotifyEat(Context context, boolean b){
		CHLogger.d("Util.setNotify", "" + b);
		sp = getSP(context);
		editor = getSP(context).edit();
		
		editor.putBoolean(MainActivity.NOTIFY, b);
		editor.commit();
	}
	
	public static boolean needNotifyOutDate(Context context){
		sp = getSP(context);
		editor = getSP(context).edit();
		
		boolean notify = sp.getBoolean(MainActivity.NOTIFY2, true);
		CHLogger.d("Util.needNotify", "" + notify);
		
		return notify;
	}
	
	public static void setNotifyOutDate(Context context, boolean b){
		CHLogger.d("Util.setNotify", "" + b);
		sp = getSP(context);
		editor = getSP(context).edit();
		
		editor.putBoolean(MainActivity.NOTIFY2, b);
		editor.commit();
	}
	
	private static SharedPreferences getSP(Context contxet){
		if(sp == null){
			sp = contxet.getSharedPreferences(MainActivity.SP, Activity.MODE_PRIVATE);
		}
		return sp;
	}
}
