package com.reeching.bluegrass;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
import com.reeching.adapter.DeleteHuaLangAdapter;
import com.reeching.bean.HuaLangShowing;
import com.reeching.utils.HttpApi;

import java.util.ArrayList;
import java.util.List;

public class ShelvesActivity extends AppCompatActivity {
    private List<HuaLangShowing.Infos> infos;
    private List<HuaLangShowing.Infos> allinfos;
    private DeleteHuaLangAdapter adapter;
    private TextView comeback;
    private int i = 2;
    private ProgressDialog progressDialog;
    HttpUtils hu = new HttpUtils();
    private PullToRefreshListView mLV_AfterSalesInfor;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0) {

                adapter = new DeleteHuaLangAdapter(allinfos, ShelvesActivity.this);
                mLV_AfterSalesInfor.setAdapter(adapter);

            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelves);
        mLV_AfterSalesInfor = (PullToRefreshListView) findViewById(R.id.fragment_maplist_lv);
        mLV_AfterSalesInfor.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        comeback = (TextView) findViewById(R.id.comeback);
        comeback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShelvesActivity.this.finish();
            }
        });
        initdata();
        mLV_AfterSalesInfor.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                RequestParams params = new RequestParams();
                params.addQueryStringParameter("userId", BaseApplication.getInstance()
                        .getId());
                params.addQueryStringParameter("pageSize", 10 + "");
                params.addQueryStringParameter("pageNo", i + "");
                hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.getdelHuangLang,
                        params, new RequestCallBack<String>() {

                            @Override
                            public void onFailure(HttpException arg0, String arg1) {
                                // TODO Auto-generated method stub
                            }

                            @Override
                            public void onSuccess(ResponseInfo<String> arg0) {
                                // TODO Auto-generated method stub
                                HuaLangShowing allHualangInfo = JSON.parseObject(
                                        arg0.result, HuaLangShowing.class);
                                i = i + 1;
                                if (allHualangInfo.getResult().equals("1")) {
                                    infos = new ArrayList<HuaLangShowing.Infos>();
                                    infos.addAll(allHualangInfo.getInfos());
                                    allinfos.addAll(infos);
                                    handler.sendEmptyMessage(0);

                                } else {
                                    Toast.makeText(ShelvesActivity.this,
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
        params.addQueryStringParameter("userId", BaseApplication.getInstance()
                .getId());
        params.addQueryStringParameter("pageSize", 10 + "");
        params.addQueryStringParameter("pageNo", 1 + "");
        hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.getdelHuangLang,
                params, new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        // TODO Auto-generated method stub
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                        progressDialog = new ProgressDialog(ShelvesActivity.this);
                        progressDialog.setMessage("加载中,请稍后...");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // TODO Auto-generated method stub
                        HuaLangShowing allHualangInfo = JSON.parseObject(
                                arg0.result, HuaLangShowing.class);
                        progressDialog.dismiss();
                        if (allHualangInfo.getResult().equals("1")) {
                            allinfos=new ArrayList<HuaLangShowing.Infos>();
                            allinfos.addAll(allHualangInfo.getInfos());
                            handler.sendEmptyMessage(0);
                        }
                    }
                });

    }

}
