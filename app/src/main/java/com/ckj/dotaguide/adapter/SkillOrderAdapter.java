package com.ckj.dotaguide.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ckj.dotaguide.R;

import java.util.ArrayList;

/**
 * Created by chenkaijian on 17-3-13.
 */

public class SkillOrderAdapter extends BaseAdapter {

    private Context context;
    private String heroId;
    private ArrayList datas;

    public SkillOrderAdapter(Context context, String heroId, ArrayList datas) {
        this.context = context;
        this.heroId = heroId;
        this.datas = datas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.skill_order_item, null);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String src = datas.get(position).toString();
        if (!src.startsWith("http")) {
            src = "http://dota.uuu9.com/hero/" + heroId + "/" + src;
        }
        Glide.with(context).load(src).placeholder(R.drawable.default_image_cover_gray).into(holder.img);

        return convertView;
    }

    private final class ViewHolder {
        ImageView img;
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
}
