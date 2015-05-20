package com.aaa.activity;

import java.util.ArrayList;

import com.wanglin.R;
import com.changhong.annotation.CHInjectView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FruitListActivity extends LlwBaseActivity {

	private ArrayList<FruitMsg> fruitList = new ArrayList<FruitMsg>();
	
	@CHInjectView(id = R.id.list_fruit)
	private ListView listView;
	
	@Override
	protected void onAfterOnCreate(Bundle savedInstanceState) {
		super.onAfterOnCreate(savedInstanceState);
		setRightBtn(false, 0);

		int season = getIntent().getIntExtra("season", 1);
		initData(season);
		listView.setAdapter(new MyAdapter());
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				FruitMsg fruit = fruitList.get(position);
				if(fruit != null){
					Intent intent = null;
					if(fruit.nameID == R.string.ping_guo){
						intent = new Intent(FruitListActivity.this, FruitDesActivity.class);
					}
					else if(fruit.nameID == R.string.xiang_jiao){
						intent = new Intent(FruitListActivity.this, BananaActivity.class);
					}
					
					if(intent != null)
						startActivity(intent);
				}
			}
		});
	}

	private void initData(int season) {
		switch (season) {
		case SeasonActivity.SPRING:
			setTitile(R.string.spring);
			break;

		case SeasonActivity.SUMMER:
			setTitile(R.string.summer);
			fruitList.add(new FruitMsg(R.drawable.xi_gua, R.string.xi_gua, 25));
			fruitList.add(new FruitMsg(R.drawable.tao_zi, R.string.tao, 48));
			fruitList.add(new FruitMsg(R.drawable.li_zi, R.string.li, 44));
			fruitList.add(new FruitMsg(R.drawable.pu_tao, R.string.pu_tao, 43));
			fruitList.add(new FruitMsg(R.drawable.ying_tao_fan_qie, R.string.ying_tao_fan_qie, 22));
			fruitList.add(new FruitMsg(R.drawable.bo_luo, R.string.bo_luo, 41));
			fruitList.add(new FruitMsg(R.drawable.huo_long_guo, R.string.huo_long_guo, 51));
			fruitList.add(new FruitMsg(R.drawable.cao_mei, R.string.cao_mei, 30));
			fruitList.add(new FruitMsg(R.drawable.mang_guo, R.string.mang_guo, 32));
			fruitList.add(new FruitMsg(R.drawable.ha_mi_gua, R.string.tian_gua, 26));
			fruitList.add(new FruitMsg(R.drawable.xing_er, R.string.li_zi, 36));
			fruitList.add(new FruitMsg(R.drawable.zao, R.string.hong_zao, 122));
			break;

		case SeasonActivity.FALL:
			setTitile(R.string.fall);
			fruitList.add(new FruitMsg(R.drawable.apple, R.string.ping_guo, 52));
			fruitList.add(new FruitMsg(R.drawable.xiang_jiao, R.string.xiang_jiao, 91));
			fruitList.add(new FruitMsg(R.drawable.cheng, R.string.cheng, 47));
			fruitList.add(new FruitMsg(R.drawable.you_zi, R.string.you_zi, 41));
			fruitList.add(new FruitMsg(R.drawable.mi_hou_tao, R.string.mi_hou_tao, 56));
//			fruitList.add(new FruitMsg(R.drawable.jv_zi, R.string.jv_zi, 43));//缺图
			fruitList.add(new FruitMsg(R.drawable.mu_gua, R.string.mu_gua, 27));
			fruitList.add(new FruitMsg(R.drawable.qing_ping_guo, R.string.qing_ping_guo, 49));
			break;

		case SeasonActivity.WINTER:
			setTitile(R.string.winter);
			fruitList.add(new FruitMsg(R.drawable.dong_ji_shui_guo, R.string.xue_lian_guo, 17));
			break;
		}
	}

	private class MyAdapter extends BaseAdapter{

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
			final FruitMsg fruit = fruitList.get(position);
			if(convertView  == null){
				convertView = LayoutInflater.from(FruitListActivity.this).inflate(R.layout.item_fruit, null);
				vh = new ViewHolder();
				vh.photo = (ImageView) convertView.findViewById(R.id.img_photo);
				vh.name = (TextView) convertView.findViewById(R.id.name);
				vh.describe = (TextView) convertView.findViewById(R.id.describe);
				convertView.setTag(vh);
			}else{
				vh = (ViewHolder) convertView.getTag();
			}
			
			vh.photo.setImageResource(fruit.imgID);
			vh.name.setText(fruit.nameID);
			vh.describe.setText(FruitListActivity.this.getResources().getString(R.string.fruit_des, fruit.energe));
			
			return convertView;
		}
	}
	
	private class ViewHolder{
		public ImageView photo;
		public TextView name;
		public TextView describe;
	}
	
	private class FruitMsg{
		public int imgID;
		public int nameID;
		public int energe;
		
		public FruitMsg(int img, int name, int e){
			imgID = img;
			nameID = name;
			energe = e;
		}
	}
}
