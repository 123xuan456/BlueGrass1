package com.reeching.bluegrass;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.reeching.adapter.HechaAdapter;
import com.reeching.bean.HechaInfobean;
import com.reeching.utils.ExitApplication;
import com.reeching.utils.HttpApi;

public class WaitForHeChaActivity extends AppCompatActivity {
    private ListView lv;
    private HechaInfobean infobean;
    private HechaAdapter adapter;
    private ProgressDialog progressDialog;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0) {
                adapter = new HechaAdapter(infobean.getInfos(), WaitForHeChaActivity.this);
                adapter.notifyDataSetChanged();
                lv.setAdapter(adapter);
            }
        };
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_for_he_cha);
        ExitApplication.getInstance().addActivity(this);
        lv = (ListView) findViewById(R.id.fragment_waitforhecha_lv);
        TextView comeBack
                = (TextView) findViewById(R.id.comeback);
        comeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WaitForHeChaActivity.this.finish();
            }
        });
        initdata();
    }

    private void initdata() {
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
                        progressDialog.dismiss();

                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                        progressDialog = new ProgressDialog(WaitForHeChaActivity.this);
                        progressDialog.setMessage("加载中,请稍后...");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // TODO Auto-generated method stub
                        infobean = JSON.parseObject(arg0.result,
                                HechaInfobean.class);
                        progressDialog.dismiss();
                        if (infobean.getResult().equals("1")) {
                            handler.sendEmptyMessage(0);
                        }
                    }
                });
    }
    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

    }

}
