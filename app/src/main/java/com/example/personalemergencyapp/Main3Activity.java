package com.example.personalemergencyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.util.Objects;


public class Main3Activity extends AppCompatActivity {

    TextView messageTextview;

    private SensorManager mSensorManager;
    private float mAbel;
    private float mAbdelCurrent;
    private float mAbdelLast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Objects.requireNonNull(mSensorManager).registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        mAbel = 10f;
        mAbdelCurrent = SensorManager.GRAVITY_EARTH;
        mAbdelLast = SensorManager.GRAVITY_EARTH;

       // String message = getIntent().getStringExtra("message key");
       // messageTextview = (TextView)findViewById(R.id.edittext);
       // messageTextview.setText(message);
    }

    private final SensorEventListener mSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            mAbdelLast = mAbdelCurrent;
            mAbdelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAbdelCurrent - mAbdelLast;
            mAbel = mAbel * 0.9f + delta;
            if (mAbel > 12) {
                // Toast.makeText(getApplicationContext(), "Shake event detected", Toast.LENGTH_SHORT).show();

                Intent intent1 = new Intent(Main3Activity.this, MapsActivity.class);
                startActivity(intent1);

                String message = getIntent().getStringExtra("message key");
                messageTextview = (TextView)findViewById(R.id.edittext);
                messageTextview.setText(message);
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + message));

                if(ActivityCompat.checkSelfPermission(Main3Activity.this,Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }


               startActivity(intent);
                if(true)
                {


                    int permissionCheck = ContextCompat.checkSelfPermission(Main3Activity.this, Manifest.permission.SEND_SMS);
                    if (permissionCheck == PackageManager.PERMISSION_GRANTED) {


                        messageTextview = (TextView)findViewById(R.id.edittext);
                        messageTextview.setText(message);
                        String Message = "EMERGENCY : ALERT - Please HELP ME....";

                        SmsManager smsManager = SmsManager.getDefault();

                        smsManager.sendTextMessage(message, null, Message, null, null);

                        Toast.makeText(Main3Activity.this, "Message Sent.", Toast.LENGTH_SHORT).show();
                    } else {
                        ActivityCompat.requestPermissions(Main3Activity.this, new String[]{Manifest.permission.SEND_SMS}, 0);
                    }
                }


                //Intent intent1 = new Intent(MainActivity.this, Main2Activity.class);
                // startActivity(intent1);

            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    @Override
    protected void onResume() {
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }
    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }
}
