package com.aaa.other;

import com.wanglin.R;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;

public class PopupTime {
	
	private static final int HEIGHT1 = 16 * 4 + 20 * 4 - 10;
	private static final int WIDTH1 = 10 * 4;
	private static final int OFFSET = 20 * 2;
	private static final int BASE = 720;
	
	private Activity mActivity;
	private PopupWindow mPopup;
	private View mAnchor;
	private int offset = OFFSET;
	
	public PopupTime(Activity activity, int width, int height, OnClickListener l, View v){
		mActivity = activity;
		mAnchor = v;
		
		DisplayMetrics dm = new DisplayMetrics();
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		//先乘后除, 避免不能整除造成的分辨率问题
		int w = dm.widthPixels * WIDTH1 / BASE;
		int h = dm.widthPixels * HEIGHT1 / BASE;
		offset = dm.widthPixels * OFFSET / BASE;
		
		width = w + width;
		height = h + height * 3;
		
		initPopup(width, height, l);
	}

	private void initPopup(int width, int height, OnClickListener l) {
		LayoutInflater layoutInflater = LayoutInflater.from(mActivity); 
		View v = layoutInflater.inflate(R.layout.popup_time, null);
		
		v.findViewById(R.id.awake_1).setOnClickListener(l);
		v.findViewById(R.id.awake_2).setOnClickListener(l);
		v.findViewById(R.id.awake_3).setOnClickListener(l);
		
		mPopup = new PopupWindow(v, width, height);
		mPopup.setFocusable(true);
		mPopup.setBackgroundDrawable(new ColorDrawable());
	}
	
	public void show(){
		mPopup.showAsDropDown(mAnchor, - offset, 0);
	}
	
	public void dismiss(){
		mPopup.dismiss();
	}
}
