package com.ckj.dotaguide.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ckj.dotaguide.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by chenkaijian on 17-8-24.
 */

public class HerosAdapter extends BaseAdapter {
    private Context context;
    private ArrayList datas;

    public HerosAdapter(Context context, ArrayList datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HerosAdapter.ViewHolder holder;
        if (convertView == null) {
            holder = new HerosAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.hero_grid_item, null);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        } else {
            holder = (HerosAdapter.ViewHolder) convertView.getTag();
        }

        String img = ((HashMap) datas.get(position)).get("img").toString();
        if (!img.startsWith("http")) {
            img = "http://dota.uuu9.com/hero/" + img;
        }

        Glide.with(context).load(img).into(holder.img);
        holder.name.setText(((HashMap) datas.get(position)).get("name").toString());

        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    private final class ViewHolder {
        ImageView img;
        TextView name;
    }
}
