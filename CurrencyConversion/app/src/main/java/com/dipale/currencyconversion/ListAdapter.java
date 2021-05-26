package com.dipale.currencyconversion;

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
    ArrayList<Currency> currencies;

    public ListAdapter(Context context, int layout, int count, ArrayList<Currency> currencies) {
        this.context = context;
        this.layout = layout;
        this.count = count;
        this.currencies = currencies;
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
        TextView txCountry;
        int id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(layout, null);

            holder.txCountry = (TextView) convertView.findViewById(R.id.txCountry);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txCountry.setText(currencies.get(position).getCountry());
        holder.id = position;

        Animation animationList = AnimationUtils.loadAnimation(context, R.anim.animation_list);
        convertView.startAnimation(animationList);

        return convertView;
    }
}
