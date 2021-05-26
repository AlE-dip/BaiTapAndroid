package com.example.nationinfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailCountry extends Activity{
    private TextView txTenQG, txMaQG, txDS, txDT;
    private ImageView ivCo, imClose, imMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_country);
        ivCo = (ImageView) findViewById(R.id.imFlag);
        imClose = (ImageView) findViewById(R.id.imClose);
        txTenQG = findViewById(R.id.txTQG);
        txMaQG = findViewById(R.id.txMQG);
        txDS = findViewById(R.id.txDanSo);
        txDT = findViewById(R.id.txDienTich);
        imMap = findViewById(R.id.imMap);


        Intent intent = getIntent();
        int index = intent.getIntExtra("index", -1);

        ArrayList<Country> countries = MainActivity.countries;
        for (int i = 0; i < countries.size(); i++){
            if(countries.get(i).Index == index){
                Country country = countries.get(i);
                txTenQG.setText(country.Ten);
                txDT.setText(country.DienTich + " (Km²)");
                txDS.setText(country.DanSo + " (Người)");
                txMaQG.setText(country.IDcountry);


                String urlCountryCode = country.IDcountry.toLowerCase();
                String urlflag = "https://img.geonames.org/flags/x/"+urlCountryCode+".gif";
                String imgUri_map = "https://img.geonames.org/img/country/250/"+urlCountryCode.toUpperCase()+".png";
                Picasso.get().load(urlflag).into(ivCo);
                Picasso.get().load(imgUri_map).into(imMap);
                break;
            }
        }
        imClose.setOnClickListener(monClickFishListener);
    }

    private View.OnClickListener monClickFishListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
}
