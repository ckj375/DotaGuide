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
 * Created by chenkaijian on 17-9-21.
 */

public class EquipmentGoodsAdapter extends RecyclerView.Adapter<EquipmentGoodsAdapter.MyViewHolder> implements View.OnClickListener {

    private Context mContext;
    private ArrayList datas;
    private OnItemClickListener listener = null;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public EquipmentGoodsAdapter(Context mContext, ArrayList datas) {
        this.mContext = mContext;
        this.datas = datas;
    }

    @Override
    public EquipmentGoodsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                mContext).inflate(R.layout.equipment_goods_list_item, parent,
                false);
        EquipmentGoodsAdapter.MyViewHolder holder = new EquipmentGoodsAdapter.MyViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onItemClick(view, (int) view.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(EquipmentGoodsAdapter.MyViewHolder holder, int position) {
        String src = ((HashMap) datas.get(position)).get("src").toString();
        String name = ((HashMap) datas.get(position)).get("name").toString();
        String price = ((HashMap) datas.get(position)).get("gold").toString();
        Glide.with(mContext).load(src).placeholder(R.drawable.default_image_cover_gray).into(holder.img);
        holder.name.setText(name);
        holder.price.setText("价格:" + price);

        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView img;
        public TextView name;
        public TextView price;

        public MyViewHolder(View view) {
            super(view);
            img = (ImageView) view.findViewById(R.id.img);
            name = (TextView) view.findViewById(R.id.name);
            price = (TextView) view.findViewById(R.id.price);
        }
    }

}

