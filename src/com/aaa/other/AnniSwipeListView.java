package com.aaa.other;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.changhong.activity.widget.SwipeListView;

public class AnniSwipeListView extends SwipeListView{

	public AnniSwipeListView(Context context) {
		super(context);
	}
	
	public AnniSwipeListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public AnniSwipeListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	@SuppressLint({ "ClickableViewAccessibility", "Recycle" })
	public boolean onTouchEvent(MotionEvent ev) {
		float lastX = ev.getX();
		float lastY = ev.getY();
		if(! canSwipe(lastX, lastY)){
			closeOpenedItems();
			return super.onSuperTouchEvent(ev);
		}
		return super.onTouchEvent(ev);
	}
	
	private boolean canSwipe(float posX, float posY) {
//		int selectPos = pointToPosition((int)posX, (int)posY);
//		if(selectPos >= getHeaderViewsCount() && selectPos < (getCount() - getFooterViewsCount())){
//			if(getAdapter().getItem(selectPos) != null && ! ((AnniItem)getAdapter().getItem(selectPos)).canSwipe){
//				return false;
//			}
//		}
		return true;
	}
}
