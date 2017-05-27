package com.reeching.fragment;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.reeching.adapter.MyFragmentPagerAdapter;
import com.reeching.bluegrass.AddHualangLocation;
import com.reeching.bluegrass.R;
import com.reeching.utils.MyViewPager;

public class HualangFragment extends Fragment {
	private MapFragment mMapFragment;
	private ListFragment mListFragment;
	private RadioGroup radioGroup;
	private ImageView add;
	private MyViewPager mPager;
	private ArrayList<Fragment> fragmentsList;
	private RadioButton rbt1, rbt2;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_hualang, null);
		add = (ImageView) view.findViewById(R.id.fragment_hualang_add);
		mMapFragment = new MapFragment();
		mListFragment = new ListFragment();

		radioGroup = (RadioGroup) view
				.findViewById(R.id.fragment_hualang_radiogroups);
		rbt1 = (RadioButton) view.findViewById(R.id.fragment_hualang_maprbt);
		rbt2 = (RadioButton) view.findViewById(R.id.fragmet_hualang_listrbt);
		rbt1.setOnClickListener(new MyOnClickListener(0));
		rbt2.setOnClickListener(new MyOnClickListener(1));
		radioGroup.check(R.id.fragment_hualang_maprbt);
		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getActivity(),
						AddHualangLocation.class);
				startActivity(intent);
			}
		});
		InitViewPager(view);
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

	public class MyOnClickListener implements OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mPager.setCurrentItem(index);
			if (index == 0) {
				radioGroup.check(R.id.fragment_hualang_maprbt);
			} else {
				radioGroup.check(R.id.fragmet_hualang_listrbt);
			}
		}
	};

	private void InitViewPager(View parentView) {
		mPager = (MyViewPager) parentView.findViewById(R.id.fragment_hualang);
		mPager.setCanScrollble(false);
		fragmentsList = new ArrayList<Fragment>();
		mMapFragment = new MapFragment();
		mListFragment = new ListFragment();
		fragmentsList.add(mMapFragment);
		fragmentsList.add(mListFragment);
		mPager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager(),
				fragmentsList));
		mPager.setCurrentItem(0);
		mPager.setOffscreenPageLimit(2);
	}
}
