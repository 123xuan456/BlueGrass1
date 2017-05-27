package com.reeching.bluegrass;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.reeching.bean.AllHuaLangHistory;
import com.reeching.utils.ExitApplication;
import com.reeching.utils.HttpApi;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AllHistoryInfoActivity extends AppCompatActivity {
    private AllHuaLangHistory.Infos infos;
    private TextView tvtheme, tvpeople, tvauthor, tvtimes, tvtimee, tvauthorinfo,
            comeback;
    private ImageView ivlevel;
    private NoScrollGridView lin;
    private ArrayList<String> list = new ArrayList<String>();
    private AllHistoryInfoActivity.GridViewAdapter adapter2;
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_all_history_info);
        tvauthor = (TextView) findViewById(R.id.allhistoryinfo_author);
        ExitApplication.getInstance().addActivity(this);
        BaseApplication.getInstance().listSelectBitmaps.clear();
        infos = (AllHuaLangHistory.Infos) getIntent().getSerializableExtra("info");
        lin = (NoScrollGridView) findViewById(R.id.allhistoryinfo_lin);
        tvtheme = (TextView) findViewById(R.id.allhistoryinfo_theme);
        tvpeople = (TextView) findViewById(R.id.allhistoryinfo_people);
        comeback = (TextView) findViewById(R.id.comeback);
        tvtimee = (TextView) findViewById(R.id.allhistoryinfo_timee);
        tvtimes = (TextView) findViewById(R.id.allhistoryinfo_times);
        tvauthorinfo = (TextView) findViewById(R.id.allhistoryinfo_authorinfo);
        ivlevel = (ImageView) findViewById(R.id.allhistoryinfo_level);
        mInfoHuaLang = (TextView) findViewById(R.id.havereportedinfo_hualang);
        initdata();

        lin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 执行浏览照片操作
                if ( BaseApplication.getInstance().listSelectBitmaps.get(position) != null&& BaseApplication.getInstance().listSelectBitmaps.size() == list.size()) {
                    Intent intent = new Intent(AllHistoryInfoActivity.this,
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

        comeback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                AllHistoryInfoActivity.this.finish();
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

            Picasso.with(AllHistoryInfoActivity.this)
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
                            imageView.clearAnimation();
                          //  BimpHandler.listSelectBitmap.put(position, bitmap);
                            BaseApplication.getInstance().listSelectBitmaps.put(position, bitmap);
                        }

                        @Override
                        public void onBitmapFailed(Drawable drawable) {
                            imageView.setImageResource(R.drawable.error);
                            imageView.clearAnimation();
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
        tvtheme.setText(infos.getTheme());
        tvpeople.setText(infos.getManager());
        Date dateBegin = infos.getDateBegin();
        mInfoHuaLang.setText(infos.getGalleryName());
        SimpleDateFormat formatter =
                new SimpleDateFormat("yyyy-MM-dd ");
        String beginTime = formatter.format(dateBegin);
        tvauthor.setText(infos.getAuthor());
        tvtimes.setText(beginTime);
        Date dateEnd = infos.getDateEnd();
        String endTime = formatter.format(dateEnd);
        tvtimee.setText(endTime);
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
        adapter2 = new AllHistoryInfoActivity.GridViewAdapter(AllHistoryInfoActivity.this, list);
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
