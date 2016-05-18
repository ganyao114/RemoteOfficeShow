package com.gy.myframework.Service.sensormonitor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Created by gy on 2016/2/25.
 */
public class SenserActionProcessor implements SensorEventListener{

    private static final String TAG = "gy-Senser";
    private ISenserActionCallBack callBack;
    private long lastshaketime = 0;
    private long shakeInter = 1000;

    public SenserActionProcessor(ISenserActionCallBack callBack,Context context) {
        this.callBack = callBack;
        init(context);
    }

    private void init(Context context){
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int sensorType = event.sensor.getType();
        //values[0]:X轴，values[1]：Y轴，values[2]：Z轴
        float[] values = event.values;

        float x = values[0];
        float y = values[1];
        float z = values[2];

        Log.i(TAG, "x:" + x + "y:" + y + "z:" + z);
        Log.i(TAG, "Math.abs(x):" + Math.abs(x) + "Math.abs(y):" + Math.abs(y) + "Math.abs(z):" + Math.abs(z));
        if (sensorType == Sensor.TYPE_ACCELEROMETER) {
            int value = 15;//摇一摇阀值,不同手机能达到的最大值不同,如某品牌手机只能达到20
            if (x >= value || x <= -value || y >= value || y <= -value || z >= value || z <= -value) {
                Log.i(TAG, "检测到摇动");
                //播放动画，更新界面，并进行对应的业务操作
                if (System.currentTimeMillis() - lastshaketime > shakeInter || lastshaketime == 0){
                    callBack.onPhoneShaked();
                    lastshaketime = System.currentTimeMillis();
                }
            }

        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    public void setShakeInter(long shakeInter) {
        this.shakeInter = shakeInter;
    }
}
