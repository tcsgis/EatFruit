package com.aaa.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaa.db.Fruit;
import com.changhong.annotation.CHInjectView;
import com.changhong.util.CHLogger;
import com.changhong.util.bitmap.CHBitmapCacheWork;
import com.changhong.util.bitmap.CHBitmapCallBackHanlder;
import com.changhong.util.bitmap.CHDownloadBitmapHandler;
import com.wanglin.R;

public class FruitLookActivity extends LlwBaseActivity{

	public static final int EDIT = 100;
	
	@CHInjectView(id = R.id.photo)
	private ImageView photo;
	@CHInjectView(id = R.id.back)
	private ImageView back;
	@CHInjectView(id = R.id.fu_lan)
	private TextView fulan;
	@CHInjectView(id = R.id.day)
	private TextView day;
	@CHInjectView(id = R.id.date)
	private TextView date;
	
	private Fruit fruit = null;
	private CHBitmapCacheWork imageFetcher;
	private CHBitmapCacheWork imageFetcher2;
	private int offset = 0;
	
	/* (non-Javadoc)
	 * @see com.aaa.activity.LlwBaseActivity#onAfterOnCreate(android.os.Bundle)
	 */
	@Override
	protected void onAfterOnCreate(Bundle savedInstanceState) {
		super.onAfterOnCreate(savedInstanceState);
		setMidBtn(true, R.drawable.delete);
		setRightBtn(true, R.drawable.edit);
		setTitile(R.string.look);
		fruit = (Fruit) ((Bundle)getIntent().getExtras()).getSerializable("fruit");
		initData();
		loadImg();
		loadBack();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}
	
	private void initData() {
		Calendar c = Calendar.getInstance();
		int today = c.get(Calendar.DAY_OF_YEAR);
		c.setTime(fruit.getDeadline());
		int deadline = c.get(Calendar.DAY_OF_YEAR);
		offset = deadline - today;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(fruit.getDeadline());
		
		int dayWeek = -1;
		switch (c.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.MONDAY:
			dayWeek = R.string.day1;
			break;
		case Calendar.TUESDAY:
			dayWeek = R.string.day2;
			break;
		case Calendar.WEDNESDAY:
			dayWeek = R.string.day3;
			break;
		case Calendar.THURSDAY:
			dayWeek = R.string.day4;
			break;
		case Calendar.FRIDAY:
			dayWeek = R.string.day5;
			break;
		case Calendar.SATURDAY:
			dayWeek = R.string.day6;
			break;
		case Calendar.SUNDAY:
			dayWeek = R.string.day7;
			break;
		}
		if(dayWeek > 0)
			date = date + "  " + getResources().getString(dayWeek);
		
		this.date.setText(date);
		day.setText(getResources().getString(R.string.offset, offset));
		fulan.setText(offset < 0 ? R.string.fulan1 : R.string.fulan2);
	}


	private void loadImg() {
		imageFetcher = new CHBitmapCacheWork(this);

		CHBitmapCallBackHanlder taBitmapCallBackHanlder = new CHBitmapCallBackHanlder();
		taBitmapCallBackHanlder
		.setLoadingImage(this, R.drawable.apple);
		taBitmapCallBackHanlder.setCircleParams(true);

		Bitmap loading = taBitmapCallBackHanlder.getmLoadingBitmap();
		if(loading != null){
			int width = taBitmapCallBackHanlder.getmLoadingBitmap().getWidth();
			int height = taBitmapCallBackHanlder.getmLoadingBitmap().getHeight();
			CHDownloadBitmapHandler downloadBitmapFetcher = new CHDownloadBitmapHandler(this, width, height);
			imageFetcher.setProcessDataHandler(downloadBitmapFetcher);
		}

		imageFetcher.setCallBackHandler(taBitmapCallBackHanlder);
		imageFetcher.setFileCache(getCHApplication().getFileCache());
		
		if(!(fruit.getPhotoUrl() == null || fruit.getPhotoUrl().length() < 10)){
			imageFetcher.loadFormCache(fruit.getPhotoUrl(), photo);
		}
	}
	
	private void loadBack(){
		imageFetcher2 = new CHBitmapCacheWork(this);

		CHBitmapCallBackHanlder taBitmapCallBackHanlder = new CHBitmapCallBackHanlder();
		taBitmapCallBackHanlder
		.setLoadingImage(this, R.drawable.apple_back);
//		taBitmapCallBackHanlder.setCircleParams(true);
		if(offset < 0)
			taBitmapCallBackHanlder.setGrayParams(true);

		Bitmap loading = taBitmapCallBackHanlder.getmLoadingBitmap();
		if(loading != null){
			int width = taBitmapCallBackHanlder.getmLoadingBitmap().getWidth();
			int height = taBitmapCallBackHanlder.getmLoadingBitmap().getHeight();
			CHDownloadBitmapHandler downloadBitmapFetcher = new CHDownloadBitmapHandler(this, width, height);
			imageFetcher2.setProcessDataHandler(downloadBitmapFetcher);
		}

		imageFetcher2.setCallBackHandler(taBitmapCallBackHanlder);
		imageFetcher2.setFileCache(getCHApplication().getFileCache());
		
		if(!(fruit.getPhotoUrl() == null || fruit.getPhotoUrl().length() < 10)){
			imageFetcher2.loadFormCache(fruit.getPhotoUrl(), back);
		}
	}
	
	@Override
	protected void clickMid() {
		try {
			getSqlitdb().delete(fruit);
			finish();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void clickRight() {
		Intent intent = new Intent(FruitLookActivity.this, AddFruitActivity.class);
		Bundle b = new Bundle();
		b.putSerializable("fruit", fruit);
		intent.putExtras(b);
		startActivityForResult(intent, EDIT);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		CHLogger.d(this, "requestCode " + requestCode + ",  resultCode " + resultCode);
		if (requestCode == EDIT){
			if (resultCode == RESULT_OK) {
				fruit = (Fruit) data.getSerializableExtra("fruit");
				initData();
				loadImg();
				loadBack();
			}
		}
	}
}
