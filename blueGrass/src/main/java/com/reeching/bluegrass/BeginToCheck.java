package com.reeching.bluegrass;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
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
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.reeching.adapter.GlideLoader;
import com.reeching.bean.ZhanlanBean.Infos;
import com.reeching.utils.BitmapUtils;
import com.reeching.utils.HttpApi;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



public class BeginToCheck extends Activity {
    private RadioGroup rgp;
    private LinearLayout lin;
    private String checkStatus;
    private Infos infos;
    private TextView tvtheme, tvlocation, tvaire, tvpeople, tvtimes, tvtimee,
            tvintroduction, tvtimestart, tvtimeend, comeback, tvremark;
    private ImageView ivlevel;
    private EditText etdescrible;
    private ArrayList<String> pathPhoto = new ArrayList<>();
    private Button btn;
    private int myear, mmonth, mday;
    private Boolean timeflag = false;
    private DatePickerDialog dpdialog;
    public static final int TAKE_PICTURE = 1;
    private PopupWindow pop = null;
    private LinearLayout ll_popup;
    private  static GridAdapter adapter;
   private static NoScrollGridView gv_share_photo;
    private NoScrollGridView linGrid;
    private static CameraImage takePhoto;
    private ProgressDialog progressDialog;
    private static Boolean ispixel = false, isPhote = false;
    private BeginToCheck.GridViewAdapter adapter2;
    private Dialog mCameraDialog;
    private RadioGroup mGroupSHD;
    private RadioGroup mGroupDismissTrue;
    private TextView mButtonDismiss;
    private static ArrayList<String> mPhotoNum = new ArrayList<>();
    private TextView mButtonTrue;
    private ArrayList<String> list = new ArrayList<>();
    private  Handler mHandler =  new Handler() {
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
                           // File file = BitmapUtils.commpressImage(splitAblum[i]);
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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_begin_to_check);
        BimpHandler.tempSelectBitmap.clear();
        BimpHandler.tempAddPhoto.clear();
        infos = (Infos) getIntent().getSerializableExtra("info");
        gv_share_photo = (NoScrollGridView) findViewById(R.id.activity_begintocheck_share_photo);
        linGrid = (NoScrollGridView) findViewById(R.id.begintotocheck_lin);
        tvremark = (TextView) findViewById(R.id.begintocheck_remark);
        btn = (Button) findViewById(R.id.begintocheck_btn);
        rgp = (RadioGroup) findViewById(R.id.begintocheck_rgp);
        comeback = (TextView) findViewById(R.id.comeback);
        lin = (LinearLayout) findViewById(R.id.begintocheck_timelin);
        tvtheme = (TextView) findViewById(R.id.begintocheck_theme);
        tvlocation = (TextView) findViewById(R.id.begintocheck_location);
        tvaire = (TextView) findViewById(R.id.begintocheck_aire);
        tvpeople = (TextView) findViewById(R.id.begintocheck_people);
        tvtimes = (TextView) findViewById(R.id.begintocheck_times);
        tvtimee = (TextView) findViewById(R.id.begintocheck_timee);
        tvintroduction = (TextView) findViewById(R.id.begintocheck_introduction);
        tvtimestart = (TextView) findViewById(R.id.begintocheck_timestart);
        tvtimeend = (TextView) findViewById(R.id.begintocheck_timeend);
        ivlevel = (ImageView) findViewById(R.id.begintocheck_level);
        etdescrible = (EditText) findViewById(R.id.begintocheck_describe);
        tvtimestart = (TextView) findViewById(R.id.begintocheck_timestart);
        tvtimeend = (TextView) findViewById(R.id.begintocheck_timeend);

        mCameraDialog = com.reeching.utils.DialogUtils.showPromptDailog(this,
                LayoutInflater.from(BeginToCheck.this),
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
                    Toast.makeText(BeginToCheck.this, "两项都要选择", Toast.LENGTH_SHORT).show();
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

        initdata();
        inittimestart();
        inittimeend();
        initPopupWindow();
        comeback.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                BeginToCheck.this.finish();
            }
        });
        etdescrible.setOnTouchListener(new View.OnTouchListener() {
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
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if ("checkStatus".equals("1")) {
                    if (tvtimestart.getText().toString().equals("")
                            || tvtimeend.getText().toString().equals("")) {
                        Toast.makeText(BeginToCheck.this, "请选择核查时间！", Toast.LENGTH_SHORT).show();
                    } else {
                        initreport();
                    }
                } else {
                    initreport();
                }
            }
        });
        rgp.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                switch (checkedId) {
                    case R.id.begintocheck_rb1:
                        lin.setVisibility(View.GONE);
                        checkStatus = 0 + "";
                        break;

                    case R.id.begintocheck_rb2:
                        lin.setVisibility(View.VISIBLE);
                        checkStatus = 1 + "";
                        break;
                }

            }
        });
        rgp.check(R.id.begintocheck_rb1);

        linGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 执行浏览照片操作
                if ( list.get(position) != null) {
                    Intent intent = new Intent(BeginToCheck.this,
                            PicViewActivity.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", position);
                    intent.putStringArrayListExtra("url", list);
                    startActivity(intent);
                } else {
                    Toast.makeText(BeginToCheck.this,"加载中，请稍后。。。",Toast.LENGTH_SHORT).show();
                    adapter2.notifyDataSetChanged();
                }
            }
        });

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

    private void initreport() {
        HttpUtils hu = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("exhibitionId", infos.getId());
        // params.addQueryStringParameter("checkPhoto", "");
        for (int i = 0; i < (BimpHandler.tempSelectBitmap.size()); i++) {
            String imagePath = BimpHandler.tempSelectBitmap.get(0)
                    .getImagePath();
            String uploadType = imagePath.substring(
                    imagePath.lastIndexOf(".") + 1, imagePath.length());
            params.addBodyParameter("postsPic" + (i + 1) + "_" + mPhotoNum.get(i), new File(
                    BimpHandler.tempSelectBitmap.get(i).getImagePath()));
            params.addBodyParameter("uploadType" + (i + 1), uploadType);
        }
        params.addQueryStringParameter("checkStatus", checkStatus);
        params.addQueryStringParameter("checkBegin", tvtimestart.getText()
                .toString());
        params.addQueryStringParameter("checkEnd", tvtimeend.getText()
                .toString());
        params.addQueryStringParameter("checkDescription", etdescrible
                .getText().toString());
        params.addQueryStringParameter("createBy", BaseApplication.getInstance()
                .getId());
        hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.reportwaitforcheck,
                params, new RequestCallBack<String>() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        progressDialog = new ProgressDialog(BeginToCheck.this);
                        progressDialog.setMessage("上传中,请稍后...");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();
                    }

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        // TODO Auto-generated method stub
                        Toast.makeText(BeginToCheck.this, "请检查网络！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // TODO Auto-generated method stub
                        JSONObject jsonObject = JSON.parseObject(arg0.result);
                        if (jsonObject.getString("result").equals("1")) {
                            Toast.makeText(BeginToCheck.this, "保存成功！", Toast.LENGTH_SHORT)
                                    .show();
                            BimpHandler.tempSelectBitmap.clear();
                            progressDialog.dismiss();
                            BeginToCheck.this.finish();
                        } else {
                            Toast.makeText(BeginToCheck.this, "保存失败！", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                });

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

    private void initdata() {
        tvtheme.setText(null == infos.getTheme() ? "" : infos.getTheme());
        tvlocation
                .setText(null == infos.getAddress() ? "" : infos.getAddress());
        tvpeople.setText(null == infos.getManager() ? "" : infos.getManager());
        tvaire.setText(null == infos.getArea() ? "" : infos.getArea() + "平方米");
        tvtimes.setText(null == infos.getDateBegin() ? "" : infos
                .getDateBegin());
        tvremark.setText(infos.getRemarks());
        if (!"".equals(infos.getPhoto())) {
            String sourceStr = infos.getPhoto();
            String[] sourceStrArray = sourceStr.split("\\|");
            for (int ii = 1; ii < sourceStrArray.length; ii++) {
                list.add(sourceStrArray[ii]);
            }
        }
        tvtimee.setText(null == infos.getDateEnd() ? "" : infos.getDateEnd());
        if (infos.getCareLevel().equals("0")) {
            ivlevel.setImageResource(R.drawable.green);

        } else if (infos.getCareLevel().equals("1")) {
            ivlevel.setImageResource(R.drawable.yellow);
        } else {
            ivlevel.setImageResource(R.drawable.red);
        }
        tvintroduction.setText(null == infos.getExhibitionIntroduction() ? ""
                : infos.getExhibitionIntroduction());

        Addimageview();
    }

    private void inittimestart() {

        tvtimestart.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year1 = calendar.get(Calendar.YEAR);
                int month1 = calendar.get(Calendar.MONTH);
                int day1 = calendar.get(Calendar.DAY_OF_MONTH);
                dpdialog = new DatePickerDialog(BeginToCheck.this,

                        new OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker arg0, int year,
                                                  int month, int day) {
                                // setTitle(year + "年" + (month + 1) + "月" + day
                                // + "日");
                                if (month < 9) {
                                    tvtimestart.setText(year + "-" + "0"
                                            + (month + 1) + "-" + day);
                                } else {
                                    tvtimestart.setText(year + "-"
                                            + (month + 1) + "-" + day);
                                }
                                myear = year;
                                mmonth = month;
                                mday = day;
                                timeflag = true;
                            }
                        }, year1, month1, day1);

                dpdialog.show();
            }
        });

    }
    private void Addimageview() {
        adapter2 = new BeginToCheck.GridViewAdapter(BeginToCheck.this, list);
        linGrid.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();
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
            Picasso.with(BeginToCheck.this)
                    .load(HttpApi.picip + list)
                    .resize(900,900)
                    .placeholder(R.drawable.downing)              //添加占位图片
                    .error(R.drawable.error)
                    .config(Bitmap.Config.RGB_565)
                    .centerInside()
                    .into(imageView);

            mHandler.sendEmptyMessage(0);
            return imageView;
        }

    }

    private void inittimeend() {

        tvtimeend.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (timeflag == false) {

                    Toast.makeText(BeginToCheck.this, "请先设置开始时间",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Calendar calendar = Calendar.getInstance();
                    int year2 = calendar.get(Calendar.YEAR);
                    int month2 = calendar.get(Calendar.MONTH);
                    int day2 = calendar.get(Calendar.DAY_OF_MONTH);
                    dpdialog = new DatePickerDialog(BeginToCheck.this,

                            new OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker arg0,
                                                      int year, int month, int day) {

                                    // setTitle(year + "年" + (month + 1) + "月" +
                                    // day + "日");
                                    if (myear < year) {
                                        if (month < 9) {
                                            tvtimeend.setText(year + "-" + "0"
                                                    + (month + 1) + "-" + day);
                                        } else {
                                            tvtimeend.setText(year + "-"
                                                    + (month + 1) + "-" + day);
                                        }
                                    } else if (myear == year && mmonth < month) {
                                        if (month < 9) {
                                            tvtimeend.setText(year + "-" + "0"
                                                    + (month + 1) + "-" + day);
                                        } else {
                                            tvtimeend.setText(year + "-"
                                                    + (month + 1) + "-" + day);
                                        }
                                    } else if (myear == year && mmonth == month
                                            && mday < day) {
                                        if (month < 9) {
                                            tvtimeend.setText(year + "-" + "0"
                                                    + (month + 1) + "-" + day);
                                        } else {
                                            tvtimeend.setText(year + "-"
                                                    + (month + 1) + "-" + day);
                                        }
                                        tvtimeend.setText(year + "-"
                                                + (month + 1) + "-" + day);
                                    } else if (myear == year && mmonth == month
                                            && mday == day) {
                                        if (month < 9) {
                                            tvtimeend.setText(year + "-" + "0"
                                                    + (month + 1) + "-" + day);
                                        } else {
                                            tvtimeend.setText(year + "-"
                                                    + (month + 1) + "-" + day);
                                        }
                                    } else if (myear == year && mmonth == month
                                            && mday > day) {
                                        if (month < 9) {
                                            tvtimeend.setText(year + "-" + "0"
                                                    + (month + 1) + "-" + mday);
                                        } else {
                                            tvtimeend.setText(year + "-"
                                                    + (month + 1) + "-" + mday);
                                        }
                                    } else if (myear == year && mmonth > month) {
                                        if (month < 9) {
                                            tvtimeend
                                                    .setText(myear + "-" + "0"
                                                            + (mmonth + 1)
                                                            + "-" + mday);
                                        } else {
                                            tvtimeend
                                                    .setText(myear + "-"
                                                            + (mmonth + 1)
                                                            + "-" + mday);
                                        }
                                    } else if (myear > year) {
                                        if (month < 9) {
                                            tvtimeend
                                                    .setText(myear + "-" + "0"
                                                            + (mmonth + 1)
                                                            + "-" + mday);
                                        } else {
                                            if (mmonth < 9) {
                                                tvtimeend.setText(myear + "-"
                                                        + "0" + (mmonth + 1)
                                                        + "-" + mday);
                                            } else {
                                                tvtimeend.setText(myear + "-"
                                                        + (mmonth + 1) + "-"
                                                        + mday);
                                            }
                                        }
                                    }
                                }
                            }, year2, month2, day2);

                    dpdialog.show();
                }

            }
        });
    }

    private void initPopupWindow() {
        pop = new PopupWindow(BeginToCheck.this);
        View view = LayoutInflater.from(BeginToCheck.this).inflate(
                R.layout.item_popupwindows, null);
        ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
        pop.setWidth(LayoutParams.MATCH_PARENT);
        pop.setHeight(LayoutParams.WRAP_CONTENT);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);
        pop.setOutsideTouchable(true);
        pop.setContentView(view);
        RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
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
        adapter = new GridAdapter(BeginToCheck.this);
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
                    // 点击选择照片时显示动画效果
                    mCameraDialog.show();

                } else {
                    // 执行浏览照片操作
                    Intent intent = new Intent(BeginToCheck.this,
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
                holder = new BeginToCheck.GridAdapter.ViewHolder();
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


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            System.gc();
            BimpHandler.tempSelectBitmap.clear();
        }
        return super.onKeyDown(keyCode, event);


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
        ImageSelector.open(BeginToCheck.this, imageConfig);   // 开启图片选择器
    }
}
