package com.reeching.bluegrass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.reeching.activity.HomeActivity;
import com.reeching.utils.ExitApplication;
import com.reeching.utils.HttpApi;
import com.reeching.utils.SPUtil;

public class LoginActivity extends Activity {
    private Button btn;
    private EditText etname, etcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        ExitApplication.getInstance().addActivity(this);
        btn = (Button) findViewById(R.id.login_userbtn);
        etcode = (EditText) findViewById(R.id.login_usercode);
        etname = (EditText) findViewById(R.id.login_username);

        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (etname.getText().toString().equals("") || etcode.getText().toString().equals("")
                        ||etname.getText()==null||etcode.getText()==null) {
                    Toast.makeText(LoginActivity.this, "请输入账号和密码", Toast.LENGTH_SHORT).show();
                } else {
                    lonin();
                    btn.setEnabled(false);
                }
            }
        });

    }

    private void lonin() {
        HttpUtils hu = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("loginName", etname.getText().toString());
        params.addQueryStringParameter("password", etcode.getText().toString());
        params.addQueryStringParameter("mobileLogin", "true");
        hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.login, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        // TODO Auto-generated method stub
                        Toast.makeText(LoginActivity.this, "请检查网络!！", Toast.LENGTH_SHORT).show();
                        btn.setEnabled(true);
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // TODO Auto-generated method stub
                        JSONObject jsonObject = JSON.parseObject(arg0.result);
                        if (jsonObject.getString("result").equals("1")) {
                            String id = jsonObject.getString("userId");
                            btn.setEnabled(true);
                            Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT)
                                    .show();
                            SPUtil.putUserSP(etname.getText().toString(),
                                    LoginActivity.this);
                            SPUtil.putPassSP(etcode.getText().toString(),
                                    LoginActivity.this);
                            SPUtil.getUserId(id, LoginActivity.this);
                            BaseApplication.getInstance().setId(id);
                            BaseApplication.getInstance().setLoginName(
                                    etname.getText().toString());
                            if (jsonObject.getString("roleName").equals("系统管理员")) {
                                BaseApplication.getInstance().setQuanxian("系统管理员");
                            } else if(jsonObject.getString("roleName").equals("普通用户")) {
                                BaseApplication.getInstance().setQuanxian("普通用户");
                            }else{
                                BaseApplication.getInstance().setQuanxian("上报用户");
                            }
                            Intent intent = new Intent(LoginActivity.this,
                                    HomeActivity.class);
                            startActivity(intent);
                            LoginActivity.this.finish();
                        } else if (jsonObject.getString("result").equals("0")) {
                            Toast.makeText(LoginActivity.this, "用户不存在！", Toast.LENGTH_SHORT)
                                    .show();
                            btn.setEnabled(true);
                        } else {
                            Toast.makeText(LoginActivity.this, "账户密码不匹配！", Toast.LENGTH_SHORT)
                                    .show();
                            btn.setEnabled(true);
                        }

                    }
                });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            LoginActivity.this.finish();
            ExitApplication.getInstance().exit();
        }
        return super.onKeyDown(keyCode, event);
    }
}
