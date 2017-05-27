package com.reeching.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.reeching.adapter.HualangAdapter;
import com.reeching.bean.AllHualangInfo;
import com.reeching.bean.AllHualangInfo.Infos;
import com.reeching.bluegrass.MyEditText;
import com.reeching.bluegrass.R;
import com.reeching.utils.HttpApi;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment implements View.OnClickListener {
	private List<Infos> infos;
	private List<Infos> allinfos;
//	 private ListView lv;
	private HualangAdapter adapter;
	private TextView tv;
	private int i = 2;
	HttpUtils hu = new HttpUtils();
	private PullToRefreshListView mLV_AfterSalesInfor;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
				tv.setVisibility(View.GONE);
				adapter = new HualangAdapter(allinfos, getActivity());

				mLV_AfterSalesInfor.setAdapter(adapter);
//				mLV_AfterSalesInfor.setSelection(allinfos.size() - 10);
			} else {
				tv.setVisibility(View.VISIBLE);
			}
		};
	};
	private MyEditText mMyEditTextA;
	private ImageView mImageViewA;
	private View mViewA;
	private ProgressDialog progressDialog;
	private MyEditText mMyEditTextB;
	private ImageView mImageViewB;
	private View mViewB;
	private MyEditText mMyEditTextC;
	private ImageView mImageViewC;
	private View mViewC;
	private MyEditText mMyEditTextD;
	private ImageView mImageViewD;
	private View mViewD;
	private MyEditText mMyEditTextE;
	private ImageView mImageViewE;
	private View mViewE;
	private MyEditText mMyEditTextF;
	private ImageView mImageViewF;
	private View mViewF;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_maplist, null);
		tv = (TextView) view.findViewById(R.id.fragment_maplist_tv);
		mLV_AfterSalesInfor = (PullToRefreshListView) view
				.findViewById(R.id.fragment_maplist_lv);
		mMyEditTextA = (MyEditText) view.findViewById(R.id.listFragment_A);
		mImageViewA = (ImageView) view.findViewById(R.id.fragment_list_ivA);
		mViewA = (View) view.findViewById(R.id.fragment_list_viewA);
		mMyEditTextB = (MyEditText) view.findViewById(R.id.listFragment_B);
		mImageViewB = (ImageView) view.findViewById(R.id.fragment_list_ivB);
		mViewB = (View) view.findViewById(R.id.fragment_list_viewB);
		mMyEditTextC = (MyEditText) view.findViewById(R.id.listFragment_C);
		mImageViewC = (ImageView) view.findViewById(R.id.fragment_list_ivC);
		mViewC = (View) view.findViewById(R.id.fragment_list_viewC);
		mMyEditTextD = (MyEditText) view.findViewById(R.id.listFragment_D);
		mImageViewD = (ImageView) view.findViewById(R.id.fragment_list_ivD);
		mViewD = (View) view.findViewById(R.id.fragment_list_viewD);
		mMyEditTextE = (MyEditText) view.findViewById(R.id.listFragment_E);
		mImageViewE = (ImageView) view.findViewById(R.id.fragment_list_ivE);
		mViewE = (View) view.findViewById(R.id.fragment_list_viewE);
		mMyEditTextF = (MyEditText) view.findViewById(R.id.listFragment_F);
		mImageViewF = (ImageView) view.findViewById(R.id.fragment_list_ivF);
		mViewF = (View) view.findViewById(R.id.fragment_list_viewF);
		mMyEditTextA.setOnClickListener(this);
		mMyEditTextB.setOnClickListener(this);
		mMyEditTextC.setOnClickListener(this);
		mMyEditTextD.setOnClickListener(this);
		mMyEditTextE.setOnClickListener(this);
		mMyEditTextF.setOnClickListener(this);
		mImageViewA.setOnClickListener(this);
		mImageViewB.setOnClickListener(this);
		mImageViewC.setOnClickListener(this);
		mImageViewD.setOnClickListener(this);
		mImageViewE.setOnClickListener(this);
		mImageViewF.setOnClickListener(this);
		allinfos = new ArrayList<Infos>();

		return view;
	}

	private void initdata(int i) {
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("areaNo", i + "");

		hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.getallhualanginfo,
				params, new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						Log.d("shuaishuai", "onSuccess:11111111 " + allinfos.size());
						progressDialog.dismiss();
					}

					@Override
					public void onStart() {
						super.onStart();

						progressDialog = new ProgressDialog(getActivity());
						progressDialog.setMessage("加载中,请稍后...");
						progressDialog.setCanceledOnTouchOutside(false);
						progressDialog.show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						AllHualangInfo allHualangInfo = JSON.parseObject(
								arg0.result, AllHualangInfo.class);
						if (allHualangInfo.getResult().equals("1")) {
							allinfos.addAll(allHualangInfo.getInfos());
							handler.sendEmptyMessage(0);
							progressDialog.dismiss();
						} else {
							Log.d("shuaishuai", "onSuccess:11111111 " );
							handler.sendEmptyMessage(1);
							progressDialog.dismiss();
						}
					}
				});

	}

	private void getImageViewGone() {
		mImageViewA.setVisibility(View.GONE);
		mImageViewB.setVisibility(View.GONE);
		mImageViewC.setVisibility(View.GONE);
		mImageViewD.setVisibility(View.GONE);
		mImageViewE.setVisibility(View.GONE);
		mImageViewF.setVisibility(View.GONE);
	}

	private void getImageViewShow() {
		mImageViewA.setVisibility(View.VISIBLE);
		mImageViewB.setVisibility(View.VISIBLE);
		mImageViewC.setVisibility(View.VISIBLE);
		mImageViewD.setVisibility(View.VISIBLE);
		mImageViewE.setVisibility(View.VISIBLE);
		mImageViewF.setVisibility(View.VISIBLE);
	}

	private void getMyEditTextShow() {
		mMyEditTextA.setVisibility(View.VISIBLE);
		mMyEditTextB.setVisibility(View.VISIBLE);
		mMyEditTextC.setVisibility(View.VISIBLE);
		mMyEditTextD.setVisibility(View.VISIBLE);
		mMyEditTextE.setVisibility(View.VISIBLE);
		mMyEditTextF.setVisibility(View.VISIBLE);
	}

	private void getMyEditTextGone() {
		mMyEditTextA.setVisibility(View.GONE);
		mMyEditTextB.setVisibility(View.GONE);
		mMyEditTextC.setVisibility(View.GONE);
		mMyEditTextD.setVisibility(View.GONE);
		mMyEditTextE.setVisibility(View.GONE);
		mMyEditTextF.setVisibility(View.GONE);
	}

	private void getViewShow() {
		mViewA.setVisibility(View.VISIBLE);
		mViewB.setVisibility(View.VISIBLE);
		mViewC.setVisibility(View.VISIBLE);
		mViewD.setVisibility(View.VISIBLE);
		mViewE.setVisibility(View.VISIBLE);
		mViewF.setVisibility(View.VISIBLE);
	}

	private void getViewGone() {
		mViewA.setVisibility(View.GONE);
		mViewB.setVisibility(View.GONE);
		mViewC.setVisibility(View.GONE);
		mViewD.setVisibility(View.GONE);
		mViewE.setVisibility(View.GONE);
		mViewF.setVisibility(View.GONE);
	}

	private void onGetEditTextClick() {
		getMyEditTextGone();
		getViewGone();
		getImageViewShow();
	}

	private void onImageViewClick() {
		getMyEditTextShow();
		getViewShow();
		getImageViewGone();
	}

	private void getClearList() {
		if (allinfos.size() != 0) {
			adapter.notifyDataSetChanged();
			allinfos.clear();
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.listFragment_A:
				onGetEditTextClick();
				initdata(1);
				break;
			case R.id.listFragment_B:
				onGetEditTextClick();
				initdata(2);
				break;
			case R.id.listFragment_C:
				onGetEditTextClick();
				initdata(3);
				break;
			case R.id.listFragment_D:
				onGetEditTextClick();
				initdata(4);
				break;
			case R.id.listFragment_E:
				onGetEditTextClick();
				initdata(5);
				break;
			case R.id.listFragment_F:
				onGetEditTextClick();
				initdata(6);
				break;


			case R.id.fragment_list_ivA:
				onImageViewClick();
			    getClearList();
				break;
			case R.id.fragment_list_ivB:
				onImageViewClick();
				getClearList();
				break;
			case R.id.fragment_list_ivC:
				onImageViewClick();
				getClearList();
				break;
			case R.id.fragment_list_ivD:
				onImageViewClick();
				getClearList();
				break;
			case R.id.fragment_list_ivE:
				onImageViewClick();
				getClearList();
				break;
			case R.id.fragment_list_ivF:
				onImageViewClick();
				getClearList();
				break;
		}

	}
}
