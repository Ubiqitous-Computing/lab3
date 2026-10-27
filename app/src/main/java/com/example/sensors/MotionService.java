package com.example.sensors;
// Import packages
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

// Sensors
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.content.Context;

public class MotionService extends Service{

    //Declare variables
    private SensorManager sensorManager;
    private Sensor accelerometer;

    @Override
    public void onCreate() {
        super.onCreate();

        sensorManager = (SensorManager)
                getSystemService(Context.SENSOR_SERVICE);

        accelerometer = sensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER);
        )

        Log.d("MotionService", "Service created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID) {

        Log.d("MotionService", "Service started");

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
