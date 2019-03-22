package edu.example.ssf.mma.userInterface;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.example.ssf.mma.R;

public class FischFangMain extends AppCompatActivity {

    private TextView tv1, tv2;
    private Button btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fisch_fang_main);

        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        btn1 = findViewById(R.id.btn1);


        btn1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    tv2.setText("Pressed");
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    tv2.setText("Released");
                    tv1.setText("Test");
                }
                return true;
            }
        });
    }
}
