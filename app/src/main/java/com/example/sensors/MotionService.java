package com.example.sensors;
// Import packages
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
public class MotionService extends Service{

    @Override
    public void onCreate() {
        super.onCreate();

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
