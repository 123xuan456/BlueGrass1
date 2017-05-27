package com.reeching.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.reeching.adapter.WellDoneAdapter;
import com.reeching.bean.ZhanlanBean;
import com.reeching.bean.ZhanlanBean.Infos;
import com.reeching.bluegrass.BaseApplication;
import com.reeching.bluegrass.R;
import com.reeching.utils.HttpApi;

public class WellDoneFragment extends Fragment {
	private List<Infos> list;
	private List<Infos> alllist;
	private WellDoneAdapter adapter;
	private PullToRefreshListView mLV_AfterSalesInfor;
	private TextView tv;
	private int i = 2;
	HttpUtils hu = new HttpUtils();
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
				tv.setVisibility(View.GONE);
				adapter = new WellDoneAdapter(alllist, getActivity());
				adapter.notifyDataSetChanged();
				mLV_AfterSalesInfor.setAdapter(adapter);
				mLV_AfterSalesInfor.setSelection(0);
			} else {
				adapter = new WellDoneAdapter(alllist, getActivity());
				adapter.notifyDataSetChanged();
				mLV_AfterSalesInfor.setAdapter(adapter);
				tv.setVisibility(View.VISIBLE);
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_welldone, null);
		mLV_AfterSalesInfor = (PullToRefreshListView) view
				.findViewById(R.id.welldone_lv);
		tv = (TextView) view.findViewById(R.id.welldone_tv);
		mLV_AfterSalesInfor.setMode(Mode.BOTH);

		initdata();
		mLV_AfterSalesInfor
				.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub
                               initdata();

					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub
						RequestParams params = new RequestParams();
						params.addQueryStringParameter("userId", BaseApplication.getInstance()
				.getId());
						params.addQueryStringParameter("pageSize", 10 + "");
						params.addQueryStringParameter("pageNo", i + "");
						hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.welldone,
								params, new RequestCallBack<String>() {

									@Override
									public void onFailure(HttpException arg0,
											String arg1) {
										// TODO Auto-generated method stub

									}

									@Override
									public void onSuccess(
											ResponseInfo<String> arg0) {
										// TODO Auto-generated method stub
										i = i + 1;
										ZhanlanBean bean = JSON.parseObject(
												arg0.result, ZhanlanBean.class);
										if (bean.getResult().equals("1")) {
											list = new ArrayList<Infos>();
											list.addAll(bean.getInfos());
											alllist.addAll(list);
											handler.sendEmptyMessage(0);
										} else {
											Toast.makeText(getActivity(),
													"没有更多数据！", Toast.LENGTH_SHORT).show();
										}
										mLV_AfterSalesInfor.onRefreshComplete();
									}
								});
					}
				});

		return view;
	}

	private void initdata() {
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("pageSize", 10 + "");
		params.addQueryStringParameter("pageNo", 1 + "");
		params.addQueryStringParameter("userId", BaseApplication.getInstance()
				.getId());
		hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.welldone, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub

						ZhanlanBean bean = JSON.parseObject(arg0.result,
								ZhanlanBean.class);
						if (bean.getResult().equals("1")) {
							alllist = new ArrayList<Infos>();
							alllist.addAll(bean.getInfos());
							handler.sendEmptyMessage(0);
							mLV_AfterSalesInfor.onRefreshComplete();
						} else {
							handler.sendEmptyMessage(1);
							mLV_AfterSalesInfor.onRefreshComplete();
						}
					}

				});

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initdata();

	}
}
