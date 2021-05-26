package com.dipale.currencyconversion;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.slider.RangeSlider;
import com.google.android.material.slider.Slider;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    ArrayList<Currency> currencies;
    TextView tx0, tx1, tx2, tx3, tx4, tx5, tx6, tx7, tx8, tx9, txNumber1, txNumber2, txNumber3,
            txCountry1, txCountry2, txCountry3, txCurrencyCode1, txCurrencyCode2, txCurrencyCode3,
            txDot, txDate;
    ImageView imDelete;
    Button btCancel;
    ConstraintLayout ctBrLs;
    ListView lsCountry;
    ListAdapter listAdapter;
    int[] dropItemList;
    int numberClick;
    int currencyCode;
    String number;
    String number1, number2, number3;
    int currencyCode1, currencyCode2, currencyCode3;
    double currentExc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InItView();
        SetEvent();
        dropItemList = new int[]{-1, -1, -1};

        currencies = new ArrayList<>();
        listAdapter = null;
        lsCountry.setAdapter(listAdapter);

        new ReadRSS().execute("https://usd.fxexchangerate.com/rss.xml");
    }

    private void SetEvent(){
        View.OnClickListener clickNumber = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = number;
                text.replaceAll("[^0-9]","");
                if(number.length() <= 15 || v instanceof ImageView){
                    if(v instanceof ImageView){
                        if(number.length() == 1){
                            number = "0";
                        }else{
                            number = number.substring(0, number.length() - 1);
                        }
                    }else{
                        if(number.equals("0") &&
                                !((TextView) v).getText().toString().trim().equals(".")){
                            if(!((TextView) v).getText().toString().trim().equals("0")){
                                number = ((TextView) v).getText().toString().trim();
                            }
                        }else{
                            if(number.indexOf('.') == -1){
                                number = number + ((TextView) v).getText().toString().trim();
                            }else{
                                if(!((TextView) v).getText().toString().trim().equals(".")){
                                    number = number + ((TextView) v).getText().toString().trim();
                                }
                            }
                        }
                    }
                }
                int p = number.indexOf('.');
                if(p != -1){
                    String t = number.substring(p + 1);
                    if(t.length() > 4){
                        number = number.substring(0, number.length() - 1);
                    }
                }

                switch (numberClick){
                    case 1:
                        number1 = formatInput(number);
                        break;
                    case 2:
                        number2 = formatInput(number);
                        break;
                    case 3:
                        number3 = formatInput(number);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + numberClick);
                }
                caculator();
                refreshTextNumber();
            }
        };
        tx0.setOnClickListener(clickNumber);
        tx1.setOnClickListener(clickNumber);
        tx2.setOnClickListener(clickNumber);
        tx3.setOnClickListener(clickNumber);
        tx4.setOnClickListener(clickNumber);
        tx5.setOnClickListener(clickNumber);
        tx6.setOnClickListener(clickNumber);
        tx7.setOnClickListener(clickNumber);
        tx8.setOnClickListener(clickNumber);
        tx9.setOnClickListener(clickNumber);
        txDot.setOnClickListener(clickNumber);
        imDelete.setOnClickListener(clickNumber);
        imDelete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                txNumber1.setText("0");
                txNumber2.setText("0");
                txNumber3.setText("0");
                number = "0";
                return false;
            }
        });

        View.OnClickListener clickNumberInput = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (((TextView) v).getId()){
                    case R.id.txNumber1:
                        numberClick = 1;
                        number1 = formatInput(number);
                        txNumber1.setTextColor(Color.parseColor("#FF7E00"));
                        txNumber2.setTextColor(Color.parseColor("#707070"));
                        txNumber3.setTextColor(Color.parseColor("#707070"));
                        currentExc = Double.parseDouble(currencies.get(currencyCode1).getExchangeRate());
                        break;
                    case R.id.txNumber2:
                        numberClick = 2;
                        number2 = formatInput(number);
                        txNumber1.setTextColor(Color.parseColor("#707070"));
                        txNumber2.setTextColor(Color.parseColor("#FF7E00"));
                        txNumber3.setTextColor(Color.parseColor("#707070"));
                        currentExc = Double.parseDouble(currencies.get(currencyCode2).getExchangeRate());
                        break;
                    case R.id.txNumber3:
                        numberClick = 3;
                        number3 = formatInput(number);
                        txNumber1.setTextColor(Color.parseColor("#707070"));
                        txNumber2.setTextColor(Color.parseColor("#707070"));
                        txNumber3.setTextColor(Color.parseColor("#FF7E00"));
                        currentExc = Double.parseDouble(currencies.get(currencyCode3).getExchangeRate());
                        break;
                }
                caculator();
                refreshTextNumber();
            }
        };
        txNumber1.setOnClickListener(clickNumberInput);
        txNumber2.setOnClickListener(clickNumberInput);
        txNumber3.setOnClickListener(clickNumberInput);

        View.OnClickListener clickCurrencyCode = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctBrLs.setVisibility(View.VISIBLE);
                switch (((TextView) v).getId()){
                    case R.id.txCurrencyCode1:
                        currencyCode = 1;
                        break;
                    case R.id.txCurrencyCode2:
                        currencyCode = 2;
                        break;
                    case R.id.txCurrencyCode3:
                        currencyCode = 3;
                        break;
                }
                caculator();
                refreshTextNumber();
            }
        };
        txCurrencyCode1.setOnClickListener(clickCurrencyCode);
        txCurrencyCode2.setOnClickListener(clickCurrencyCode);
        txCurrencyCode3.setOnClickListener(clickCurrencyCode);

        View.OnClickListener clickCancel = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctBrLs.setVisibility(View.GONE);
            }
        };
        btCancel.setOnClickListener(clickCancel);
        ctBrLs.setOnClickListener(clickCancel);

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (currencyCode){
                    case 1: currencyCode1 = position;
                        txCurrencyCode1.setText(currencies.get(position).getCurrencyCode());
                        txCountry1.setText(currencies.get(position).getCountry());
                        break;
                    case 2: currencyCode2 = position;
                        txCurrencyCode2.setText(currencies.get(position).getCurrencyCode());
                        txCountry2.setText(currencies.get(position).getCountry());
                        break;
                    case 3: currencyCode3 = position;
                        txCurrencyCode3.setText(currencies.get(position).getCurrencyCode());
                        txCountry3.setText(currencies.get(position).getCountry());
                        break;
                }
                ctBrLs.setVisibility(View.GONE);
                caculator();
                refreshTextNumber();
            }
        };
        lsCountry.setOnItemClickListener(itemClickListener);

    }

    private void caculator(){
        double num, num1 = 0, num2 = 0, num3 = 0;
        double exc1, exc2, exc3;
        num = Double.parseDouble(number);

        exc1 = Double.parseDouble(currencies.get(currencyCode1).getExchangeRate());
        exc2 = Double.parseDouble(currencies.get(currencyCode2).getExchangeRate());
        exc3 = Double.parseDouble(currencies.get(currencyCode3).getExchangeRate());

        switch (numberClick){
            case 1:
                number2 = cacu(num, exc2);
                number3 = cacu(num, exc3);
                break;
            case 2:
                number1 = cacu(num, exc1);
                number3 = cacu(num, exc3);
                break;
            case 3:
                number1 = cacu(num, exc1);
                number2 = cacu(num, exc2);
                break;
        }
    }

    private String formatInput(String snum){
        snum = snum.replaceAll("[^0-9.]","");
        int p = snum.indexOf('.');
        String l = "";
        String r = "";
        if(p != -1){
            l = snum.substring(0, p);
            r = snum.substring(p + 1);
        }else {
            l = snum;
        }
        int t = 0;
        String le = "";
        for(int i = l.length() - 1; i >= 0; i--){
            t++;
            le = l.charAt(i) + le;
            if(t == 3 && i != 0){
                le = "," + le;
                t = 0;
            }
        }
        if(p != -1){
            snum = le + '.' + r;
        }else {
            snum = le;
        }
        return snum;
    }

    private String cacu(double num, double exc){
        double number = num / currentExc * exc;
        String snum;
        number = (double) Math.ceil(number * 10000)/10000;
        snum = String.format("%1$,.4f", number);
        snum = drop0(snum);
        if(snum.length() > 18) snum = String.format("%e", number);
        return format$(snum);
    }

    private String drop0(String str){
        int p = str.indexOf(',');
        if(p != -1){
            for(int i = str.length() - 1; ((str.charAt(i) == '0' && i > p) || str.charAt(i) == ',') && str.length() > 1; i--){
                str = str.substring(0, str.length() - 1);
            }
        }
        return str;
    }

    private String format$(String str){
        String s = "";
        for(int i = 0; i < str.length(); i++){
            if(str.charAt(i) == ','){
                s += '.';
            }else {
                if(str.charAt(i) == '.'){
                    s += ',';
                }else {
                    s += str.charAt(i);
                }
            }
        }
        return s;
    }

    private void refreshTextNumber(){
        txNumber1.setText(number1);
        txNumber2.setText(number2);
        txNumber3.setText(number3);
    }

    private void InItView(){
        number = "0";
        number1 = "0";
        number2 = "0";
        number3 = "0";
        numberClick = 2;
        currencyCode = 1;

        tx0 = (TextView) findViewById(R.id.tx0);
        tx1 = (TextView) findViewById(R.id.tx1);
        tx2 = (TextView) findViewById(R.id.tx2);
        tx3 = (TextView) findViewById(R.id.tx3);
        tx4 = (TextView) findViewById(R.id.tx4);
        tx5 = (TextView) findViewById(R.id.tx5);
        tx6 = (TextView) findViewById(R.id.tx6);
        tx7 = (TextView) findViewById(R.id.tx7);
        tx8 = (TextView) findViewById(R.id.tx8);
        tx9 = (TextView) findViewById(R.id.tx9);
        txDot = (TextView) findViewById(R.id.txDot);
        txDate = (TextView) findViewById(R.id.txDate);
        txNumber1 = (TextView) findViewById(R.id.txNumber1);
        txNumber2 = (TextView) findViewById(R.id.txNumber2);
        txNumber3 = (TextView) findViewById(R.id.txNumber3);
        txCountry1 = (TextView) findViewById(R.id.txCountry1);
        txCountry2 = (TextView) findViewById(R.id.txCountry2);
        txCountry3 = (TextView) findViewById(R.id.txCountry3);
        txCurrencyCode1 = (TextView) findViewById(R.id.txCurrencyCode1);
        txCurrencyCode2 = (TextView) findViewById(R.id.txCurrencyCode2);
        txCurrencyCode3 = (TextView) findViewById(R.id.txCurrencyCode3);
        imDelete = (ImageView) findViewById(R.id.imDelete);
        btCancel = (Button) findViewById(R.id.btCancel);
        ctBrLs = (ConstraintLayout) findViewById(R.id.ctBrLs);
        lsCountry = (ListView) findViewById(R.id.lsCountry);
    }

    private class ReadRSS extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder content = new StringBuilder();
            try {
                URL url = new URL(strings[0]);

                InputStreamReader inputStreamReader = new InputStreamReader(url.openConnection().getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String line = "";
                while ((line = bufferedReader.readLine()) != null){
                    content.append(line);
                }
                bufferedReader.close();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return content.toString();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            XMLDOMParser parser = new XMLDOMParser();
            Document document = parser.getDocument(s);

            NodeList nodeList = document.getElementsByTagName("item");
            Element node = (Element) nodeList.item(0);
            String date = parser.getValue(node, "pubDate");

            for(int i = 0; i < nodeList.getLength(); i++){
                Element element = (Element) nodeList.item(i);

                Currency currency = new Currency();
                String title = parser.getValue(element, "title");
                String description = parser.getValue(element, "description");

                String country = title.substring(title.indexOf('/') + 1, title.lastIndexOf('('));
                String currencyCode = title.substring(title.lastIndexOf('(') + 1, title.lastIndexOf(')'));
                String exchangeRate = description.substring(description.indexOf('=') + 2).replaceAll("[^0-9.]","");

                currency.setCountry(country);
                currency.setCurrencyCode(currencyCode);
                currency.setExchangeRate(exchangeRate);

                currencies.add(currency);
            }

            currencies.sort(new Comparator<Currency>() {
                @Override
                public int compare(Currency o1, Currency o2) {
                    return o1.compareTo(o2);
                }
            });

            Currency VND = null, USD = null, EUR = null;

            for(Currency currency: currencies){
                if(currency.getCurrencyCode().equals("VND")){
                    VND = currency;
                    currencyCode2 = currencies.indexOf(currency);
                    currentExc = Double.parseDouble(currency.getExchangeRate());
                }
                if(currency.getCurrencyCode().equals("USD")){
                    USD = currency;
                    currencyCode1 = currencies.indexOf(currency);
                }
                if(currency.getCurrencyCode().equals("EUR")){
                    EUR = currency;
                    currencyCode3 = currencies.indexOf(currency);
                }
                if(VND != null && USD != null && EUR != null){
                    break;
                }
            }

            txCurrencyCode1.setText(USD.getCurrencyCode());
            txCurrencyCode2.setText(VND.getCurrencyCode());
            txCurrencyCode3.setText(EUR.getCurrencyCode());
            txCountry1.setText(USD.getCountry());
            txCountry2.setText(VND.getCountry());
            txCountry3.setText(EUR.getCountry());
            txDate.setText(date);

            listAdapter = new ListAdapter(
                    MainActivity.this,
                    R.layout.item_country,
                    currencies.size(),
                    currencies);
            lsCountry.setAdapter(listAdapter);
        }
    }
}