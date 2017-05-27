package com.reeching.bluegrass;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
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
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.reeching.bluegrass.MyOrientaionListener.OnOrientationListener;
import com.reeching.utils.ExitApplication;

public class GoHere extends Activity {
	MapView mMapView = null;
	private BaiduMap mBaiduMap;
	private LocationClient mLocationClient;
	private MyLocationListener myLocationListener;
	private boolean isFirst = true;
	private Double mlatitude,lat;
	private Double mlongitude,lng;
	private Button btn, btnroad;
	private BitmapDescriptor micondirection;
	private MyOrientaionListener myOrientaionListener;
	private float mcurrentx;
	private BitmapDescriptor mMarker;
	private RoutePlanSearch mSearch;
	private LatLng msLatLng, meLatLng;
	private TextView comeback;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		SDKInitializer.initialize(this.getApplicationContext());
		setContentView(R.layout.activity_go_here);
		 ExitApplication.getInstance().addActivity(this);
		btn = (Button) findViewById(R.id.returnlocation);
		btnroad = (Button) findViewById(R.id.start_btn);
		mMapView = (MapView) findViewById(R.id.bmapView);
		comeback = (TextView) findViewById(R.id.comeback);
		mBaiduMap = mMapView.getMap();
		lat=Double.parseDouble(getIntent().getStringExtra("lat"));
		lng=Double.parseDouble(getIntent().getStringExtra("lng"));
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
		mBaiduMap.setMapStatus(msu);
		mLocationClient = new LocationClient(GoHere.this);
		myLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(myLocationListener);
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
		option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		option.setScanSpan(1000);// 设置发起定位请求的间隔时间为1000ms
		mMapView.showZoomControls(false);
		mLocationClient.setLocOption(option);
		micondirection = BitmapDescriptorFactory
				.fromResource(R.drawable.zhixiang);
		myOrientaionListener = new MyOrientaionListener(GoHere.this);
		myOrientaionListener
				.setOnOrientationListener(new OnOrientationListener() {

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
		mMarker = BitmapDescriptorFactory.fromResource(R.drawable.marker);
		addoverlays();

		meLatLng = new LatLng(lat, lng);
		btnroad.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!lat.equals("")&&lat!=null&&!lng.equals("")&&lng!=null){
					routePlan();
				}else{
					Toast.makeText(GoHere.this,"未获取到坐标点！",Toast.LENGTH_LONG).show();
				}

				btnroad.setEnabled(false);
			}
		});
		comeback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GoHere.this.finish();
			}
		});

	}

	private void addoverlays() {
		// mBaiduMap.clear();
		LatLng latLng = null;
		Marker marker = null;
		OverlayOptions options;
		latLng = new LatLng(lat, lng);
		options = new MarkerOptions().position(latLng).icon(mMarker).zIndex(5);
		marker = (Marker) mBaiduMap.addOverlay(options);
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
		mBaiduMap.setMapStatus(msu);

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
		isFirst = true;

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
	protected void onRestart() {
		super.onRestart();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		if (mMapView != null) {
//			mMapView.onDestroy();
		}
		if (mSearch != null) {
			mSearch.destroy();
		}
	}

	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub
			MyLocationData data = new MyLocationData.Builder()
					.direction(mcurrentx).accuracy(location.getRadius())
					.latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(data);
			mlatitude = location.getLatitude();
			mlongitude = location.getLongitude();
			msLatLng = new LatLng(location.getLatitude(),
					location.getLongitude());
			MyLocationConfiguration configuration = new MyLocationConfiguration(
					MyLocationConfiguration.LocationMode.NORMAL,
					true, micondirection);
			mBaiduMap.setMyLocationConfigeration(configuration);
			if (isFirst) {
				LatLng latLng = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
				mBaiduMap.animateMapStatus(msu);
				isFirst = false;
			}
		}
	}

	public void routePlan() {
		mSearch = RoutePlanSearch.newInstance();
		mSearch.setOnGetRoutePlanResultListener(listener);
		// 起点与终点
		PlanNode stNode = PlanNode.withLocation(msLatLng);
		PlanNode enNode = PlanNode.withLocation(meLatLng);
		// 步行路线规划
		boolean res = mSearch.walkingSearch(new WalkingRoutePlanOption().from(
				stNode).to(enNode));

	}

	OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {
		/**
		 * 步行
		 */
		public void onGetWalkingRouteResult(WalkingRouteResult result) {
			// 获取步行线路规划结果
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
				Toast.makeText(GoHere.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
						.show();
			}
			if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
				// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
				// result.getSuggestAddrInfo()
				return;
			}
			if (result.error == SearchResult.ERRORNO.NO_ERROR) {
				WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(
						mBaiduMap);
				mBaiduMap.setOnMarkerClickListener(overlay);
				overlay.setData(result.getRouteLines().get(0));
				overlay.addToMap();
				overlay.zoomToSpan();
				btnroad.setEnabled(true);
			}
		}

		public void onGetTransitRouteResult(TransitRouteResult result) {
			// 获取公交换乘路径规划结果
		}

		public void onGetDrivingRouteResult(DrivingRouteResult result) {
			// 获取驾车线路规划结果
		}

	};

	/**
	 * 继承步行规划的子类,通过覆盖相应方法实现功能
	 * 
	 * BitmapDescriptor getStartMarker() 覆写此方法以改变默认起点图标 BitmapDescriptor
	 * getTerminalMarker() 覆写此方法以改变默认终点图标
	 * 
	 */
	class MyWalkingRouteOverlay extends WalkingRouteOverlay {

		public MyWalkingRouteOverlay(BaiduMap arg0) {
			super(arg0);
			// TODO Auto-generated constructor stub
		}
	}

}
