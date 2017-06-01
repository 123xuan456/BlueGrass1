package com.reeching.bluegrass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GalleryReportActivity extends Activity {

    private Intent intent;
    // 返回按钮
    private LinearLayout ImageView_Linearlayout_Back;
    // 发送按钮
    private TextView send_bt;
    // 删除按钮
    private TextView del_bt;
    // 顶部显示预览图片位置的textview
    private TextView positionTextView;
    // 获取前一个activity传过来的position
    private int position;
    // 当前的位置
    private int location = 0;

    private ArrayList<View> listViews = null;
    private ViewPagerFixed pager;
    private GalleryReportActivity.MyPageAdapter adapter;

    public List<Bitmap> bmp = new ArrayList<Bitmap>();
    public List<String> drr = new ArrayList<String>();
    public List<String> del = new ArrayList<String>();

    private Context mContext;

    RelativeLayout photo_relativeLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_report);
        // CloseMe.add(this);
        mContext = this;
        ImageView_Linearlayout_Back = (LinearLayout) findViewById(R.id.ImageView_Linearlayout_Back);
        send_bt = (TextView) findViewById(R.id.send_button);
        del_bt = (TextView) findViewById(R.id.gallery_del);
        del_bt.setVisibility(View.GONE);
        ImageView_Linearlayout_Back.setOnClickListener(new GalleryReportActivity.BackListener());
        send_bt.setOnClickListener(new GalleryReportActivity.GallerySendListener());
        del_bt.setOnClickListener(new GalleryReportActivity.DelListener());
        intent = getIntent();
        position = Integer.parseInt(intent.getStringExtra("position"));
        isShowOkBt();

        pager = (ViewPagerFixed) findViewById(R.id.gallery01);
        pager.setOnPageChangeListener(pageChangeListener);


        for (int i = 0; i < BimpHandler.tempSelectBitmap.size(); i++) {
            initListViews(BimpHandler.tempSelectBitmap.get(i).getBitmap());
        }

        adapter = new GalleryReportActivity.MyPageAdapter(listViews);
        pager.setAdapter(adapter);
        pager.setPageMargin(10);
        int id = intent.getIntExtra("ID", 0);
        pager.setCurrentItem(id);
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {

        public void onPageSelected(int arg0) {
            location = arg0;
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void initListViews(Bitmap bm) {
        if (listViews == null)
            listViews = new ArrayList<View>();
        PhotoView img = new PhotoView(this);
        img.setBackgroundColor(0xff000000);
        img.setImageBitmap(bm);
        img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        listViews.add(img);
    }


    private class BackListener implements View.OnClickListener {

        public void onClick(View v) {
            // intent.setClass(GalleryActivity.this, ImageFolderActivity.class);
            // startActivity(intent);
            finish();
        }
    }

    private class DelListener implements View.OnClickListener {

        public void onClick(View v) {
            if (listViews.size() == 1) {
                BimpHandler.tempSelectBitmap.clear();
                BimpHandler.max = 0;
                send_bt.setText(getResources().getString(R.string.finish));
                Intent intent = new Intent("data.broadcast.action");
                sendBroadcast(intent);
                finish();
            } else {
                // pager.removeViewAt(location);
                BimpHandler.tempSelectBitmap.remove(location);
                // BimpHandler.max--;
                // pager.removeAllViews();
                // listViews.remove(location);

                pager = (ViewPagerFixed) findViewById(R.id.gallery01);
                pager.setOnPageChangeListener(pageChangeListener);
                for (int i = 0; i < BimpHandler.tempSelectBitmap.size(); i++) {
                    initListViews(BimpHandler.tempSelectBitmap.get(i).getBitmap());

                }

                adapter = new GalleryReportActivity.MyPageAdapter(listViews);
                pager.setAdapter(adapter);
                pager.setPageMargin(10);
                // int id = intent.getIntExtra("ID", 0);
                // pager.setCurrentItem(id);

                // int id = intent.getIntExtra("ID", 0);
                int pos = location - 1;
                if (pos < 0) {
                    pos = 0;
                }
                pager.setCurrentItem(pos);

                send_bt.setText(getResources().getString(R.string.finish));
                // adapter.notifyDataSetChanged();
                // adapter.notifyDataSetChanged();
            }
        }
    }

    private class GallerySendListener implements View.OnClickListener {
        public void onClick(View v) {
            finish();
            findViewById(R.id.textView_Title_Left).setVisibility(View.GONE);

        }
    }

    public void isShowOkBt() {
        if (BimpHandler.tempSelectBitmap.size() > 0) {
            send_bt.setText(getResources().getString(R.string.finish));
            send_bt.setPressed(true);
            send_bt.setClickable(true);
            send_bt.setTextColor(Color.WHITE);
        } else {
            send_bt.setPressed(false);
            send_bt.setClickable(false);
            send_bt.setTextColor(Color.parseColor("#E1E0DE"));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
		/*	if (position == 1) {
				this.finish();
				intent.setClass(GalleryActivity.this, AlbumActivity.class);
				startActivity(intent);
			} else if (position == 2) {
				this.finish();
				// intent.setClass(GalleryActivity.this,
				// ShowAllPhotoActivity.class);
				// startActivity(intent);
			}*/
            finish();
        }
        return true;
    }

    class MyPageAdapter extends PagerAdapter {

        private ArrayList<View> listViews;

        private int size;

        public MyPageAdapter(ArrayList<View> listViews) {
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }

        public void setListViews(ArrayList<View> listViews) {
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }

        public int getCount() {
            return size;
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPagerFixed) arg0).removeView(listViews.get(arg1 % size));
        }

        public void finishUpdate(View arg0) {
        }

        public Object instantiateItem(View arg0, int arg1) {
            try {
                ((ViewPagerFixed) arg0).addView(listViews.get(arg1 % size), 0);

            } catch (Exception e) {
            }
            return listViews.get(arg1 % size);
        }

        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }



}
