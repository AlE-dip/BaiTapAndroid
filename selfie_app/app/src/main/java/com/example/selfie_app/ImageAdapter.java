package com.example.selfie_app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    ArrayList<String> paths;
    int count;

    public ImageAdapter(Context context, int layout, ArrayList<String> paths, int count) {
        this.context = context;
        this.layout = layout;
        this.paths = paths;
        this.count = count;
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
    public long getItemId(int position) {
        return 0;
    }

    public class ViewHolder {
        ImageView cell;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(layout, null);

            holder.cell = (ImageView) convertView.findViewById(R.id.imImage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String pathFile = paths.get(position);
        Bitmap bm = BitmapFactory.decodeFile(pathFile);
        holder.cell.setImageBitmap(bm);

        return convertView;
    }
}
