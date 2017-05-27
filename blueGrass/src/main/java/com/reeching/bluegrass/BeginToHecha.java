package com.reeching.bluegrass;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.reeching.adapter.GlideLoader;
import com.reeching.bean.HechaInfobean.Infos;
import com.reeching.utils.BitmapUtils;
import com.reeching.utils.ExitApplication;
import com.reeching.utils.HttpApi;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;



public class BeginToHecha extends Activity {
    private TextView tvtheme, tvaddress, tvaire, tvpeople, tvtimes, tvtimee,
            tvintroduction, tvremark, comeback;
    private ImageView iv;
    private ArrayList<String> pathPhoto = new ArrayList<>();
    private EditText ethistorydescribe, etdescribe;
    private Button btn;
    private Infos infos;
    private RadioGroup group;
    private String recheckStatus;
    public static final int TAKE_PICTURE = 1;
    private PopupWindow pop = null;
    private LinearLayout ll_popup;
    private static GridAdapter adapter;
    private GridViewAdapter adapter2;
    private NoScrollGridView gv_share_photo, lin;
    private static ArrayList<String> mPhotoNum = new ArrayList<>();
    private ProgressDialog progressDialog;
    private static Boolean ispixel = false, isPhote = false;
    private Dialog mCameraDialog;
    private RadioGroup mGroupSHD;
    private RadioGroup mGroupDismissTrue;
    private TextView mButtonDismiss;
    private TextView mButtonTrue;
    private static CameraImage takePhoto;
    private ArrayList<String> list = new ArrayList<String>();
    public  Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case 199:
                    Bundle dataAblum = msg.getData();
                    BimpHandler.tempSelectBitmap.clear();
                    String ablum = dataAblum.getString("ablum");
                    if (!"".equals(ablum)) {
                        String[] splitAblum = ablum.split("MMMMMMMMMM");
                        BimpHandler.tempSelectBitmap.clear();
                        for (int i = 0; i < splitAblum.length; i++) {
                            // 保存到照片列表里
                            // 保存到文件夹
                            // 图片在保存时直接进行压缩
                            //  File file = BitmapUtils.commpressImage(splitAblum[i]);
                            File file2 = BitmapUtils.commpressImage2(splitAblum[i]);
                            Bitmap bitmap = BitmapFactory.decodeFile(file2.getAbsolutePath());
                            takePhoto = new CameraImage();
                            takePhoto.setBitmap(bitmap);
                            if (ispixel == true && isPhote == true) {
                                mPhotoNum.add("1");
                                takePhoto.setImagePath(file2.getAbsolutePath());
                            } else if (ispixel == false && isPhote == true) {
                                takePhoto.setImagePath(file2.getAbsolutePath());
                                mPhotoNum.add("1");
                            } else if (ispixel == true && isPhote == false) {
                                takePhoto.setImagePath(file2.getAbsolutePath());
                                mPhotoNum.add("0");
                            } else if (ispixel == false && isPhote == false) {
                                takePhoto.setImagePath(file2.getAbsolutePath());
                                mPhotoNum.add("0");

                            }
                            BimpHandler.tempSelectBitmap.add(takePhoto);
                        }
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case 0:
                    adapter2.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("shuaishuai", "onCreate: ");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_begin_to_hecha);
        ExitApplication.getInstance().addActivity(this);
        BaseApplication.getInstance().listSelectBitmaps.clear();
        BimpHandler.tempSelectBitmap.clear();
        tvtheme = (TextView) findViewById(R.id.begintohecha_theme);
        gv_share_photo = (NoScrollGridView) findViewById(R.id.activity_begintohecha_share_photo);
        tvaddress = (TextView) findViewById(R.id.begintohecha_address);
        tvaire = (TextView) findViewById(R.id.begintohecha_aire);
        tvpeople = (TextView) findViewById(R.id.begintohecha_people);
        tvtimes = (TextView) findViewById(R.id.begintohecha_times);
        tvtimee = (TextView) findViewById(R.id.begintohecha_timee);
        tvremark = (TextView) findViewById(R.id.begintohecha_remark);
        tvintroduction = (TextView) findViewById(R.id.begintohecha_introduction);
        iv = (ImageView) findViewById(R.id.begintohecha_iv);
        ethistorydescribe = (EditText) findViewById(R.id.begintohecha_history_describe);
        etdescribe = (EditText) findViewById(R.id.begintohecha_describe);
        btn = (Button) findViewById(R.id.begintohecha_btn);
        infos = (Infos) getIntent().getSerializableExtra("info");
        lin = (NoScrollGridView) findViewById(R.id.begintohecha_lin);
        group = (RadioGroup) findViewById(R.id.begintohecha_rgp);
        comeback = (TextView) findViewById(R.id.comeback);
        mCameraDialog = com.reeching.utils.DialogUtils.showPromptDailog(this,
                LayoutInflater.from(this),
                R.layout.cameradialog);
        mGroupSHD = (RadioGroup) mCameraDialog.findViewById(R.id.radiogroup_HSD);
        mGroupDismissTrue = (RadioGroup) mCameraDialog.findViewById(R.id.radiogroup_dismisstrue);
        mButtonDismiss = (TextView) mCameraDialog.findViewById(R.id.radiobutton_dismiss);
        mButtonTrue = (TextView) mCameraDialog.findViewById(R.id.radiobutton_true);

        mButtonDismiss.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraDialog.dismiss();
            }
        });

        mButtonTrue.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getPixelRadioButton() == -1 || getErrorRadioButton() == -1) {
                    Toast.makeText(BeginToHecha.this, "两项都要选择", Toast.LENGTH_SHORT).show();
                } else if (getPixelRadioButton() == R.id.radiobutton_one && getErrorRadioButton() == R.id.radiobutton_three) {
                    ispixel = true;
                    isPhote = true;
                    mCameraDialog.dismiss();
                    openCameraPopupWindow();
                } else if (getPixelRadioButton() == R.id.radiobutton_one && getErrorRadioButton() == R.id.radiobutton_four) {
                    ispixel = true;
                    isPhote = false;
                    mCameraDialog.dismiss();
                    openCameraPopupWindow();
                } else if (getPixelRadioButton() == R.id.radiobutton_two && getErrorRadioButton() == R.id.radiobutton_three) {
                    ispixel = false;
                    isPhote = true;
                    mCameraDialog.dismiss();
                    openCameraPopupWindow();
                } else if (getPixelRadioButton() == R.id.radiobutton_two && getErrorRadioButton() == R.id.radiobutton_four) {
                    ispixel = false;
                    isPhote = false;
                    mCameraDialog.dismiss();
                    openCameraPopupWindow();
                }


            }
        });


        etdescribe.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 解决scrollView中嵌套EditText导致其不能上下滑动的问题
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });
        comeback.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                BeginToHecha.this.finish();
            }
        });
        ethistorydescribe.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 解决scrollView中嵌套EditText导致其不能上下滑动的问题
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });
        initdata();
        initrgp();

        initPopupWindow();
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                report();
            }
        });
    }

    private void initdata() {
        tvtheme.setText(infos.getExhibition().getTheme());
        tvaddress.setText(infos.getExhibition().getAddress());
        tvaire.setText(infos.getExhibition().getArea() + "平方米");
        tvpeople.setText(infos.getExhibition().getManager());
        tvremark.setText(infos.getExhibition().getRemarks());
        String ss = infos.getExhibition().getDateBegin();
        tvtimes.setText(ss);
        String sss = infos.getExhibition().getDateEnd();
        tvtimee.setText(sss);
        if (!"".equals(infos.getExhibition().getPhoto())) {
            String sourceStr = infos.getExhibition().getPhoto();
            String[] sourceStrArray = sourceStr.split("\\|");
            for (int ii = 1; ii < sourceStrArray.length; ii++) {
                list.add(sourceStrArray[ii]);
            }
        }
        if (infos.getExhibition().getCareLevel().equals("0")) {
            iv.setImageResource(R.drawable.green);
        } else if (infos.getExhibition().getCareLevel().equals("1")) {
            iv.setImageResource(R.drawable.yellow);
        } else {
            iv.setImageResource(R.drawable.red);
        }
        tvintroduction.setText(infos.getExhibition()
                .getExhibitionIntroduction());

        if (null != infos.getCheckInfo()) {
            ethistorydescribe.setVisibility(View.VISIBLE);
            ethistorydescribe.setText(infos.getCheckInfo()
                    .getCheckDescription());
        }
        Addimageview();
    }

    private void Addimageview() {

        adapter2 = new GridViewAdapter(BeginToHecha.this, list);
        lin.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();
        lin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 执行浏览照片操作
                if ( BaseApplication.getInstance().listSelectBitmaps.get(position) != null&& BaseApplication.getInstance().listSelectBitmaps.size() == list.size()) {
                    Intent intent = new Intent(BeginToHecha.this,
                            PicViewActivity.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", position);
                    intent.putStringArrayListExtra("url", list);
                    startActivity(intent);
                } else {
                    //   Toast.makeText(BeginToHecha.this,"加载中，请稍后。。。",Toast.LENGTH_SHORT).show();
                    adapter2.notifyDataSetChanged();
                }
            }
        });


    }

    private void report() {
        HttpUtils hu = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("exhibitionId", infos.getExhibition()
                .getId());
        params.addQueryStringParameter("createBy", BaseApplication.getInstance()
                .getId());
        params.addQueryStringParameter("checkInfoId", null == infos
                .getCheckInfo() ? "" : infos.getCheckInfo().getId());
        params.addQueryStringParameter("recheckStatus", recheckStatus);
        // params.addQueryStringParameter("recheckPhoto", "");
        params.addQueryStringParameter("recheckDescription", etdescribe
                .getText().toString());

        for (int i = 0; i < (BimpHandler.tempSelectBitmap.size()); i++) {
            String imagePath = BimpHandler.tempSelectBitmap.get(0)
                    .getImagePath();
            String uploadType = imagePath.substring(
                    imagePath.lastIndexOf(".") + 1, imagePath.length());
            params.addBodyParameter("postsPic" + (i + 1) + "_" + mPhotoNum.get(i), new File(
                    BimpHandler.tempSelectBitmap.get(i).getImagePath()));
            params.addBodyParameter("uploadType" + (i + 1), uploadType);
        }
        hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.reportwaitforhecha,
                params, new RequestCallBack<String>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        progressDialog = new ProgressDialog(BeginToHecha.this);
                        progressDialog.setMessage("上传中,请稍后...");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // TODO Auto-generated method stub
                        JSONObject jsonObject = JSONObject
                                .parseObject(arg0.result);
                        if (jsonObject.getString("result").equals("1")) {
                            Toast.makeText(BeginToHecha.this, "保存成功！", Toast.LENGTH_SHORT)
                                    .show();
                            BimpHandler.tempSelectBitmap.clear();
                            progressDialog.dismiss();
                            BeginToHecha.this.finish();
                        } else {
                            Toast.makeText(BeginToHecha.this, "保存失败！", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });

    }

    private void initrgp() {
        group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                switch (checkedId) {
                    case R.id.begintohecha_rbt1:
                        recheckStatus = 0 + "";
                        break;

                    case R.id.begintohecha_rbt2:
                        recheckStatus = 1 + "";
                        break;
                }
            }
        });
        group.check(R.id.begintohecha_rbt1);
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == 10 || requestCode == 100) {
                final String fileName = String.valueOf(System.currentTimeMillis());

                List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
                if (pathList == null) {
                    return;
                }
                final StringBuilder stringBuffer = new StringBuilder();
                for (String path : pathList) {
                    if (!BimpHandler.tempAddPhoto.contains(path)) {
                        BimpHandler.tempAddPhoto.add(path);
                    }
                    stringBuffer.append(path).append("MMMMMMMMMM");
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message obtainAblum = Message.obtain();
                        obtainAblum.what = 199;
                        Bundle bundle = new Bundle();
                        bundle.putString("ablum", stringBuffer.toString());
                        bundle.putString("fileName", fileName);
                        bundle.putInt("pixel", requestCode);
                        obtainAblum.setData(bundle);
                        mHandler.sendMessage(obtainAblum);
                    }
                }).start();
            }
        }
    }

    private void initPopupWindow() {
        pop = new PopupWindow(BeginToHecha.this);
        View view = LayoutInflater.from(BeginToHecha.this).inflate(
                R.layout.item_popupwindows, null);
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
        // 点击父布局消失框pop
        parent.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // pop消失 清除动画
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        // 选择相机拍照

        // 选择相册
        bt2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (ispixel) {
                    getPhoto(100);
                } else {
                    getPhoto(10);
                }
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
        adapter = new GridAdapter(BeginToHecha.this);
        gv_share_photo.setAdapter(adapter);
        gv_share_photo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == BimpHandler.tempSelectBitmap.size()) {
                    return false;
                } else {
                    BimpHandler.tempSelectBitmap.remove(position);
                    pathPhoto.remove(position);
                    mPhotoNum.remove(position);
                    adapter.notifyDataSetChanged();
                    return true;
                }
            }
        });
        gv_share_photo.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // 初次进来都为0
                if (position == BimpHandler.tempSelectBitmap.size()) {

                    mCameraDialog.show();

                } else {
                    // 执行浏览照片操作
                    Intent intent = new Intent(BeginToHecha.this,
                            PicViewActivityTemp.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", position);

                    startActivity(intent);
                }
            }
        });
    }

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
            // 和数量相等的position图片设置为添加样式 如果position等于9 说明已经最大 则隐藏
            if (position == BimpHandler.tempSelectBitmap.size()) {
                // 超过6张隐藏添加照片按钮
                if (position == 9) {
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

    public void systemCamera() {
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    public class GridViewAdapter extends BaseAdapter {
        private Context context;
        private List<String> lists = new ArrayList<String>();

        public GridViewAdapter(Context context, List<String> lists) {
            this.context = context;
            this.lists = lists;
        }

        @Override
        public int getCount() {
            return lists.size();
        }

        @Override
        public Object getItem(int position) {
            return lists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            final ImageView imageView;
            String list = lists.get(position);
            if (convertView == null) {
                imageView = new ImageView(context);
                imageView.setLayoutParams(new GridView.LayoutParams(300, 300));//设置ImageView对象布局
                imageView.setAdjustViewBounds(false);//设置边界对齐
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//设置刻度的类型
                imageView.setPadding(8, 8, 8, 8);//设置间距
            } else {
                imageView = (ImageView) convertView;
            }
            // 加载网络图片
            Picasso.with(BeginToHecha.this)
                    .load(HttpApi.picip + list)
                    .resize(900, 900)
                    .placeholder(R.drawable.downing)              //添加占位图片
                    .error(R.drawable.error)
                    .config(Bitmap.Config.RGB_565)
                    .centerInside()
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                            imageView.setImageBitmap(bitmap);
                            BaseApplication.getInstance().listSelectBitmaps.put(position, bitmap);


                        }

                        @Override
                        public void onBitmapFailed(Drawable drawable) {
                            imageView.setImageResource(R.drawable.error);
                        }

                        @Override
                        public void onPrepareLoad(Drawable drawable) {
                            imageView.setImageResource(R.drawable.downing);
                        }
                    });

            mHandler.sendEmptyMessage(0);
            return imageView;
        }

    }


    private void openCameraPopupWindow() {
        ll_popup.startAnimation(AnimationUtils.loadAnimation(
                this, R.anim.activity_translate_in));
        pop.showAtLocation(this.findViewById(R.id.parent),
                Gravity.BOTTOM, 0, 0);
    }

    private int getPixelRadioButton() {
        return mGroupSHD.getCheckedRadioButtonId();
    }

    private int getErrorRadioButton() {
        return mGroupDismissTrue.getCheckedRadioButtonId();
    }

    public void getPhoto(int num) {
        ImageConfig imageConfig
                = new ImageConfig.Builder(
                // GlideLoader 可用自己用的缓存库
                new GlideLoader())
                // 如果在 4.4 以上，则修改状态栏颜色 （默认黑色）
                .steepToolBarColor(getResources().getColor(R.color.blue))
                // 标题的背景颜色 （默认黑色）
                .titleBgColor(getResources().getColor(R.color.blue))
                // 提交按钮字体的颜色  （默认白色）
                .titleSubmitTextColor(getResources().getColor(R.color.white))
                // 标题颜色 （默认白色）
                .titleTextColor(getResources().getColor(R.color.white))
                // 开启多选   （默认为多选）  (单选 为 singleSelect)
                // 多选时的最大数量   （默认 9 张）
                .mutiSelectMaxSize(9)
                // 已选择的图片路径
                .pathList(pathPhoto)
                // 拍照后存放的图片路径（默认 /temp/picture）
                // 开启拍照功能 （默认开启）
                .showCamera()
                .requestCode(num)
                .build();
        ImageSelector.open(BeginToHecha.this, imageConfig);   // 开启图片选择器
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            System.gc();
            BimpHandler.tempSelectBitmap.clear();
        }
        return super.onKeyDown(keyCode, event);
    }
}
