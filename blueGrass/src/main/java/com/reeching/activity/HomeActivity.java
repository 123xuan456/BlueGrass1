package com.reeching.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.reeching.bluegrass.MainActivity;
import com.reeching.bluegrass.R;
import com.reeching.core.GlideImageLoader;
import com.reeching.utils.ExitApplication;
import com.youth.banner.Banner;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends Activity implements View.OnClickListener{
    private CircleImageView iv_798,iv_prairie,iv_winery,iv_one,iv_sever,iv_;
    private Banner banner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        banner = (Banner) findViewById(R.id.banner);
        intView();

    }

    private void intView() {
        iv_798= (CircleImageView) findViewById(R.id.iv_798);
        iv_798.setOnClickListener(this);
        iv_prairie= (CircleImageView) findViewById(R.id.iv_prairie);
        iv_prairie.setOnClickListener(this);
        iv_winery= (CircleImageView) findViewById(R.id.iv_winery);
        iv_winery.setOnClickListener(this);
        iv_one= (CircleImageView) findViewById(R.id.iv_one);
        iv_one.setOnClickListener(this);
        iv_sever= (CircleImageView) findViewById(R.id.iv_sever);
        iv_sever.setOnClickListener(this);
        iv_= (CircleImageView) findViewById(R.id.iv_);
        iv_.setOnClickListener(this);
        String[] urls = getResources().getStringArray(R.array.url);
        List list = Arrays.asList(urls);
        ArrayList images = new ArrayList(list);
        //banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //设置图片集合
        banner.setImages(images);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //banner.setOnBannerClickListener(this);
        banner.start();

    }

    @Override
    public void onStart() {
        super.onStart();
        banner.startAutoPlay();
    }
    @Override
    public void onStop()    {
        super.onStop();
        banner.stopAutoPlay();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setTitle("确认退出?");
            builder.setNegativeButton("退出程序",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            File file = new File("/temp/picture");
                            if (file.exists()) {
                                deleteAllFiles(file);
                            }
                            ExitApplication.getInstance().exit();
                        }
                    });
            builder.setPositiveButton("再看看",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub

                        }
                    });
            builder.show();

        }
        return super.onKeyDown(keyCode, event);
    }
    private void deleteAllFiles(File root) {
        File files[] = root.listFiles();
        if (files != null)
            for (File f : files) {
                if (f.isDirectory()) { // 判断是否为文件夹
                    deleteAllFiles(f);
                    try {
                        f.delete();
                    } catch (Exception e) {
                    }
                } else {
                    if (f.exists()) { // 判断是否存在
                        deleteAllFiles(f);
                        try {
                            f.delete();
                        } catch (Exception e) {
                        }
                    }
                }
            }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_798:
                Intent i=new Intent();
                i.setClass(HomeActivity.this, MainActivity.class);
                startActivity(i);
                break;
            case R.id.iv_prairie:
                Toast.makeText(this, "敬请期待", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_winery:
                Toast.makeText(this, "敬请期待", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_one:
                Toast.makeText(this, "敬请期待", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_sever:
                Toast.makeText(this, "敬请期待", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_:
                Toast.makeText(this, "敬请期待", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
