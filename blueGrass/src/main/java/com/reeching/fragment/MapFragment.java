package com.reeching.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
import com.baidu.mapapi.map.InfoWindow;
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
import com.reeching.bean.AllHualangInfo;
import com.reeching.bean.AllHualangInfo.Infos;
import com.reeching.bluegrass.AddHualangLocation;
import com.reeching.bluegrass.AlterHualangActivity;
import com.reeching.bluegrass.BaseApplication;
import com.reeching.bluegrass.CheckHualangActivity;
import com.reeching.bluegrass.GoHere;
import com.reeching.bluegrass.HuaLangSerchActivity;
import com.reeching.bluegrass.MyEditText;
import com.reeching.bluegrass.MyEditText.DrawableRightListener;
import com.reeching.bluegrass.MyOrientaionListener;
import com.reeching.bluegrass.MyOrientaionListener.OnOrientationListener;
import com.reeching.bluegrass.R;
import com.reeching.utils.HttpApi;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment {
    private LinearLayout lin;
    MapView mMapView = null;
    private static BaiduMap mBaiduMap;
    private static JSONArray array;
    private static MyEditText met;
    private ImageView iv;
    private LocationClient mLocationClient;
    private MyLocationListener myLocationListener;
    private Double mlatitude;
    private Double mlongitude;
    private Button btn;
    private BitmapDescriptor micondirection;
    private MyOrientaionListener myOrientaionListener;
    private float mcurrentx;
    private static BitmapDescriptor mMarker;
    static HttpUtils hu = new HttpUtils();
    private static List<Infos> infos;
    private static Infos sDeleteInfos;
    private static LatLng latLng = null;
    private static Marker marker;
    private static TextView tv;
    public static Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            if (msg.what == 0) {
                addoverlays(infos, true);
            }
            if (msg.what == 111 && msg != null) {

                Bundle editPaint = msg.getData();
                String editText = editPaint.getString("editText");
                met.setText(editText);
                RequestParams params = new RequestParams();
                params.addQueryStringParameter("name", editText);
                hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.nametolng, params,
                        new RequestCallBack<String>() {
                            @Override
                            public void onFailure(HttpException arg0, String arg1) {
                                // TODO Auto-generated method stub
                            }

                            @Override
                            public void onSuccess(ResponseInfo<String> arg0) {
                                // TODO Auto-generated method stub
                                JSONObject object = JSON.parseObject(arg0.result);
                                if (object.getString("result").equals("1")) {
                                    array = object.getJSONArray("infos");
                                    JSONObject jsonObject = array.getJSONObject(0);
                                    InfoWindow infoWindow;
                                    tv.setTextColor(Color.WHITE);
                                    tv.setBackgroundResource(R.drawable.location_tips);
                                    tv.setPadding(30, 20, 45, 30);
                                    tv.setText(jsonObject.getString("name"));
                                    double lat = Double.valueOf(jsonObject
                                            .getString("mapLat"));
                                    double lng = Double.valueOf(jsonObject
                                            .getString("mapLng"));
                                    Log.d("shuaishuai", "onSuccess: " + lat + "......" + lng);
                                    final LatLng mlatLng = new LatLng(lat, lng);
                                    Point p = mBaiduMap.getProjection()
                                            .toScreenLocation(mlatLng);
                                    p.y -= 47;
                                    LatLng ll = mBaiduMap.getProjection()
                                            .fromScreenLocation(p);
                                    infoWindow = new InfoWindow(tv, ll, -47);
                                    mBaiduMap.showInfoWindow(infoWindow);
                                    MapStatusUpdate u = MapStatusUpdateFactory
                                            .newLatLng(mlatLng);
                                    mBaiduMap.setMapStatus(u);
                                }
                            }
                        });
            }
        }

        ;
    };
    private TextView mDeleteText;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        SDKInitializer.initialize(getActivity().getApplicationContext());
        View view = inflater.inflate(R.layout.fragment_map, null);
        tv = new TextView(getActivity());
        btn = (Button) view.findViewById(R.id.frament_returnlocation);
        lin = (LinearLayout) view.findViewById(R.id.fragment_map_lin);
        met = (MyEditText) view.findViewById(R.id.fragment_map_met);
        mMapView = (MapView) view.findViewById(R.id.frament_bdmapView);
        iv = (ImageView) view.findViewById(R.id.fragment_map_iv);
        mDeleteText = (TextView) view.findViewById(R.id.fragment_map_delete);
        mBaiduMap = mMapView.getMap();
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(20.0f);
        mBaiduMap.setMapStatus(msu);
        mLocationClient = new LocationClient(getActivity());
        myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        option.setScanSpan(000);// 设置发起定位请求的间隔时间为3000ms
        mMapView.showZoomControls(false);
        mLocationClient.setLocOption(option);
        micondirection = BitmapDescriptorFactory
                .fromResource(R.drawable.zhixiang);
        myOrientaionListener = new MyOrientaionListener(getActivity());
        myOrientaionListener
                .setOnOrientationListener(
                        new OnOrientationListener() {

                            @Override
                            public void onOrientationChanged(float x) {
                                // TODO Auto-generated method stub
                                mcurrentx = x;

                            }
                        });
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                LatLng latLng = new LatLng(mlatitude, mlongitude);
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                mBaiduMap.animateMapStatus(msu);
            }
        });


        met.setDrawableRightListener(new DrawableRightListener() {

            @Override
            public void onDrawableRightClick(View view) {
                // TODO Auto-generated method stub
                met.setVisibility(view.GONE);
                iv.setVisibility(view.VISIBLE);
            }
        });
        met.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                initdata();
                Intent intent = new Intent(getActivity(),
                        HuaLangSerchActivity.class);
                intent.putExtra("MapFragment", "mapFragment");
                startActivityForResult(intent, 001);
            }
        });
        iv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                met.setVisibility(v.VISIBLE);
                iv.setVisibility(v.GONE);
            }
        });

        mDeleteText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2017/3/11 删除点
                // TODO: 2017/3/11  删除定位点的坐标。
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("确定删除？");
                builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadData(sDeleteInfos.getId());
                    }
                });
                builder.setNegativeButton("点错了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();


            }
        });

        mMarker = BitmapDescriptorFactory.fromResource(R.drawable.marker);

        initdata();
        mBaiduMap.setOnMapTouchListener(new BaiduMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                lin.setVisibility(View.INVISIBLE);
            }
        });

        mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker arg0) {

                // TODO Auto-generated method stub
                Bundle bundle = arg0.getExtraInfo();
                // String name = bundle.getString("name");
                Infos infos = (Infos) bundle.getSerializable("info");
                InfoWindow infoWindow;
                tv.setTextColor(Color.WHITE);
                tv.setBackgroundResource(R.drawable.location_tips);
                tv.setPadding(30, 20, 45, 30);
                tv.setText(infos.getName());
                final LatLng latLng = arg0.getPosition();
                Point p = mBaiduMap.getProjection().toScreenLocation(latLng);
                p.y -= 47;
                LatLng ll = mBaiduMap.getProjection().fromScreenLocation(p);
                infoWindow = new InfoWindow(tv, ll, -47);
                mBaiduMap.showInfoWindow(infoWindow);
                lin.setVisibility(View.VISIBLE);
                // TODO: 2017/3/11  对坐标点进行删除操作
                sDeleteInfos = infos;

                pubinfo(lin, infos);
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
                mBaiduMap.setMapStatus(u);
                return true;
            }
        });

        return view;

    }

    private void initdata() {

        RequestParams params = new RequestParams();
//		params.addQueryStringParameter("pageSize", "");
//		params.addQueryStringParameter("pageNo", "");
        hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.getallhualanginfo,
                params, new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        // TODO Auto-generated method stub
                        // Toast.makeText(getActivity(), "请检查网络连接！", 1).show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // TODO Auto-generated method stub
                        AllHualangInfo allHualangInfo = JSON.parseObject(
                                arg0.result, AllHualangInfo.class);
                        if (allHualangInfo.getResult().equals("1")) {
                            infos = new ArrayList<Infos>();
                            infos.addAll(allHualangInfo.getInfos());
                            handler.sendEmptyMessage(0);

                        } else {
                        }
                    }
                });

    }

    private void pubinfo(LinearLayout layout, final Infos info) {
        viewholder vh = null;
        if (layout.getTag() == null) {
            vh = new viewholder();
            vh.tvalter = (TextView) layout
                    .findViewById(R.id.fragment_map_alter);
            vh.tvgothere = (TextView) layout
                    .findViewById(R.id.fragment_map_gothere);
            vh.tvview = (TextView) layout.findViewById(R.id.fragment_map_view);
            vh.tvreport = (TextView) layout
                    .findViewById(R.id.fragment_map_report);
            layout.setTag(vh);
        }
        vh = (viewholder) layout.getTag();
        if (BaseApplication.getInstance().getQuanxian().equals("上报用户")) {
            vh.tvalter.setVisibility(View.GONE);
            mDeleteText.setVisibility(View.GONE);

        }
        vh.tvgothere.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(), GoHere.class);
                intent.putExtra("lat", info.getMapLat());
                intent.putExtra("lng", info.getMapLng());
                intent.putExtra("id", info.getId());
                if ("".equals(info.getMapLat()) || "".equals(info.getMapLng()) || null == info.getMapLat()
                        || null == info.getMapLng()) {
                    Toast.makeText(getActivity(), "未获取到坐标信息！", Toast.LENGTH_LONG).show();
                } else {
                    startActivity(intent);
                }
            }
        });
        vh.tvalter.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(),
                        AlterHualangActivity.class);
                intent.putExtra("lat", info.getMapLat());
                intent.putExtra("lng", info.getMapLng());
                intent.putExtra("id", info.getId());
                startActivity(intent);
            }
        });
        vh.tvview.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(),
                        CheckHualangActivity.class);
                intent.putExtra("lat", info.getMapLat());
                intent.putExtra("lng", info.getMapLng());
                startActivity(intent);
            }
        });
        vh.tvreport.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getActivity(),
                        AddHualangLocation.class);
                startActivity(intent);
            }
        });
    }

    private class viewholder {
        TextView tvalter, tvgothere, tvview, tvreport;
    }

    private static void addoverlays(List<Infos> infos, Boolean flag) {
        OverlayOptions options = null;
        mBaiduMap.clear();
        if (null != infos) {
            for (Infos info : infos) {
                if (null != info && info.getMapLat() != null
                        && !"".equals(info.getMapLat())
                        && info.getMapLng() != null
                        || !"".equals(info.getMapLat())) {

                    latLng = new LatLng(Double.valueOf(info.getMapLat()),
                            Double.valueOf(info.getMapLng()));
                    // 图标
                    options = new MarkerOptions().position(latLng)
                            .icon(mMarker).zIndex(5);
                    marker = (Marker) (mBaiduMap.addOverlay(options));
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("info", info);
                    marker.setExtraInfo(bundle);

                }
            }

            if (flag) {
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLng);
                mBaiduMap.setMapStatus(u);
            } else {
                LatLng latLngDelete = new LatLng(Double.valueOf(sDeleteInfos.getMapLat()),
                        Double.valueOf(sDeleteInfos.getMapLng()));
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(latLngDelete);
                mBaiduMap.setMapStatus(u);
            }


        }
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
        if (BaseApplication.getInstance().isInitflag() == true) {
            initdata();
        } else {

        }
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
        myOrientaionListener.stop();
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (location == null || mMapView == null)
                return;
            //定位的位置， 和精度
            MyLocationData data = new MyLocationData.Builder()
                    .direction(mcurrentx).accuracy(20)
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(data);
            mlatitude = location.getLatitude();
            mlongitude = location.getLongitude();
            MyLocationConfiguration configuration = new MyLocationConfiguration(
                    MyLocationConfiguration.LocationMode.NORMAL,
                    true, micondirection);
            mBaiduMap.setMyLocationConfigeration(configuration);
        }
    }


    private Fragment getRootFragment() {
        Fragment fragment = getParentFragment();
        while (fragment.getParentFragment() != null) {
            fragment = fragment.getParentFragment();
        }
        return fragment;

    }

    private void loadData(String i) {
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("id", i);

        hu.send(HttpMethod.POST, HttpApi.ip + HttpApi.deleteHuaLang,
                params, new RequestCallBack<String>() {

                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        // TODO Auto-generated method stub
                        Toast.makeText(getActivity(), "服务器错误", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        // TODO Auto-generated method stub
                        AllHualangInfo allHualangInfo = JSON.parseObject(
                                arg0.result, AllHualangInfo.class);
                        if ("1".equals(allHualangInfo.getResult())) {
                            infos.remove(sDeleteInfos);
                            addoverlays(infos, false);
                            lin.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "删除失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
