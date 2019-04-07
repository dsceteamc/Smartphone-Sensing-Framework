package edu.example.ssf.mma.userInterface;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.example.ssf.mma.R;
import edu.example.ssf.mma.data.CsvFileWriter;
import edu.example.ssf.mma.data.CurrentTickData;
import edu.example.ssf.mma.hardwareAdapter.HardwareFactory;

public class FischFangMain extends AppCompatActivity {
    private double height = 2;
    private double correctionConstant = 1;
    private double gravityConstant = 9.81;
    private TextView tvSpeed, tvAngle, tvDistance, tv3, tv4, tv5, tv6, tv7, tv8;
    private Button btn1;
    boolean isButtonReleased = false, isButtonPressed = false, isRecordingStarted = false;

    List<Double> accVecAValues, accVecAValuesMax;
    List<Float> accXValues, angleXValues;
    List<Float> accXValuesMax, angleXValuesMax;

    SensorManager sensorManager;
    Sensor sensor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fisch_fang_main);

        accVecAValues = Collections.synchronizedList(new ArrayList());
        accXValues = Collections.synchronizedList(new ArrayList());
        angleXValues = Collections.synchronizedList(new ArrayList());

        accVecAValuesMax = new ArrayList<Double>();
        accXValuesMax = new ArrayList<Float>();
        angleXValuesMax = new ArrayList<Float>();

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


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRecordingStarted) {
                    btn1.setText("Click to Stop recording");
                    isRecordingStarted = true;

                    thread.start();
                } else {
                    btn1.setText("Click to Start recording");
                    isRecordingStarted = false;

                    HardwareFactory.hwAcc.stop();
                    HardwareFactory.hwGyro.stop();

                   // tvSpeed.setText(String.format("%.2f", Collections.max(accVecAValues)));

                    synchronized (accXValues) {
                        Log.e("Acc x Length", accXValues.size() + "");
                        if (accXValues.size() > 0)
                            accXValuesMax.add(Collections.max(accXValues));
                    }
                    synchronized (angleXValues) {
                        Log.e("Acc angle Length", angleXValues.size() + "");
                        if (angleXValues.size() > 0)
                            angleXValuesMax.add(Collections.max(angleXValues));
                    }
                    synchronized (accVecAValues) {
                        Log.e("Acc VecA Length", accVecAValues.size() + "");
                        if (accVecAValues.size() > 0)
                            accVecAValuesMax.add(Collections.max(accVecAValues));
                    }

                    accXValues.clear();
                    angleXValues.clear();
                    accVecAValues.clear();
                    output();
                }
            }
        });


        /*btn1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    tv8.setText("Pressed");
                    isButtonPressed = true;
                    isButtonReleased = false;
                    HardwareFactory.hwAcc.start();
                    HardwareFactory.hwGyro.start();
                    CurrentTickData.rotationX = HardwareFactory.hwGyro.getRotX();
                    CurrentTickData.accVecA = HardwareFactory.hwAcc.getAccA();;
                    CurrentTickData.accX = HardwareFactory.hwAcc.getAccX();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    isButtonReleased = true;
                    isButtonPressed = false;
                    HardwareFactory.hwAcc.stop();
                    HardwareFactory.hwGyro.stop();
                    tv8.setText("Released");

                    tvDistance.setText(String.format("%.2f", ((CurrentTickData.accVecA - 10))/3));
                    tvAngle.setText(String.format("%.2f", CurrentTickData.angleXValues));
                    tvSpeed.setText(String.format("%.2f", (CurrentTickData.accVecA-10)*2) + "km/h");
                }
                return true;
            }
        });
        */
    }

    Thread thread = new Thread() {
        @Override
        public void run() {
            try {
                while (true) {
                    sleep(1);

                    if (isRecordingStarted) {
                        HardwareFactory.hwAcc.start();
                        HardwareFactory.hwGyro.start();

                        CurrentTickData.rotationX = HardwareFactory.hwGyro.getRotX();
                        CurrentTickData.accVecA = HardwareFactory.hwAcc.getAccA();
                        CurrentTickData.accX = HardwareFactory.hwAcc.getAccX();

                        synchronized (angleXValues) {
                            angleXValues.add((CurrentTickData.rotationX));
                        }
                        synchronized (accVecAValues) {
                            accVecAValues.add(CurrentTickData.accVecA);
                        }
                        synchronized (accXValues
                        ) {
                            accXValues.add(CurrentTickData.accX);
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onStop() {
        super.onStop();

        CsvFileWriter.crtFile();
        int counter = 0;
        for (Float val : accXValuesMax) {
            CsvFileWriter.write(++counter + " accXvalues Max: " + val + "\n");
        }
        counter = 0;
        for (Float val : angleXValuesMax) {
                   CsvFileWriter.write(++counter + " angleXValuesMax Max: " + val + "\n");

        }
        counter = 0;
        for (Double val : accVecAValuesMax) {
                       CsvFileWriter.write(++counter + " accVecAValuesMax Max: " + val + "\n");
                }
        counter = 0;
        double speed2 = Collections.max(accXValuesMax);
        if (Collections.max(angleXValuesMax) > 2.0f) {

        } else {
            double distance = horizontalerWurf(speed2);
            CsvFileWriter.write(++counter + " distance: " + distance + "\n");
        }
        CsvFileWriter.closeFile();
          }


    public void output() {
        double speed = Collections.max(accVecAValuesMax);
        double speed2 = Collections.max(accXValuesMax);
        double angle = Collections.max(angleXValuesMax);
        tvAngle.setText(String.format("%.2f", angle));
        Log.e("angle", angle + "");

        tvSpeed.setText(String.format("%.2f", (speed2)));
        Log.e("speed", speed + "");
        Log.e("speed2", speed2 + "");

        if (angle > 2) {
            tvDistance.setText(String.format("Faulty Throw"));
            Log.e("Distance", "Faulty Throw, Angle: " + Collections.max(angleXValuesMax));
        } else {
            horizontalerWurf(speed2);
            //double distance2 =schieferWurf(speed2, angle); // does not work
            }
    }

    public double horizontalerWurf(double speed){
        double distance =  speed * Math.sqrt(2*height/gravityConstant)* correctionConstant;
        tvDistance.setText(String.format("%.2f", (distance)));
        Log.e("Distance", distance + "");
        return distance;
    }
    public double schieferWurf(double speed, double angle){
        double distance =  ((speed*speed) *Math.sin(2*angle))/gravityConstant;
        tvDistance.setText(String.format("%.2f", (distance)));
        Log.e("Distance", distance + "");
        return distance;
    }
   }
