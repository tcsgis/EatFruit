package com.aaa.activity;

import com.wanglin.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class SeasonActivity extends LlwBaseActivity implements OnClickListener{
	
	public static final int SPRING = 1;
	public static final int SUMMER = 2;
	public static final int FALL = 3;
	public static final int WINTER = 4;
	
	@Override
	protected void onAfterOnCreate(Bundle savedInstanceState) {
		super.onAfterOnCreate(savedInstanceState);
		setRightBtn(false, 0);
		setTitile(R.string.season);
		
		findViewById(R.id.spring).setOnClickListener(this);
		findViewById(R.id.summer).setOnClickListener(this);
		findViewById(R.id.fall).setOnClickListener(this);
		findViewById(R.id.winter).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int season = -1;
		switch (v.getId()) {
		case R.id.spring:
			season = SPRING;
			break;

		case R.id.summer:
			season = SUMMER;
			break;
			
		case R.id.fall:
			season = FALL;
			break;
			
		case R.id.winter:
			season = WINTER;
			break;
		}
		
		if(season > 0){
			Intent intent = new Intent(SeasonActivity.this, FruitListActivity.class);
			intent.putExtra("season", season);
			startActivity(intent);
		}
	}
}
