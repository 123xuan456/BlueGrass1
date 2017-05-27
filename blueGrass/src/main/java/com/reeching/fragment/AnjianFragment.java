package com.reeching.fragment;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.reeching.adapter.MyFragmentPagerAdapter;
import com.reeching.bluegrass.R;
import com.reeching.utils.MyViewPager;

public class AnjianFragment extends Fragment {
	private WaitForCheckFragment mWaitForCheckFragment;
	private WaitForHeChaFragment mWaitForHeChaFragment;
	private WellDoneFragment mWellDoneFragment;
	private MyViewPager mPager;
	private ArrayList<Fragment> fragmentsList;
	private RadioButton rbt1, rbt2, rbt3;
	private RadioGroup mRadioGroup;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_anjian, null);

		rbt1 = (RadioButton) view.findViewById(R.id.fragment_anjian_rbt1);
		rbt2 = (RadioButton) view.findViewById(R.id.fragment_anjian_rbt2);
		rbt3 = (RadioButton) view.findViewById(R.id.fragment_anjian_rbt3);
		rbt1.setOnClickListener(new MyOnClickListener(0));
		rbt2.setOnClickListener(new MyOnClickListener(1));
		rbt3.setOnClickListener(new MyOnClickListener(2));
		
		mRadioGroup = (RadioGroup) view
				.findViewById(R.id.fragment_anjian_radiogroups);
		InitViewPager(view);
		mRadioGroup.check(R.id.fragment_anjian_rbt1);
		return view;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class
					.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);

		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mPager.setCurrentItem(index);
			if (index == 0) {
				mRadioGroup.check(R.id.fragment_anjian_rbt1);
			} else if (index == 1) {
				mRadioGroup.check(R.id.fragment_anjian_rbt2);
			} else {
				mRadioGroup.check(R.id.fragment_anjian_rbt3);
			}
		}
	};

	private void InitViewPager(View parentView) {
		mPager = (MyViewPager) parentView
				.findViewById(R.id.fragment_anjiandetail);
		mPager.setCanScrollble(false);
		fragmentsList = new ArrayList<Fragment>();
		mWaitForCheckFragment = new WaitForCheckFragment();
		mWaitForHeChaFragment = new WaitForHeChaFragment();
		mWellDoneFragment = new WellDoneFragment();
		fragmentsList.add(mWaitForCheckFragment);
		fragmentsList.add(mWaitForHeChaFragment);
		fragmentsList.add(mWellDoneFragment);
		mPager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager(),
				fragmentsList));
		mPager.setCurrentItem(0);

	}

}