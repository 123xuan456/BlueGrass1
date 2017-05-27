package com.reeching.bluegrass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.reeching.utils.HackyViewPager;

public class PicViewActivityTemp extends AppCompatActivity {

    private HackyViewPager viewPager;//声明ViewPager
    //创建数据源，这里采用本地数据源
    private int mposition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_view);
        viewPager = (HackyViewPager) findViewById(R.id.viewPager);
        Intent intent = getIntent();
        mposition = Integer.parseInt(intent.getStringExtra("position"));
        viewPager.setAdapter(new PicViewActivityTemp.MyAdapter());
        int id = intent.getIntExtra("ID", 0);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                int childCount = viewPager.getChildCount();//viewPager得到页面的数量
                // 遍历当前所有加载过的PhotoView，恢复所有图片的默认状态
                for (int i = 0; i < childCount; i++) {
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
            return BimpHandler.tempSelectBitmap.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            photoView.setImageBitmap(BimpHandler.tempSelectBitmap.get(position).getBitmap());

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

            PicViewActivityTemp.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
