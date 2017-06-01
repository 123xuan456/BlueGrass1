package com.reeching.bluegrass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.reeching.bean.ZhanlanBean;
import com.reeching.utils.HttpApi;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class HaveReportedActivity extends Activity {
    private ZhanlanBean.Infos infos;
    private TextView tvtheme, tvpeople, tvtimes, tvtimee, tvauthor, tvauthorinfo,
            comeback;
    private ImageView ivlevel;
    private NoScrollGridView lin;
    private ArrayList<String> list = new ArrayList<String>();
    private HaveReportedActivity.GridViewAdapter adapter2;
    private TextView mHuaLang;
    private TextView mCurrentTime;
    private android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                adapter2.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_have_reported);
        infos = (ZhanlanBean.Infos) getIntent().getSerializableExtra("info");
        lin = (NoScrollGridView) findViewById(R.id.havereportedinfo_lin);
        tvtheme = (TextView) findViewById(R.id.havereportedinfo_theme);
        tvpeople = (TextView) findViewById(R.id.havereportedinfo_people);
        comeback = (TextView) findViewById(R.id.comeback);
        tvauthor = (TextView) findViewById(R.id.havereportedinfo_author);
        tvtimee = (TextView) findViewById(R.id.havereportedinfo_timee);
        tvtimes = (TextView) findViewById(R.id.havereportedinfo_times);
        tvauthorinfo = (TextView) findViewById(R.id.havereportedinfo_authorinfo);
        ivlevel = (ImageView) findViewById(R.id.havereportedinfo_level);
        mHuaLang = (TextView) findViewById(R.id.havereported_hualang);
        mCurrentTime = (TextView) findViewById(R.id.history_currenttime);
        initdata();

        comeback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                HaveReportedActivity.this.finish();
            }
        });
        lin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 执行浏览照片操作
                if (list.get(position) != null ) {
                    Intent intent = new Intent(HaveReportedActivity.this,
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
    }

    private void initdata() {
        tvtheme.setText(infos.getTheme());
        tvpeople.setText(infos.getManager());
        tvtimes.setText(infos.getDateBegin());
        tvtimee.setText(infos.getDateEnd());
        tvauthor.setText(infos.getAuthor());
        mHuaLang.setText(infos.getGalleryName());
        mCurrentTime.setText(infos.getCreateDate());
        tvauthorinfo.setText(infos.getManagerIntroduction());
        if (infos.getCareLevel().equals("0")) {
            ivlevel.setImageResource(R.drawable.green);
        } else if (infos.getCareLevel().equals("1")) {
            ivlevel.setImageResource(R.drawable.yellow);
        } else if (infos.getCareLevel().equals("2")) {
            ivlevel.setImageResource(R.drawable.red);
        }
        if (!"".equals(infos.getPhoto())) {
            String sourceStr = infos.getPhoto();
            String[] sourceStrArray = sourceStr.split("\\|");
            for (int ii = 1; ii < sourceStrArray.length; ii++) {
                list.add(sourceStrArray[ii]);
            }
        }
        adapter2 = new HaveReportedActivity.GridViewAdapter(HaveReportedActivity.this, list);
        lin.setAdapter(adapter2);
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
                imageView.setLayoutParams(new GridView.LayoutParams(200, 200));//设置ImageView对象布局
                imageView.setAdjustViewBounds(false);//设置边界对齐
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//设置刻度的类型
                imageView.setPadding(5, 5, 5, 5);//设置间距
            } else {
                imageView = (ImageView) convertView;
            }
            Picasso.with(HaveReportedActivity.this)
                    .load(HttpApi.picip + list)
                    .resize(900, 900)
                    .placeholder(R.drawable.downing)              //添加占位图片
                    .error(R.drawable.error)
                    .config(Bitmap.Config.RGB_565)
                    .centerInside()
                    .into(imageView);
            handler.sendEmptyMessage(0);
            return imageView;
        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            System.gc();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


}
