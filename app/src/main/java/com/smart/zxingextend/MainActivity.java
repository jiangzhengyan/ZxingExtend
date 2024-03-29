package com.smart.zxingextend;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private QrImageResultPointView qr_point;
    private RelativeLayout rl_root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        qr_point = findViewById(R.id.qr_point);
        rl_root = findViewById(R.id.rl_root);
        rl_root.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                qr_point.decodeQrcode(rl_root);
                return true;
            }
        });
        qr_point.setOnResultPointClickListener(new QrImageResultPointView.OnResultPointClickListener() {
            @Override
            public void onPointClick(String result) {
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancle() {

            }
        });
    }
}