package com.websarva.wings.android.kaimono2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    //定数フィールド
    private static final String DATABASE_NAME = "kaimono.db";
    // ヴァージョン情報の定数フィールド
    private static final int DATABASE_VERSION = 5;
    private static final String SHOP_NAME = "shopname";
    private static final String BUYING_NAME = "buyingname";
    private static final String BUYING_PRICE = "buyingprice";
    private static final String BUYING_YMD = "buyingymd";
    private static final String TABLE_TASKS = "kaimono";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ").append(TABLE_TASKS).append(" ("); // 修正
        sb.append("_id INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sb.append(SHOP_NAME).append(" TEXT, ");
        sb.append(BUYING_NAME).append(" TEXT, ");
        sb.append(BUYING_PRICE).append(" TEXT, ");
        sb.append(BUYING_YMD).append(" TEXT); ");

        db.execSQL(sb.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // テーブルが存在する場合に削除し、再作成します。
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }


    public boolean isTableExists(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", new String[]{tableName});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }



    public void addTask(String title, String description, String deadline,String ymd) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SHOP_NAME, title);
        values.put(BUYING_NAME, description);
        values.put(BUYING_PRICE, deadline);
        values.put(BUYING_YMD, ymd);

        db.insert(TABLE_TASKS, null, values);
        db.close();
    }
}
