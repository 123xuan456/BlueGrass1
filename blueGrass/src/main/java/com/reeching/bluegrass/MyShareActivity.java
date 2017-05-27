package com.reeching.bluegrass;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyShareActivity extends Activity implements OnClickListener {

	private Dialog loadingDialog;
	private TextView tv_share;

	private ImageView img_share_back;

	private LinearLayout share_fish_tie;// 钓贴区域

	private LinearLayout share_other_tie;

	/** 帖子选择 */
	private RadioGroup rg_share_invitation;

	/** 帖子内容 */
	// private BorderEditText et_share_content;

	/** 定位信息 */
	private RelativeLayout rl_share_location;
	private TextView tv_share_address;

	/** 钓法选择 */
	private RelativeLayout rl_share_method;
	private TextView tv_share_method;
	private String fishId;// 钓法id

	// 装备
	private RelativeLayout rl_share_equip;
	private TextView tv_share_equip;
	private String equipmentId = "";// 装备id

	// 鱼量 鱼长 鱼重
	private EditText edit_share_count;
	private EditText edit_share_lenth;
	private EditText edit_share_weight;

	/** 显示选择的照�? */
	private NoScrollGridView gv_share_photo;

	// BitmapCache类里调用
	public static Bitmap bimap;

	/** pop窗口 */
	private PopupWindow pop = null;
	/** pop布局 */
	private LinearLayout ll_popup;

	/** 照片适配�? */
	private GridAdapter adapter;

	/** 选择系统相机的请求码 */
	public static final int TAKE_PICTURE = 1;
	/** 地图位置请求�? */
	public static final int LOCATION_MAP = 0;
	/** 钓法选择请求�? */
	public static final int SELECT_CONDITION = 2;
	/** 装备选择请求�? */
	public static final int EQUIP = 3;

	private String invitation = "钓贴";// 得到选择的帖�?
	private double mLongitude;// 精度
	private double mLatitude;// 纬度

	/** 标记分享到何�? */
	private int categroy = 0;;

	@Override
	protected void onRestart() {

		notifyAdapter();
		System.out.println("存入的数�?:" + BimpHandler.tempSelectBitmap.size());
		super.onRestart();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myshare);

		categroy = getIntent().getIntExtra("category", 0);

		initView();
		setListener();
		initPopupWindow();
	
	}

	private void setListener() {
		img_share_back.setOnClickListener(this);
		tv_share.setOnClickListener(this);
		rl_share_location.setOnClickListener(this);
		rl_share_method.setOnClickListener(this);
		rl_share_equip.setOnClickListener(this);
		getInvitation();
	}

	private void getInvitation() {
		rg_share_invitation
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						int buttonId = group.getCheckedRadioButtonId();
						RadioButton rb = (RadioButton) findViewById(buttonId);
						// 获取点击的�?�项内容
						invitation = rb.getText().toString();
						if (invitation.equals("钓点�?")) {
							// share_fish_tie.setVisibility(View.VISIBLE);
							// share_other_tie.setVisibility(View.GONE);
							rl_share_method.setVisibility(View.VISIBLE);
							rl_share_equip.setVisibility(View.VISIBLE);
						} else if (invitation.equals("其他�?")) {
							// share_fish_tie.setVisibility(View.GONE);
							// share_other_tie.setVisibility(View.VISIBLE);
							rl_share_method.setVisibility(View.GONE);
							rl_share_equip.setVisibility(View.GONE);
						}
					}
				});
	}

	private void initView() {
		loadingDialog = DialogUtils.requestDialog(this);
		bimap = BitmapFactory.decodeResource(getResources(),
				R.drawable.icon_addpic_unfocused);

		// img_share_back = (ImageView) findViewById(R.id.img_share_back);

		tv_share = (TextView) findViewById(R.id.tv_share);

	}

	private void initPopupWindow() {
		pop = new PopupWindow(MyShareActivity.this);
		View view = getLayoutInflater().inflate(R.layout.item_popupwindows,
				null);
		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);
		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
//		Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
		// 点击父布�?消失框pop
		parent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// pop消失 清除动画
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		// 选择相机拍照
//		bt1.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				systemCamera();
//				pop.dismiss();
//				ll_popup.clearAnimation();
//			}
//		});
		// 选择相册
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(MyShareActivity.this,
						AlbumActivity.class);
				startActivity(intent);

				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		// 取消
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		gv_share_photo = (NoScrollGridView) findViewById(R.id.share_photo);
		gv_share_photo.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
		gv_share_photo.setAdapter(adapter);
		gv_share_photo.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				System.out.println("position=" + position);
				// 初次进来都为0
				if (position == BimpHandler.tempSelectBitmap.size()) {
					// 点击选择照片时显示动画效�?

					pop.showAtLocation(
							MyShareActivity.this.findViewById(R.id.parent),
							Gravity.BOTTOM, 0, 0);
				} else {
					// 执行浏览照片操作
					Intent intent = new Intent(MyShareActivity.this,
							PicViewActivity.class);
					intent.putExtra("position", "1");
					intent.putExtra("ID", position);
					startActivity(intent);
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null && resultCode == RESULT_OK) {
			switch (requestCode) {
			case LOCATION_MAP:
				// 地图上�?�点 返回地址信息并显�?
				String address = data.getStringExtra("address");
				tv_share_address.setText(address);
				break;
			// 选择钓法返回结果
			case SELECT_CONDITION:
				String fishMothed = data.getStringExtra("fishName");
				tv_share_method.setText(fishMothed);
				fishId = data.getStringExtra("fishId");
				break;
			// 装备选择返回结果
			case EQUIP:
				String equipContent = data.getStringExtra("equip");
				tv_share_equip.setText(equipContent);
				equipmentId = data.getStringExtra("equipmentId");
				break;
			// 拍照返回图片及路�?
			case TAKE_PICTURE:
				if (BimpHandler.tempSelectBitmap.size() < 7) {
					String fileName = String
							.valueOf(System.currentTimeMillis());
					// 获取照片
					Bitmap bm = (Bitmap) data.getExtras().get("data");
					Cursor cursor = this.getContentResolver().query(
							data.getData(), null, null, null, null);
					String filePath = "";
					if (cursor.moveToFirst()) {
						filePath = cursor.getString(cursor
								.getColumnIndex("_data"));// 获取绝对路径
						// System.out.println("拍照返回路径:"+filePath);
					}
					cursor.close();
					// 保存到文件夹
					ImageFileUtils.saveBitmap(bm, fileName);

					// 保存到照片列表里
					CameraImage takePhoto = new CameraImage();
					takePhoto.setBitmap(bm);
					takePhoto.setImagePath(filePath);
					BimpHandler.tempSelectBitmap.add(takePhoto);
				}
				break;
			}
		}
	}

	private void shareToWX() {

	}

	/**
	 * 调用系统相机拍照
	 */
	public void systemCamera() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);

	}

	/**
	 * 图片适配�?
	 * 
	 * @author ZhangZhaoCheng
	 */
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater;

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public int getCount() {
			if (BimpHandler.tempSelectBitmap.size() == 9) {
				return 9;
			}
			return (BimpHandler.tempSelectBitmap.size() + 1);
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.share_photo_gvitem,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			// 和数量相等的position图片设置为添加样�? 如果position等于9 说明已经�?�? 则隐�?
			if (position == BimpHandler.tempSelectBitmap.size()) {
				// 超过6张隐藏添加照片按�?
				if (position == 6) {
					holder.image.setVisibility(View.GONE);
				} else {
					holder.image.setImageBitmap(BitmapFactory.decodeResource(
							getResources(), R.drawable.icon_addpic_unfocused));
				}
			} else {
				holder.image.setImageBitmap(BimpHandler.tempSelectBitmap.get(
						position).getBitmap());
			}
			return convertView;
		}

		public class ViewHolder {
			public ImageView image;
		}
	}

	/**
	 * 图片选择更改时gridView显示的图�? 在返回时更新
	 */
	public void notifyAdapter() {
		adapter.notifyDataSetChanged();
	}

	/**
	 * 定位回调函数 用于获取当前地理信息
	 * 
	 * @author ZhangZhaoCheng
	 */

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 关掉页面先把选择的照片集合清�?
		BimpHandler.tempSelectBitmap.clear();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}
}
