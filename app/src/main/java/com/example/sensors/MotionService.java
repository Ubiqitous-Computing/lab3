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

// Threads handled on separate thread
import android.os.Handler;
import android.os.HandlerThread;

// Listens to accelerometer
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class MotionService extends Service{

    //Declare variables
    private SensorManager sensorManager;
    private Sensor accelerometer;

    // Threads
    private HandlerThread sensorThread;
    private Handler sensorHandler;

    // Allow app to receive updates when sensor's values change
    private SensorEventListener sensorListener = new SensorEventListener() {

        //Whenever new sensor reading is delivered, call this
        @Override
        public void onSensorChanged(SensorEvent event) {

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            Log.d("MotionService",
                    "X: " + x + " Y: " + y + " Z: " + z);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();

        sensorManager = (SensorManager)
                getSystemService(Context.SENSOR_SERVICE);

        accelerometer = sensorManager.getDefaultSensor(
                Sensor.TYPE_ACCELEROMETER);

        // Create separate threads
        sensorThread = new HandlerThread("SensorThread");

        // Start thread to process tasks
        sensorThread.start();

        //Create handler connected to the new thread
        sensorHandler = new Handler(sensorThread.getLooper());

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
