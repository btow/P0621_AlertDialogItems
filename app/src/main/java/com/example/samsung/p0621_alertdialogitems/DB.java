package com.example.samsung.p0621_alertdialogitems;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by samsung on 13.03.2017.
 */

public class DB {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "mydb",
                                DB_TABLE = "mytab",
                                COLUMN_ID = "_id",
                                COLUMN_TXT = "txt",
    CREATE_TABLE = "create table ", INTEGER = " integer ", PRIMARY_KEY = " primary key ", TEXT = " text ",
    DB_CREATE = CREATE_TABLE + DB_TABLE + "(" + COLUMN_ID + INTEGER + PRIMARY_KEY + "," + COLUMN_TXT + TEXT + ");";

    private Context mCtx;
    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public DB(Context context) {
        mCtx = context;
    }
    //открыть подключение
    public void open() {
        mDBHelper = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
    }
    //закрыть подключение
    public void close() {
        if (mDBHelper != null) {
            mDBHelper.close();
        }
    }
    //получить все данные из таблицы DB_TABLE
    public Cursor getAllData() {
        return mDB.query(DB_TABLE, null, null, null, null, null, null);
    }
    //изменить запись в DB_TABLE
    public void chengeRec(int id, String txt) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TXT, txt);
        mDB.update(DB_TABLE, contentValues, COLUMN_ID + " = " + id, null);
    }
    //класс для управления БД
    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }
        //создание и заполнение БД
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);

            ContentValues contentValues = new ContentValues();
            for (int i = 1; i < mCtx.getResources().getInteger(R.integer.number_items); i++) {
                contentValues.put(COLUMN_ID, i);
                contentValues.put(COLUMN_TXT, "Sometext " + i);
                db.insert(DB_TABLE, null, contentValues);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (oldVersion != DB_VERSION) {

            } else if (newVersion != DB_VERSION) {

            } else if (oldVersion == DB_VERSION) {

            } else {

            }
        }
    }
}
