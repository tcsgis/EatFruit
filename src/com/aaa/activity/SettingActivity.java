package com.aaa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.wanglin.R;
import com.changhong.activity.widget.AppMainDialog;

public class SettingActivity extends LlwBaseActivity implements OnClickListener{

	private AppMainDialog dialog2 = null;
	private AppMainDialog dialog3 = null;
	private AppMainDialog dialog4 = null;
	
	@Override
	protected void onAfterOnCreate(Bundle savedInstanceState) {
		super.onAfterOnCreate(savedInstanceState);
		setRightBtn(false, 0);
		setTitile(R.string.setting);
		
		findViewById(R.id.setting1).setOnClickListener(this);
		findViewById(R.id.setting2).setOnClickListener(this);
		findViewById(R.id.setting3).setOnClickListener(this);
		findViewById(R.id.setting4).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setting1:
			Intent intent  = new Intent(SettingActivity.this, SettingOneActivity.class);
			startActivity(intent);
			break;

		case R.id.setting2:
			if(dialog2 == null){
				dialog2 = new AppMainDialog(this,R.style.appdialog);
				dialog2.withTitle(R.string.setting2)
				.withMessage(R.string.setting2_1)
				.setOKClick(R.string.ok_queding, new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						dialog2.dismiss();
					}
				});
			}
			dialog2.show();
			break;
			
		case R.id.setting3:
			if(dialog3 == null){
				dialog3 = new AppMainDialog(this,R.style.appdialog);
				dialog3.withTitle(R.string.setting3)
				.withMessage(R.string.setting3_1)
				.setOKClick(R.string.ok_queding, new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						dialog3.dismiss();
					}
				});
			}
			dialog3.show();
			break;
			
		case R.id.setting4:
			if(dialog4 == null){
				dialog4 = new AppMainDialog(this,R.style.appdialog);
				dialog4.withTitle(R.string.setting4)
				.withMessage(R.string.setting4_1)
				.setOKClick(R.string.ok_queding, new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						dialog4.dismiss();
					}
				});
			}
			dialog4.show();
			break;
		}
	}
}
