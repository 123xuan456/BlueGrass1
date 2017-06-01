package com.reeching.bluegrass;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
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
import com.reeching.utils.HttpApi;

import java.util.ArrayList;
import java.util.List;

public class ReportedDetailActivity extends Activity implements AdapterView.OnItemClickListener {
    private ZhanLanAdapter adapter;
    private List<ZhanlanBean.Infos> infos;
    private List<ZhanlanBean.Infos> allinfos;
    private ProgressDialog progressDialog;
    private int i = 2;
    HttpUtils hu = new HttpUtils();
    private PullToRefreshListView mLV_AfterSalesInfor;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                adapter = new ZhanLanAdapter(allinfos, ReportedDetailActivity.this);
                mLV_AfterSalesInfor.setAdapter(adapter);
                mLV_AfterSalesInfor.setSelection(allinfos.size() - 10);
            }
        };
    };
    private TextView mComBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);

        mLV_AfterSalesInfor = (PullToRefreshListView)
                findViewById(R.id.detailreported_lv);
        mLV_AfterSalesInfor.setMode(PullToRefreshBase.Mode.PULL_FROM_END);

        mComBack = (TextView) findViewById(R.id.comeback);
        mComBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReportedDetailActivity.this.finish();
            }
        });
        initdata();


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
                                            Toast.makeText(ReportedDetailActivity.this,
                                                    "没有更多数据！", Toast.LENGTH_SHORT).show();
                                        }
                                        mLV_AfterSalesInfor.onRefreshComplete();
                                    }
                                });

                    }
                });

        mLV_AfterSalesInfor.setOnItemClickListener(this);
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
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                        progressDialog = new ProgressDialog(ReportedDetailActivity.this);
                        progressDialog.setMessage("加载中,请稍后...");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // TODO Auto-generated method stub
                        ZhanlanBean bean = JSON.parseObject(arg0.result,
                                ZhanlanBean.class);
                        progressDialog.dismiss();
                        if (bean.getResult().equals("1")) {
                            allinfos = new ArrayList<ZhanlanBean.Infos>();
                            allinfos.addAll(bean.getInfos());

                            handler.sendEmptyMessage(0);
                        }
                    }
                });

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,
                HaveReportedActivity.class);
        ZhanlanBean.Infos infos = allinfos.get(position - 1);
        intent.putExtra("info", infos);
        startActivity(intent);
    }
}
