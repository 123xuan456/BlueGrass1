package com.reeching.bluegrass;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * 进入相册显示所有图片
 * 
 * @author ZhangZhaoCheng
 */
public class AlbumActivity extends Activity {

	// 显示手机里的所有图片的列表控件
	private GridView gridView;
	// 当手机里没有图片时，提示用户没有图片的控件
	private TextView tv;
	// gridView的adapter
	private AlbumGridViewAdapter gridImageAdapter;
	// 完成按钮
	private TextView okButton;
	// 取消按钮
	private TextView cancel;
	private Intent intent;
	// 预览按钮
	private ArrayList<CameraImage> dataList;
	/** 获取相片帮助类 */
	private AlbumHelper helper;
	/** 用于装载获取的相片 */

	public static List<ImageBucket> contentList;
	public static Bitmap bitmap;

	public int picType;
	private Dialog dialog;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plugin_camera_album);
		// PublicWay.activityList.add(this);
		dialog = DialogUtils.requestDialog(getApplicationContext());
		intent = getIntent();
		// 注册一个广播，这个广播主要是用于在GalleryActivity进行预览时，防止当所有图片都删除完后，再回到该页面时被取消选中的图片仍处于选中状态
		IntentFilter filter = new IntentFilter("data.broadcast.action");
		registerReceiver(broadcastReceiver, filter);
		bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.plugin_camera_no_pictures);
		init();
		initListener();
		// 这个函数主要用来控制预览和完成按钮的状态
		isShowOkBt();

		picType = getIntent().getIntExtra("pic", 0);
	}

	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// mContext.unregisterReceiver(this);
			gridImageAdapter.notifyDataSetChanged();
		}
	};

	//
	

	// 完成按钮的监听
	private class AlbumSendListener implements OnClickListener {
		public void onClick(View v) {
			// overridePendingTransition(R.anim.activity_translate_in,
			// R.anim.activity_translate_out);
			// intent.setClass(mContext, MainActivity.class);
			// startActivity(intent);

			switch (picType) {
			case 0:
				try {
					/* Thread.sleep(2000); */
					Thread.sleep(10);
					finish();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				break;
			case 1:
				Intent i = new Intent();
				setResult(RESULT_OK, i);
				finish();
			}
		}

	}

	// 返回按钮监听
	private class BackListener implements OnClickListener {
		public void onClick(View v) {
			// intent.setClass(AlbumActivity.this, ImageFolderActivity.class);
			// startActivity(intent);
			finish();
		}
	}

	// 取消按钮的监听
	private class CancelListener implements OnClickListener {
		public void onClick(View v) {
			// BimpHandler.tempSelectBitmap.clear();
			// intent.setClass(mContext, MainActivity.class);
			// startActivity(intent);
			if (dataList != null && dataList.size() > 0) {

				for (int i = 0; i < dataList.size(); i++) {
					BimpHandler.tempSelectBitmap.remove(dataList.get(i));
				}
			}

			finish();
		}
	}

	// 初始化，给一些对象赋值
	private void init() {
		helper = AlbumHelper.getHelper();
		helper.init(getApplicationContext());

		contentList = helper.getImagesBucketList(false);
		dataList = new ArrayList<CameraImage>();
		for (int i = 0; i < contentList.size(); i++) {
			dataList.addAll(contentList.get(i).imageList);
		}

		cancel = (TextView) findViewById(R.id.cancel);
		cancel.setOnClickListener(new CancelListener());

		// preview = (TextView) findViewById(R.id.preview);
		// preview.setOnClickListener(new PreviewListener());
		gridView = (GridView) findViewById(R.id.myGrid);
		gridImageAdapter = new AlbumGridViewAdapter(this, dataList,
				BimpHandler.tempSelectBitmap);
		gridView.setAdapter(gridImageAdapter);
		tv = (TextView) findViewById(R.id.myText);
		gridView.setEmptyView(tv);
		okButton = (TextView) findViewById(R.id.btn_ok);
		okButton.setText(getResources().getString(R.string.finish) + "("
				+ BimpHandler.tempSelectBitmap.size() + "/" + BimpHandler.num
				+ ")");
	}

	private void initListener() {

		gridImageAdapter
				.setOnItemClickListener(new AlbumGridViewAdapter.OnItemClickListener() {

					@Override
					public void onItemClick(final ToggleButton toggleButton,
							int position, boolean isChecked, Button chooseBt) {
						// 选择照片的数量
						if (picType == 1 ? BimpHandler.tempSelectBitmap.size() >= 1
								: BimpHandler.tempSelectBitmap.size() >= BimpHandler.num) {
							toggleButton.setChecked(false);
							chooseBt.setVisibility(View.GONE);
							if (!removeOneData(dataList.get(position))) {
								// 选择单张时 不提示
								if (picType != 1) {
									Toast.makeText(AlbumActivity.this,
											R.string.only_choose_num, Toast.LENGTH_SHORT).show();
								}
							}
							return;
						}
						if (isChecked) {
							chooseBt.setVisibility(View.VISIBLE);
							BimpHandler.tempSelectBitmap.add(dataList
									.get(position));
							okButton.setText(getResources().getString(
									R.string.finish)
									+ "("
									+ BimpHandler.tempSelectBitmap.size()
									+ "/" + BimpHandler.num + ")");
						} else {
							BimpHandler.tempSelectBitmap.remove(dataList
									.get(position));
							chooseBt.setVisibility(View.GONE);
							okButton.setText(getResources().getString(
									R.string.finish)
									+ "("
									+ BimpHandler.tempSelectBitmap.size()
									+ "/" + BimpHandler.num + ")");
						}
						isShowOkBt();
					}
				});
		AlbumSendListener albumSendListener = new AlbumSendListener();
		// okButton.setOnClickListener(new AlbumSendListener());
		okButton.setOnClickListener(albumSendListener);

	}

	private boolean removeOneData(CameraImage imageItem) {
		if (BimpHandler.tempSelectBitmap.contains(imageItem)) {
			BimpHandler.tempSelectBitmap.remove(imageItem);
			okButton.setText(getResources().getString(R.string.finish) + "("
					+ BimpHandler.tempSelectBitmap.size() + "/"
					+ BimpHandler.num + ")");
			return true;
		}
		return false;
	}

	public void isShowOkBt() {
		if (BimpHandler.tempSelectBitmap.size() > 0) {
			okButton.setText(getResources().getString(R.string.finish) + "("
					+ BimpHandler.tempSelectBitmap.size() + "/"
					+ BimpHandler.num + ")");
			// preview.setPressed(true);
			okButton.setPressed(true);
			// preview.setClickable(true);
			okButton.setClickable(true);
			okButton.setTextColor(Color.WHITE);
			// preview.setTextColor(Color.WHITE);
		} else {
			okButton.setText(getResources().getString(R.string.finish) + "("
					+ BimpHandler.tempSelectBitmap.size() + "/"
					+ BimpHandler.num + ")");
			// preview.setPressed(false);
			// preview.setClickable(false);
			okButton.setPressed(false);
			okButton.setClickable(false);
			okButton.setTextColor(Color.parseColor("#E1E0DE"));
			// preview.setTextColor(Color.parseColor("#E1E0DE"));
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			AlbumActivity.this.finish();
		}
		return false;
	}

	@Override
	protected void onRestart() {
		isShowOkBt();
		super.onRestart();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
	}
}
