package com.reeching.bluegrass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.reeching.bean.HualangHistory.Infos;
import com.reeching.utils.HttpApi;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;



public class HistoryInfoActivity extends Activity {
    private Infos infos;
    private TextView tvtheme, tvpeople, tvtimes, tvtimee, tvauthor,tvauthorinfo,
            comeback;
    private ImageView ivlevel;
    private NoScrollGridView lin;
    private ArrayList<String> list = new ArrayList<String>();
    private GridViewAdapter adapter2;
    private TextView mInfoHuaLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_history_info);
        infos = (Infos) getIntent().getSerializableExtra("info");
        lin = (NoScrollGridView) findViewById(R.id.historyinfo_lin);
        tvtheme = (TextView) findViewById(R.id.historyinfo_theme);
        tvpeople = (TextView) findViewById(R.id.historyinfo_people);
        comeback = (TextView) findViewById(R.id.comeback);
        tvauthor= (TextView) findViewById(R.id.historyinfo_author);
        tvtimee = (TextView) findViewById(R.id.historyinfo_timee);
        tvtimes = (TextView) findViewById(R.id.historyinfo_times);
        tvauthorinfo = (TextView) findViewById(R.id.historyinfo_authorinfo);
        ivlevel = (ImageView) findViewById(R.id.historyinfo_level);
        mInfoHuaLang = (TextView) findViewById(R.id.havehistoryinfo_hualang);
        initdata();

        lin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 执行浏览照片操作
                if ( list.get(position) != null) {
                    Intent intent = new Intent(HistoryInfoActivity.this,
                            PicViewActivity.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", position);
                    intent.putStringArrayListExtra("url", list);
                    startActivity(intent);
                } else {
                    Toast.makeText(HistoryInfoActivity.this,"加载中，请稍后。。。",Toast.LENGTH_SHORT).show();
                    adapter2.notifyDataSetChanged();
                }
            }
        });


        comeback.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                HistoryInfoActivity.this.finish();
            }
        });
    }

    private void initdata() {
        tvtheme.setText(infos.getTheme());
        tvpeople.setText(infos.getManager());
        tvtimes.setText(infos.getDateBegin());
        tvtimee.setText(infos.getDateEnd());
        tvauthor.setText(infos.getAuthor());
        mInfoHuaLang.setText(infos.getGalleryName());
        tvauthorinfo.setText(infos.getManagerIntroduction());
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
        adapter2 = new GridViewAdapter(HistoryInfoActivity.this, list);
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
            Picasso.with(HistoryInfoActivity.this)
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
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            System.gc();
        }
        return super.onKeyDown(keyCode, event);
    }
}
