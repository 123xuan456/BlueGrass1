package com.reeching.bluegrass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.reeching.adapter.HistoryAdapter;
import com.reeching.bean.HualangHistory;
import com.reeching.bean.HualangHistory.Infos;
import com.reeching.utils.ExitApplication;
import com.reeching.utils.HttpApi;

import java.util.ArrayList;
import java.util.List;

public class HualangHistoryActivity extends Activity {
	private List<Infos> lists;
	private List<Infos> alllists;
	private HistoryAdapter adapter;
	private int i = 2;
	private String id;
	HttpUtils hu = new HttpUtils();
	private TextView comeback;
	private PullToRefreshListView mLV_AfterSalesInfor;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
				adapter = new HistoryAdapter(alllists,
						HualangHistoryActivity.this);
				mLV_AfterSalesInfor.setAdapter(adapter);
			} else {
				mTextNull.setVisibility(View.VISIBLE);
			}
		};
	};
	private TextView mTextNull;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_hualang_history);
		ExitApplication.getInstance().addActivity(this);
		comeback = (TextView) findViewById(R.id.comeback);
		mTextNull = (TextView) findViewById(R.id.fragment_waitforhecha_tv);
		mLV_AfterSalesInfor = (PullToRefreshListView) findViewById(R.id.Hualang_history_lv);
		mLV_AfterSalesInfor.setMode(Mode.PULL_FROM_END);
		id = getIntent().getStringExtra("id");
		initdata();
		comeback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				HualangHistoryActivity.this.finish();
			}
		});
		mLV_AfterSalesInfor.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(HualangHistoryActivity.this,
						HistoryInfoActivity.class);
				Infos infos = alllists.get(position-1);
				intent.putExtra("info", infos);
				startActivity(intent);
			}

		});
		mLV_AfterSalesInfor
				.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener2<ListView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						// TODO Auto-generated method stub
						RequestParams params = new RequestParams();
						params.addQueryStringParameter("pageSize", 10 + "");
						params.addQueryStringParameter("pageNo", i + "");
						params.addQueryStringParameter("galleryId", id);
						hu.send(HttpMethod.POST, HttpApi.ip
								+ HttpApi.gethistory, params,
								new RequestCallBack<String>() {

									@Override
									public void onFailure(HttpException arg0,
											String arg1) {
										// TODO Auto-generated method stub
										Toast.makeText(
												HualangHistoryActivity.this,
												"请求不成功！", Toast.LENGTH_SHORT).show();
									}

									@Override
									public void onSuccess(
											ResponseInfo<String> arg0) {
										// TODO Auto-generated method stub
										i = i + 1;
										Log.d("shuaishuai", "onSuccess:0000000");
										HualangHistory history = JSON
												.parseObject(arg0.result,
														HualangHistory.class);
										if (history.getResult().equals("1")) {
											Log.d("shuaishuai", "onSuccess: 33333");
											lists = new ArrayList<Infos>();
											lists.addAll(history.getInfos());
											alllists.addAll(lists);


										}else{
											Log.d("shuaishuai", "onSuccess: 555555");
											Toast.makeText(HualangHistoryActivity.this,
													"没有更多数据！", Toast.LENGTH_SHORT).show();
										}
										mLV_AfterSalesInfor.onRefreshComplete();
									}
								});
					}

				});
	}

	private void initdata() {

		RequestParams params = new RequestParams();
		params.addQueryStringParameter("pageSize", 10 + "");
		params.addQueryStringParameter("pageNo", 1 + "");
		params.addQueryStringParameter("galleryId", id);
		hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.gethistory, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						Toast.makeText(HualangHistoryActivity.this, "请求不成功！", Toast.LENGTH_SHORT)
								.show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						Log.d("xiaoru", "onSuccess: ");
						HualangHistory history = JSON.parseObject(arg0.result,
								HualangHistory.class);
						if (history.getResult().equals("1")) {
							alllists = new ArrayList<Infos>();
							alllists.addAll(history.getInfos());
							handler.sendEmptyMessage(0);

						} else {
							Toast.makeText(HualangHistoryActivity.this, "没有数据！", Toast.LENGTH_SHORT).show();
						}
					}
				});
	}
}
