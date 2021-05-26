package com.example.nationinfo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    EditText edTim;
    private int index = -1;
    public static ArrayList<Country> countries = new ArrayList<Country>();
    private CustomCountryList customCountryList;
    private ArrayList<Country> searchCountries;
    private ProgressDialog progressDialog;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        listView = findViewById(R.id.listview_row);
        edTim = findViewById(R.id.edTim);
        sendRequest();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                CustomCountryList.ViewHolder viewHolder = (CustomCountryList.ViewHolder) view.getTag();
                View.OnClickListener detail = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), DetailCountry.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("index",viewHolder.Id);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                };
                viewHolder.imCountry1.setOnClickListener(detail);
                viewHolder.txMore1.setOnClickListener(detail);

                if(viewHolder.imCountry1.getVisibility() == View.GONE){
                    expandItem(viewHolder);
                }else {
                    collapseItem(viewHolder);
                }
                int countItem = listView.getChildCount();
                for(int i = 0; i < countItem; i++){
                    View v = listView.getChildAt(i);
                    CustomCountryList.ViewHolder vh = (CustomCountryList.ViewHolder) v.getTag();

                    if(vh.imCountry1.getVisibility() == View.VISIBLE &&
                            vh.Id != viewHolder.Id){
                        collapseItem(vh);
                        break;
                    }
                }
            }
        });

        edTim.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchCountries = new ArrayList<>();
                String text = edTim.getText().toString().trim().toLowerCase();
                for(Country country: countries){
                    String sub = country.Ten.substring(0, text.length()).toLowerCase();
                    if(sub.compareTo(text) == 0) {
                        searchCountries.add(country);
                    }
                }
                CustomCountryList customCountryList = new CustomCountryList(activity, searchCountries, index);
                listView.setAdapter(customCountryList);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void expandItem(CustomCountryList.ViewHolder viewHolder){
        viewHolder.imCountry.setVisibility(View.GONE);
        viewHolder.imDown.setVisibility(View.GONE);
        viewHolder.txNameCountry.setVisibility(View.GONE);

        viewHolder.imCountry1.setVisibility(View.VISIBLE);
        viewHolder.txNameCountry1.setVisibility(View.VISIBLE);
        viewHolder.imUp1.setVisibility(View.VISIBLE);
        viewHolder.txMQG1.setVisibility(View.VISIBLE);
        viewHolder.txDS1.setVisibility(View.VISIBLE);
        viewHolder.txDT1.setVisibility(View.VISIBLE);
        viewHolder.txM1.setVisibility(View.VISIBLE);
        viewHolder.txMore1.setVisibility(View.VISIBLE);
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

    public void sendRequest() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang lấy dữ liệu từ Server.... ");
        progressDialog.show();

        String url="http://api.geonames.org/countryInfoJSON?username=behung2000&formatted=true";

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject1 = new JSONObject(response);
                            JSONArray jsonObject2 = jsonObject1.getJSONArray("geonames");
                            for(int i=0; i<jsonObject2.length(); i++)
                            {
                                DecimalFormat decimalFormat = new DecimalFormat("#,###");
                                JSONObject jsonObject = jsonObject2.getJSONObject(i);
                                String spopulation = decimalFormat.format(Double.parseDouble(jsonObject.getString("population"))) + "";
                                String sareainsqkm = jsonObject.getString("areaInSqKm");
                                String scountrycode = jsonObject.getString("countryCode");
                                String scountryname = jsonObject.getString("countryName");
                                Country newCountry = new Country(i,scountrycode,sareainsqkm,scountryname,spopulation);
                                countries.add(newCountry);
                            }

                            countries.sort(new Comparator<Country>() {
                                @Override
                                public int compare(Country o1, Country o2) {
                                    return o1.compareTo(o2);
                                }
                            });
                            customCountryList = new CustomCountryList(activity, countries, index);
                            listView.setAdapter(customCountryList);
                        }
                        catch (JSONException e){
                                progressDialog.setMessage("Can't take data for server !!!");
                                progressDialog.show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        progressDialog.setMessage("Server error!!!");
                    }
                }
        );
        queue.add(request);
    }
}