package com.ckj.dotaraiders.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ckj.dotaraiders.R;
import com.ckj.dotaraiders.server.HttpClientGenerator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv1 = (TextView) findViewById(R.id.textView1);
        tv2 = (TextView) findViewById(R.id.textView2);
        tv3 = (TextView) findViewById(R.id.textView3);
        tv4 = (TextView) findViewById(R.id.textView4);

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

        tv4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, EquipmentInfoActivity.class);
                startActivity(intent);
            }
        });
    }

}
