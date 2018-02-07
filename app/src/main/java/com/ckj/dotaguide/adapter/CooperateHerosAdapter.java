package com.ckj.dotaguide.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ckj.dotaguide.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by chenkaijian on 17-9-12.
 */

public class CooperateHerosAdapter extends RecyclerView.Adapter<CooperateHerosAdapter.MyViewHolder> {

    private Context mContext;
    private String heroId;
    private ArrayList datas;

    public CooperateHerosAdapter(Context mContext, String heroId, ArrayList datas) {
        this.mContext = mContext;
        this.heroId = heroId;
        this.datas = datas;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.cooperate_hero_list_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String src = ((HashMap) datas.get(position)).get("src").toString();
        String name = ((HashMap) datas.get(position)).get("name").toString();
        if (!src.startsWith("http")) {
            src = "http://dota.uuu9.com/hero/" + heroId + "/" + src;
        }
        Glide.with(mContext).load(src).placeholder(R.drawable.default_image_cover_gray).into(holder.img);
        holder.name.setText(name);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView img;
        public TextView name;

        public MyViewHolder(View view) {
            super(view);
            img = (ImageView) view.findViewById(R.id.img);
            name = (TextView) view.findViewById(R.id.name);
        }
    }

}
