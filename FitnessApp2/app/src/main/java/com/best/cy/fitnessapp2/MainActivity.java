package com.best.cy.fitnessapp2;

import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager sm;
    Sensor sensor_accelerometer;

    long myTime, myTime2;

    float x, y, z;
    float lastX, lastY, lastZ;

    final int walkThreshold = 455; //걷기 인식 임계값

    double acceleration = 0;
    StepCounterView mJumpGame;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //화면을 세로로 설정하기
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.stepcounter);

        mJumpGame = findViewById(R.id.mJumpGame);
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);

        sensor_accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return false;
        }

        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        sm.registerListener(this, sensor_accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sm.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            myTime2 = System.currentTimeMillis();
            long gabOfTime = myTime2 - myTime;

            if (gabOfTime > 100) {
                myTime = myTime2;
                x = event.values[0];
                y = event.values[1];
                z = event.values[2];
                acceleration = Math.abs(x + y + z - lastX - lastY - lastZ) / gabOfTime * 10000;

                if (acceleration > walkThreshold) {
                    StepCounterView.walkingCount += 1.0;
                    mJumpGame.postInvalidate();
                }

                lastX = event.values[0];
                lastY = event.values[1];
                lastZ = event.values[2];
            }

        }
    }
}

