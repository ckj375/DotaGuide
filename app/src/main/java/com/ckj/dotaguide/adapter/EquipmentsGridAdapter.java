package com.ckj.dotaguide.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ckj.dotaguide.R;
import com.ckj.dotaguide.activity.EquipmentInfoActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by chenkaijian on 17-3-13.
 */

public class EquipmentsGridAdapter extends BaseAdapter {

    private Context mContext;
    private String heroId;
    private ArrayList datas;

    public EquipmentsGridAdapter(Context context, String heroId, ArrayList datas) {
        this.mContext = context;
        this.heroId = heroId;
        this.datas = datas;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.equipment_goods_grid_item, null);
            holder.img = (ImageView) convertView.findViewById(R.id.img);
            holder.effect = (ImageView) convertView.findViewById(R.id.effect);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String src = ((HashMap) datas.get(position)).get("src").toString();
        if (!src.startsWith("http")) {
            src = "http://dota.uuu9.com/hero/" + heroId + "/" + src;
        }
        Glide.with(mContext).load(src).placeholder(R.drawable.default_image_cover_gray).into(holder.img);

        holder.effect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEquipmentInfo(datas, position);
            }
        });

        return convertView;
    }

    private final class ViewHolder {
        ImageView img;
        ImageView effect;
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

    /**
     * 跳转进入物品介绍页面
     */
    private void showEquipmentInfo(ArrayList datas, int position) {
        String itemid = ((HashMap) datas.get(position)).get("itemid").toString();
        Intent intent = new Intent();
        intent.putExtra("type", 1);
        intent.putExtra("itemid", itemid);
        intent.setClass(mContext, EquipmentInfoActivity.class);
        mContext.startActivity(intent);
    }
}
