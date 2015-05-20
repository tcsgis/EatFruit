package com.aaa.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.aaa.activity.DateDialog.onDateChangedListener;
import com.aaa.db.Fruit;
import com.aaa.db.FruitEat;
import com.aaa.other.TimeDialog;
import com.aaa.other.TimeDialog.onTimeChangedListener;
import com.changhong.activity.widget.AppMainDialog;
import com.changhong.activity.widget.HorizontalListView;
import com.changhong.annotation.CHInjectView;
import com.changhong.util.bitmap.CHBitmapCacheWork;
import com.changhong.util.bitmap.CHBitmapCallBackHanlder;
import com.changhong.util.bitmap.CHDownloadBitmapHandler;
import com.wanglin.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EatFruitActivity extends LlwBaseActivity{

	@CHInjectView(id = R.id.txt_has_eat)
	private TextView hasEat;
	@CHInjectView(id = R.id.txt_hasnt_eat)
	private TextView hasntEat;
	@CHInjectView(id = R.id.date1)
	private TextView date1;
	@CHInjectView(id = R.id.date2)
	private TextView date2;
	@CHInjectView(id = R.id.txt_no_fruit)
	private TextView noFruit;
	@CHInjectView(id = R.id.list_eat_1)
	private HorizontalListView list1;
	@CHInjectView(id = R.id.list_eat_2)
	private HorizontalListView list2;
	
	private List<FruitEat> fruitList1 = new ArrayList<FruitEat>();
	private List<Fruit> fruitList2 = new ArrayList<Fruit>();
	private MyAdapter1 adapter1 = null;
	private MyAdapter2 adapter2 = null;
	private Date d1 = Calendar.getInstance().getTime();
	private Date d2 = Calendar.getInstance().getTime();
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	private String today;
	private DateDialog dialog = null;
	private AppMainDialog dialog2 = null;
	private TimeDialog dialog3 = null;
	private SharedPreferences mSP = null;
	private SharedPreferences.Editor mSPEditor = null;
	
	@Override
	protected void onAfterOnCreate(Bundle savedInstanceState) {
		super.onAfterOnCreate(savedInstanceState);
		setRightBtn(true, R.drawable.awake_2);
		setTitile(R.string.eat_fruit);
		
		mSP = getSharedPreferences(MainActivity.SP, Activity.MODE_PRIVATE);
		mSPEditor = mSP.edit();
		
		today = format.format(Calendar.getInstance().getTime());
		date1.setText(today);
		date2.setText(today);
		
		adapter1 = new MyAdapter1(this);
		adapter2 = new MyAdapter2(this);
		list1.setAdapter(adapter1);
		list2.setAdapter(adapter2);
		
		setList1(today, true);
		setList2(today);
		
		date1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(dialog == null){
					dialog = new DateDialog(EatFruitActivity.this, R.style.appdialog, new onDateChangedListener() {
						@Override
						public void onDateChanged(Date date) {
							d1 = date;
							date1.setText(format.format(date));
							setList1(format.format(date), true);
						}
					}, d1, false);
				}
				dialog.show();
			}
		});
		
		date2.setVisibility(View.GONE);
//		date2.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if(dialog == null){
//					dialog = new DateDialog(EatFruitActivity.this, R.style.appdialog, new onDateChangedListener() {
//						@Override
//						public void onDateChanged(Date date) {
//							d2 = date;
//							date2.setText(format.format(date));
//							setList2(format.format(date));
//						}
//					}, d2, false);
//				}
//				dialog.show();
//			}
//		});
	}
	
	@Override
	protected void clickRight() {
		int[] time = Util.getAwakeTime(this);
				
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, time[0]);
		c.set(Calendar.MINUTE, time[1]);
		
		if(dialog3 == null){
			dialog3 = new TimeDialog(this, R.style.appdialog, new onTimeChangedListener() {
				
				@Override
				public void onTimeChanged(int h, int m) {
					Util.setAwakeTime(EatFruitActivity.this, h, m);
				}
			}, c.getTime());
		}
		
		dialog3.show();
	}

	private void setList1(String date, boolean query) {
		if(query){
			try {
				fruitList1 = getSqlitdb().query(FruitEat.class, true, "Date=" + "'" + date + "'", null, null, null, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(fruitList1 != null && fruitList1.size() > 0){
			hasEat.setVisibility(View.VISIBLE);
			hasntEat.setVisibility(View.GONE);
			list1.setVisibility(View.VISIBLE);
			adapter1.notifyDataSetChanged();
		}else{
			hasEat.setVisibility(View.GONE);
			hasntEat.setVisibility(View.VISIBLE);
			list1.setVisibility(View.GONE);
		}
		
		list1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
				if(dialog2 == null){
					dialog2 = new AppMainDialog(EatFruitActivity.this,R.style.appdialog);
					dialog2.withTitle(R.string.eat1)
					.setCancelClick(R.string.cancel_quxiao)
					.setOKClick(R.string.delete, new View.OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							try {
								getSqlitdb().delete(fruitList1.get(position));
								fruitList1.remove(position);
							} catch (Exception e) {
								e.printStackTrace();
							}
							
							setList1(format.format(d1), false);
							dialog2.dismiss();
						}
					});
				}
				dialog2.show();
			}
		});
	}
	
	private void setList2(final String date) {
		try {
			fruitList2 = getSqlitdb().query(Fruit.class, true, "isDelete=0", null, null, null, null);
//			fruitList2 = getSqlitdb().query(Fruit.class, true, "Date=" + "'" + date + "'" + " AND " + "isDelete=0", null, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(fruitList2 != null && fruitList2.size() > 0){
			noFruit.setVisibility(View.GONE);
			list2.setVisibility(View.VISIBLE);
			adapter2.notifyDataSetChanged();
		}else{
			noFruit.setVisibility(View.VISIBLE);
			list2.setVisibility(View.GONE);
		}
		
		list2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				if(format.format(d2).equals(today)){
					Fruit f = fruitList2.get(position);
					
					try {
						String where = "fid=" + f.getID() + " AND Date=" + "'" + date + "'"; 
						List<FruitEat> list = getSqlitdb().query(FruitEat.class, true, where, null, null, null, null);
						if(list != null && list.size() > 0){
							Toast.makeText(EatFruitActivity.this, R.string.toast_eat2, Toast.LENGTH_SHORT).show();
						}else{
							FruitEat e = new FruitEat();
							e.setID(System.currentTimeMillis());
							e.setfid(f.getID());
							e.setName(f.getName());
							e.setPhotoUrl(f.getPhotoUrl());
							e.setDate(today);
							
							try {
								getSqlitdb().insert(e);
							} catch (Exception e2) {
								e2.printStackTrace();
							}
							
							try {
								f.seteatDate(Calendar.getInstance().getTime());
								getSqlitdb().update(f);
							} catch (Exception e2) {
								e2.printStackTrace();
							}
							
							d1 = Calendar.getInstance().getTime();
							date1.setText(today);
							setList1(today, true);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					Toast.makeText(EatFruitActivity.this, R.string.toast_eat, Toast.LENGTH_SHORT).show();
				}
				
			}
		});
	}


	private class MyAdapter1 extends BaseAdapter{
		private CHBitmapCacheWork imageFetcher;
		private Context context;
		
		public MyAdapter1(Context c){
			context = c;
			init1();
		}

		private void init1() {
			imageFetcher = new CHBitmapCacheWork(context);
			
			CHBitmapCallBackHanlder taBitmapCallBackHanlder = new CHBitmapCallBackHanlder();
			taBitmapCallBackHanlder
			.setLoadingImage(context, R.drawable.apple);
			taBitmapCallBackHanlder.setCircleParams(true);
			
			Bitmap loading = taBitmapCallBackHanlder.getmLoadingBitmap();
			if(loading != null){
				int width = taBitmapCallBackHanlder.getmLoadingBitmap().getWidth();
				int height = taBitmapCallBackHanlder.getmLoadingBitmap().getHeight();
				CHDownloadBitmapHandler downloadBitmapFetcher = new CHDownloadBitmapHandler(context, width, height);
				imageFetcher.setProcessDataHandler(downloadBitmapFetcher);
			}
			
			imageFetcher.setCallBackHandler(taBitmapCallBackHanlder);
			imageFetcher.setFileCache(EatFruitActivity.this.getCHApplication().getFileCache());
		}
		
		@Override
		public int getCount() {
			if(fruitList1 != null){
				return fruitList1.size();
			}
			return 0;	
		}

		@Override
		public Object getItem(int position) {
			if(fruitList1 != null){
				return fruitList1.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh = null;
			final FruitEat item = fruitList1.get(position);
			if (convertView == null) {
				vh = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.item_eat_1, null);
				vh.photo = (ImageView) convertView.findViewById(R.id.photo);
				vh.name = (TextView) convertView.findViewById(R.id.name);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			
			vh.name.setText(item.getName());
			
			if(!(item.getPhotoUrl() == null || item.getPhotoUrl().length() < 10)){
				imageFetcher.loadFormCache(item.getPhotoUrl(), vh.photo);
			}
			
			return convertView;
		}
		
		private class ViewHolder{
			ImageView photo;
			TextView name;
		}
	}
	
	private class MyAdapter2 extends BaseAdapter{
		private CHBitmapCacheWork imageFetcher;
		private CHBitmapCacheWork imageFetcher2;
		private Context context;
		
		public MyAdapter2(Context c){
			context = c;
			init1();
			init2();
		}

		private void init1() {
			imageFetcher = new CHBitmapCacheWork(context);
			
			CHBitmapCallBackHanlder taBitmapCallBackHanlder = new CHBitmapCallBackHanlder();
			taBitmapCallBackHanlder
			.setLoadingImage(context, R.drawable.apple);
			taBitmapCallBackHanlder.setCircleParams(true);
			
			Bitmap loading = taBitmapCallBackHanlder.getmLoadingBitmap();
			if(loading != null){
				int width = taBitmapCallBackHanlder.getmLoadingBitmap().getWidth();
				int height = taBitmapCallBackHanlder.getmLoadingBitmap().getHeight();
				CHDownloadBitmapHandler downloadBitmapFetcher = new CHDownloadBitmapHandler(context, width, height);
				imageFetcher.setProcessDataHandler(downloadBitmapFetcher);
			}
			
			imageFetcher.setCallBackHandler(taBitmapCallBackHanlder);
			imageFetcher.setFileCache(EatFruitActivity.this.getCHApplication().getFileCache());
		}

		private void init2() {
			imageFetcher2 = new CHBitmapCacheWork(context);
			
			CHBitmapCallBackHanlder taBitmapCallBackHanlder = new CHBitmapCallBackHanlder();
			taBitmapCallBackHanlder
			.setLoadingImage(context, R.drawable.apple);
			taBitmapCallBackHanlder.setCircleParams(true);
			taBitmapCallBackHanlder.setGrayParams(true);
			
			Bitmap loading = taBitmapCallBackHanlder.getmLoadingBitmap();
			if(loading != null){
				int width = taBitmapCallBackHanlder.getmLoadingBitmap().getWidth();
				int height = taBitmapCallBackHanlder.getmLoadingBitmap().getHeight();
				CHDownloadBitmapHandler downloadBitmapFetcher = new CHDownloadBitmapHandler(context, width, height);
				imageFetcher2.setProcessDataHandler(downloadBitmapFetcher);
			}
			
			imageFetcher2.setCallBackHandler(taBitmapCallBackHanlder);
			imageFetcher2.setFileCache(EatFruitActivity.this.getCHApplication().getFileCache());
		}
		
		@Override
		public int getCount() {
			if(fruitList2 != null){
				return fruitList2.size();
			}
			return 0;	
		}

		@Override
		public Object getItem(int position) {
			if(fruitList2 != null){
				return fruitList2.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh = null;
			final Fruit item = fruitList2.get(position);
			if (convertView == null) {
				vh = new ViewHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.item_eat_2, null);
				vh.photo = (ImageView) convertView.findViewById(R.id.photo);
				vh.name = (TextView) convertView.findViewById(R.id.name);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			
			vh.name.setText(item.getName());
			
			Calendar c = Calendar.getInstance();
			int today = c.get(Calendar.DAY_OF_YEAR);
			c.setTime(item.getDeadline());
			int deadline = c.get(Calendar.DAY_OF_YEAR);
			int offset = deadline - today;
			
			if(!(item.getPhotoUrl() == null || item.getPhotoUrl().length() < 10)){
				if(offset >= 0){
					imageFetcher.loadFormCache(item.getPhotoUrl(), vh.photo);
				}else{
					imageFetcher2.loadFormCache(item.getPhotoUrl(), vh.photo);
				}
			}
			
			return convertView;
		}
		
		private class ViewHolder{
			ImageView photo;
			TextView name;
		}
	}
}
