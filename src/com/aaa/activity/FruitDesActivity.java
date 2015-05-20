package com.aaa.activity;

import com.wanglin.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class FruitDesActivity extends LlwBaseActivity{
	
	@Override
	protected void onAfterOnCreate(Bundle savedInstanceState) {
		super.onAfterOnCreate(savedInstanceState);
		setRightBtn(false, 0);
		setTitile(R.string.ping_guo);
		
		findViewById(R.id.rl_apple_1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FruitDesActivity.this, AppleZhouActivity.class);
				startActivity(intent);
			}
		});
	}
}
