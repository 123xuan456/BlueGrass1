package com.reeching.bluegrass;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
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
import com.reeching.adapter.PlanAdapter;
import com.reeching.bean.HuaLangShowing;
import com.reeching.utils.HttpApi;

import java.util.ArrayList;
import java.util.List;

public class HuaLangPlanActivity extends Activity {
    private List<HuaLangShowing.Infos> lists;
    private List<HuaLangShowing.Infos> alllists;
    private PlanAdapter adapter;
    HttpUtils hu = new HttpUtils();
    private TextView comeback;
    private int i = 2;
    private ProgressDialog progressDialog;
    private PullToRefreshListView mLV_AfterSalesInfor;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0) {
                adapter = new PlanAdapter(alllists,
                        HuaLangPlanActivity.this);
                mLV_AfterSalesInfor.setAdapter(adapter);
                mLV_AfterSalesInfor.setSelection(alllists.size() - 10);
            }
        };
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_hua_lang_plan);
        comeback = (TextView) findViewById(R.id.comeback);
        mLV_AfterSalesInfor = (PullToRefreshListView) findViewById(R.id.Hualang_plan_lv);
        mLV_AfterSalesInfor.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        initdata();
        comeback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                HuaLangPlanActivity.this.finish();
            }
        });

        mLV_AfterSalesInfor.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(HuaLangPlanActivity.this,
                        PlanInfoActivity.class);
                HuaLangShowing.Infos infos = alllists.get(position - 1);
                intent.putExtra("info", infos);
                startActivityForResult(intent,22);
            }

        });
        mLV_AfterSalesInfor.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                RequestParams params = new RequestParams();
                String id = BaseApplication.getInstance().getId();
                params.addQueryStringParameter("pageSize", 10 + "");
                params.addQueryStringParameter("pageNo", i + "");
                params.addQueryStringParameter("userId", id);
                hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.plan, params,
                        new RequestCallBack<String>() {

                            @Override
                            public void onFailure(HttpException arg0, String arg1) {
                                // TODO Auto-generated method stub
                                Toast.makeText(HuaLangPlanActivity.this, "请求不成功！", Toast.LENGTH_SHORT)
                                        .show();
                            }

                            @Override
                            public void onSuccess(ResponseInfo<String> arg0) {
                                // TODO Auto-generated method stub
                                HuaLangShowing showing = JSON.parseObject(arg0.result,
                                        HuaLangShowing.class);
                                i = i + 1;
                                if (showing.getResult().equals("1")) {
                                    lists = new ArrayList<HuaLangShowing.Infos>();
                                    lists.addAll(showing.getInfos());
                                    alllists.addAll(lists);
                                    handler.sendEmptyMessage(0);
                                }else{
                                    Toast.makeText(HuaLangPlanActivity.this,
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
        String id = BaseApplication.getInstance().getId();
        params.addQueryStringParameter("pageSize", 10 + "");
        params.addQueryStringParameter("pageNo", 1+"");
        params.addQueryStringParameter("userId", id);
        hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.plan, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        // TODO Auto-generated method stub
                        Toast.makeText(HuaLangPlanActivity.this, "请求不成功！", Toast.LENGTH_SHORT)
                                .show();
                        progressDialog.dismiss(); progressDialog.dismiss();
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                        progressDialog = new ProgressDialog(HuaLangPlanActivity.this);
                        progressDialog.setMessage("加载中,请稍后...");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // TODO Auto-generated method stub
                        HuaLangShowing showing = JSON.parseObject(arg0.result,
                                HuaLangShowing.class);
                        progressDialog.dismiss();
                        if (showing.getResult().equals("1")) {
                            alllists = new ArrayList<HuaLangShowing.Infos>();
                            alllists.addAll(showing.getInfos());
                            handler.sendEmptyMessage(0);
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==221){
            i=2;
            initdata();
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode==KeyEvent.KEYCODE_BACK){

            adapter=null;
            HuaLangPlanActivity.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
