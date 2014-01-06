package com.mindframe.awesome_o_metter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity implements SensorEventListener {
	Float azimut;

	public class CustomDrawableView extends View {
		Paint paint = new Paint();
		Paint paint2 = new Paint();
		

		public CustomDrawableView(Context context) {
			super(context);
			paint.setColor(0xffff0000);
			paint.setStyle(Style.STROKE);
			paint.setStrokeWidth(20);
			paint.setAntiAlias(true);
			
			paint2.setColor(0xffff0000);
			paint2.setStyle(Style.STROKE);
			paint2.setStrokeWidth(2);
			paint2.setAntiAlias(true);
			
		};

		protected void onDraw(Canvas canvas) {
			int width = getWidth();
			int height = getHeight();
			int centerx = width / 2;
			int centery = height / 2;
			int maxAz = 3;
			Float percent;
			Float size;
			
			//With this we made the bar moving.
			if(azimut != null){
				
				percent = azimut * 100 / maxAz;
				
				size = Math.abs(percent) * height /100;
				canvas.drawLine(centerx, height, centerx, size ,paint);
				if(size != null && azimut != null && percent != null){
					canvas.drawText("size " + size.toString(), 10, 10, paint2);
					canvas.drawText("azimut " + azimut.toString(), 10, 50, paint2);
					canvas.drawText("percent " + percent.toString(), 10, 100, paint2);
				}
			}
			
			
			
			
//			canvas.drawLine(centerx, 0, centerx, height, paint);
//			canvas.drawLine(0, centery, width, centery, paint);
//			if (azimut != null)
//				canvas.rotate(-azimut * 360 / (2 * 3.14159f), centerx, centery);
//			paint.setColor(0xff0000ff);
//			canvas.drawLine(centerx, -1000, centerx, +1000, paint);
//			canvas.drawLine(-1000, centery, 1000, centery, paint);
//			canvas.drawText("N", centerx + 5, centery - 10, paint);
//			canvas.drawText("S", centerx - 10, centery + 15, paint);
//			paint.setColor(0xff00ff00);
		}
	}

	CustomDrawableView mCustomDrawableView;
	private SensorManager mSensorManager;
	Sensor accelerometer;
	Sensor magnetometer;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCustomDrawableView = new CustomDrawableView(this);
		setContentView(mCustomDrawableView);
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
	}

	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, accelerometer,SensorManager.SENSOR_DELAY_UI);
		mSensorManager.registerListener(this, magnetometer,SensorManager.SENSOR_DELAY_UI);
	}

	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	float[] mGravity;
	float[] mGeomagnetic;

	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
			mGravity = event.values;
		if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
			mGeomagnetic = event.values;
		if (mGravity != null && mGeomagnetic != null) {
			float R[] = new float[9];
			float I[] = new float[9];
			boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
			if (success) {
				float orientation[] = new float[3];
				SensorManager.getOrientation(R, orientation);
				azimut = orientation[0]; 
			}
		}
		mCustomDrawableView.invalidate();
	}
}