package com.websarva.wings.android.kaimono2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import java.util.ArrayList;
import java.util.Arrays;

public class KaimonoList extends AppCompatActivity {
    private DatabaseHelper dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);


        //インテントを取得
        Intent intent = getIntent();
        int year = intent.getIntExtra("year",-1);
        int month = intent.getIntExtra("month",-1);
        int day = intent.getIntExtra("day",-1);

        String ymd = String.valueOf(year)  + String.valueOf(month) + String.valueOf(day);

        //テキストビューに日付を表示
        TextView textView = findViewById(R.id.text1);
        textView.setText(year + "年" + month + "月" + day + "日");

        //sqliteに書き込むためのインスタンス
        dbhelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbhelper .getWritableDatabase();
        //クエリを実行
        String sql = "SELECT * FROM kaimono WHERE buyingymd = ?";
        Cursor cursor = null;

        boolean exists = dbhelper.isTableExists(db,"kaimono");

        //recyclerviewを取得
        RecyclerView recyclerView = findViewById(R.id.recycleview);
        //リストの表示方法
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<String> listdata = new ArrayList<>();

        if(exists == true){
            try{
                cursor = db.rawQuery(sql, new String[]{ymd});
                    if (cursor != null && cursor.moveToFirst()){
                        do{
                            String shopadata = cursor.getString(cursor.getColumnIndexOrThrow("shopname"));
                            listdata.add(shopadata);
                        }while(cursor.moveToNext());
                    }else{}
            }catch (Exception e){
            }finally{
                if(cursor != null){
                    cursor.close();
                }
            }
        }

        //書き込みの画面に移動
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent2 = new Intent(KaimonoList.this, nyuryoku.class);
            intent2.putExtra("ymd",ymd);
            startActivity(intent2);
            }
        });

        ExpandableAdapter adapter2 = new ExpandableAdapter(listdata, ymd,this);
        recyclerView.setAdapter(adapter2);
        db.close();
    }
}
