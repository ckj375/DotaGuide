package com.ckj.dotaguide.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ckj.dotaguide.R;
import com.ckj.dotaguide.adapter.EquipmentGoodsAdapter;
import com.ckj.dotaguide.server.HttpClientGenerator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EquipmentInfoActivity extends Activity {

    private Context mContext;
    private int type;
    private String itemid;
    private String path;

    private ImageView mPortraitView;
    private TextView mNameTextView;
    private TextView mPriceTextView;
    private TextView mDescTextView;

    private View mRequestView;
    private RecyclerView mRequestRecyclerView;
    private ArrayList mRequestGoodsDatas;
    private EquipmentGoodsAdapter mRequestGoodsAdapter;

    private View mProvideView;
    private RecyclerView mProvideRecyclerView;
    private ArrayList mProvideGoodsDatas;
    private EquipmentGoodsAdapter mProvideGoodsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment_info);

        mContext = this;
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 1);
        if (type == 1) {
            itemid = intent.getStringExtra("itemid");
            Log.v("ckjc", "itemid=" + itemid);
        } else {
            path = intent.getStringExtra("path");
        }

        // 物品基本信息
        mNameTextView = (TextView) findViewById(R.id.name);
        mPortraitView = (ImageView) findViewById(R.id.img);
        mPriceTextView = (TextView) findViewById(R.id.price);
        mDescTextView = (TextView) findViewById(R.id.desc_textview);

        mRequestView = findViewById(R.id.request_goods_view);
        mRequestRecyclerView = (RecyclerView) findViewById(R.id.request_recyclerview);
        LinearLayoutManager ll = new LinearLayoutManager(mContext);
        ll.setOrientation(LinearLayoutManager.VERTICAL);
        mRequestRecyclerView.setLayoutManager(ll);
        mRequestGoodsDatas = new ArrayList();
        mRequestGoodsAdapter = new EquipmentGoodsAdapter(mContext, mRequestGoodsDatas);
        mRequestRecyclerView.setAdapter(mRequestGoodsAdapter);
        mRequestGoodsAdapter.setOnItemClickListener(new EquipmentGoodsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                showEquipmentInfo(mRequestGoodsDatas, position);
            }
        });

        mProvideView = findViewById(R.id.provide_goods_view);
        mProvideRecyclerView = (RecyclerView) findViewById(R.id.provide_recyclerview);
        LinearLayoutManager ll2 = new LinearLayoutManager(mContext);
        ll2.setOrientation(LinearLayoutManager.VERTICAL);
        mProvideRecyclerView.setLayoutManager(ll2);
        mProvideGoodsDatas = new ArrayList();
        mProvideGoodsAdapter = new EquipmentGoodsAdapter(mContext, mProvideGoodsDatas);
        mProvideRecyclerView.setAdapter(mProvideGoodsAdapter);
        mProvideGoodsAdapter.setOnItemClickListener(new EquipmentGoodsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                showEquipmentInfo(mProvideGoodsDatas, position);
            }
        });

        getItem(type);
    }

    private void getItem(int type) {
        rx.Observable<ResponseBody> observable;
        if (type == 1) {
            String url = "http://dotadb.uuu9.com/items_index.aspx";
            observable = HttpClientGenerator.getHttpClientService().getItem(url, itemid);
        } else {
            String url = "http://db.dota.uuu9.com" + path;
            observable = HttpClientGenerator.getHttpClientService().getItem(url);
        }
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            String html = new String(responseBody.bytes(), "utf-8");
                            Document doc = Jsoup.parse(html);
                            Element bodyElement = doc.getElementsByClass("w719 r").first();
                            Element nameElement = bodyElement.getElementsByClass("picbox l").first();
                            String name = nameElement.getElementsByTag("img").attr("title");
                            String src = nameElement.getElementsByTag("img").attr("src");
                            Log.v("ckjc", "name=" + name + " src=" + src);
                            Glide.with(mContext).load(src).placeholder(R.drawable.default_image_cover_gray).into(mPortraitView);
                            mNameTextView.setText(name);

                            Element infoElement = bodyElement.getElementsByClass("textbox r").first();
                            Element goldElement = infoElement.getElementsByClass("gold").first();
                            mPriceTextView.setText("价格:" + goldElement.text());
                            Element descElement = infoElement.getElementsByTag("p").get(1);
                            String desc = descElement.ownText();
                            Elements links = descElement.getElementsByTag("span");
                            for (Element link : links) {
                                String item = link.text();
                                desc = desc + "\n" + item;
                            }
                            Log.v("ckjc", "mDescList=" + desc);
                            mDescTextView.setText(desc);

                            // 合成配方
                            Element requestGoodsElement = bodyElement.getElementsByClass("content jianbg cl").first();
                            Elements requestGoods = requestGoodsElement.getElementsByTag("li");
                            if (requestGoods != null) {
                                for (Element link : requestGoods) {
                                    String goodsSrc = link.getElementsByTag("img").attr("src");
                                    String goodsHref = link.getElementsByTag("a").attr("href");
                                    String goodsName = link.getElementsByTag("a").get(1).text();
                                    String goodsGold = link.getElementsByClass("gold").text();
                                    HashMap map = new HashMap();
                                    map.put("src", goodsSrc);
                                    map.put("href", goodsHref);
                                    map.put("name", goodsName);
                                    map.put("gold", goodsGold);
                                    mRequestGoodsDatas.add(map);
                                }
                            }
                            if (mRequestGoodsDatas.size() != 0) {
                                mRequestView.setVisibility(View.VISIBLE);
                                mProvideGoodsAdapter.notifyDataSetChanged();
                            }

                            // 可升级为
                            Element provideGoodsElement = bodyElement.getElementsByClass("content jianbg cl").get(1);
                            Elements provideGoods = provideGoodsElement.getElementsByTag("li");
                            if (provideGoods != null) {
                                for (Element link : provideGoods) {
                                    String goodsSrc = link.getElementsByTag("img").attr("src");
                                    String goodsHref = link.getElementsByTag("a").attr("href");
                                    String goodsName = link.getElementsByTag("a").get(1).text();
                                    String goodsGold = link.getElementsByClass("gold").text();
                                    HashMap map = new HashMap();
                                    map.put("src", goodsSrc);
                                    map.put("href", goodsHref);
                                    map.put("name", goodsName);
                                    map.put("gold", goodsGold);
                                    mProvideGoodsDatas.add(map);
                                }
                            }
                            if (mProvideGoodsDatas.size() != 0) {
                                mProvideView.setVisibility(View.VISIBLE);
                                mProvideGoodsAdapter.notifyDataSetChanged();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.v("ckjc", "failure");
                        e.printStackTrace();
                    }
                });

    }

    /**
     * 跳转进入物品详情页面
     */
    private void showEquipmentInfo(ArrayList datas, int position) {
        String path = ((HashMap) datas.get(position)).get("href").toString();
        Intent intent = new Intent();
        intent.putExtra("type", 2);
        intent.putExtra("path", path);
        intent.setClass(mContext, EquipmentInfoActivity.class);
        startActivity(intent);
    }

}
