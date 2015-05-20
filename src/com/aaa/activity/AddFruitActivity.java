package com.aaa.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aaa.activity.DateDialog.onDateChangedListener;
import com.aaa.db.Fruit;
import com.aaa.other.PopupTime;
import com.wanglin.R;
import com.changhong.activity.util.PictureUtil;
import com.changhong.activity.widget.PhotoSelectPopupView;
import com.changhong.annotation.CHInjectView;
import com.changhong.util.CHLogger;

public class AddFruitActivity extends LlwBaseActivity implements OnClickListener{

	private static final int WIDTH = 130;
	private static final int HEIGHT = 130;
	private static final int BASE = 720;
	
	@CHInjectView(id = R.id.img_photo)
	private ImageView mImgPhoto;
	@CHInjectView(id = R.id.edit_fruit)
	private EditText mEditFruit;
	@CHInjectView(id = R.id.txt_deadline)
	private TextView mTxtDeadline;
	@CHInjectView(id = R.id.edit_fresh_time)
	private TextView mTxtFreshTime;
	@CHInjectView(id = R.id.img_zhi_ding)
	private ImageView mImgZhiDing;
	@CHInjectView(id = R.id.img_awake)
	private ImageView mImgAwake;
	@CHInjectView(id = R.id.txt_awake)
	private TextView mTxtAwake;
	
	private PhotoSelectPopupView mPopupAltView;
	private PopupTime mPopupTime;
	private Uri mPhotoUri;
	private int mWidth = WIDTH;
	private int mHeight = HEIGHT;
	private DateDialog dialog = null;
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	private int awakeTime2 = -1;
	
	private boolean needZhiDing = false;
	private boolean needAwake = true;
	private int mAwakeTime = 3;
	private Date mDate = null;
	private String mPhotoPath = null;
	private Fruit fruit = null;
	
	@Override
	protected void onAfterOnCreate(Bundle savedInstanceState) {
		super.onAfterOnCreate(savedInstanceState);
		
		setRightBtn(true, R.drawable.confirm);
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		//先乘后除, 避免不能整除造成的分辨率问题
		mWidth = dm.widthPixels * WIDTH / BASE;
		mHeight = dm.widthPixels * HEIGHT / BASE;
		
		mImgPhoto.setOnClickListener(this);
		mTxtDeadline.setOnClickListener(this);
		mImgZhiDing.setOnClickListener(this);
		mImgAwake.setOnClickListener(this);
		mTxtAwake.setOnClickListener(this);
		
		Bundle b = getIntent().getExtras();
		if(b != null && b.containsKey("fruit")){
			setTitile(R.string.edit);
			setMidBtn(true, R.drawable.delete);
			fruit = (Fruit) b.getSerializable("fruit");
			initEdit();
			awakeTime2 = fruit.getAwakeTime();
		}else{
			setTitile(R.string.upload_fruit);
			mDate = Calendar.getInstance().getTime();
			String date = format.format(mDate);
			mTxtDeadline.setText(date);
			mTxtFreshTime.setText(getResources().getString(R.string.some_day, 0));
		}
	}
	
	public void initEdit(){
		mPhotoPath = fruit.getPhotoUrl();
		needAwake = fruit.getneedAwake();
		needZhiDing = fruit.getneedZD();
		mAwakeTime = fruit.getAwakeTime();
		mEditFruit.setText(fruit.getName());
		mDate = fruit.getDeadline();
		
		try {
			mImgPhoto.setImageBitmap(PictureUtil.
					decodeSampledBitmapFromFile(fruit.getPhotoUrl(), 
							mImgPhoto.getWidth(), 
							mImgPhoto.getHeight()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Calendar c = Calendar.getInstance();
		int today = c.get(Calendar.DAY_OF_YEAR);
		c.setTime(mDate);
		int deadline = c.get(Calendar.DAY_OF_YEAR);
		int offset = deadline - today;
		mTxtFreshTime.setText(getResources().getString(R.string.some_day, offset));
		
		mTxtDeadline.setText(format.format(fruit.getDeadline()));
		
		mImgZhiDing.setImageResource(fruit.getneedZD() ? R.drawable.check_box2 : R.drawable.check_box);
		mImgAwake.setImageResource(fruit.getneedAwake() ? R.drawable.check_box2 : R.drawable.check_box);
		
		if(mAwakeTime == 1){
			mTxtAwake.setText(R.string.awake_time_1);
		}
		else if(mAwakeTime == 2){
			mTxtAwake.setText(R.string.awake_time_2);
		}
		else if(mAwakeTime == 3){
			mTxtAwake.setText(R.string.awake_time_3);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_photo:
			if (mPopupAltView == null) {
				mPopupAltView = new PhotoSelectPopupView(this);
			}
			mPopupAltView.show();
			break;
			
		case R.id.txt_deadline:
			if(dialog == null){
				dialog = new DateDialog(this, R.style.appdialog, new onDateChangedListener() {
					@Override
					public void onDateChanged(Date date) {
						mDate = date;
						mTxtDeadline.setText(format.format(date));
						
						Calendar c = Calendar.getInstance();
						int today = c.get(Calendar.DAY_OF_YEAR);
						c.setTime(mDate);
						int deadline = c.get(Calendar.DAY_OF_YEAR);
						int offset = deadline - today;
						mTxtFreshTime.setText(getResources().getString(R.string.some_day, offset));
					}
				}, mDate, true);
			}
			dialog.show();
			break;
			
		case R.id.txt_awake:
			if(mPopupTime == null){
				mPopupTime = new PopupTime(this, mTxtAwake.getWidth(), mTxtAwake.getHeight(), this, mTxtAwake);
			}
			mPopupTime.show();
			break;
			
		case R.id.img_zhi_ding:
			if(! needZhiDing){
				needZhiDing = true;
				mImgZhiDing.setImageResource(R.drawable.check_box2);
			}else{
				needZhiDing = false;
				mImgZhiDing.setImageResource(R.drawable.check_box);
			}
			break;
			
		case R.id.img_awake:
			if(! needAwake){
				needAwake = true;
				mImgAwake.setImageResource(R.drawable.check_box2);
			}else{
				needAwake = false;
				mImgAwake.setImageResource(R.drawable.check_box);
			}
			break;
			
		case R.id.awake_1:
			mAwakeTime = 1;
			mTxtAwake.setText(R.string.awake_time_1);
			mPopupTime.dismiss();
			break;
			
		case R.id.awake_2:
			mAwakeTime = 2;
			mTxtAwake.setText(R.string.awake_time_2);
			mPopupTime.dismiss();
			break;
			
		case R.id.awake_3:
			mAwakeTime = 3;
			mTxtAwake.setText(R.string.awake_time_3);
			mPopupTime.dismiss();
			break;
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		CHLogger.d(this, "requestCode " + requestCode + ",  resultCode " + resultCode);
		try {
			if (resultCode == RESULT_OK) {
				if (requestCode == PhotoSelectPopupView.TAKE_PHOTO_FROM_LOCAL){
					if (data != null) {

						//quxy mod start
						//Uri uri = data.getData();
						String filepath = data.getStringExtra("filepath");
						Uri uri = Uri.fromFile(new File(filepath));
						//quxy mod end

						mPopupAltView.cutPhoto(uri, mWidth, mHeight);
					}
				}
				else if(requestCode == PhotoSelectPopupView.TAKE_PHOTO_FROM_CAMERA) {
					Uri uri = mPopupAltView.getPhotoUri();
					mPopupAltView.cutPhoto(uri, mWidth, mHeight);
				}
				else if(requestCode == PhotoSelectPopupView.CUT_PHOTO){
					mPhotoUri = mPopupAltView.getPhotoUri();
					if(mPhotoUri != null && mPhotoUri.getPath() != null){
						File file = new File(mPhotoUri.getPath());
						if(file.exists() && file.isFile()){
							try {
								mPhotoPath = file.getPath();
								mImgPhoto.setImageBitmap(PictureUtil.
										decodeSampledBitmapFromFile(file.getPath(), 
												mWidth / 2, 
												mHeight / 2));
							}catch (Exception e) {
								e.printStackTrace();
							}catch (OutOfMemoryError e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void clickRight() {
		if(mPhotoPath == null){
			Toast.makeText(this, R.string.addfruit_toast_1, Toast.LENGTH_SHORT).show();
		}
		else if(mEditFruit.getText().toString().trim().equals("")){
			Toast.makeText(this, R.string.addfruit_toast_2, Toast.LENGTH_SHORT).show();
		}
		else{
			if(fruit == null){
				Fruit f = new Fruit();
				f.setID(System.currentTimeMillis());
				f.setAwakeTime(mAwakeTime);
				f.setDeadline(mDate);
				f.setName(mEditFruit.getText().toString().trim());
				f.setneedAwake(needAwake);
				f.setneedZD(needZhiDing);
				f.setPhotoUrl(mPhotoPath);
				f.setDate(format.format(Calendar.getInstance().getTime()));
				f.setOrder(mDate.getTime());
				
				try {
					getSqlitdb().insert(f);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				Fruit f = new Fruit();
				f.setID(fruit.getID());
				f.setAwakeTime(mAwakeTime);
				f.setDeadline(mDate);
				f.setName(mEditFruit.getText().toString().trim());
				f.setneedAwake(needAwake);
				f.setneedZD(needZhiDing);
				f.setPhotoUrl(mPhotoPath);
				f.setDate(fruit.getDate());
				f.setOrder(mDate.getTime());
				
				try {
					getSqlitdb().update(f, "ID=" + fruit.getID());
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				Intent i = new Intent();
				i.putExtra("fruit", f);
				setResult(RESULT_OK, i);
			}
			
			finish();
		}
	}
	
	@Override
	protected void clickMid() {
		try {
			fruit.setIsDelete(true);
			getSqlitdb().update(fruit);
			finish();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Intent i = new Intent(AddFruitActivity.this, MainActivity.class);
		startActivity(i);
		finish();
	}
}
