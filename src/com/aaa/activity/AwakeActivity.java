package com.aaa.activity;

import java.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aaa.other.TimeDialog;
import com.aaa.other.TimeDialog.onTimeChangedListener;
import com.changhong.annotation.CHInjectView;
import com.wanglin.R;

public class AwakeActivity extends LlwBaseActivity{

	@CHInjectView(id = R.id.ll_awake1)
	private LinearLayout ll1;
	@CHInjectView(id = R.id.ll_awake2)
	private LinearLayout ll2;
	@CHInjectView(id = R.id.ll_awake3)
	private LinearLayout ll3;
	@CHInjectView(id = R.id.noti_awake)
	private ImageView noti;
	@CHInjectView(id = R.id.window_awake)
	private ImageView window;
	@CHInjectView(id = R.id.awake_time)
	private TextView awake_time;
	
	private boolean notiAwake = false;
	private boolean windowAwake = false;
	private TimeDialog dialog = null;
	private int hour = 20;
	private int minute = 0;
	
	@Override
	protected void onAfterOnCreate(Bundle savedInstanceState) {
		super.onAfterOnCreate(savedInstanceState);
		setRightBtn(false, 0);
		setTitile(R.string.awake);
		
		int[] time = Util.getAwakeTime(this);
		hour = time[0];
		minute = time[1];
		
		awake_time.setText(getTimeString(hour, minute));
		
		notiAwake = Util.needNotifyEat(this);
		windowAwake = Util.needNotifyOutDate(this);
		noti.setImageResource(notiAwake ? R.drawable.check_box2 : R.drawable.check_box);
		window.setImageResource(windowAwake ? R.drawable.check_box2 : R.drawable.check_box);
		
		ll1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(notiAwake){
					notiAwake = false;
					noti.setImageResource(R.drawable.check_box);
				}else{
					notiAwake = true;
					noti.setImageResource(R.drawable.check_box2);
				}
			}
		});
		
		ll2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(windowAwake){
					windowAwake = false;
					window.setImageResource(R.drawable.check_box);
				}else{
					windowAwake = true;
					window.setImageResource(R.drawable.check_box2);
				}
			}
		});
		
		ll3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Calendar c = Calendar.getInstance();
				c.set(Calendar.HOUR_OF_DAY, hour);
				c.set(Calendar.MINUTE, minute);
				
				if(dialog == null){
					dialog = new TimeDialog(AwakeActivity.this, R.style.appdialog, new onTimeChangedListener() {
						
						@Override
						public void onTimeChanged(int h, int m) {
							Util.setAwakeTime(AwakeActivity.this, h, m);
							awake_time.setText(getTimeString(h, m));
						}
					}, c.getTime());
				}
				dialog.show();
			}
		});
	}
	
	private String getTimeString(int h, int m) {
		String time = null;
		if(h < 10){
			time = "0" + h;
		}else{
			time = "" + h;
		}
		
		time += ":";
		
		if(m < 10){
			time += "0" + m;
		}else{
			time += m;
		}
		
		return time;
	}
	
	@Override
	protected void clickLeft() {
		Util.setNotifyEat(this, notiAwake);
		Util.setNotifyOutDate(this, windowAwake);
		super.clickLeft();
	}
}
