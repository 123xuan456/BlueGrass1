package com.reeching.bluegrass;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.reeching.utils.HackyViewPager;
import com.reeching.utils.HttpApi;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;


public class PicViewActivity extends Activity {
    private static HackyViewPager viewPager;//声明ViewPager
    private static MyAdapter adapter;
    private List<String> url = new ArrayList<String>();
    public static android.os.Handler mmhandler = new android.os.Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 000) {
                if (null != adapter) {
                    adapter.notifyDataSetChanged();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_view);
        viewPager = (HackyViewPager) findViewById(R.id.viewPager);
        Intent intent = getIntent();
        url = intent.getStringArrayListExtra("url");
        adapter = new MyAdapter();
        viewPager.setAdapter(adapter);
        int id = intent.getIntExtra("ID", 0);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                int childCount = viewPager.getChildCount();//viewPager得到页面的数量
                // 遍历当前所有加载过的PhotoView，恢复所有图片的默认状态
                for (int i = 0; i <childCount; i++) {
                    View childAt = viewPager.getChildAt(i);
                    try {
                        if (childAt != null && childAt instanceof PhotoView) {
                            PhotoView photoView = (PhotoView) childAt;//得到viewPager里面的页面
                            PhotoViewAttacher mAttacher = new PhotoViewAttacher(photoView);//把得到的photoView放到这个负责变形的类当中
                            //mAttacher.getDisplayMatrix().reset();//得到这个页面的显示状态，然后重置为默认状态
                            mAttacher.setScaleType(ImageView.ScaleType.FIT_CENTER);//设置充满全屏
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setCurrentItem(id);
    }

    public class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return  url==null?0:url.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            Picasso.with(PicViewActivity.this)
                    .load(HttpApi.picip + url.get(position))
                    .resize(900, 900)
                    .placeholder(R.drawable.downing)              //添加占位图片
                    .error(R.drawable.error)
                    .config(Bitmap.Config.RGB_565)
                    .centerInside()
                    .into(photoView);
            photoView.setScaleType(ImageView.ScaleType.FIT_CENTER);//设置图片显示为充满全屏
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            System.gc();
        }
        return super.onKeyDown(keyCode, event);
    }

}
