package com.aaa.other;

import com.aaa.activity.AwakeActivity;
import com.aaa.activity.EatFruitActivity;
import com.aaa.activity.SeasonActivity;
import com.aaa.activity.SettingActivity;
import com.wanglin.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

public class MenuPopup implements OnClickListener, OnKeyListener{

	private static final int BASE = 720;
	private static final int WIDTH = 512;
	
	private Activity mActivity;
	private PopupWindow mPopup;
	private LinearLayout mLL1;
	
	public MenuPopup(Activity activity, int height){
		mActivity = activity;
		initPopup(height);
	}
	
	private void initPopup(int height) {
		LayoutInflater layoutInflater = LayoutInflater.from(mActivity); 
		View popup = layoutInflater.inflate(R.layout.wl_main_menu, null);
		
		mLL1 = (LinearLayout) popup.findViewById(R.id.ll_fruit_bascket);
		popup.findViewById(R.id.ll_fruit_bascket).setOnClickListener(this);
		popup.findViewById(R.id.ll_eat_fruit).setOnClickListener(this);
		popup.findViewById(R.id.ll_season).setOnClickListener(this);
		popup.findViewById(R.id.ll_awake).setOnClickListener(this);
		popup.findViewById(R.id.ll_setting).setOnClickListener(this);
		
		DisplayMetrics dm = new DisplayMetrics();
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		//先乘后除, 避免不能整除造成的分辨率问题
		int width = dm.widthPixels * WIDTH / BASE;
		
		mPopup = new PopupWindow(popup, width, height);
		mPopup.setAnimationStyle(R.style.popup_left_anim);
		mPopup.setFocusable(true);
		mPopup.setBackgroundDrawable(new ColorDrawable());
		mPopup.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				dismissEffect();
			}
		});
	}
	
	public void show(){
		mPopup.showAtLocation(mLL1, Gravity.BOTTOM|Gravity.LEFT, 0, 0);
		showEffect();
	}
	
	public void dismiss(){
		mPopup.dismiss();
		dismissEffect();
	}
	
	public boolean isShowing(){
		return mPopup.isShowing();
	}
	
	@Override
	public void onClick(View v) {
		Intent intent = null;
		
		switch (v.getId()) {
		case R.id.ll_fruit_bascket:
			dismiss();
			break;
			
		case R.id.ll_eat_fruit:
			intent = new Intent(mActivity, EatFruitActivity.class); 
			break;
			
		case R.id.ll_season:
			intent = new Intent(mActivity, SeasonActivity.class); 
			break;
			
		case R.id.ll_awake:
			intent = new Intent(mActivity, AwakeActivity.class); 
			break;
			
		case R.id.ll_setting:
			intent = new Intent(mActivity, SettingActivity.class); 
			break;
		}
		
		if(intent != null){
			mActivity.startActivity(intent);
		}
	}
	
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (mPopup.isShowing()) {
			if ((keyCode == android.view.KeyEvent.KEYCODE_BACK) &&
					(event.getAction() == android.view.KeyEvent.ACTION_DOWN)) {
				MenuPopup.this.dismiss();
				return true;
			}
		}
		return false;
	}


	private void showEffect(){
		WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();  
		lp.alpha = 0.7f;  
		mActivity.getWindow().setAttributes(lp);
	}
	
	private void dismissEffect(){
		WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();  
        lp.alpha = 1f;  
        mActivity.getWindow().setAttributes(lp);
	}
}
