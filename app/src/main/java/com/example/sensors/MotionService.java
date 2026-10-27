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

//Display Motion notification
import android.app.Notification;
import android.app.PendingIntent;
import android.content.pm.PackageManager;
import android.Manifest;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

                showMotionNotification();
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

    private void showMotionNotification() {

        // Get the current time
        String time = new SimpleDateFormat(
                "HH:mm:ss", Locale.getDefault()
        ).format(new Date());

        // Create the notification
        Notification notification = new Notification.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle("Motion Detected!")
                .setContentText("Phone moved at " + time)
                .setAutoCancel(true)
                .build();

        // Get the notification manager
        NotificationManager manager =
                getSystemService(NotificationManager.class);

        // Check notification permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // Display notification
        manager.notify(1, notification);
    }
}
