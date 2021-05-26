package com.dipale.foodapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static boolean lsUp = false, lsDown = false;
    ListView lsFood;
    ScrollView scroll;
    ImageView im1, im2, im3;
    TextView tx1, tx2, tx3;
    ArrayList<Food> foods;
    EditText search;
    static int[] location = {0,1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inItView();

        foods = foods();

        im1.setImageResource(foods.get(0).img);
        tx1.setText(foods.get(0).name);
        im2.setImageResource(foods.get(1).img);
        tx2.setText(foods.get(1).name);
        im3.setImageResource(foods.get(2).img);
        tx3.setText(foods.get(2).name);

        ListAdapter listAdapter = new ListAdapter(
                MainActivity.this,
                R.layout.item_grid,
                foods.size(),
                foods);
        lsFood.setAdapter(listAdapter);

        lsFood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListAdapter.ViewHolder holder = (ListAdapter.ViewHolder) view.getTag();
                loadDetail(holder.id);
            }
        });

        im1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDetail(0);
            }
        });
        im2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDetail(1);
            }
        });
        im3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDetail(2);
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<Food> searchFood = new ArrayList<>();
                String text = search.getText().toString().trim().toLowerCase();
                for(Food food: foods){
                    String sub = food.name.substring(0, text.length()).toLowerCase();
                    if(sub.compareTo(text) == 0) {
                        searchFood.add(food);
                    }
                }
                ListAdapter listAdapter = new ListAdapter(
                        MainActivity.this,
                        R.layout.item_grid,
                        searchFood.size(),
                        searchFood);
                lsFood.setAdapter(listAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void inItView(){
        lsFood = (ListView) findViewById(R.id.lsFood);
        scroll = (ScrollView) findViewById(R.id.scmain);
        im1 = (ImageView) findViewById(R.id.im1);
        im2 = (ImageView) findViewById(R.id.im2);
        im3 = (ImageView) findViewById(R.id.im3);
        tx1 = (TextView) findViewById(R.id.tx1);
        tx2 = (TextView) findViewById(R.id.tx2);
        tx3 = (TextView) findViewById(R.id.tx3);
        search = (EditText) findViewById(R.id.edSearch);

        lsFood.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scroll.requestDisallowInterceptTouchEvent(true);
                int action = event.getActionMasked();
                switch (action) {
                    case MotionEvent.ACTION_UP:
                        scroll.requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });
    }

    private void loadDetail(int position){
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    public static ArrayList<Food> foods(){
        ArrayList<Food> foodList = new ArrayList<>();
        foodList.add(new Food(R.drawable.img,"Chả giò",50000,7000,0 ,
                "https://www.foody.vn/ho-chi-minh/co-tuyet-cha-gio-cha-ram",
                "geo:10.776805698080334, 106.68051099924503?q=185/30 Đường 3 Tháng 2, P. 11 , Quận 10, TP. HCM"));
        foodList.add(new Food(R.drawable.food2,"Gà nướng",60000,7000,1,
                "https://www.foody.vn/ho-chi-minh/chicken-kitchen-cac-mon-ga",
                "geo:10.774705111379955, 106.66892449714344?q=796/19B Sư Vạn Hạnh Nối Dài, P. 12, Quận 10, TP. HCM"));
        foodList.add(new Food(R.drawable.food3,"Bánh mì thịt",25000,7000,2 ,
                "https://www.foody.vn/ho-chi-minh/banh-mi-thit-nuong-banh-mi-bien-tau-nguyen-trai",
                "geo:10.771003365726857, 106.69265992406467?q=Đầu Hẻm 39 Nguyễn Trãi, Quận 1, TP. HCM"));
        foodList.add(new Food(R.drawable.food4,"Bánh Taco",68000,7000,3,
                "https://www.foody.vn/ho-chi-minh/delicioso-taco",
                "geo:10.76708963119452, 106.66682069158989?q=533/5 Nguyễn Tri Phương, P. 8, Quận 10, TP. HCM"));
        foodList.add(new Food(R.drawable.food5,"Cơm trộn",57000,7000 ,4,
                "https://www.foody.vn/ho-chi-minh/yre-com-tron-han-quoc",
                "geo:10.741802074016285, 106.71383053947136?q=23C Mai Văn Vĩnh, P. Tân Quy, Quận 7, TP. HCM"));
        foodList.add(new Food(R.drawable.food6,"Hamburger",68000,7000,5,
                "https://www.foody.vn/ho-chi-minh/mr-bmao-hamburger-hotdog-ton-dan",
                "geo:10.754278064740154, 106.70545287619144?q=384 Tôn Đản, P. 4, Quận 4, TP. HCM"));
        foodList.add(new Food(R.drawable.food7,"Cơm chiên chay",20000,7000 ,6,
                "https://www.foody.vn/ho-chi-minh/a-chay-cafe-com-chien-nui-xao-bo",
                "geo:10.757550544827478, 106.68090827569142?q=102A Cư Xá Chợ Quán, Trần Bình Trọng, Quận 5, TP. HCM"));
        foodList.add(new Food(R.drawable.food8,"Ức gà hấp",60800,7000,7,
                "https://www.foody.vn/ho-chi-minh/hong-ky-com-ga-xoi-mo",
                "geo:10.790165992312227, 106.75317243947197?q=189G Nguyễn Thị Định, P. An Phú, Quận 2, TP. HCM"));
        foodList.add(new Food(R.drawable.food9,"Khoai tây rau củ",50700,7000 ,8,
                "https://www.foody.vn/ho-chi-minh/robata-dining-an",
                "geo:10.781085010705409, 106.70522212597943?q=15C Lê Thánh Tôn, Quận 1, TP. HCM"));
        foodList.add(new Food(R.drawable.food10,"Gà sốt cay",68000,7000,9,
                "https://www.foody.vn/ho-chi-minh/chan-ga-sot-cay-shop-online",
                "geo:10.762687272542957, 106.59179216830752?q=Chung Cư Tân Mai, Quốc Lộ 1A, Quận Bình Tân, TP. HCM"));
        return foodList;
    }
}