package com.ckj.dotaguide.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ckj.dotaguide.R;

public class MainActivity extends AppCompatActivity {

    private View tv1;
    private View tv2;
    private View tv3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv1 = findViewById(R.id.catalog1);
        tv2 = findViewById(R.id.catalog2);
        tv3 = findViewById(R.id.catalog3);

        tv1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("type", 0);
                intent.setClass(MainActivity.this, HerosActivity.class);
                startActivity(intent);
            }
        });

        tv2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("type", 1);
                intent.setClass(MainActivity.this, HerosActivity.class);
                startActivity(intent);
            }
        });

        tv3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("type", 2);
                intent.setClass(MainActivity.this, HerosActivity.class);
                startActivity(intent);
            }
        });

    }

}
