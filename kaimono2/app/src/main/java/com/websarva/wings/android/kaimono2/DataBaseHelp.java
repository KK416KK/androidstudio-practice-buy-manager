package com.websarva.wings.android.kaimono2;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelp extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "data.db";
    private static final int DATABASE_VERSION = 5;
    private static final String TABLE_DATA = "data";
    private static final String YOKIN = "yokin";
    private static final String SYUEKI = "syueki";
    private static final String YMD = "ymd";
    public DataBaseHelp(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ").append(TABLE_DATA).append(" ("); // 修正
        sb.append("_id INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sb.append(YMD).append(" TEXT, ");
        sb.append(SYUEKI).append(" TEXT, ");
        sb.append(YOKIN).append(" TEXT); ");

        db.execSQL(sb.toString());

        String first = "INSERT INTO " + TABLE_DATA + "(" + YMD + "," + SYUEKI + "," + YOKIN + ") VALUES" + "('0','0','0')";

        db.execSQL(first);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            // 古いバージョンのデータベースを削除して新しいバージョンを作成
            db.execSQL("DROP TABLE IF EXISTS table_name"); // テーブルの名前は適切に変更してください
            onCreate(db);
        }
    }
    public void addTask(String ymd, String syueki, String yokin) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(YMD, ymd);
        values.put(SYUEKI, syueki);
        values.put(YOKIN, yokin);

        db.insert(TABLE_DATA, null, values);
        db.close();
    }
}
