package com.reeching.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.reeching.adapter.MyFragmentPagerAdapter;
import com.reeching.bluegrass.R;
import com.reeching.utils.MyViewPager;

import java.lang.reflect.Field;
import java.util.ArrayList;
//上报页面
public class ReportFragment extends Fragment {

	private ReportedDetailFagment mReportedDetailFagment;
	private ReportDetailFragment mReportDetailFragment;
	private RadioGroup mRadioGroup;
	private MyViewPager mPager;
	private ArrayList<Fragment> fragmentsList;
	private RadioButton rbt1, rbt2;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_report, null);

		mRadioGroup = (RadioGroup) view
				.findViewById(R.id.fragment_report_radiogroups);
		rbt1 = (RadioButton) view.findViewById(R.id.fragment_report_rbt1);
		rbt2 = (RadioButton) view.findViewById(R.id.fragmet_reported_rbt2);
		rbt1.setOnClickListener(new MyOnClickListener(0));
		rbt2.setOnClickListener(new MyOnClickListener(1));
		
		InitViewPager(view);
		mRadioGroup.check(R.id.fragment_report_rbt1);
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

	private void InitViewPager(View parentView) {
		mPager = (MyViewPager) parentView
				.findViewById(R.id.viewvager_reportdetail);
		mPager.setCanScrollble(false);
		fragmentsList = new ArrayList<Fragment>();
		mReportDetailFragment = new ReportDetailFragment();//上报
		mReportedDetailFagment = new ReportedDetailFagment();//已上报

		fragmentsList.add(mReportDetailFragment);
		fragmentsList.add(mReportedDetailFagment);

		mPager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager(),
				fragmentsList));
		mPager.setCurrentItem(0);

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
				mRadioGroup.check(R.id.fragment_report_rbt1);
			} else {
				mRadioGroup.check(R.id.fragmet_reported_rbt2);
			}
		}
	};
	
}
