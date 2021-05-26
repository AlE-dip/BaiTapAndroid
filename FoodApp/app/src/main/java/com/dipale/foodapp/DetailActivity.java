package com.dipale.foodapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {
    private static final int MY_PERMISSION_REQUEST_CODE_SEND_SMS = 1;
    WebView weDetail;
    ImageView imBack, imMap, imEat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        inItView();

        int positionFood = getIntent().getIntExtra("position", 0);

        setUpWebView();
        loadDetail(MainActivity.foods().get(positionFood).linkDetail);

        imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imEat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String text = "Món ăn: " + MainActivity.foods().get(positionFood).name + "\n"
                            + "Giá: " + MainActivity.foods().get(positionFood).price + "VND\n"
                            + "Ship: " + MainActivity.foods().get(positionFood).shipCost + "VND\n"
                            + "Tổng: " + (MainActivity.foods().get(positionFood).shipCost + MainActivity.foods().get(positionFood).price);
                    askPermissionAndSendSMS();
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(
                            "0358318074",
                            null,
                            text,
                            null,
                            null);
                    Toast.makeText(getApplicationContext(), "Đã gửi SMS", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Không gửi được SMS", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = MainActivity.foods().get(positionFood).linkMap;
                Intent imMap = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                imMap.setPackage("com.google.android.apps.maps");
                try
                {
                    startActivity(imMap);
                }
                catch (ActivityNotFoundException ex)
                {
                    try
                    {
                        Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        startActivity(unrestrictedIntent);
                    }
                    catch (ActivityNotFoundException innerEx)
                    {
                        Toast.makeText(DetailActivity.this, "Required Map App First!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void inItView(){
        imBack = (ImageView) findViewById(R.id.imBack);
        imMap = (ImageView) findViewById(R.id.imMap);
        imEat = (ImageView) findViewById(R.id.imEat);
        weDetail = (WebView) findViewById(R.id.weDetail);
    }

    private void loadDetail(String url)  {
        weDetail.loadUrl(url);
    }

    private void setUpWebView(){
        weDetail.setWebViewClient(new WebViewClient());
        weDetail.getSettings().setLoadsImagesAutomatically(true);
        weDetail.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        weDetail.getSettings().setJavaScriptEnabled(true);
    }

    private void askPermissionAndSendSMS() {

        // With Android Level >= 23, you have to ask the user
        // for permission to send SMS.
        if (android.os.Build.VERSION.SDK_INT >=  android.os.Build.VERSION_CODES.M) { // 23

            // Check if we have send SMS permission
            int sendSmsPermisson = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.SEND_SMS);

            if (sendSmsPermisson != PackageManager.PERMISSION_GRANTED) {
                // If don't have permission so prompt the user.
                this.requestPermissions(
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSION_REQUEST_CODE_SEND_SMS
                );
                return;
            }
        }
    }
}