package com.websarva.wings.android.kaimono2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private CalendarView calendarview;
    private TextView syueki;
    private TextView yokin;
    private TextView sonsitu;
    private Button btn;
    private DatabaseHelper helper;
    private DataBaseHelp helps;
    private EditText ed1;
    private EditText ed2;
    LocalDate today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarview = findViewById(R.id.calendarview);

        calendarview.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView View, int year, int month, int day) {
                int todayyear = year;
                int todaymonth = month + 1;
                int today = day;

                //タッチした年月日を取得し遷移

                Intent intent = new Intent(MainActivity.this,KaimonoList.class);
                intent.putExtra("year",todayyear );
                intent.putExtra("month",todaymonth);
                intent.putExtra("day",today);

                startActivity(intent);
            }
        });
        syueki = findViewById(R.id.syueki);
        yokin = findViewById(R.id.yokin);
        sonsitu = findViewById(R.id.sonsitu);
        btn = findViewById(R.id.btn);
        ed1 = findViewById(R.id.syuekied);
        ed2 = findViewById(R.id.yokined);

        helper = new DatabaseHelper(this);
        helps = new DataBaseHelp(this);

        SQLiteDatabase db = helper.getWritableDatabase();
        SQLiteDatabase db2 = helps.getWritableDatabase();

        today = LocalDate.now();
        int year = today.getYear();
        int month = today.getMonthValue();
        String my = String.format("%04d%02d", year, month);

        String sql;
        sql = "SELECT * FROM data WHERE _id = 1";
        Cursor cursor = db2.rawQuery(sql, null);

        String sql2 = "SELECT * FROM kaimono WHERE buyingymd LIKE ?";
        Cursor cursor2 = db.rawQuery(sql2, new String[]{my + "%"});
        int xxa = 0;
        if (cursor2.moveToFirst()) {
            do {
                xxa += cursor2.getInt(cursor2.getColumnIndexOrThrow("buyingprice"));
            } while (cursor2.moveToNext());
        }

        if (cursor.moveToFirst()) {
            //現在の預金、先月の収益などを表示
            String datayokin = "";
            datayokin = cursor.getString(cursor.getColumnIndexOrThrow("yokin"));

            String datasyueki = "";
            datasyueki = cursor.getString(cursor.getColumnIndexOrThrow("syueki"));
            cursor.close();
            syueki.setText(datasyueki);
            yokin.setText(datayokin);
            sonsitu.setText(String.valueOf(xxa));
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sqldel = "DELETE FROM data WHERE _id = 1";
                String kyuryou = ed1.getText().toString();
                String tyokin = ed2.getText().toString();
                String ymds = year + "/" + month;

                SQLiteStatement stst = db2.compileStatement(sqldel);
                stst.executeUpdateDelete();

                helps.addTask(ymds,kyuryou,tyokin);
            }
        });

        cursor.close();
        cursor2.close();
        helper.close();
        helps.close();
        db.close();
        db2.close();
    }
}