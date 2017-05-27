package com.reeching.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.reeching.adapter.HechaAdapter;
import com.reeching.bean.HechaInfobean;
import com.reeching.bluegrass.BaseApplication;
import com.reeching.bluegrass.R;
import com.reeching.utils.HttpApi;

public class WaitForHeChaFragment extends Fragment {
	private PullToRefreshListView lv;
	private HechaInfobean infobean;
	private HechaAdapter adapter;
	private TextView tv;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
				tv.setVisibility(View.GONE);
				lv.setSelection(0);
				adapter = new HechaAdapter(infobean.getInfos(), getActivity());
				adapter.notifyDataSetChanged();
				lv.setAdapter(adapter);
			} else {
				adapter = new HechaAdapter(infobean.getInfos(), getActivity());
				adapter.notifyDataSetChanged();
				lv.setAdapter(adapter);
				tv.setVisibility(View.VISIBLE);
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.fragment_waitforhecha, null);
		lv = (PullToRefreshListView) view.findViewById(R.id.fragment_waitforhecha_lv);
		tv = (TextView) view.findViewById(R.id.fragment_waitforhecha_tv);
		initdata();
		lv.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
		lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				  initdata();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

			}
		});
		return view;
	}

	private void initdata() {
		Log.d("wuyuzhong........", "onCreateView: ");
		HttpUtils hu = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("userId", BaseApplication.getInstance()
				.getId());
		hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.waitforhecha,params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
					//	Toast.makeText(getActivity(), "请检查网络！", 1).show();
						lv.onRefreshComplete();

					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						infobean = JSON.parseObject(arg0.result,
								HechaInfobean.class);
						if (infobean.getResult().equals("1")) {
							handler.sendEmptyMessage(0);
							lv.onRefreshComplete();
						} else {
							lv.onRefreshComplete();
							handler.sendEmptyMessage(1);
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
