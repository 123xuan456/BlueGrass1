package com.reeching.bluegrass;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.reeching.bean.AlterHualngBean;
import com.reeching.bluegrass.MyOrientaionListener.OnOrientationListener;
import com.reeching.utils.ExitApplication;
import com.reeching.utils.HttpApi;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;



public class CheckHualangActivity extends Activity {
    private String lng, lat;
    private TextView tvname, tvlocation, tvpeople, tvphone, tvdutyman,
            tvaire, tvstyle, tvaddress, tvmanagetype, tvtype, tvstate;
    private EditText eteplain;
    private Button btn;
    private NoScrollGridView lin;
    private RadioButton rbt;
    MapView mMapView = null;
    private ProgressDialog progressDialog;
    private BaiduMap mBaiduMap;
    private LocationClient mLocationClient;
    private MyLocationListener myLocationListener;
    private boolean isFirst = false;
    private Double mlatitude, mlat;
    private Double mlongitude, mlng;
    private Button btnreturn;
    private BitmapDescriptor micondirection;
    private MyOrientaionListener myOrientaionListener;
    private float mcurrentx;
    private BitmapDescriptor mMarker;
    private String id;
    private TextView mQujianText;
    private String[] qujianHao = new String[]{"A区间", "B区间", "C区间", "D区间", "E区间", "F区间"};
    private MapStatusUpdate mMsu;
    private GridViewAdapter adapter;
    private TextView comeback;

    private ArrayList<String> list = new ArrayList<String>();
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0) {
                Addimageview();
            }else {
                adapter.notifyDataSetChanged();
            }

        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(this.getApplicationContext());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_check_hualang);
        ExitApplication.getInstance().addActivity(this);
        tvname = (TextView) findViewById(R.id.check_hualang_name);
        tvlocation = (TextView) findViewById(R.id.check_hualang_location);
        comeback = (TextView) findViewById(R.id.comeback);
        BaseApplication.getInstance().listSelectBitmaps.clear();
        tvpeople = (TextView) findViewById(R.id.check_hualang_people);
        tvphone = (TextView) findViewById(R.id.check_hualang_phone);
        eteplain = (EditText) findViewById(R.id.check_hualang_eplain);
        tvdutyman = (TextView) findViewById(R.id.check_hualang_dutyman);
        tvaire = (TextView) findViewById(R.id.check_hualang_aire);
        tvstyle = (TextView) findViewById(R.id.check_hualang_style);
        tvaddress = (TextView) findViewById(R.id.check_hualang_ddress);
        tvmanagetype = (TextView) findViewById(R.id.check_hualang_managetype);
        tvtype = (TextView) findViewById(R.id.check_hualang_type);
        tvstate = (TextView) findViewById(R.id.check_hualang_state);
        btn = (Button) findViewById(R.id.check_hualang);
        rbt = (RadioButton) findViewById(R.id.check_hualang_rbtnred);
        btnreturn = (Button) findViewById(R.id.checkhualang_returnlocation);
        mMapView = (MapView) findViewById(R.id.checkhualang_mapview);
        mQujianText = (TextView) findViewById(R.id.alter_hualang_qujian);
        mBaiduMap = mMapView.getMap();
        lin = (NoScrollGridView) findViewById(R.id.check_hualang_lin);
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(20.0f);
        mBaiduMap.setMapStatus(msu);
        mLocationClient = new LocationClient(this);
        myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        option.setScanSpan(1000);// 设置发起定位请求的间隔时间为1000ms
        mMapView.showZoomControls(true);
        mLocationClient.setLocOption(option);
        micondirection = BitmapDescriptorFactory
                .fromResource(R.drawable.zhixiang);
        myOrientaionListener = new MyOrientaionListener(this);
        eteplain.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 解决scrollView中嵌套EditText导致其不能上下滑动的问题
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });
        comeback.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                CheckHualangActivity.this.finish();
            }
        });
        myOrientaionListener
                .setOnOrientationListener(new OnOrientationListener() {

                    @Override
                    public void onOrientationChanged(float x) {
                        // TODO Auto-generated method stub
                        mcurrentx = x;
                    }
                });
        btnreturn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                LatLng latLng = new LatLng(mlatitude, mlongitude);
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                mBaiduMap.animateMapStatus(msu);
                isFirst = true;


            }
        });
        mMarker = BitmapDescriptorFactory.fromResource(R.drawable.marker);

        lng = getIntent().getStringExtra("lng");
        lat = getIntent().getStringExtra("lat");
        initdata();
        mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker arg0) {
                // TODO Auto-generated method stub
                MapStatusUpdate u = MapStatusUpdateFactory
                        .newLatLng(new LatLng(mlat, mlng));
                mBaiduMap.setMapStatus(u);
                return false;
            }
        });
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(CheckHualangActivity.this,
                        HualangHistoryActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);

            }
        });
    }

    private void addoverlays() {
        // mBaiduMap.clear();
        LatLng latLng = null;
        Marker marker = null;
        OverlayOptions options;
        latLng = new LatLng(mlat, mlng);
        options = new MarkerOptions().position(latLng).icon(mMarker).zIndex(5);
        marker = (Marker) mBaiduMap.addOverlay(options);
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.setMapStatus(msu);
    }

    private void Addimageview() {
        adapter = new GridViewAdapter(CheckHualangActivity.this, list);
        lin.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 执行浏览照片操作
                if ( BaseApplication.getInstance().listSelectBitmaps.get(position)!= null) {
                    Intent intent = new Intent(CheckHualangActivity.this,
                            PicViewActivity.class);
                    intent.putExtra("position", "1");
                    intent.putExtra("ID", position);
                    intent.putStringArrayListExtra("url", list);
                    startActivity(intent);
                } else {
                    adapter.notifyDataSetChanged();
                }
            }
        });
//        lin.setOnItemClickListener(new OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//                                    long arg3) {
//                // TODO Auto-generated method stub
//                LayoutInflater inflater = LayoutInflater
//                        .from(CheckHualangActivity.this);
//                View imgEntryView = inflater.inflate(R.layout.dialog, null); // 加载自定义的布局文件
//                final AlertDialog dialog = new AlertDialog.Builder(
//                        CheckHualangActivity.this).create();
//                ImageView img = (ImageView) imgEntryView
//                        .findViewById(R.id.large_image);
//
//                // 加载网络图片
//                Picasso.with(CheckHualangActivity.this)
//                        .load(HttpApi.picip + list.get(arg2))
//                        .resize(900, 900)
//                        .placeholder(R.drawable.downing)              //添加占位图片
//                        .error(R.drawable.error)
//                        .config(Bitmap.Config.RGB_565)
//                        .centerInside()
//                        .into(img);
//
//                // 这个是加载网络图片的，可以是自己的图片设置方法
//                dialog.setView(imgEntryView); // 自定义dialog
//                dialog.show();
//                // 点击布局文件（也可以理解为点击大图）后关闭dialog，这里的dialog不需要按钮
//                imgEntryView.setOnClickListener(new OnClickListener() {
//                    public void onClick(View paramView) {
//                        dialog.cancel();
//                    }
//                });
//            }
//        });


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
                imageView.setLayoutParams(new GridView.LayoutParams(300, 300));//设置ImageView对象布局
                imageView.setAdjustViewBounds(false);//设置边界对齐
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//设置刻度的类型
                imageView.setPadding(8, 8, 8, 8);//设置间距
            } else {
                imageView = (ImageView) convertView;
            }

            Picasso.with(CheckHualangActivity.this)
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
            handler.sendEmptyMessage(22);
            return imageView;
        }

    }

    private void initdata() {
        HttpUtils hu = new HttpUtils();
        progressDialog = new ProgressDialog(CheckHualangActivity.this);
        progressDialog.setMessage("加载数据信息,请稍后...");
        progressDialog.show();
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("mapLng", lng);
        params.addQueryStringParameter("mapLat", lat);
        hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.findhualang, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {

                        Toast.makeText(CheckHualangActivity.this, "请求不成功！", Toast.LENGTH_SHORT)
                                .show();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {

                        AlterHualngBean bean = JSON.parseObject(arg0.result,
                                AlterHualngBean.class);
                        if (bean.getResult().equals("1")) {

                            tvname.setText(bean.getInfos().get(0).getName());
                            tvlocation.setText(bean.getInfos().get(0)
                                    .getAddress());

                            tvpeople.setText(bean.getInfos().get(0)
                                    .getLinkMan());
                            String qujianString = qujianHao[Integer.parseInt(bean.getInfos().get(0).getAreaNo()) - 1];
                            mQujianText.setText(qujianString.toString());
                            tvphone.setText(bean.getInfos().get(0).getMobile());
                            eteplain.setText(bean.getInfos().get(0)
                                    .getDescription());
                            tvdutyman.setText(bean.getInfos().get(0)
                                    .getLegalPerson());
                            tvaire.setText(bean.getInfos().get(0).getArea()
                                    + "平方米");
                            tvstyle.setText(bean.getInfos().get(0).getStyle());
                            tvaddress.setText(null==bean.getInfos().get(0)
                                    .getRegistAddress()?"":bean.getInfos().get(0)
                                    .getRegistAddress());
                            tvmanagetype.setText(bean.getInfos().get(0)
                                    .getManageType());
                            String i = bean.getInfos().get(0)
                                    .getEnterpriseType();
                            if (i.equals("0")) {
                                tvtype.setText("有限责任");
                            } else if (i.equals("1")) {
                                tvtype.setText("股份有限");
                            } else {
                                tvtype.setText("私营企业");
                            }

                            String j = bean.getInfos().get(0).getStatus();
                            if (j.equals("0")) {
                                tvstate.setText("歇业中");
                            } else {
                                tvstate.setText("运营中");
                            }

                            if (bean.getInfos().get(0).getCareLevel()
                                    .equals("0")) {
                                rbt.setBackgroundResource(R.drawable.green);

                            } else if (bean.getInfos().get(0).getCareLevel()
                                    .equals("1")) {
                                rbt.setBackgroundResource(R.drawable.yellow);

                            } else {
                                rbt.setBackgroundResource(R.drawable.red);

                            }
                            mlat = Double.parseDouble(bean.getInfos().get(0)
                                    .getMapLat());
                            mlng = Double.parseDouble(bean.getInfos().get(0)
                                    .getMapLng());
                            id = bean.getInfos().get(0).getId();
                            if (!"".equals(bean.getInfos().get(0).getPhoto())) {
                                String sourceStr = bean.getInfos().get(0)
                                        .getPhoto();
                                String[] sourceStrArray = sourceStr
                                        .split("\\|");
                                for (int ii = 1; ii < sourceStrArray.length; ii++) {
                                    list.add(sourceStrArray[ii]);
                                }
                            }
                            addoverlays();
                            handler.sendEmptyMessage(0);
                        } else {
                            Toast.makeText(CheckHualangActivity.this,
                                    "没有详细画廊数据！", Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    @Override
    public void onStart() {
        super.onStart();

        // 设置可以定位 并开始定位
        mBaiduMap.setMyLocationEnabled(true);
        mLocationClient.start();
        // kaiqifangxaingchuanganqi
        myOrientaionListener.start();

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        // 停止定位 并且设置不可定位
        mBaiduMap.setMyLocationEnabled(false);
        mLocationClient.stop();
        //
        myOrientaionListener.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        if (mMapView != null) {
            mBaiduMap.clear();
//            mMapView.onDestroy();
        }
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            MyLocationData data = new MyLocationData.Builder()
                    .direction(mcurrentx).accuracy(10)
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(data);
            mlatitude = location.getLatitude();
            mlongitude = location.getLongitude();
            MyLocationConfiguration configuration = new MyLocationConfiguration(
                    MyLocationConfiguration.LocationMode.NORMAL,
                    true, micondirection);
            mBaiduMap.setMyLocationConfigeration(configuration);

            LatLng latLng = new LatLng(location.getLatitude(),
                    location.getLongitude());
            mMsu = MapStatusUpdateFactory.newLatLng(latLng);
            if (isFirst) {
                mBaiduMap.animateMapStatus(mMsu);
            }

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
