package com.reeching.bluegrass;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.reeching.adapter.GlideLoader;
import com.reeching.bean.HuaLangShowing;
import com.reeching.utils.BitmapUtils;
import com.reeching.utils.HttpApi;
import com.squareup.picasso.Picasso;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;



public class PlanInfoActivity extends Activity {
    private HuaLangShowing.Infos infos;
    private TextView tvtheme, tvtimes, tvtimee,
            comeback;
    private RadioGroup mGroupSHD;
    private LinearLayout ll_popup;
    private TextView mButtonDismiss;
    private RadioGroup mGroupDismissTrue;
    private TextView mButtonTrue;
    private PopupWindow pop = null;
    private ArrayList<String> pathPhoto = new ArrayList<>();
    private EditText etpeople, tvcezhanreninfo;
    private EditText mZuoZheText;
    private ImageView ivlevel;
    private GridAdapter adapter;
    private ProgressDialog progressDialog;
    private NoScrollGridView lin, lin2;
    private ArrayList<String> list = new ArrayList<String>();
    private PlanInfoActivity.GridViewAdapter adapter2;
    private TextView mInfoHuaLang;
    private String id;
    private Dialog mCameraDialog;
    private CameraImage takePhoto;
    private Button btnalter;
    private ArrayList<String> mPhotoNum = new ArrayList<>();
    private Boolean ispixel = false, isPhote = false;
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case 199:
                    BimpHandler.tempSelectBitmap.clear();
                    Bundle dataAblum = msg.getData();
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
                case 11:
                    adapter2.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_plan_info);
        BimpHandler.tempSelectBitmap.clear();
        infos = (HuaLangShowing.Infos) getIntent().getSerializableExtra("info");
        lin = (NoScrollGridView) findViewById(R.id.planinfo_lin);
        tvtheme = (TextView) findViewById(R.id.planinfo_theme);
        etpeople = (EditText) findViewById(R.id.planinfo_people);
        comeback = (TextView) findViewById(R.id.comeback);
        mZuoZheText = (EditText) findViewById(R.id.planinfo_zuozhe);
        tvtimee = (TextView) findViewById(R.id.planinfo_timee);
        tvtimes = (TextView) findViewById(R.id.planinfo_times);
        tvcezhanreninfo = (EditText) findViewById(R.id.planinfo_authorinfo);
        ivlevel = (ImageView) findViewById(R.id.planinfo_level);
        lin2 = (NoScrollGridView) findViewById(R.id.planinfo_lin2);
        mInfoHuaLang = (TextView) findViewById(R.id.planinfo_hualang);
        btnalter = (Button) findViewById(R.id.planinfo_report);
        initdata();
        initPopupWindow();
        comeback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                PlanInfoActivity.this.finish();
            }
        });

        lin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 执行浏览照片操作
                if ( list.get(position) != null) {
                    Intent intent = new Intent(PlanInfoActivity.this,
                            PicViewActivity.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", position);
                    intent.putStringArrayListExtra("url", list);
                    startActivity(intent);
                } else {

                    adapter2.notifyDataSetChanged();
                }
            }
        });
        if (BaseApplication.getInstance().getQuanxian().equals("上报用户")) {

            btnalter.setVisibility(View.GONE);
            lin2.setVisibility(View.GONE);
        } else {
            btnalter.setVisibility(View.VISIBLE);
        }
        btnalter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                report();
            }
        });
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

                imageView.setLayoutParams(new GridView.LayoutParams(200, 200));//设置ImageView对象布局
                imageView.setAdjustViewBounds(false);//设置边界对齐
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//设置刻度的类型
                imageView.setPadding(5, 5, 5, 5);//设置间距
            } else {
                imageView = (ImageView) convertView;
            }
            Picasso.with(PlanInfoActivity.this)
                    .load(HttpApi.picip + list)
                    .resize(900, 900)
                    .placeholder(R.drawable.downing)              //添加占位图片
                    .error(R.drawable.error)
                    .config(Bitmap.Config.RGB_565)
                    .centerInside()
                    .into(imageView);


            return imageView;
        }
    }

    private void initdata() {
        tvtheme.setText(infos.getTheme());
        etpeople.setText(infos.getManager());
        String dateBegin = infos.getDateBegin();
        mInfoHuaLang.setText(infos.getGalleryName());
        mZuoZheText.setText(infos.getAuthor());
        tvtimes.setText(dateBegin);
        id = infos.getId();
        String dateEnd = infos.getDateEnd();
        tvtimee.setText(dateEnd);
        tvcezhanreninfo.setText(infos.getManagerIntroduction());
        if (infos.getCareLevel().equals("0")) {
            ivlevel.setImageResource(R.drawable.green);
        } else if (infos.getCareLevel().equals("1")) {
            ivlevel.setImageResource(R.drawable.yellow);
        } else {
            ivlevel.setImageResource(R.drawable.red);
        }
        if (!"".equals(infos.getPhoto())) {
            String sourceStr = infos.getPhoto();
            String[] sourceStrArray = sourceStr.split("\\|");
            for (int ii = 1; ii < sourceStrArray.length; ii++) {
                list.add(sourceStrArray[ii]);
            }
        }
        adapter2 = new PlanInfoActivity.GridViewAdapter(PlanInfoActivity.this, list);
        lin.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();
    }

    private void report() {
        HttpUtils hu = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("galleryId", infos.getGalleryId());
        params.addQueryStringParameter("theme", infos.getTheme());
        params.addQueryStringParameter("status", infos.getStatus());
        params.addQueryStringParameter("dateBegin", tvtimes.getText().toString());
        params.addQueryStringParameter("dateEnd", tvtimee.getText().toString());
        params.addQueryStringParameter("careLevel", infos.getCareLevel());
        params.addQueryStringParameter("exhibitionIntroduction", infos.getExhibitionIntroduction());
        params.addQueryStringParameter("remarks", infos.getRemarks());
        params.addQueryStringParameter("authorIntroduction", infos.getAuthorIntroduction());
        params.addQueryStringParameter("author", mZuoZheText.getText().toString());
        params.addQueryStringParameter("manager", etpeople.getText().toString());
        params.addQueryStringParameter("managerIntroduction", tvcezhanreninfo.getText().toString());
        params.addQueryStringParameter("userId", BaseApplication.getInstance()
                .getId());
        for (int i = 0; i < (BimpHandler.tempSelectBitmap.size()); i++) {
            String imagePath = BimpHandler.tempSelectBitmap.get(0)
                    .getImagePath();
            String uploadType = imagePath.substring(
                    imagePath.lastIndexOf(".") + 1, imagePath.length());
            params.addBodyParameter("postsPic" + (i + 1) + "_" + mPhotoNum.get(i), new File(
                    BimpHandler.tempSelectBitmap.get(i).getImagePath()));
            params.addBodyParameter("uploadType" + (i + 1), uploadType);
        }
        hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.reporthualang, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        // TODO Auto-generated method stub
                        btnalter.setEnabled(true);
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onStart() {
                        // TODO Auto-generated method stub
                        super.onStart();
                        progressDialog = new ProgressDialog(PlanInfoActivity.this);
                        progressDialog.setMessage("上传中,请稍后...");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // TODO Auto-generated method stub
                        JSONObject jsonObject = JSON.parseObject(arg0.result);
                        if (jsonObject.getString("result").equals("1")) {
                            Toast.makeText(PlanInfoActivity.this, "保存成功！", Toast.LENGTH_SHORT).show();
                            btnalter.setEnabled(true);
                            BimpHandler.tempSelectBitmap.clear();
                            progressDialog.dismiss();
                            setResult(221);
                            adapter.notifyDataSetChanged();

                        } else {
                            Toast.makeText(PlanInfoActivity.this, "保存失败！", Toast.LENGTH_SHORT).show();
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            btnalter.setEnabled(true);
                        }
                    }
                });

    }

    private void initPopupWindow() {
        pop = new PopupWindow(PlanInfoActivity.this);
        View view = LayoutInflater.from(PlanInfoActivity.this).inflate(
                R.layout.item_popupwindows, null);
        ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
        pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);
        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);

        Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
        Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
        // 点击父布局消失框pop
        parent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // pop消失 清除动画
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });

        // 选择相册
        bt2.setOnClickListener(new View.OnClickListener() {
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
        bt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pop.dismiss();
                ll_popup.clearAnimation();
            }
        });
        adapter = new GridAdapter(PlanInfoActivity.this);
        lin2.setAdapter(adapter);
        lin2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == BimpHandler.tempSaveBitmap.size()) {
                    return false;
                } else {
                    BimpHandler.tempSelectBitmap.remove(position);
                    pathPhoto.remove(position);
                    adapter.notifyDataSetChanged();
                    return true;
                }
            }
        });
        lin2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // 初次进来都为0
                if (position == BimpHandler.tempSelectBitmap.size()) {
                    // 点击选择照片时显示动画效果
                    mCameraDialog.show();

                } else {
                    // 执行浏览照片操作
                    Intent intent = new Intent(PlanInfoActivity.this,
                            PicViewActivityTemp.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", position);
                    startActivity(intent);
                }
            }
        });
        mCameraDialog = com.reeching.utils.DialogUtils.showPromptDailog(this,
                LayoutInflater.from(PlanInfoActivity.this),
                R.layout.cameradialog);
        mGroupSHD = (RadioGroup) mCameraDialog.findViewById(R.id.radiogroup_HSD);
        mGroupDismissTrue = (RadioGroup) mCameraDialog.findViewById(R.id.radiogroup_dismisstrue);
        mButtonDismiss = (TextView) mCameraDialog.findViewById(R.id.radiobutton_dismiss);
        mButtonTrue = (TextView) mCameraDialog.findViewById(R.id.radiobutton_true);

        mButtonDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraDialog.dismiss();
            }
        });

        mButtonTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getPixelRadioButton() == -1 || getErrorRadioButton() == -1) {
                    Toast.makeText(PlanInfoActivity.this, "两项都要选择", Toast.LENGTH_SHORT).show();
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
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
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
                pathPhoto.clear();
                pathPhoto.addAll(pathList);
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
            GridAdapter.ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.share_photo_gvitem,
                        parent, false);
                holder = new GridAdapter.ViewHolder();
                holder.image = (ImageView) convertView
                        .findViewById(R.id.item_grida_image);
                convertView.setTag(holder);
            } else {
                holder = (GridAdapter.ViewHolder) convertView.getTag();
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
//                        .crop()
                // 多选时的最大数量   （默认 9 张）
                .mutiSelectMaxSize(9)
                // 已选择的图片路径
                .pathList(pathPhoto)
                // 拍照后存放的图片路径（默认 /temp/picture）
                // 开启拍照功能 （默认开启）
                .showCamera()
                .requestCode(num)
                .build();
        ImageSelector.open(PlanInfoActivity.this, imageConfig);   // 开启图片选择器
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        BimpHandler.tempSelectBitmap.clear();
        System.gc();
    }

    private int getPixelRadioButton() {
        return mGroupSHD.getCheckedRadioButtonId();
    }

    private int getErrorRadioButton() {
        return mGroupDismissTrue.getCheckedRadioButtonId();
    }

    private void openCameraPopupWindow() {
        ll_popup.startAnimation(AnimationUtils.loadAnimation(
                this, R.anim.activity_translate_in));
        pop.showAtLocation(this.findViewById(R.id.parent),
                Gravity.BOTTOM, 0, 0);
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
