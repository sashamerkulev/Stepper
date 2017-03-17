package ru.merkulyevsasha.stepper;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StepperActivity extends AppCompatActivity implements SensorEventListener{


    private TextView mTextViewSteps;
    private int mCounter;

    private SensorManager mSensorManager;
    private Sensor mSensor;

    private Settings mSettings;

    private float[] gravity;
    final float alpha = 0.9F;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTextViewSteps = (TextView) findViewById(R.id.steps);
        TextView label = (TextView) findViewById(R.id.label_step);
        Button clear = (Button)findViewById(R.id.clear_button);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){
            mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

            gravity = new float[3];

            mSettings = new Settings(this);

            clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCounter = 0;
                    mSettings.saveSteps(mCounter);
                    showValue();
                }
            });
        }
        else{
            mSensorManager = null;
            mTextViewSteps.setVisibility(View.GONE);
            clear.setVisibility(View.GONE);
            label.setText(R.string.no_accelerometer_message);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        mCounter = mSettings.getSteps();;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSettings.saveSteps(mCounter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSensorManager != null) {
            mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
            showValue();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

            float linear_acceleration_x = event.values[0] - gravity[0];
            float linear_acceleration_y = event.values[1] - gravity[1];
            float linear_acceleration_z = event.values[2] - gravity[2];

            if(linear_acceleration_x > 3F || linear_acceleration_y > 3F || linear_acceleration_z > 3F){
                mCounter++;
                showValue();
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void showValue(){
        mTextViewSteps.setText(String.valueOf(mCounter));
    }

}
