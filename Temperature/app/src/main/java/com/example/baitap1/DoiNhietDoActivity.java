package com.example.baitap1;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class DoiNhietDoActivity extends AppCompatActivity {
    EditText textctof,textftoc;
    Button changeC,changeF,reset,clear;
    TextView history;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);
        //Objects.requireNonNull(getSupportActionBar()).hide();

        textctof=(EditText) findViewById(R.id.textctof);
        textftoc=(EditText) findViewById(R.id.textftoc);
        changeC =(Button)   findViewById(R.id.changeC);
        changeF =(Button)   findViewById(R.id.changeF);
        reset   =(Button)   findViewById(R.id.reset);
        history =(TextView) findViewById(R.id.Kq);
        clear   =(Button)   findViewById(R.id.clear);


        changeC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int c = 0;
                try
                {
                    c = Integer.parseInt(textctof.getText().toString());
                    System.out.println(c);
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("CONVERT", e.getMessage());
                    textftoc.setText(e.getMessage());
                }
                DoiNhietDo dnd = new DoiNhietDo(c,0);
                int f = dnd.changeCtoF();
                System.out.println(f + "");
                textftoc.setText(f + "");
                history.append("C to F : "+ c +"-->" + f + "\n");
            }
        });
        changeF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int f = 0;
                try
                {
                    f = Integer.parseInt(textftoc.getText().toString());
                    System.out.println(f);
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("CONVERT", e.getMessage());
                    textctof.setText(e.getMessage());
                }
                DoiNhietDo dnd = new DoiNhietDo(0,f);
                int c = dnd.changeFtoC();
                textctof.setText(c + "");
                history.append("F to C : "+ f +"-->" + c + "\n");
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textctof.setText("");
                textftoc.setText("");
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                history.setText("");
            }
        });
    }
}
