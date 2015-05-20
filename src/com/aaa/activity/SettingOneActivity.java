package com.aaa.activity;

import com.wanglin.R;

import android.os.Bundle;

public class SettingOneActivity extends LlwBaseActivity{

	@Override
	protected void onAfterOnCreate(Bundle savedInstanceState) {
		super.onAfterOnCreate(savedInstanceState);
		setRightBtn(false, 0);
		setTitile(R.string.setting1);
	}
}
