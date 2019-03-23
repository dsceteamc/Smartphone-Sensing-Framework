package edu.example.ssf.mma.userInterface;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.example.ssf.mma.R;
import edu.example.ssf.mma.data.CurrentTickData;

public class FischFangMain extends AppCompatActivity {

    private TextView tvSpeed, tvAngle, tvDistance, tv3, tv4, tv5, tv6, tv7, tv8;
    private Button btn1;
    boolean isButtonReleased = false, isButtonPressed = false;

    SensorManager sensorManager;
    Sensor sensor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fisch_fang_main);

        tvSpeed = findViewById(R.id.tvSpeed);
        tvAngle = findViewById(R.id.tvAngle);
        tvDistance = findViewById(R.id.tvDistance);

        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        tv5 = findViewById(R.id.tv5);
        tv6 = findViewById(R.id.tv6);
        tv7 = findViewById(R.id.tv7);
        tv8 = findViewById(R.id.tv8);
        btn1 = findViewById(R.id.btn1);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);


        btn1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    tv8.setText("Pressed");
                    isButtonPressed = true;
                    isButtonReleased = false;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    isButtonReleased = true;
                    isButtonPressed = false;
                    tv8.setText("Released");

                    tv3.setText("X: " + String.format("%.2f", CurrentTickData.accX));
                    tv4.setText("Y: " + String.format("%.2f", CurrentTickData.accY));
                    tv5.setText("Z: " + String.format("%.2f", CurrentTickData.accZ));
                    tv6.setText("AccV: " + String.format("%.2f", CurrentTickData.accVecA));
                    tvAngle.setText(String.format("%.2f", CurrentTickData.angleX));
                }
                return true;
            }
        });
    }
}
