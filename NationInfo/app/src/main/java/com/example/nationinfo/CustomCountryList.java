package com.example.nationinfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//import com.squareup.picasso.Picasso;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.ArrayList;

public class CustomCountryList extends BaseAdapter {
    int index;
    private Activity context;
    ArrayList<Country> countries;

    public CustomCountryList(Activity context, ArrayList<Country> countries, int index) {
        this.context = context;
        this.countries = countries;
        this.index = index;
    }

    public static class ViewHolder {
        int Id;
        TextView txNameCountry;
        ImageView imCountry;
        ImageView imDown;

        TextView txNameCountry1;
        ImageView imCountry1;
        ImageView imUp1;
        TextView txMQG1;
        TextView txDS1;
        TextView txDT1;
        TextView txM1;
        TextView txMore1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            row = inflater.inflate(R.layout.row_item, null, true);
            vh.txNameCountry = row.findViewById(R.id.txName);
            vh.imCountry = row.findViewById(R.id.imimg);
            vh.imDown = row.findViewById(R.id.imDown);

            //GONE
            vh.txNameCountry1 = row.findViewById(R.id.txName1);
            vh.imCountry1 = row.findViewById(R.id.imimg1);
            vh.imUp1 = row.findViewById(R.id.imUp1);
            vh.txMQG1 = row.findViewById(R.id.txMQG1);
            vh.txDS1 = row.findViewById(R.id.txDS1);
            vh.txDT1 = row.findViewById(R.id.txDT1);
            vh.txM1 = row.findViewById(R.id.txM);
            vh.txMore1 = row.findViewById(R.id.txMore1);
            row.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
            if(vh.imCountry1.getVisibility() == View.VISIBLE){
                collapseItem(vh);
            }
        }
        String urlCountryCode = (countries.get(position).getIDcountry()).toLowerCase();
        String urlflag = "https://img.geonames.org/flags/x/"+urlCountryCode+".gif";
        RequestCreator requestCreator = Picasso.get().load(urlflag);
        requestCreator.into(vh.imCountry);
        vh.txNameCountry.setText(countries.get(position).getTen());
        vh.Id = countries.get(position).Index;

        //GONE
        requestCreator.into(vh.imCountry1);
        vh.txNameCountry1.setText(countries.get(position).getTen());
        vh.txMQG1.setText("Mã quốc gia: " + countries.get(position).getIDcountry());
        vh.txDS1.setText("Dân số: " + countries.get(position).getDanSo() + " (Người)");
        vh.txDT1.setText("Diện tích: " + countries.get(position).getDienTich() + " (Km²)");

        Animation animationList = AnimationUtils.loadAnimation(context, R.anim.animation_list);
        row.startAnimation(animationList);

        return row;
    }

    private void collapseItem(CustomCountryList.ViewHolder viewHolder){
        viewHolder.imCountry.setVisibility(View.VISIBLE);
        viewHolder.imDown.setVisibility(View.VISIBLE);
        viewHolder.txNameCountry.setVisibility(View.VISIBLE);

        viewHolder.imCountry1.setVisibility(View.GONE);
        viewHolder.txNameCountry1.setVisibility(View.GONE);
        viewHolder.imUp1.setVisibility(View.GONE);
        viewHolder.txMQG1.setVisibility(View.GONE);
        viewHolder.txDS1.setVisibility(View.GONE);
        viewHolder.txDT1.setVisibility(View.GONE);
        viewHolder.txM1.setVisibility(View.GONE);
        viewHolder.txMore1.setVisibility(View.GONE);
    }

    public long getItemId(int position) {
        return position;
    }

    public Object getItem(int position) {
        return position;
    }

    public int getCount() {
        return countries.size();
    }
}
