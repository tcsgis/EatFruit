package com.aaa.activity;

import com.wanglin.R;

import android.os.Bundle;

public class BananaActivity extends LlwBaseActivity{

	@Override
	protected void onAfterOnCreate(Bundle savedInstanceState) {
		super.onAfterOnCreate(savedInstanceState);
		setRightBtn(false, 0);
		setTitile(R.string.xiang_jiao);
	}
}
