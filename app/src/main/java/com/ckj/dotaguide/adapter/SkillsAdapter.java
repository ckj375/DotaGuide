
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
 * Created by chenkaijian on 17-3-14.
 */

public class SkillsAdapter extends RecyclerView.Adapter<SkillsAdapter.ViewHolder> implements View.OnClickListener {

    private Context context;
    private String heroId;
    private ArrayList datas;
    private OnItemClickListener listener = null;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public SkillsAdapter(Context context, String heroId, ArrayList datas) {
        this.context = context;
        this.heroId = heroId;
        this.datas = datas;
    }


    //创建新View，被LayoutManager所调用
    @Override
    public SkillsAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.skill_list_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(this);
        return vh;
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

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        String src = (((HashMap) datas.get(position)).get("img")).toString();
        if (!src.startsWith("http")) {
            src = "http://dota.uuu9.com/hero/" + heroId + "/" + src;
        }
        Glide.with(context).load(src).placeholder(R.drawable.default_image_cover_gray).into(viewHolder.img);
        String name = ((HashMap) datas.get(position)).get("name").toString();
        String attr = ((HashMap) datas.get(position)).get("attr").toString();
        String hotkey = ((HashMap) datas.get(position)).get("hotkey").toString();
        viewHolder.name.setText(name);
        viewHolder.attr.setText(attr);
        viewHolder.hotkey.setText(hotkey);

        viewHolder.itemView.setTag(position);
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView img;
        public TextView name;
        public TextView attr;
        public TextView hotkey;

        public ViewHolder(View view) {
            super(view);
            img = (ImageView) view.findViewById(R.id.img);
            name = (TextView) view.findViewById(R.id.name);
            attr = (TextView) view.findViewById(R.id.attr);
            hotkey = (TextView) view.findViewById(R.id.hot_key);
        }

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }
}
