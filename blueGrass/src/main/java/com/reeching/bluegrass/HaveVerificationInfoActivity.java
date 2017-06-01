package com.reeching.bluegrass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
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
import com.reeching.bean.HuaLangShowing;
import com.reeching.utils.HttpApi;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;



public class HaveVerificationInfoActivity extends Activity {
    private HuaLangShowing.Infos infos;
    private TextView tvtheme, tvpeople, tvtimes, tvtimee, tvauthorinfo, tvauthor,
            comeback;
    private EditText questremark;
    private ImageView ivlevel;
    private NoScrollGridView lin;
    private ArrayList<String> list = new ArrayList<String>();
    private HaveVerificationInfoActivity.GridViewAdapter adapter2;
    private TextView mInfoHuaLang;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_have_verification_info);
        infos = (HuaLangShowing.Infos) getIntent().getSerializableExtra("info");
        lin = (NoScrollGridView) findViewById(R.id.haveverificationinfo_lin);
        tvtheme = (TextView) findViewById(R.id.haveverificationinfo_theme);
        tvpeople = (TextView) findViewById(R.id.haveverificationinfo_people);
        btn = (Button) findViewById(R.id.haveverificationinfo_xiada);
        comeback = (TextView) findViewById(R.id.comeback);
        questremark= (EditText) findViewById(R.id.haveverificationinfo_remark);
        tvauthor = (TextView) findViewById(R.id.haveverificationinfo_author);
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
                if (list.get(position) != null) {
                    Intent intent = new Intent(HaveVerificationInfoActivity.this,
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

                HaveVerificationInfoActivity.this.finish();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               report();
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
            Picasso.with(HaveVerificationInfoActivity.this)
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

    private void initdata() {
        tvtheme.setText(infos.getTheme());
        tvpeople.setText(infos.getManager());
        tvauthor.setText(infos.getAuthor());
        questremark.setText(infos.getRemarks());
        String dateBegin = infos.getDateBegin();
        mInfoHuaLang.setText(infos.getGalleryName());
        tvtimes.setText(dateBegin);
        questremark.setText(infos.getQuestionRemarks());
        tvtimee.setText(infos.getDateEnd());
        //  }
        tvauthorinfo.setText(infos.getManagerIntroduction());
        if (infos.getCareLevel().equals("0")) {
            ivlevel.setImageResource(R.drawable.green);
        } else if (infos.getCareLevel().equals("1")) {
            ivlevel.setImageResource(R.drawable.yellow);
        } else {
            ivlevel.setImageResource(R.drawable.red);
        }
        if (!"".equals(infos.getPhotoFalse())) {
            String sourceStr = infos.getPhotoFalse();
            String[] sourceStrArray = sourceStr.split("\\|");
            for (int ii = 1; ii < sourceStrArray.length; ii++) {
                list.add(sourceStrArray[ii]);
            }
        }
        adapter2 = new HaveVerificationInfoActivity.GridViewAdapter(HaveVerificationInfoActivity.this, list);
        lin.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();
    }

    private void report() {
        HttpUtils hu = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("id", infos.getId());
        params.addQueryStringParameter("galleryId", infos.getGalleryId());
        params.addQueryStringParameter("theme", infos.getTheme());
        params.addQueryStringParameter("status", infos.getStatus());
        params.addQueryStringParameter("dateBegin", tvtimes.getText().toString());
        params.addQueryStringParameter("dateEnd", tvtimee.getText().toString());
        params.addQueryStringParameter("careLevel", infos.getCareLevel());
        params.addQueryStringParameter("exhibitionIntroduction", infos.getExhibitionIntroduction());
        params.addQueryStringParameter("remarks", infos.getRemarks());
        params.addQueryStringParameter("authorIntroduction", infos.getAuthorIntroduction());
        params.addQueryStringParameter("author", infos.getAuthor());
        params.addQueryStringParameter("manager", infos.getManager());
        params.addQueryStringParameter("questionRemarks",questremark.getText().toString());
        params.addQueryStringParameter("managerIntroduction", infos.getManagerIntroduction());
        params.addQueryStringParameter("userId", BaseApplication.getInstance()
                .getId());
//        for (int i = 0; i < (BimpHandler.tempSelectBitmap.size()); i++) {
//            String imagePath = BimpHandler.tempSelectBitmap.get(0)
//                    .getImagePath();
//            String uploadType = imagePath.substring(
//                    imagePath.lastIndexOf(".") + 1, imagePath.length());
//            params.addBodyParameter("postsPic" + (i + 1) + "_" + mPhotoNum.get(i), new File(
//                    BimpHandler.tempSelectBitmap.get(i).getImagePath()));
//            params.addBodyParameter("uploadType" + (i + 1), uploadType);
//        }
        hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.reporthualang, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        // TODO Auto-generated method stub
                        btn.setEnabled(true);
                    }

                    @Override
                    public void onStart() {
                        // TODO Auto-generated method stub
                        super.onStart();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // TODO Auto-generated method stub
                        JSONObject jsonObject = JSON.parseObject(arg0.result);
                        if (jsonObject.getString("result").equals("1")) {
                            Toast.makeText(HaveVerificationInfoActivity.this, "保存成功！", Toast.LENGTH_SHORT).show();
                            btn.setEnabled(true);

                        } else {
                            Toast.makeText(HaveVerificationInfoActivity.this, "保存失败！", Toast.LENGTH_SHORT).show();
                            btn.setEnabled(true);
                        }
                    }
                });

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            System.gc();
        }
        return super.onKeyDown(keyCode, event);
    }
}
