package com.reeching.fragment;

import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.reeching.bluegrass.BaseApplication;
import com.reeching.bluegrass.BimpHandler;
import com.reeching.bluegrass.CameraImage;
import com.reeching.bluegrass.HuaLangSerchActivity;
import com.reeching.bluegrass.NoScrollGridView;
import com.reeching.bluegrass.PicViewActivityTemp;
import com.reeching.bluegrass.R;
import com.reeching.utils.BitmapUtils;
import com.reeching.utils.DBHelper;
import com.reeching.utils.DialogUtils;
import com.reeching.utils.HttpApi;
import com.reeching.utils.SPUtil;
import com.reeching.utils.UserDao;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
//上报页面
public class ReportDetailFragment extends Fragment implements ImageSelectorFragment.SelectorFragment {
    /**
     * pop窗口
     */
    private PopupWindow pop = null;
    private String[] picArrayPath;
    private String path;
    private LinearLayout ll_popup;
    private static GridAdapter adapter;
    private static NoScrollGridView gv_share_photo;
    private static CameraImage takePhoto;
    private ProgressDialog progressDialog;
    private static EditText myEditText;
    private EditText ettheme, etcehua, etgaiyao, etremark,
            etcontent, etauthor, etauthorshuoming, etcehuashuoming;
    private Button btn, btnrest;
    private TextView starttime, etstatus, endtime;
    private DatePickerDialog dpdialog;
    private String[] status = new String[]{"已完成", "进行中", "布展中"};
    private String[] statu = new String[]{"1", "0", "2"};
    private String state = "";
    private static String id = "";
    private String careLevel = "";
    private RadioGroup rgp;
    private int myear, mmonth, mday;
    private Boolean timeflag = false;
    private ImageSelectorFragment mSelectorFragment;
    private ArrayList<String> pathPhoto = new ArrayList<>();
    private static Boolean ispixel = false, isPhote = true;
    private static ArrayList<String> mPhotoNum = new ArrayList<>();
    UserDao userdao;
    Cursor cur;
    public static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case 199:
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
                                mPhotoNum.add("1");//1为正常图片，0为问题图片
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
                case 003:
                    Bundle editPaint = msg.getData();
                    String editText = editPaint.getString("editText");
                    String editId = editPaint.getString("id");
                    id = editId;
                    myEditText.setText(editText);
                    break;
            }
        }
    };
    private static Bitmap sBitmap;
    private Dialog mCameraDialog;
    private RadioGroup mGroupSHD;
    private RadioGroup mGroupDismissTrue;
    private TextView mButtonDismiss;
    private TextView mButtonTrue;
    private ImageSelectorFragment mImageSelector;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.fragment_detailreport, null);

        userdao = UserDao.getInstance(getActivity());
        cur = userdao.queryUserList();
        btnrest = (Button) view.findViewById(R.id.detailreport_rest);
        gv_share_photo = (NoScrollGridView) view.findViewById(R.id.share_photo);
        etauthorshuoming = (EditText) view
                .findViewById(R.id.detailreport_authorshuoming);
        etcehuashuoming = (EditText) view
                .findViewById(R.id.detailreport_cehuashuoming);
        rgp = (RadioGroup) view.findViewById(R.id.detailreport_rgroup);
        myEditText = (EditText) view.findViewById(R.id.detailreport_myedittext);
        ettheme = (EditText) view.findViewById(R.id.detailreport_theme);
        etcehua = (EditText) view.findViewById(R.id.detailreport_cehuapeople);
        starttime = (TextView) view.findViewById(R.id.detailreport_starttime);
        endtime = (TextView) view.findViewById(R.id.detailreport_endtime);
        etgaiyao = (EditText) view.findViewById(R.id.detailreport_gaiyao);
        etremark = (EditText) view.findViewById(R.id.detailreport_remark);
        etcontent = (EditText) view.findViewById(R.id.detailreport_content);
        btn = (Button) view.findViewById(R.id.detailreport_report);
        etstatus = (TextView) view.findViewById(R.id.detailreport_status);
        etauthor = (EditText) view.findViewById(R.id.detailreport_author);
        mSelectorFragment = new ImageSelectorFragment();
        mSelectorFragment.setSelectorFragment(this);
        mCameraDialog = DialogUtils.showPromptDailog(getContext(),
                LayoutInflater.from(getActivity()),R.layout.cameradialog);
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
                    Toast.makeText(getActivity(), "两项都要选择", Toast.LENGTH_SHORT).show();
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

        etcontent.setOnTouchListener(new View.OnTouchListener() {
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
        etremark.setOnTouchListener(new View.OnTouchListener() {
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
        btnrest.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
            }
        });
        etgaiyao.setOnTouchListener(new View.OnTouchListener() {
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
        etauthorshuoming.setOnTouchListener(new View.OnTouchListener() {
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
        etcehuashuoming.setOnTouchListener(new View.OnTouchListener() {
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

        etstatus.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Builder builder = new Builder(getActivity());
                builder.setTitle("请选择展览状态");
                builder.setSingleChoiceItems(status, 0,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                etstatus.setText(status[which]);
                                state = statu[which];
                                dialog.dismiss();
                            }
                        });
                builder.show();
            }
        });


        myEditText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(),
                        HuaLangSerchActivity.class);
                getRootFragment().startActivityForResult(intent, 001);
            }
        });
        rgp.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                switch (checkedId) {
                    case R.id.detailreport_rbtn1:
                        careLevel = "2";
                        break;

                    case R.id.detailreport_rbtn2:
                        careLevel = "1";
                        break;
                    case R.id.detailreport_rbtn3:
                        careLevel = "0";
                        break;
                }
            }
        });
        mreport();
        initstarttime();
        initendtime();
        initPopupWindow();
        return view;

    }

    private void openCameraPopupWindow() {
        ll_popup.startAnimation(AnimationUtils.loadAnimation(
                getActivity(), R.anim.activity_translate_in));
        pop.showAtLocation(getActivity().findViewById(R.id.parent),
                Gravity.BOTTOM, 0, 0);
    }

    private int getPixelRadioButton() {
        return mGroupSHD.getCheckedRadioButtonId();
    }

    private int getErrorRadioButton() {
        return mGroupDismissTrue.getCheckedRadioButtonId();
    }

    private static void getBitMap(Bundle dataCamera) {
        byte[] bitMaps = dataCamera.getByteArray("bitMap");
        sBitmap = BitmapFactory.decodeByteArray(bitMaps, 0, bitMaps.length);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private Fragment getRootFragment() {
        Fragment fragment = getParentFragment();
        while (fragment.getParentFragment() != null) {
            fragment = fragment.getParentFragment();
        }
        return fragment;

    }

    private void initstarttime() {
        starttime.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year1 = calendar.get(Calendar.YEAR);
                int month1 = calendar.get(Calendar.MONTH);
                int day1 = calendar.get(Calendar.DAY_OF_MONTH);
                dpdialog = new DatePickerDialog(getActivity(),
                        new OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker arg0, int year,
                                                  int month, int day) {
                                // setTitle(year + "年" + (month + 1) + "月" + day
                                // + "日");
                                if (month < 9) {
                                    if (day <= 9) {
                                        starttime
                                                .setText(year + "-" + "0"
                                                        + (month + 1) + "-"
                                                        + "0" + day);
                                    } else {
                                        starttime.setText(year + "-" + "0"
                                                + (month + 1) + "-" + day);
                                    }
                                } else {
                                    if (day <= 9) {
                                        starttime
                                                .setText(year + "-"
                                                        + (month + 1) + "-"
                                                        + "0" + day);
                                    } else {
                                        starttime.setText(year + "-"
                                                + (month + 1) + "-" + day);
                                    }
                                }
                                myear = year;
                                mmonth = month;
                                mday = day;
                                timeflag = true;
                            }
                        }, year1, month1, day1);

                dpdialog.show();
                endtime.setText("");
            }
        });

    }

    private void initendtime() {

        endtime.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (timeflag == false) {

                    Toast.makeText(getActivity(), "请先设置开始时间",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Calendar calendar = Calendar.getInstance();
                    int year2 = calendar.get(Calendar.YEAR);
                    int month2 = calendar.get(Calendar.MONTH);
                    int day2 = calendar.get(Calendar.DAY_OF_MONTH);
                    dpdialog = new DatePickerDialog(getActivity(),
                            new OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker arg0,
                                                      int year, int month, int day) {

                                    // setTitle(year + "年" + (month + 1) + "月" +
                                    // day + "日");
                                    if (myear < year) {
                                        if (month < 9) {
                                            if (day <= 9) {
                                                endtime.setText(year + "-"
                                                        + "0" + (month + 1)
                                                        + "-" + "0" + day);
                                            } else {
                                                endtime.setText(year + "-"
                                                        + "0" + (month + 1)
                                                        + "-" + day);
                                            }
                                        } else {
                                            if (day <= 9) {
                                                endtime.setText(year + "-"
                                                        + (month + 1) + "-"
                                                        + "0" + day);
                                            } else {
                                                endtime.setText(year + "-"
                                                        + (month + 1) + "-"
                                                        + day);
                                            }

                                        }
                                    } else if (myear == year && mmonth < month) {
                                        if (month < 9) {
                                            if (day <= 9) {
                                                endtime.setText(year + "-"
                                                        + "0" + (month + 1)
                                                        + "-" + "0" + day);
                                            } else {
                                                endtime.setText(year + "-"
                                                        + "0" + (month + 1)
                                                        + "-" + day);
                                            }

                                        } else {
                                            if (day <= 9) {
                                                endtime.setText(year + "-"
                                                        + (month + 1) + "-"
                                                        + "0" + day);
                                            } else {
                                                endtime.setText(year + "-"
                                                        + (month + 1) + "-"
                                                        + day);
                                            }

                                        }
                                    } else if (myear == year && mmonth == month
                                            && mday < day) {
                                        if (month < 9) {
                                            if (day <= 9) {
                                                endtime.setText(year + "-"
                                                        + "0" + (month + 1)
                                                        + "-" + "0" + day);
                                            } else {
                                                endtime.setText(year + "-"
                                                        + "0" + (month + 1)
                                                        + "-" + day);
                                            }

                                        } else {
                                            if (day <= 9) {
                                                endtime.setText(year + "-"
                                                        + (month + 1) + "-"
                                                        + "0" + day);
                                            } else {
                                                endtime.setText(year + "-"
                                                        + (month + 1) + "-"
                                                        + day);
                                            }

                                        }

                                    } else if (myear == year && mmonth == month
                                            && mday == day) {
                                        if (month < 9) {
                                            if (day <= 9) {
                                                endtime.setText(year + "-"
                                                        + "0" + (month + 1)
                                                        + "-" + "0" + day);
                                            } else {
                                                endtime.setText(year + "-"
                                                        + "0" + (month + 1)
                                                        + "-" + day);
                                            }

                                        } else {
                                            if (day <= 9) {
                                                endtime.setText(year + "-"
                                                        + (month + 1) + "-"
                                                        + "0" + day);
                                            } else {
                                                endtime.setText(year + "-"
                                                        + (month + 1) + "-"
                                                        + day);
                                            }

                                        }
                                    } else if (myear == year && mmonth == month
                                            && mday > day) {
                                        if (month < 9) {
                                            if (mday <= 9) {
                                                endtime.setText(year + "-"
                                                        + "0" + (month + 1)
                                                        + "-" + "0" + mday);
                                            } else {
                                                endtime.setText(year + "-"
                                                        + "0" + (month + 1)
                                                        + "-" + mday);
                                            }

                                        } else {
                                            if (mday <= 9) {
                                                endtime.setText(year + "-"
                                                        + (month + 1) + "-"
                                                        + "0" + mday);
                                            } else {
                                                endtime.setText(year + "-"
                                                        + (month + 1) + "-"
                                                        + mday);
                                            }

                                        }
                                    } else if (myear == year && mmonth > month) {
                                        if (month < 9) {
                                            if (mday <= 9) {
                                                endtime.setText(myear + "-"
                                                        + "0" + (mmonth + 1)
                                                        + "-" + "0" + mday);
                                            } else {
                                                endtime.setText(myear + "-"
                                                        + "0" + (mmonth + 1)
                                                        + "-" + mday);
                                            }

                                        } else {
                                            if (mday <= 9) {
                                                endtime.setText(myear + "-"
                                                        + +(mmonth + 1) + "-"
                                                        + "0" + mday);
                                            } else {
                                                endtime.setText(myear + "-"
                                                        + +(mmonth + 1) + "-"
                                                        + mday);
                                            }

                                        }
                                    } else if (myear > year) {
                                        if (month < 9) {
                                            if (mday <= 9) {
                                                endtime.setText(myear + "-"
                                                        + "0" + (mmonth + 1)
                                                        + "-" + "0" + mday);
                                            } else {
                                                endtime.setText(myear + "-"
                                                        + "0" + (mmonth + 1)
                                                        + "-" + mday);
                                            }

                                        } else {
                                            if (mmonth < 9) {
                                                if (mday <= 9) {
                                                    endtime.setText(myear + "-"
                                                            + "0"
                                                            + (mmonth + 1)
                                                            + "-" + "0" + mday);
                                                } else {
                                                    endtime.setText(myear + "-"
                                                            + "0"
                                                            + (mmonth + 1)
                                                            + "-" + mday);
                                                }

                                            } else {
                                                if (mday <= 9) {
                                                    endtime.setText(myear + "-"
                                                            + (mmonth + 1)
                                                            + "-" + "0" + mday);
                                                } else {
                                                    endtime.setText(myear + "-"
                                                            + (mmonth + 1)
                                                            + "-" + mday);
                                                }

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

    private void report() {
        HttpUtils hu = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("galleryId", id);
        params.addQueryStringParameter("theme", ettheme.getText().toString());
        params.addQueryStringParameter("status", state);
        params.addQueryStringParameter("dateBegin", starttime.getText()
                .toString());
        params.addQueryStringParameter("dateEnd", endtime.getText().toString());
        params.addQueryStringParameter("careLevel", careLevel);
        params.addQueryStringParameter("exhibitionIntroduction", etgaiyao
                .getText().toString());
        params.addQueryStringParameter("remarks", etremark.getText().toString());
        params.addQueryStringParameter("author", etauthor.getText().toString());
        params.addQueryStringParameter("authorIntroduction", etauthorshuoming
                .getText().toString());
        params.addQueryStringParameter("manager", etcehua.getText().toString());
        params.addQueryStringParameter("managerIntroduction", etcehuashuoming
                .getText().toString());
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
                        btn.setEnabled(true);
                        Toast.makeText(getActivity(), "传输失败！", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onStart() {
                        // TODO Auto-generated method stub
                        super.onStart();
                        progressDialog = new ProgressDialog(getActivity());
                        progressDialog.setMessage("上传中,请稍后...");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // TODO Auto-generated method stub
                        JSONObject jsonObject = JSON.parseObject(arg0.result);
                        btn.setEnabled(true);
                        if (jsonObject.getString("result").equals("1")) {
                            Toast.makeText(getActivity(), "保存成功！", Toast.LENGTH_SHORT).show();
                            btn.setEnabled(true);
                            for (cur.moveToFirst(); !cur.isAfterLast(); cur
                                    .moveToNext()) {
                                int id = cur.getColumnIndex(DBHelper.COLUMN_ID);
                                String id2 = cur.getString(id);
                                userdao.delete(id2);
                            }
                            clear();
                            BimpHandler.tempSelectBitmap.clear();
                            progressDialog.dismiss();
                            adapter.notifyDataSetChanged();

                        } else {
                            Toast.makeText(getActivity(), "保存失败！", Toast.LENGTH_SHORT).show();
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            btn.setEnabled(true);
                        }
                    }
                });

    }

    private void mreport() {
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                btn.setEnabled(false);


                if (id.equals("") || myEditText.getText().equals("") || "".equals(ettheme.getText().toString())
                        || "".equals(myEditText.getText())
                        || "".equals(state)
                        || "".equals(starttime.getText().toString())
                        || "".equals(endtime.getText().toString())
                        || careLevel.equals("")
                        || (rgp.getCheckedRadioButtonId() == -1)) {
                    Toast.makeText(getActivity(), "请填写必填项信息！", Toast.LENGTH_SHORT).show();
                    btn.setEnabled(true);
                } else {

                    if (BaseApplication.getInstance().isHasnet() == false) {
                        report();
                    } else {
                        btn.setEnabled(true);
                        saveinfo();
                    }
                }
            }
        });

    }

    private void initPopupWindow() {
        pop = new PopupWindow(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(
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
        adapter = new GridAdapter(getActivity());
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
                    Intent intent = new Intent(getActivity(),
                            PicViewActivityTemp.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", position);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void delTempList() {
        BimpHandler.tempAddPhoto.clear();
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

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    private void clear() {
        myEditText.setText("");
        id = "";
        state = "";
        etstatus.setText("");
        pathPhoto.clear();
        // etreportman.setText("");
        ettheme.setText("");
        etcehua.setText("");
        btn.setEnabled(true);
        starttime.setText("");
        endtime.setText("");
        etauthor.setText("");
        etauthorshuoming.setText("");
        etcehuashuoming.setText("");
        etremark.setText("");
        etgaiyao.setText("");
        BimpHandler.tempSelectBitmap.clear();
        adapter.notifyDataSetChanged();

    }

    private void saveinfo() {
        for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
            int id = cur.getColumnIndex(DBHelper.COLUMN_ID);
            String id2 = cur.getString(id);

            userdao.delete(id2);

        }
        picArrayPath = new String[BimpHandler.tempSelectBitmap.size()];
        StringBuffer str = new StringBuffer();
        for (int i = 0; i < BimpHandler.tempSelectBitmap.size(); i++) {
            picArrayPath[i] = BimpHandler.tempSelectBitmap.get(i)
                    .getImagePath();
            BimpHandler.tempSaveBitmap.add(BimpHandler.tempSelectBitmap.get(i));
            String ss = picArrayPath[i];
            str.append(ss + ",");
        }
        path = str.toString();
        path = path.substring(0, path.length() - 1);
        userdao.addList(id, ettheme.getText().toString(), state, starttime
                        .getText().toString(), endtime.getText().toString(), careLevel,
                etauthor.getText().toString(), etauthorshuoming.getText()
                        .toString(), etcehua.getText().toString(),
                etcehuashuoming.getText().toString(), BaseApplication
                        .getInstance().getId(), etgaiyao.getText().toString(),
                etremark.getText().toString(), path);
        clear();

        Toast.makeText(getActivity(), "已缓存！", Toast.LENGTH_SHORT).show();
        BimpHandler.tempSelectBitmap.clear();
        SPUtil.putflag(true, getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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

        ImageSelector.open(ReportDetailFragment.this, imageConfig);   // 开启图片选择器
    }
}
