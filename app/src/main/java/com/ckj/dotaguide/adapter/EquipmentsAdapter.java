package com.ckj.dotaguide.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ckj.dotaguide.CustomGridView;
import com.ckj.dotaguide.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by chenkaijian on 17-3-13.
 */

public class EquipmentsAdapter extends RecyclerView.Adapter<EquipmentsAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList datas;
    private String abbreviation;
    private ArrayList goodsList;

    public EquipmentsAdapter(Context context, ArrayList datas) {
        this.mContext = context;
        this.datas = datas;
    }

    @Override
    public EquipmentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.equipment_list_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String title = (((HashMap) datas.get(position)).get("title")).toString();
        String desc = (((HashMap) datas.get(position)).get("desc")).toString();
        goodsList = (ArrayList) ((HashMap) datas.get(position)).get("goodsList");
        holder.title.setText(title);
        holder.desc.setText(desc);
        holder.gridview.setAdapter(new EquipmentsGridAdapter(mContext, abbreviation, goodsList));
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView desc;
        public CustomGridView gridview;

        public ViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            desc = (TextView) view.findViewById(R.id.desc);
            gridview = (CustomGridView) view.findViewById(R.id.goods_gridview);
        }

    }
}
