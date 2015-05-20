package com.changhong.service.task.receiver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import com.aaa.activity.MainActivity;
import com.aaa.activity.Util;
import com.aaa.db.Fruit;
import com.changhong.CHApplication;
import com.changhong.service.task.AlarmBackRunTask;
import com.changhong.util.CHLogger;
import com.wanglin.R;

public class AlarmAnniReceiver extends AlarmBackRunTask{

	private static final int mBackRunInterval = 30 * 1000;//后台运行时触发事件间隔为1小时
	private static final int mForegroudInterval = 10 * 1000;//前台运行时触发事件间隔
	
	private SharedPreferences sp;
	private CHApplication application = CHApplication.getApplication();
	private List<Fruit> fruitList2 = new ArrayList<Fruit>();
	private Date awakeTime = null;

	private static AlarmAnniReceiver anniInstance;
	private AlarmAnniReceiver(){};
	public static AlarmAnniReceiver getInstance(){
		
		if(anniInstance == null){
			anniInstance = new AlarmAnniReceiver();
		}
		return anniInstance;
	};
	
	
	
	@Override
	protected Bundle doTask(Date date) {
		CHLogger.d(this ,"onTimeOut");
		Bundle ret = null;
		
		boolean eatFruit = false;
		boolean outDate = false;
		
		try {
			fruitList2 = getSqlitdb().query(Fruit.class, true, "isDelete=0", null, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(Util.needNotifyEat(application) && shouldCheckEat()){
			if(fruitList2 != null && fruitList2.size() > 0){
				for(Fruit item : fruitList2){
					if(item.geteatDate() == null || Util.getOffset(item.geteatDate(), 0) < 0){
						eatFruit = true;
						try {
							item.seteatDate(Calendar.getInstance().getTime());
							getSqlitdb().update(item);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		
		if(shouleCheckOutDate()){
			if(fruitList2 != null && fruitList2.size() > 0){
				for(Fruit item : fruitList2){
					if(Util.getOffset(item.getDeadline(), - item.getAwakeTime()) == 0){
						outDate = true;
						try {
							item.sethasAwake(true);
							getSqlitdb().update(item);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		
		if(eatFruit || outDate){
			soundRemind();
			String s = null; 
					
			if(eatFruit && outDate){
				s = application.getResources().getString(R.string.noti3);
			}
			else if(eatFruit){
				s = application.getResources().getString(R.string.noti1);
			}else if(outDate){
				s = application.getResources().getString(R.string.noti2);
			}
			
			ret = new Bundle();
			ret.putInt("title", R.string.app_name);
			ret.putString("text", s);
			ret.putInt("actionRes", R.string.AlarmAnniServiceNotify);
		}
		
		releaseSqlitdb();
		return ret;
	}
	
	private boolean shouleCheckOutDate() {
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		
		if(hour == 20 && minute == 0){
			return true;
		}
		
		return false;
	}
	
	private boolean shouldCheckEat() {
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		
		int[] time = Util.getAwakeTime(application);
		
		if(hour == time[0] && minute == time[1]){
			return true;
		}
		
		return false;
	}
	
	@Override
	protected int getRunInterval(boolean isAppForegroud) {
		if(isAppForegroud){
			return mForegroudInterval;
		}else{
			return mBackRunInterval;
		}
	}

	private void soundRemind() {
		Uri uri = RingtoneManager.getActualDefaultRingtoneUri(application, RingtoneManager.TYPE_NOTIFICATION);
		Ringtone r = RingtoneManager.getRingtone(application, uri);
		if(r != null){
			r.play();
		}
	}
	
	private Date getTime(){
		int[] time = Util.getAwakeTime(application);
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, time[0]);
		c.set(Calendar.MINUTE, time[1]);
		
		return awakeTime;
	}
	
	private SharedPreferences getSP(){
		
		if(sp == null){
			sp = application.getSharedPreferences(MainActivity.SP, Activity.MODE_PRIVATE);
		}
		
		return sp;
	}
	@Override
	public void doExit() {
		// TODO Auto-generated method stub
		anniInstance = null;
	}
	

}
