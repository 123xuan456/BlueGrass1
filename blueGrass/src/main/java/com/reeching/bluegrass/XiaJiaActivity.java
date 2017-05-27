package com.reeching.bluegrass;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.reeching.bean.HuaLangShowing;
import com.reeching.utils.ExitApplication;
import com.reeching.utils.HttpApi;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;



public class XiaJiaActivity extends AppCompatActivity {
    private HuaLangShowing.Infos infos;
    private TextView tvtheme, tvpeople, tvtimes, tvtimee, tvauthorinfo,
            comeback;
    private ImageView ivlevel;
    private NoScrollGridView lin;
    private ArrayList<String> list = new ArrayList<String>();
    private XiaJiaActivity.GridViewAdapter adapter2;
    private TextView mInfoHuaLang;
    private android.os.Handler handler=new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){
                adapter2.notifyDataSetChanged();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xia_jia);
        BaseApplication.getInstance().listSelectBitmaps.clear();
        ExitApplication.getInstance().addActivity(this);
        infos = (HuaLangShowing.Infos) getIntent().getSerializableExtra("info");
        lin = (NoScrollGridView) findViewById(R.id.haveverificationinfo_lin);
        tvtheme = (TextView) findViewById(R.id.haveverificationinfo_theme);
        tvpeople = (TextView) findViewById(R.id.haveverificationinfo_people);
        comeback = (TextView) findViewById(R.id.comeback);
        tvtimee = (TextView) findViewById(R.id.haveverificationinfo_timee);
        tvtimes = (TextView) findViewById(R.id.haveverificationinfo_times);
        tvauthorinfo = (TextView) findViewById(R.id.haveverificationinfo_authorinfo);
        ivlevel = (ImageView) findViewById(R.id.haveverificationinfo_level);
        mInfoHuaLang = (TextView) findViewById(R.id.havereportedinfo_hualang);
        initdata();

        lin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 执行浏览照片操作
                if ( BaseApplication.getInstance().listSelectBitmaps.get(position) != null&& BaseApplication.getInstance().listSelectBitmaps.size() == list.size()) {
                    Intent intent = new Intent(XiaJiaActivity.this,
                            PicViewActivity.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", position);
                    intent.putStringArrayListExtra("url", list);
                    startActivity(intent);
                } else {
                    Toast.makeText(XiaJiaActivity.this, "加载中，请稍后。。。", Toast.LENGTH_SHORT).show();
                    adapter2.notifyDataSetChanged();
                }
            }
        });

        comeback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                XiaJiaActivity.this.finish();
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
            Picasso.with(XiaJiaActivity.this)
                    .load(HttpApi.picip + list)
                    .resize(900, 900)
                    .placeholder(R.drawable.downing)              //添加占位图片
                    .error(R.drawable.error)
                    .config(Bitmap.Config.RGB_565)
                    .centerInside()
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
                            imageView.setImageBitmap(bitmap);
                            BaseApplication.getInstance().listSelectBitmaps.put(position, bitmap);
                        }

                        @Override
                        public void onBitmapFailed(Drawable drawable) {
                            imageView.setImageResource(R.drawable.error);
                        }

                        @Override
                        public void onPrepareLoad(Drawable drawable) {
                            imageView.setImageResource(R.drawable.downing);
                        }
                    });
            handler.sendEmptyMessage(0);
            return imageView;
        }
    }

    private void initdata() {
        tvtheme.setText(infos.getName());
        tvpeople.setText(infos.getMobile());
        String dateBegin = infos.getLiveTime();
        mInfoHuaLang.setText(infos.getLinkMan());
        tvtimes.setText(dateBegin);

        tvauthorinfo.setText(infos.getDescription());
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
        adapter2 = new XiaJiaActivity.GridViewAdapter(XiaJiaActivity.this, list);
        lin.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            System.gc();
        }
        return super.onKeyDown(keyCode, event);
    }
}

