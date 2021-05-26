package com.dipale.foodapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    int count;
    ArrayList<Food> foods;

    public ListAdapter(Context context, int layout, int count, ArrayList<Food> foods) {
        this.context = context;
        this.layout = layout;
        this.count = count;
        this.foods = foods;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) { return 0; }

    public class ViewHolder {
        ConstraintLayout view;
        ImageView imImg;
        TextView txName;
        TextView txPrice;
        TextView txFreeship;
        int id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListAdapter.ViewHolder holder;

        if (convertView == null) {
            holder = new ListAdapter.ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(layout, null);

            holder.view = (ConstraintLayout) convertView.findViewById(R.id.clGrid);
            holder.imImg = (ImageView) convertView.findViewById(R.id.imimg);
            holder.txName = (TextView) convertView.findViewById(R.id.txName);
            holder.txPrice = (TextView) convertView.findViewById(R.id.txPrice);
            holder.txFreeship = (TextView) convertView.findViewById(R.id.txFREESHIP);

            convertView.setTag(holder);
        } else {
            holder = (ListAdapter.ViewHolder) convertView.getTag();
        }

        holder.imImg.setImageResource(foods.get(position).img);
        holder.txName.setText(foods.get(position).name);
        holder.txPrice.setText("Price: " + foods.get(position).price + "");
        holder.txFreeship.setText("SHIPCOST: " + foods.get(position).shipCost);
        holder.id = foods.get(position).food_ID;

        Animation animationList = AnimationUtils.loadAnimation(context, R.anim.animation_list);
        convertView.startAnimation(animationList);

        return convertView;
    }
}
