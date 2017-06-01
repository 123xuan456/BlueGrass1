package com.reeching.bluegrass;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
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
import com.reeching.utils.HttpApi;
import com.reeching.utils.SPUtil;
import com.reeching.utils.UpdateAppManager;

public class SpActivity extends Activity {
	private String name, psd;
	private boolean flag;
	private String murl;
//	UserDao userdao;
//	Cursor cur;
	private HttpUtils hu = new HttpUtils();
	private UpdateAppManager updateManager;
	private String version = BaseApplication.getInstance().getVersonnum();
	private static final long SPLASH_TIME = 3000;// splash界面显示的时间
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (flag == true) {
				startActivity(new Intent(SpActivity.this, HomeActivity.class));
			} else {
				startActivity(new Intent(SpActivity.this, LoginActivity.class));
			}
			finish();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sp);
		name = SPUtil.getUserSP(this);
		psd = SPUtil.getPassSP(this);
//		userdao = UserDao.getInstance(SpActivity.this);
//		cur = userdao.queryUserList();
		updata();

	}

	private void login() {

		RequestParams params = new RequestParams();
		params.addQueryStringParameter("loginName", name);
		params.addQueryStringParameter("password", psd);
		params.addQueryStringParameter("mobileLogin", "true");
		hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.login, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						flag = false;
						handler.sendEmptyMessageDelayed(0, SPLASH_TIME);
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						JSONObject jsonObject = JSON.parseObject(arg0.result);
						if (jsonObject.getString("result").equals("1")) {
							Toast.makeText(SpActivity.this, "自动登录成功！", Toast.LENGTH_SHORT).show();
							if (jsonObject.getString("roleName").equals("系统管理员")) {
								BaseApplication.getInstance().setQuanxian("系统管理员");
							} else if(jsonObject.getString("roleName").equals("普通用户")) {
								BaseApplication.getInstance().setQuanxian("普通用户");
							}else{
								BaseApplication.getInstance().setQuanxian("上报用户");
							}
							BaseApplication.getInstance().setLoginName(name);
							BaseApplication.getInstance().setId(jsonObject.getString("userId"));
							flag = true;
							handler.sendEmptyMessageDelayed(0, SPLASH_TIME);
						} else {
							flag = false;
							handler.sendEmptyMessageDelayed(0, SPLASH_TIME);
						}

					}
				});

	}

	private void updata() {

		RequestParams params = new RequestParams();
		params.addQueryStringParameter("version", version);
		hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.updata, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						Builder builder = new Builder(
								SpActivity.this);
						builder.setTitle("网络异常!");
						builder.setMessage("请检查网络!");
						builder.setNegativeButton("退出",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										System.exit(0);

									}
								});
						builder.setPositiveButton("手动登录!",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated
										// method stub
										startActivity(new Intent(
												SpActivity.this,
												LoginActivity.class));
										finish();
									}
								});
						builder.show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						JSONObject object = JSON.parseObject(arg0.result);
						if (object.getString("result").equals("2")) {
							JSONObject obj=object.getJSONObject("info");
							murl = obj.getString("downAddress");
							BaseApplication.getInstance().setMurl(murl);
							updateManager = new UpdateAppManager(
									SpActivity.this);
							updateManager.checkUpdateInfo();
						} else {
							login();
						//	initdate();
						}
					}
				});
	}

	private void initdate() {

		hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.getallhualangname,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub
						Toast.makeText(SpActivity.this, "请检查网络！", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						JSONObject object = JSON.parseObject(arg0.result,
								JSONObject.class);
						Toast.makeText(SpActivity.this, "更新数据完成！", Toast.LENGTH_SHORT).show();
						BaseApplication.getInstance().setObj(object);
//						login();
					}
				});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}
}
