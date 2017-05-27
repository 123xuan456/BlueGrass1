package com.reeching.bluegrass;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.provider.ContactsContract.Contacts;

public class MyOrientaionListener implements SensorEventListener {
	private SensorManager manager;
	private Context context;
	private Sensor sensor;
	private float lastx;

	public MyOrientaionListener(Context context) {
		this.context = context;
	}

	public void start() {
		manager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);

		if (manager != null) {
			// 获得方向传感器
			sensor = manager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

		}
		if (sensor != null) {
			manager.registerListener(this, sensor, manager.SENSOR_DELAY_UI);

		}
	}

	public void stop() {
		manager.unregisterListener(this);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
			float x = event.values[SensorManager.DATA_X];
			if (Math.abs(x - lastx) > 1.0) {
				if (mOnOrientationListener != null) {
					mOnOrientationListener.onOrientationChanged(x);
				}

			}
			lastx = x;
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	private OnOrientationListener mOnOrientationListener;

	public void setOnOrientationListener(
			OnOrientationListener mOnOrientationListener) {
		this.mOnOrientationListener = mOnOrientationListener;
	}

	public interface OnOrientationListener {
		void onOrientationChanged(float x);
	}
}
