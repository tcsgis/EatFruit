package com.aaa.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.aaa.db.Fruit;
import com.aaa.other.AnniSwipeListView;
import com.aaa.other.MenuPopup;
import com.wanglin.R;
import com.changhong.activity.util.PollingUtils;
import com.changhong.annotation.CHInjectView;
import com.changhong.service.PollingService;
import com.changhong.service.task.receiver.AlarmAnniReceiver;
import com.changhong.util.CHLogger;
import com.changhong.util.bitmap.CHBitmapCacheWork;
import com.changhong.util.bitmap.CHBitmapCallBackHanlder;
import com.changhong.util.bitmap.CHDownloadBitmapHandler;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends LlwBaseActivity{

	public static final String SP = "sgisllw";
	public static final String HOUR = "hour";
	public static final String MINUTE = "minute";
	public static final String NOTIFY = "notifyEat";
	public static final String NOTIFY2 = "notifyOutDate";
	
	@CHInjectView(id = R.id.window)
	private RelativeLayout mWindow;
	@CHInjectView(id = R.id.title)
	private RelativeLayout mTitle;
	@CHInjectView(id = R.id.list_fruit)
	private AnniSwipeListView listView;
	@CHInjectView(id = R.id.rl_null_back)
	private RelativeLayout nullBack;
	@CHInjectView(id = R.id.ll_list)
	private LinearLayout fruitView;
	
	private MenuPopup mPopup = null;
	private List<Fruit> fruitList = new ArrayList<Fruit>();
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private MyAdapter adapter = null;
	
	@Override
	protected void onAfterOnCreate(Bundle savedInstanceState) {
		super.onAfterOnCreate(savedInstanceState);
		setLeftBtn(true, R.drawable.menu);
		AlarmAnniReceiver.getInstance();
		PollingUtils.startPollingService(this, 10, PollingService.class, getString(R.string.AlarmAnniServiceNotify));
	}
	
	protected void clickLeft() {
		if(mPopup == null){
			int height = mWindow.getHeight() - mTitle.getHeight();
			mPopup = new MenuPopup(MainActivity.this, height);
			mPopup.show();
		}
		mPopup.show();
	}
	
	protected void clickRight() {
		Intent intent = new Intent(MainActivity.this, AddFruitActivity.class);
		startActivity(intent);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		if(mPopup != null && mPopup.isShowing())
			mPopup.dismiss();
		
		String today = sdf.format(Calendar.getInstance().getTime());
		
		try {
			String where = "isDelete=0";
//			String where = "Date=" + "'" + today + "'" + " AND " + "isDelete=0";
			fruitList = getSqlitdb().query(Fruit.class, true, where, null, null, "time ASC", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(fruitList == null || fruitList.size() == 0){
			nullBack.setVisibility(View.VISIBLE);
			fruitView.setVisibility(View.GONE);
		}else{
			nullBack.setVisibility(View.GONE);
			fruitView.setVisibility(View.VISIBLE);
			sort();
			adapter = new MyAdapter(this);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> list, View arg1, int position, long arg3) {
					Intent intent = new Intent(MainActivity.this, FruitLookActivity.class);
					Bundle b = new Bundle();
					b.putSerializable("fruit", fruitList.get(position));
					intent.putExtras(b);
					startActivity(intent);
				}
			});
		}
	}
	
	private void sort() {
		int location = 0;
		Fruit f = null;
		for(int i = 0; i < fruitList.size(); i++){
			f = fruitList.get(i);
			if(f.getneedZD()){
				fruitList.remove(i);
				fruitList.add(location, f);
				location++;
			}
		}
	}

	private class MyAdapter extends BaseAdapter{
		private CHBitmapCacheWork imageFetcher;
		private CHBitmapCacheWork imageFetcher2;
		private Context context;
		
		public MyAdapter(Context c){
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
			imageFetcher.setFileCache(MainActivity.this.getCHApplication().getFileCache());
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
			imageFetcher2.setFileCache(MainActivity.this.getCHApplication().getFileCache());
		}
		
		@Override
		public int getCount() {
			return fruitList.size();
		}

		@Override
		public Object getItem(int position) {
			return fruitList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh = null;
			final Fruit fruit = fruitList.get(position);
			if(convertView  == null){
				convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_fruit_2, null);
				vh = new ViewHolder();
				vh.photo = (ImageView) convertView.findViewById(R.id.img_photo);
				vh.name = (TextView) convertView.findViewById(R.id.name);
				vh.deadline = (TextView) convertView.findViewById(R.id.deadline);
				vh.day = (TextView) convertView.findViewById(R.id.txt_day);
				vh.layout_right = (RelativeLayout) convertView.findViewById(R.id.layout_right);
				convertView.setTag(vh);
			}else{
				vh = (ViewHolder) convertView.getTag();
			}
			
			try {
				Calendar c = Calendar.getInstance();
				int today = c.get(Calendar.DAY_OF_YEAR);
				c.setTime(fruit.getDeadline());
				int deadline = c.get(Calendar.DAY_OF_YEAR);
				int offset = deadline - today;
				String date = sdf.format(fruit.getDeadline());
				
				int dayWeek = -1;
				switch (c.get(Calendar.DAY_OF_WEEK)) {
				case 1:
					dayWeek = R.string.day1;
					break;
				case 2:
					dayWeek = R.string.day2;
					break;
				case 3:
					dayWeek = R.string.day3;
					break;
				case 4:
					dayWeek = R.string.day4;
					break;
				case 5:
					dayWeek = R.string.day5;
					break;
				case 6:
					dayWeek = R.string.day6;
					break;
				case 7:
					dayWeek = R.string.day7;
					break;
				}
				if(dayWeek > 0)
					date = date + "  " + context.getResources().getString(dayWeek);
				
				vh.day.setText(context.getResources().getString(R.string.offset, offset));
				vh.deadline.setText(date);
				vh.name.setText(fruit.getName());
				
				if(!(fruit.getPhotoUrl() == null || fruit.getPhotoUrl().length() < 10)){
					if(offset >= 0){
						imageFetcher.loadFormCache(fruit.getPhotoUrl(), vh.photo);
					}else{
						imageFetcher2.loadFormCache(fruit.getPhotoUrl(), vh.photo);
					}
				}
				
		        vh.layout_right.setOnClickListener(new OnClickListener() {
		            @Override
		            public void onClick(View v) {
		            	fruitList.remove(fruit);
		            	try {
		            		getSqlitdb().delete(fruit);
						} catch (Exception e) {
							e.printStackTrace();
						}
		            	
		            	if(fruitList.size() > 0){
		            		adapter.notifyDataSetChanged();
		            	}else{
		            		fruitView.setVisibility(View.GONE);
		            		nullBack.setVisibility(View.VISIBLE);
		            	}
		            }
		        });
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return convertView;
		}
	}
	
	private class ViewHolder{
		public ImageView photo;
		public TextView name;
		public TextView deadline;
		public TextView day;
		public RelativeLayout layout_right;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		PollingUtils.stopPollingService(this, PollingService.class, getString(R.string.AlarmAnniServiceNotify));
	}
}
