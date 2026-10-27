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

//For notifications
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class MotionService extends Service{

    //Declare variables
    private SensorManager sensorManager;
    private Sensor accelerometer;

    // Threads
    private HandlerThread sensorThread;
    private Handler sensorHandler;

    //For readings
    private float lastX, lastY, lastZ;
    private boolean firstReading = true;
    private static final float MOVEMENT_THRESHOLD = 3.0f;

    // For Notification
    private static final String CHANNEL_ID = "motion_channel";

    // Allow app to receive updates when sensor's values change
    private SensorEventListener sensorListener = new SensorEventListener() {

        //Whenever new sensor reading is delivered, call this
        @Override
        public void onSensorChanged(SensorEvent event) {

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            if (firstReading) {
                lastX = x;
                lastY = y;
                lastZ = z;

                firstReading = false;
                return;
            }
            float deltaX = x - lastX;
            float deltaY = y - lastY;
            float deltaZ = z - lastZ;

            float movement = (float) Math.sqrt(
                    deltaX * deltaX +
                            deltaY * deltaY +
                            deltaZ * deltaZ
            );

            if (movement > MOVEMENT_THRESHOLD) {
                Log.d("MotionService", "MOVEMENT DETECTED");
            }
            lastX = x;
            lastY = y;
            lastZ = z;
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

        // Call NotificationChannel Method
        createNotificationChannel();

        Log.d("MotionService", "Service created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID) {

        //Tell SensorManager to send readings
        if (accelerometer != null) {
            sensorManager.registerListener(
                    sensorListener,
                    accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL,
                    sensorHandler
            );
        }
        Log.d("MotionService", "Service started");

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Motion Alerts",
                    NotificationManager.IMPORTANCE_HIGH
            );

            NotificationManager manager =
                    getSystemService(NotificationManager.class);

            manager.createNotificationChannel(channel);
        }
    }

}
