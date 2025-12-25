package com.websarva.wings.android.kaimono2;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class nyuryoku extends AppCompatActivity {
    List<String> shop_name_list = new ArrayList<String>();
    Spinner spinner;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nyuryokuform);

        spinner = findViewById(R.id.spinner);

        Button shop_name = findViewById(R.id.btn_shop_name);
        Button price = findViewById(R.id.btn_price);

        EditText shopname = findViewById(R.id.shop_name);
        EditText Buying = findViewById(R.id.buying_item);
        EditText buyingprice = findViewById(R.id.buying_price);

        dbHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        String ymd = intent.getStringExtra("ymd");

        shop_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String shop__name = shopname.getText().toString();
                if(shop__name.length() == 0){
                    Toast.makeText(nyuryoku.this, "名前が設定されておりません", Toast.LENGTH_SHORT).show();
                }else{
                    //if同じ名前が既にある時
                    shop_name_list.add(shop__name);
                    updateSpinner(shop_name_list);
                }
            }
        });

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String buying = Buying.getText().toString();
                String bprice = buyingprice.getText().toString();
                if(buying.length() == 0 || bprice.length() == 0){
                    Toast.makeText(nyuryoku.this, "名前、もしくは価格が設定されておりません", Toast.LENGTH_SHORT).show();
                }else{
                    //データベースに保存
                    String shop_name_ = spinner.getSelectedItem().toString();
                    String buying_ = buying;
                    String buying_price_ = bprice;
                    String ymd_ = ymd;

                    dbHelper.addTask(shop_name_,buying_,buying_price_, String.valueOf(ymd_));
                    db.close();
                }
            }
        });
    }

    @Override
    protected void onDestroy(){
        dbHelper.close();
        super.onDestroy();
    }

    private void updateSpinner(List<String> shop_name_list){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(nyuryoku.this, android.R.layout.simple_spinner_item,shop_name_list);
        spinner.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
