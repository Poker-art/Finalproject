package com.example.finalproject;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DatabaseHandler extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION =1;
    public static final String DATABASE_NAME ="crypto.db";

    public static final String TABLE_PORTFOLIO ="portfolio";
    public static final String KEY_ID ="_ID";
    public static final String COLUMN_NAME_TICKER ="ticker";
    public static final String COLUMN_NAME_ICON ="icon";
    public static final String COLUMN_NAME_QUANTITY ="quantity";
    public static final String COLUMN_NAME_DATE ="date";
    public static final String COLUMN_NAME_TIME ="time";
    public static final String COLUMN_NAME_PRICE ="PRICE";

    public static final String TABLE_FAVORITES ="favorites";

    private static final String SQL_CREATE_TABLE_PORTFOLIO =
            "CREATE TABLE " + TABLE_PORTFOLIO + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_TICKER + " TEXT," +
                    COLUMN_NAME_ICON + " TEXT," +
                    COLUMN_NAME_QUANTITY + " TEXT," +
                    COLUMN_NAME_DATE + " TEXT," +
                    COLUMN_NAME_TIME + " TEXT," +
                    COLUMN_NAME_PRICE + " TEXT)";

    private static final String SQL_CREATE_TABLE_FAVORITES =
            "CREATE TABLE " + TABLE_FAVORITES + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_TICKER + " TEXT)";

    private static final String SQL_DELETE_TABLE_PORTFOLIO =
            "DROP TABLE IF EXISTS " + TABLE_PORTFOLIO;

    private static final String SQL_DELETE_TABLE_FAVORITES =
            "DROP TABLE IF EXISTS " + TABLE_FAVORITES;


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_PORTFOLIO);
        db.execSQL(SQL_CREATE_TABLE_FAVORITES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
