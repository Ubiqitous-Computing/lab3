
package com.example.sensors;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MotionDetailsActivity extends AppCompatActivity {

    private TextView motionTimeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_motion_details);

        motionTimeText = findViewById(R.id.motionTimeText);

        String time = getIntent().getStringExtra("motion_time");

        motionTimeText.setText("Phone moved at: " + time);
    }
}
