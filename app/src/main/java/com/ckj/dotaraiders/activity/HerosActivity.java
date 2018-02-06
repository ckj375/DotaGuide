package com.ckj.dotaraiders.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.ckj.dotaraiders.CustomGridView;
import com.ckj.dotaraiders.R;
import com.ckj.dotaraiders.adapter.HerosAdapter;
import com.ckj.dotaraiders.server.HttpClientGenerator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by chenkaijian on 17-8-24.
 */

public class HerosActivity extends Activity {

    /**
     * hero type
     */
    private int type;
    private CustomGridView mHerosGridView;
    private HerosAdapter mHerosAdapter;
    private ArrayList datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heros);

        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);

        mHerosGridView = (CustomGridView) findViewById(R.id.heros_gridview);
        datas = new ArrayList();
        mHerosAdapter = new HerosAdapter(this, datas);
        mHerosGridView.setAdapter(mHerosAdapter);
        mHerosGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.putExtra("heroId", ((HashMap) datas.get(i)).get("heroId").toString());
                intent.setClass(HerosActivity.this, HeroInfoActivity.class);
                startActivity(intent);
            }
        });

        getHeros();
    }

    private void getHeros() {
        HttpClientGenerator.getHttpClientService().getHeros()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            String html = new String(responseBody.bytes(), "GB2312");
                            Document doc = Jsoup.parse(html);
                            Element bodyElement = doc.getElementsByClass("l hero_box").first();
                            Element powerElement = bodyElement.getElementsByClass("cl con picbox").get(type);
                            Elements links = powerElement.getElementsByTag("li");

                            for (Element link : links) {
                                String img = link.getElementsByTag("img").attr("src");
                                String name = link.text();
                                String href = link.getElementsByTag("a").attr("href");
                                String heroId = href.substring(26, href.length() - 1);
                                HashMap map = new HashMap();
                                map.put("img", img);
                                map.put("name", name);
                                map.put("heroId", heroId);
                                datas.add(map);
                            }
                            mHerosAdapter.notifyDataSetChanged();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.v("ckjc", "getHeros() failure");
                        e.printStackTrace();
                    }
                });

    }

}
