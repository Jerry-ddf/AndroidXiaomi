package com.example.sqlitetest.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.sqlitetest.entry.Userinfo;


public class DatabaseHelper extends SQLiteOpenHelper {
    private SQLiteDatabase mRDB = null;
    private SQLiteDatabase mWDB = null;
    private static final String DATABASE_NAME = "json_data1.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "json_table1";
    private static DatabaseHelper mHelper = null;
    private static final String USERID = "userId";
    public static final String ID = "id";
    private static final String TITLE = "title";
    private static final String BODY = "body";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // 单例模式获取数据库帮助器的唯一实例
    public static DatabaseHelper getInstance(Context context) {
        if (mHelper == null) mHelper = new DatabaseHelper(context);
        return mHelper;
    }

    // 打开数据库读连接
    public SQLiteDatabase openReadLink() {
        if (mRDB == null || !mRDB.isOpen()) {
            mRDB = mHelper.getReadableDatabase();
        }
        return mRDB;
    }

    // 打开数据库写连接
    public SQLiteDatabase openWriteLink() {
        if (mWDB == null || !mWDB.isOpen()) {
            mWDB = mHelper.getWritableDatabase();
        }
        return mWDB;
    }

    // 关闭数据库连接
    public void closeLink() {
        if (mRDB != null && mRDB.isOpen()) {
            mRDB.close();
            mRDB = null;
        }
        if (mWDB != null && mWDB.isOpen()) {
            mWDB.close();
            mWDB = null;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" + USERID + " INTEGER PRIMARY " +
                "KEY, " + ID + " INTEGER, " + TITLE + " TEXT, " + BODY + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertData(Userinfo userinfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try {
            values.put(USERID, userinfo.getUserId());
            values.put(ID, userinfo.getId());
            values.put(TITLE, userinfo.getTitle());
            values.put(BODY, userinfo.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
        long result = db.insert(TABLE_NAME, null, values);
        if (result == -1) {
            Log.e("DatabaseHelper", "Failed to insert data");
        } else {
            Log.d("DatabaseHelper", "Data inserted successfully");
        }
        db.close();
    }

    @SuppressLint("Range")
    public Userinfo getJsonData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{USERID, ID, TITLE, BODY}, ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Userinfo userinfo = new Userinfo();
            userinfo.setUserId(cursor.getInt(cursor.getColumnIndex(USERID)));
            userinfo.setId(cursor.getInt(cursor.getColumnIndex(ID)));
            userinfo.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
            userinfo.setBody(cursor.getString(cursor.getColumnIndex(BODY)));
            cursor.close();
            return userinfo;
        } else return null;
    }


}
