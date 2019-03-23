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
import edu.example.ssf.mma.hardwareAdapter.HardwareFactory;

public class FischFangMain extends AppCompatActivity {

    private TextView tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8;
    private Button btn1;
    boolean isButtonReleased = false, isButtonPressed = false;
    private float nullpointAccelX;
    private double nullpointVectorA;

    SensorManager sensorManager;
    Sensor sensor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fisch_fang_main);

        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
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
                    tv2.setText("Pressed");
                    isButtonPressed = true;
                    isButtonReleased = false;
                    HardwareFactory.hwAcc.start();
                    HardwareFactory.hwGyro.start();
                    nullpointAccelX = HardwareFactory.hwAcc.getAccX();
                    nullpointVectorA = HardwareFactory.hwAcc.getAccA();
                    CurrentTickData.rotationX = HardwareFactory.hwGyro.getRotX();
                    CurrentTickData.accVecA = HardwareFactory.hwAcc.getAccA();;
                    CurrentTickData.accX = HardwareFactory.hwAcc.getAccX();


                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    HardwareFactory.hwAcc.stop();
                    HardwareFactory.hwGyro.stop();
                    isButtonReleased = true;
                    isButtonPressed = false;
                    tv2.setText("Released");

                    tv3.setText("Meter" + String.format("%.2f",((CurrentTickData.accVecA - 10)/3)));
                    tv4.setText("Angle: " + String.format("%.2f", CurrentTickData.angleX));
                    tv5.setText("Force: " + String.format("%.2f", (CurrentTickData.accVecA)));
                    tv6.setVisibility(View.INVISIBLE);

                    tv7.setVisibility(View.INVISIBLE);

                    tv8.setVisibility(View.INVISIBLE);
                }
                return true;
            }
        });
    }
}
