package com.reeching.utils;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyViewPager extends ViewPager {

	private boolean mScrollble = true;

	public  MyViewPager(Context context) {
		super(context);
	}

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (!mScrollble) {
			return false;
		}
		return super.onTouchEvent(ev);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (!mScrollble) {
			return false;
		}
		return super.onInterceptTouchEvent(ev);
	}

	public boolean isCanScrollble() {
		return mScrollble;
	}

	public void setCanScrollble(boolean scrollble) {
		this.mScrollble = scrollble;
	}
}
