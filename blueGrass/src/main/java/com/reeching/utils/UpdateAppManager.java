package com.reeching.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.reeching.bluegrass.BaseApplication;
import com.reeching.bluegrass.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateAppManager {
	private static final String FILE_SEPARATOR = "/";
	// 外存sdcard存放路径
	private static final String FILE_PATH = Environment
			.getExternalStorageDirectory()
			+ FILE_SEPARATOR
			+ "logistics"
			+ FILE_SEPARATOR;
	// 下载应用存放全路径
	private static final String FILE_NAME = FILE_PATH + "bluegrass.apk";
	// 更新应用版本标记
	private static final String UPDARE_TOKEN = BaseApplication.getInstance()
			.getVersonnum();
	// 准备安装新版本应用标记
//	private static final String INSTALL_TOKEN = BaseApplication.getInstance()
//			.getVerson();
	private Context context;
	private String message = "检测到本程序有新版本发布，请更新！";

	private String spec = HttpApi.picip+BaseApplication.getInstance().getMurl();
	// 下载应用的对话框
	private Dialog dialog;
	// 下载应用的进度条
	private ProgressBar progressBar;
	// 进度条的当前刻度值
	private int curProgress;
	// 用户是否取消下载
	private boolean isCancel;

	public UpdateAppManager(Context context) {
		this.context = context;
	}

	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 000:
				progressBar.setProgress(curProgress);
				break;

			case 001:
				installApp();
				break;
			}
		}
	};

	/**
	 * 检测应用更新信息
	 */
	public void checkUpdateInfo() {
		showNoticeDialog();
	}

	/**
	 * 显示提示更新对话框
	 */
	private void showNoticeDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context).setTitle("软件版本更新").setMessage(message)
				.setPositiveButton("下载", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						showDownloadDialog();
					}
				}).setNegativeButton("以后再说", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						System.exit(0);
					}
				});
		builder.setCancelable(false);//设置不能消失
		builder.create();
		builder.show();


	}

	/**
	 * 显示下载进度对话框
	 */
	private void showDownloadDialog() {
		View view = LayoutInflater.from(context).inflate(R.layout.progressbar,
				null);
		progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("软件版本更新");
		builder.setView(view);
		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				isCancel = true;
				System.exit(0);
			}
		});
		dialog = builder.create();
		dialog.show();
		dialog.setCanceledOnTouchOutside(false);
		downloadApp();
	}

	/**
	 * 下载新版本应用
	 */
	private void downloadApp() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				URL url = null;
				InputStream in = null;
				FileOutputStream out = null;
				HttpURLConnection conn = null;
				try {
					url = new URL(spec);
					conn = (HttpURLConnection) url.openConnection();
					conn.connect();
					long fileLength = conn.getContentLength();
					in = conn.getInputStream();
					File filePath = new File(FILE_PATH);
					if (!filePath.exists()) {
						filePath.mkdir();
					}
					out = new FileOutputStream(new File(FILE_NAME));
					byte[] buffer = new byte[1024];
					int len = 0;
					long readedLength = 0l;
					while ((len = in.read(buffer)) != -1) {
						// 用户点击“取消”按钮，下载中断
						if (isCancel) {
							break;
						}
						out.write(buffer, 0, len);
						readedLength += len;
						curProgress = (int) (((float) readedLength / fileLength) * 100);
						handler.sendEmptyMessage(000);
						if (readedLength >= fileLength) {
							dialog.dismiss();
							// 下载完毕，通知安装
							handler.sendEmptyMessage(001);
							break;
						}
					}
					out.flush();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (out != null) {
						try {
							out.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (in != null) {
						try {
							in.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (conn != null) {
						conn.disconnect();
					}
				}
			}
		}).start();
	}

	/**
	 * 安装新版本应用
	 */
	private void installApp() {
		File appFile = new File(FILE_NAME);
		if (!appFile.exists()) {
			return;
		}
		// 跳转到新版本应用安装页面
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse("file://" + appFile.toString()),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}
}