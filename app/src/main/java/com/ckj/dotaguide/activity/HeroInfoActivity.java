package com.ckj.dotaguide.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ckj.dotaguide.adapter.CooperateHerosAdapter;
import com.ckj.dotaguide.adapter.EquipmentsAdapter;
import com.ckj.dotaguide.adapter.SkillOrderAdapter;
import com.ckj.dotaguide.adapter.SkillsAdapter;
import com.ckj.dotaguide.R;
import com.ckj.dotaguide.server.HttpClientGenerator;
import com.ckj.dotaguide.util.RotateTextView;

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

public class HeroInfoActivity extends Activity {

    private Context mContext = null;
    private String heroId;

    private TextView mTitleView;
    private ImageView mPortraitView;
    private TextView mHPView, mMPView;
    private TextView mAttrView1, mAttrView2, mAttrView3, mAttrView4, mAttrView5, mAttrView6;
    private TextView mCooperateBtn, mRestraintBtn;
    private GridView mSkillOrderGridView;
    private TextView mSkillOrderDescView;
    private RecyclerView mSkillRecyclerView;

    // 配合英雄和克制英雄
    private ArrayList mCooperateList, mRestraintList;
    private CooperateHerosAdapter adapter;
    // 技能加点顺序
    private String skillOrderDesc;
    private SkillOrderAdapter mSkillOrderAdapter;
    private ArrayList skillOrder;
    // 推荐出装
    private RecyclerView mEquipmentRecyclerView;
    private ArrayList<HashMap> mEquipmentDatas;
    private EquipmentsAdapter mEquipmentsAdapter;
    // 技能介绍
    private SkillsAdapter mSkillsAdapter;
    private ArrayList skills;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero_info);

        mContext = this;

        Intent intent = getIntent();
        heroId = intent.getStringExtra("heroId");
        Log.v("ckjc", "heroId=" + heroId);

        // 基础属性
        mTitleView = (TextView) findViewById(R.id.title);
        mPortraitView = (ImageView) findViewById(R.id.img);
        mHPView = (TextView) findViewById(R.id.hp);
        mMPView = (TextView) findViewById(R.id.mp);
        mAttrView1 = (TextView) findViewById(R.id.attr1);
        mAttrView2 = (TextView) findViewById(R.id.attr2);
        mAttrView3 = (TextView) findViewById(R.id.attr3);
        mAttrView4 = (TextView) findViewById(R.id.attr4);
        mAttrView5 = (TextView) findViewById(R.id.attr5);
        mAttrView6 = (TextView) findViewById(R.id.attr6);

        // 配合英雄和克制英雄
        mCooperateBtn = (TextView) findViewById(R.id.cooperateBtn);
        mRestraintBtn = (TextView) findViewById(R.id.restraintBtn);
        mCooperateList = new ArrayList();
        mRestraintList = new ArrayList();
        mCooperateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCooperatePopup("配合英雄", mCooperateList);
            }
        });
        mRestraintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCooperatePopup("克制英雄", mRestraintList);
            }
        });

        // 加点顺序
        mSkillOrderGridView = (GridView) findViewById(R.id.skill_order_grid);
        mSkillOrderDescView = (TextView) findViewById(R.id.skillOrderDesc);
        skillOrder = new ArrayList();
        mSkillOrderAdapter = new SkillOrderAdapter(mContext, heroId, skillOrder);
        mSkillOrderGridView.setAdapter(mSkillOrderAdapter);

        // 推荐出装
        mEquipmentRecyclerView = (RecyclerView) findViewById(R.id.equipments_recyclerview);
        LinearLayoutManager ll2 = new LinearLayoutManager(HeroInfoActivity.this);
        ll2.setOrientation(LinearLayoutManager.VERTICAL);
        mEquipmentRecyclerView.setLayoutManager(ll2);
        mEquipmentDatas = new ArrayList();
        mEquipmentsAdapter = new EquipmentsAdapter(this, mEquipmentDatas);
        mEquipmentRecyclerView.setAdapter(mEquipmentsAdapter);

        // 技能介绍
        mSkillRecyclerView = (RecyclerView) findViewById(R.id.skill_recyclerview);
        LinearLayoutManager ll = new LinearLayoutManager(HeroInfoActivity.this);
        ll.setOrientation(LinearLayoutManager.VERTICAL);
        mSkillRecyclerView.setLayoutManager(ll);
        skills = new ArrayList();
        mSkillsAdapter = new SkillsAdapter(mContext, heroId, skills);
        mSkillRecyclerView.setAdapter(mSkillsAdapter);
        mSkillsAdapter.setOnItemClickListener(new SkillsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                showSkillDetailDialog(position);
            }
        });

        getHeroInfo();
    }

    private void getHeroInfo() {
        HttpClientGenerator.getHttpClientService().getHero(heroId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            String html = new String(responseBody.bytes(), "GB2312");
                            Document doc = Jsoup.parse(html);

                            // 英雄信息
                            Element bodyElement = doc.getElementsByClass("l zhiye_jieshao").first();
                            String portraitUrl = bodyElement.getElementsByTag("img").first().attr("src");
                            String name = bodyElement.getElementsByTag("h4").first().text();
                            String nickname = bodyElement.getElementsByTag("dd").first().ownText().split(" ")[0];
                            String abbreviation = bodyElement.getElementsByTag("em").first().text();
                            Log.v("ckjc", "name=" + name + " nickname=" + nickname + " abbreviation=" + abbreviation + "  portraitUrl=" + portraitUrl);
                            mTitleView.setText(name + "(" + abbreviation + ")");
                            if (!portraitUrl.startsWith("http")) {
                                portraitUrl = "http://dota.uuu9.com/hero/" + heroId + "/" + portraitUrl;
                            }
                            Glide.with(mContext).load(portraitUrl).placeholder(R.drawable.default_image_cover_gray).into(mPortraitView);

                            // 血量和魔量
                            Element capacityElement = doc.getElementsByClass("cl capacity").first();
                            String hp = capacityElement.getElementsByTag("dd").first().text();
                            String mp = capacityElement.getElementsByTag("dd").get(1).text();

                            mHPView.setText(hp);
                            mMPView.setText(mp);

                            // 基础属性
                            Element table = doc.getElementsByTag("table").first();
                            String attr1 = table.getElementsByTag("td").first().text();
                            String attr2 = table.getElementsByTag("td").get(1).text();
                            String attr3 = table.getElementsByTag("td").get(2).text();
                            String attr4 = table.getElementsByTag("td").get(3).text();
                            String attr5 = table.getElementsByTag("td").get(4).text();
                            String attr6 = table.getElementsByTag("td").get(5).text();
                            mAttrView1.setText(attr1);
                            mAttrView2.setText(attr2);
                            mAttrView3.setText(attr3);
                            mAttrView4.setText(attr4);
                            mAttrView5.setText(attr5);
                            mAttrView6.setText(attr6);

                            // 配合及克制英雄
                            Element cooperateElement = bodyElement.getElementsByClass("cl cooperate_box").first();
                            Elements cooperateItems = cooperateElement.getElementsByTag("a");
                            for (Element link : cooperateItems) {
                                HashMap map = new HashMap();
                                String cooperateSrc = link.getElementsByTag("img").attr("src");
                                String cooperateName = link.text();
                                map.put("src", cooperateSrc);
                                map.put("name", cooperateName);
                                mCooperateList.add(map);
                            }

                            Element restraintElement = bodyElement.getElementsByClass("cl cooperate_box").get(1);
                            Elements restraintItems = restraintElement.getElementsByTag("a");
                            for (Element link : restraintItems) {
                                String restraintSrc = link.getElementsByTag("img").attr("src");
                                String restraintName = link.text();
                                HashMap map = new HashMap();
                                map.put("src", restraintSrc);
                                map.put("name", restraintName);
                                mRestraintList.add(map);
                            }

                            // 技能加点顺序
                            Element skillOrderElement = doc.getElementsByClass("cl chuzhuang").first();
                            Element firstSkillOrderElement = skillOrderElement.getElementsByClass("cl lpicbox").first();
                            Elements links = firstSkillOrderElement.getElementsByTag("img");
                            for (Element link : links) {
                                String title = link.attr("title");
                                String src = link.attr("src");
                                skillOrder.add(src);
                            }
                            mSkillOrderAdapter.notifyDataSetChanged();

                            skillOrderDesc = skillOrderElement.getElementsByClass("cgcon").first().text();
                            mSkillOrderDescView.setText(skillOrderDesc);

                            // 推荐出装
                            Element equipmentElement = doc.getElementsByClass("cl m-10 chuzhuang").first();
                            Elements titleElements = equipmentElement.getElementsByTag("strong");
                            Elements goodsElements = equipmentElement.getElementsByClass("cl lpicbox");
                            Elements descElements = equipmentElement.getElementsByClass("cgcon");
                            for (int i = 0; i < titleElements.size(); i++) {
                                HashMap hashMap = new HashMap();
                                String title = titleElements.get(i).text();
                                ArrayList goodsList = new ArrayList();
                                Elements goods = goodsElements.get(i).getElementsByTag("li");
                                for (Element good : goods) {
                                    String itemid = "";
                                    String href = good.getElementsByTag("a").attr("href");
                                    // http://dotadb.uuu9.com/items_index.aspx?itemid=reja
                                    // http://db.dota.uuu9.com/goods/show/SoulRing
                                    if (href.contains("=")) {
                                        itemid = href.split("=")[1];
                                    }
//                                    else if (!href.equals("")) {
//                                        itemid = href.substring(45).split("\\.")[0];
//                                    }
                                    String src = good.getElementsByTag("img").attr("src");
                                    HashMap map = new HashMap();
                                    map.put("itemid", itemid);
                                    map.put("src", src);
                                    if (src.endsWith("bmp")) {
                                        continue;
                                    }
                                    goodsList.add(map);
                                }
                                String desc = descElements.get(i).text();

                                hashMap.put("title", title);
                                hashMap.put("goodsList", goodsList);
                                hashMap.put("desc", desc);
                                mEquipmentDatas.add(hashMap);
                            }

                            mEquipmentsAdapter.setAbbreviation(abbreviation);
                            mEquipmentsAdapter.notifyDataSetChanged();

                            // 技能介绍
                            Elements skillElements = doc.getElementsByClass("spell-body");
                            for (Element skillElement : skillElements) {
                                String img = skillElement.getElementsByTag("img").attr("src");
                                String skillName = skillElement.getElementsByTag("img").attr("alt");
                                String skillAttr = skillElement.getElementsByClass("spellicon").text();
                                String hotKey = skillElement.getElementsByClass("hotkey").text();
                                StringBuilder desc = new StringBuilder();
                                HashMap map = new HashMap();
                                Elements elements = skillElement.getElementsByTag("p");
                                for (int i = 1; i < elements.size(); i++) {
                                    desc.append(elements.get(i).text() + "\n");
                                }
                                map.put("img", img);
                                map.put("name", skillName);
                                map.put("attr", skillAttr);
                                map.put("hotkey", hotKey);
                                map.put("desc", desc);
                                Log.v("ckjc", "img=" + img + " skillName=" + skillName + " skillAttr=" + skillAttr + " hotKey=" + hotKey);
                                skills.add(map);
                            }
                            mSkillsAdapter.notifyDataSetChanged();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.v("ckjc", "getHero failure");
                        e.printStackTrace();
                    }
                });

    }

    /**
     * 显示配合克制英雄弹框
     */
    private void showCooperatePopup(String title, ArrayList datas) {
        View v = getLayoutInflater().inflate(R.layout.cooperate_hero_dialog, null);
        TextView mTitleView = (TextView) v.findViewById(R.id.title);
        mTitleView.setText(title);
        RecyclerView mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        LinearLayoutManager ll = new LinearLayoutManager(HeroInfoActivity.this);
        ll.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(ll);
        adapter = new CooperateHerosAdapter(mContext, heroId, datas);
        mRecyclerView.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setView(v);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * 显示技能详细信息
     */
    private void showSkillDetailDialog(int position) {
        String src = (((HashMap) skills.get(position)).get("img")).toString();
        if (!src.startsWith("http")) {
            src = "http://dota.uuu9.com/hero/" + heroId + "/" + src;
        }
        String name = ((HashMap) skills.get(position)).get("name").toString();
        String attr = ((HashMap) skills.get(position)).get("attr").toString();
        String hotkey = ((HashMap) skills.get(position)).get("hotkey").toString();
        String desc = ((HashMap) skills.get(position)).get("desc").toString();

        View v = getLayoutInflater().inflate(R.layout.skill_detail_dialog, null);
        ImageView img = (ImageView) v.findViewById(R.id.img);
        RotateTextView mAttrTV = (RotateTextView) v.findViewById(R.id.attr);
        TextView mNameTV = (TextView) v.findViewById(R.id.name);
        TextView mHotkeyTV = (TextView) v.findViewById(R.id.hot_key);
        TextView mDescTV = (TextView) v.findViewById(R.id.desc);
        Glide.with(mContext).load(src).placeholder(R.drawable.default_image_cover_gray).into(img);
        mAttrTV.setText(attr);
        mNameTV.setText(name);
        mHotkeyTV.setText(hotkey);
        mDescTV.setText(desc);

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setView(v);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
