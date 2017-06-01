package com.reeching.bluegrass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.reeching.utils.HttpApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HuaLangSerchActivity extends Activity {
    private EditText et;
    private JSONArray array;
    private ArrayAdapter<String> adapter;
    private List<String> list = new ArrayList<String>();
    private List<String> mlist = new ArrayList<String>();
    private TextView comeback;
    private ListView lv;
    private HttpUtils hu = new HttpUtils();
    private HashMap<String, String> hm = new HashMap<String, String>();
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 3) {
                adapter = new ArrayAdapter<String>(HuaLangSerchActivity.this,
                        R.layout.serchactivity_lvitem, R.id.serchactivity_tv,
                        mlist);
                lv.setAdapter(adapter);

            }
            if (msg.what == 1) {
                mlist.clear();
                mlist.addAll(list);
                adapter = new ArrayAdapter<String>(HuaLangSerchActivity.this,
                        R.layout.serchactivity_lvitem, R.id.serchactivity_tv,
                        mlist);
                lv.setAdapter(adapter);

            }
        }

        ;
    };
    private String mFragment = "fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_hua_lang_serch);
        et = (EditText) findViewById(R.id.hualangserch_et);
        comeback = (TextView) findViewById(R.id.comeback);
        BaseApplication.getInstance().setInitflag(false);
        lv = (ListView) findViewById(R.id.hualangserch_lv);
        mFragment = getIntent().getStringExtra("MapFragment");
        init();

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.putExtra("result", mlist.get(position));
                intent.putExtra("id", hm.get(mlist.get(position)));
                if ("mapFragment".equals(mFragment)) {
                    setResult(004, intent);
                } else {
                    setResult(003, intent);
                }
                HuaLangSerchActivity.this.finish();
            }
        });
        comeback.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                setResult(003);
                HuaLangSerchActivity.this.finish();
            }
        });
        et.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (TextUtils.isEmpty(s)) {
                    handler.sendEmptyMessage(1);

                } else {
                    findpart(s.toString());
                }
            }
        });
    }

    private void init() {


//        if (null==object||null == object.getString("result")) {
            hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.getallhualangname,
                    new RequestCallBack<String>() {

                        @Override
                        public void onFailure(HttpException arg0, String arg1) {
                            // TODO Auto-generated method stub
                            Toast.makeText(HuaLangSerchActivity.this, "请检查网络！", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> arg0) {
                            // TODO Auto-generated method stub
                            JSONObject object = JSON.parseObject(arg0.result,
                                    JSONObject.class);
                          //  Toast.makeText(HuaLangSerchActivity.this, "更新数据完成！", Toast.LENGTH_SHORT).show();
                          //  BaseApplication.getInstance().setObj(object);
                            if (object.getString("result").equals("1")) {
                                array = object.getJSONArray("infos");
                                for (int i = 0; i < array.size(); i++) {
                                    JSONObject jsonObject = array.getJSONObject(i);
                                    list.add(jsonObject.getString("name"));
                                    hm.put(jsonObject.getString("name"), jsonObject.getString("id"));
                                }
                            } else {
                                Toast.makeText(HuaLangSerchActivity.this, "无数据！", Toast.LENGTH_LONG).show();
                            }

                        }
                    });
//        }
        JSONObject  object = BaseApplication.getInstance().getObj();


    }


    private void findpart(final String s) {
        new Thread() {
            public void run() {
                super.run();

                List<String> llist = new ArrayList<String>();
                for (String ss : list) {

                    if (ss.toLowerCase().contains(s.toLowerCase())) {
                        llist.add(ss);
                    }
                }
                mlist.clear();
                mlist.addAll(llist);
                handler.sendEmptyMessage(3);
            }
        }.start();

    }


}
