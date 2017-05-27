package com.reeching.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.reeching.bluegrass.BaseApplication;
import com.reeching.bluegrass.SpActivity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.widget.Toast;

public class NetCheckReceiver extends BroadcastReceiver {

	// android 中网络变化时所发的Intent的名字
	private static final String netACTION = "android.net.conn.CONNECTIVITY_CHANGE";
	UserDao userdao;
	Cursor cur;
	//private ProgressDialog progressDialog;
	private List<RequestParams> listparams;
	private HttpUtils hu = new HttpUtils();

	@Override
	public void onReceive(Context context, Intent intent) {

		userdao = UserDao.getInstance(context);
		cur = userdao.queryUserList();
		if (intent.getAction().equals(netACTION)) {
			// Intent中ConnectivityManager.EXTRA_NO_CONNECTIVITY这个关键字表示着当前是否连接上了网络
			// true 代表网络断开 false 代表网络没有断开
			if (intent.getBooleanExtra(
					ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)) {
				// aActivity.stopFlag = true;
				Toast.makeText(context, "当前无网络！", 1).show();
				BaseApplication.getInstance().setHasnet(true);
			} else {
				// aActivity.stopFlag = false;
				BaseApplication.getInstance().setHasnet(false);
				if (SPUtil.getflag(context).equals(true)) {
					initcache(context);
				}
			}
		}
	}

	private void initcache(Context context) {

		listparams = new ArrayList<RequestParams>();
		for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
			int themeid = cur.getColumnIndex(DBHelper.COLUMN_theme);
			int statusid = cur.getColumnIndex(DBHelper.COLUMN_status);
			int dateBeginid = cur.getColumnIndex(DBHelper.COLUMN_dateBegin);
			int dateEndid = cur.getColumnIndex(DBHelper.COLUMN_dateEnd);
			int careLevelid = cur.getColumnIndex(DBHelper.COLUMN_careLevel);
			int authorid = cur.getColumnIndex(DBHelper.COLUMN_author);
			int authorIntroductionid = cur
					.getColumnIndex(DBHelper.COLUMN_authorIntroduction);
			int galleryIdid = cur.getColumnIndex(DBHelper.COLUMN_galleryId);
			int managerid = cur.getColumnIndex(DBHelper.COLUMN_manager);
			int managerIntroductionid = cur
					.getColumnIndex(DBHelper.COLUMN_managerIntroduction);
			int userIdid = cur.getColumnIndex(DBHelper.COLUMN_userId);
			int exhibitionIntroductionid = cur
					.getColumnIndex(DBHelper.COLUMN_exhibitionIntroduction);
			int remarksid = cur.getColumnIndex(DBHelper.COLUMN_remarks);
			int pathid = cur.getColumnIndex(DBHelper.COLUMN_path);
			String theme = cur.getString(themeid);
			String mstate = cur.getString(statusid);
			String dateBegin = cur.getString(dateBeginid);
			String dateEnd = cur.getString(dateEndid);
			String careLevel = cur.getString(careLevelid);
			String author = cur.getString(authorid);
			String authorIntroduction = cur.getString(authorIntroductionid);
			String manager = cur.getString(managerid);
			String managerIntroduction = cur.getString(managerIntroductionid);
			String userId = cur.getString(userIdid);
			String exhibitionIntroduction = cur
					.getString(exhibitionIntroductionid);
			String path = cur.getString(pathid);
			String remarks = cur.getString(remarksid);
			String galleryId = cur.getString(galleryIdid);
			final RequestParams params = new RequestParams();
			params.addQueryStringParameter("galleryId", galleryId);
			params.addQueryStringParameter("theme", theme);
			params.addQueryStringParameter("status", mstate);
			params.addQueryStringParameter("dateBegin", dateBegin);
			params.addQueryStringParameter("dateEnd", dateEnd);
			params.addQueryStringParameter("careLevel", careLevel);
			params.addQueryStringParameter("exhibitionIntroduction",
					exhibitionIntroduction);
			params.addQueryStringParameter("remarks", remarks);
			params.addQueryStringParameter("author", author);
			params.addQueryStringParameter("authorIntroduction",
					authorIntroduction);
			params.addQueryStringParameter("manager", manager);
			params.addQueryStringParameter("managerIntroduction",
					managerIntroduction);
			params.addQueryStringParameter("userId", userId);

			String[] pathStrArray = path.split(",");
			for (int i = 0; i < pathStrArray.length; i++) {
				String imagePath = pathStrArray[i];
				String uploadType = imagePath.substring(
						imagePath.lastIndexOf(".") + 1, imagePath.length());
				params.addBodyParameter("postsPic" + (i + 1), new File(
						imagePath));
				params.addBodyParameter("uploadType" + (i + 1), uploadType);
			}

			listparams.add(params);
		}
		upload(context);

	}

	private void upload(Context context) {
//		progressDialog = new ProgressDialog(context);
//		progressDialog.setMessage("上传缓存信息,请稍后...");
//		progressDialog.show();
		for (int i = 0; i < listparams.size(); i++) {
			report(listparams.get(i));
		}
		for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
			int id = cur.getColumnIndex(DBHelper.COLUMN_ID);
			String id2 = cur.getString(id);

			userdao.delete(id2);
		}
		SPUtil.putflag(false, context);
	//	progressDialog.dismiss();
		// handler.sendEmptyMessageDelayed(0, SPLASH_TIME);
	}

	private void report(RequestParams params) {
		hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.reporthualang, params,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						// TODO Auto-generated method stub
						JSONObject jsonObject = JSON.parseObject(arg0.result);
						if (jsonObject.getString("result").equals("1")) {

						} else {

						}
					}
				});
	}
}
