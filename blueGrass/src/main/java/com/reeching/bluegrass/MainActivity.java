package com.reeching.bluegrass;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.reeching.fragment.AnjianFragment;
import com.reeching.fragment.BaseFragment;
import com.reeching.fragment.HualangFragment;
import com.reeching.fragment.MineFragment;
import com.reeching.fragment.ReportDetailFragment;
import com.reeching.fragment.ReportFragment;
import com.reeching.utils.MyViewPager;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.yancy.imageselector.ImageSelectorActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.reeching.fragment.MapFragment.handler;

public class MainActivity extends BaseFragment {
    private AnjianFragment anjianFragment;
    private HualangFragment hualangFragment;
    private MineFragment mineFragment;
    private ReportFragment reportFragment;
    private ArrayList<Fragment> fragmentsList;
    private RadioButton rbt1, rbt2, rbt3, rbt4;
    private MyViewPager mPager;
    private RadioGroup mRadioGroup;
    private ArrayList<String> filePathList;
    private byte[] bitmapByte;
    private Bitmap bm;
    private String pathDefinition;
    //高清的路径  照相机功能
    private String HD;
    //标清的路径  照相机功能
    private String SD;
    private String mAbsolutePath;
    private PushAgent mPushAgent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);
        rbt1 = (RadioButton) findViewById(R.id.rbtn_report);
        rbt2 = (RadioButton) findViewById(R.id.rbtn_anjian);
        rbt3 = (RadioButton) findViewById(R.id.rbtn_hualang);
        rbt4 = (RadioButton) findViewById(R.id.rbtn_mine);
        rbt1.setOnClickListener(new MyOnClickListener(0));
        rbt2.setOnClickListener(new MyOnClickListener(1));
        rbt3.setOnClickListener(new MyOnClickListener(2));
        rbt4.setOnClickListener(new MyOnClickListener(3));
        mRadioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        mRadioGroup.check(R.id.rbtn_report);
        // 注册广播
        MainReceiver mainReceiver = new MainReceiver();
        IntentFilter filter = new IntentFilter("com.spice.spicytemptation.net.HttpOpearation.checkVersion");
        registerReceiver(mainReceiver, filter);
        if(BaseApplication.getInstance().getQuanxian().equals("上报用户")){
            rbt2.setVisibility(View.GONE);
        }else {
            rbt2.setVisibility(View.VISIBLE);
        }
        InitViewPager();
        mPushAgent = PushAgent.getInstance(this);
        mPushAgent.onAppStart();


        mPushAgent.addAlias(BaseApplication.getInstance().getId(),
                BaseApplication.getInstance().getId(), new UTrack.ICallBack() {
                    @Override
                    public void onMessage(boolean isSuccess, String message) {

                    }
                });


    }

    File filePath;

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取相册的照片
        if (resultCode == RESULT_OK && data != null) {
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
                    ReportDetailFragment.mHandler.sendMessage(obtainAblum);
                }
            }).start();
        }

        //用来搜索列表的， 并且展示在上面
        if (resultCode == 3) {
            if (null != data) {
                Message editPaint = Message.obtain();
                editPaint.what = 003;
                Bundle bundle = new Bundle();
                bundle.putString("editText", data.getStringExtra("result"));
                bundle.putString("id", data.getStringExtra("id"));
                editPaint.setData(bundle);
                ReportDetailFragment.mHandler.sendMessage(editPaint);
            }
        }
        if (resultCode == 4) {
            if (null != data) {
                Message editPaint = Message.obtain();
                editPaint.what = 111;
                Bundle bundle = new Bundle();
                bundle.putString("editText", data.getStringExtra("result"));
                bundle.putString("id", data.getStringExtra("id"));
                editPaint.setData(bundle);
                handler.sendMessage(editPaint);
            }
        }
    }

    // 得到bitmap的字节流。
    private byte[] getData(Bitmap bm, String fileName) {
        if (bm != null && filePath != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
            bitmapByte = baos.toByteArray();
        }
        return bitmapByte;
    }
    private void InitViewPager() {
        mPager = (MyViewPager) findViewById(R.id.act_framelayout);
        mPager.setCanScrollble(false);
        mPager.setOffscreenPageLimit(4);
        fragmentsList = new ArrayList<Fragment>();
        anjianFragment = new AnjianFragment();
        hualangFragment = new HualangFragment();
        mineFragment = new MineFragment();
        reportFragment = new ReportFragment();
        fragmentsList.add(reportFragment);
        fragmentsList.add(anjianFragment);
        fragmentsList.add(hualangFragment);
        fragmentsList.add(mineFragment);
        mPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        mPager.setCurrentItem(0);

    }

    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            mPager.setCurrentItem(index);
            if (index == 0) {
                mRadioGroup.check(R.id.rbtn_report);
            } else if (index == 1) {
                mRadioGroup.check(R.id.rbtn_anjian);
            } else if (index == 2) {
                mRadioGroup.check(R.id.rbtn_hualang);
            } else {
                mRadioGroup.check(R.id.rbtn_mine);
            }

        }
    }

    ;

    class MyAdapter extends FragmentStatePagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return fragmentsList.size();
        }

        // 得到每个item
        @Override
        public Fragment getItem(int position) {
            return fragmentsList.get(position);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    class MainReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            BimpHandler.tempAddPhoto.clear();
        }
    }
}
