package com.reeching.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.reeching.adapter.ZhanLanAdapter;
import com.reeching.bean.ZhanlanBean;
import com.reeching.bean.ZhanlanBean.Infos;
import com.reeching.bluegrass.BaseApplication;
import com.reeching.bluegrass.HaveReportedActivity;
import com.reeching.bluegrass.R;
import com.reeching.utils.HttpApi;

import java.util.ArrayList;
import java.util.List;

public class ReportedDetailFagment extends Fragment implements AdapterView.OnItemClickListener {

	private ZhanLanAdapter adapter;
	private List<Infos> infos;
	private List<Infos> allinfos;

	private int i = 2;
	HttpUtils hu = new HttpUtils();
	private PullToRefreshListView mLV_AfterSalesInfor;
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				adapter = new ZhanLanAdapter(allinfos, getActivity());
				mLV_AfterSalesInfor.setAdapter(adapter);
				mLV_AfterSalesInfor.setSelection(allinfos.size() - 10);//加载下一页后显示为翻页前走后一条
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_detailreported, null);

		mLV_AfterSalesInfor = (PullToRefreshListView) view
				.findViewById(R.id.detailreported_lv);
		mLV_AfterSalesInfor.setMode(PullToRefreshBase.Mode.BOTH);
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
						params.addQueryStringParameter("pageSize", 10 + "");
						params.addQueryStringParameter("pageNo", i + "");
						params.addQueryStringParameter("userId", BaseApplication.getInstance()
								.getId());
						hu.send(HttpMethod.POST, HttpApi.ip
										+ HttpApi.getallzhanlan, params,
								new RequestCallBack<String>() {

									@Override
									public void onFailure(HttpException arg0,
														  String arg1) {
										// TODO Auto-generated method stub
										// Toast.makeText(getActivity(),
										// "请检查网络！", 1).show();
									}

									@Override
									public void onSuccess(
											ResponseInfo<String> arg0) {
										// TODO Auto-generated method stub
										ZhanlanBean bean = JSON.parseObject(
												arg0.result, ZhanlanBean.class);
										i = i + 1;
										if (bean.getResult().equals("1")) {
											infos = new ArrayList<ZhanlanBean.Infos>();
											infos.addAll(bean.getInfos());
											allinfos.addAll(infos);
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

		mLV_AfterSalesInfor.setOnItemClickListener(this);
		return view;
	}

	private void initdata() {

		RequestParams params = new RequestParams();
		params.addQueryStringParameter("pageSize", 10 + "");
		params.addQueryStringParameter("pageNo", 1 + "");
		params.addQueryStringParameter("userId", BaseApplication.getInstance()
				.getId());
		hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.getallzhanlan, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						// Toast.makeText(getActivity(), "请检查网络！", 1).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						ZhanlanBean bean = JSON.parseObject(arg0.result,
								ZhanlanBean.class);
						if (bean.getResult().equals("1")) {
							allinfos = new ArrayList<Infos>();
							allinfos.addAll(bean.getInfos());
							handler.sendEmptyMessage(0);
							mLV_AfterSalesInfor.onRefreshComplete();
						} else {
							Toast.makeText(getActivity(),
									"没有更多数据！", Toast.LENGTH_SHORT).show();
							handler.sendEmptyMessage(1);
						}
					}
				});

	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.d("","");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub

		Intent intent = new Intent(getActivity(),
				HaveReportedActivity.class);
		ZhanlanBean.Infos infos = allinfos.get(position - 1);
		intent.putExtra("info", infos);
		startActivity(intent);
	}
}
