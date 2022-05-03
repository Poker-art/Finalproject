package com.example.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.LinkedList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper  {
    public static final int DATABASE_VERSION =1;
    public static final String DATABASE_NAME ="crypto.db";

    public static final String TABLE_PORTFOLIO ="portfolio";
    public static final String KEY_ID ="_ID";
    public static final String COLUMN_NAME_NAME ="name";
    public static final String COLUMN_NAME_ICON ="icon";
    public static final String COLUMN_NAME_QUANTITY ="quantity";
    public static final String COLUMN_NAME_DATE ="date";
    public static final String COLUMN_NAME_TIME ="time";
    public static final String COLUMN_NAME_PRICE ="PRICE";

    public static final String TABLE_FAVORITES ="favorites";

    private static final String SQL_CREATE_TABLE_PORTFOLIO =
            "CREATE TABLE " + TABLE_PORTFOLIO + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_NAME + " TEXT," +
                    COLUMN_NAME_QUANTITY + " TEXT," +
                    COLUMN_NAME_DATE + " TEXT," +
                    COLUMN_NAME_PRICE + " TEXT)";

    private static final String SQL_CREATE_TABLE_FAVORITES =
            "CREATE TABLE " + TABLE_FAVORITES + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_NAME + " TEXT)";

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

    public void addCoin(String name, String quantity, String date, String price){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_NAME, name);
        values.put(COLUMN_NAME_QUANTITY, quantity);
        values.put(COLUMN_NAME_DATE, date);
        values.put(COLUMN_NAME_PRICE, price);

        //inserting values
        db.insert(TABLE_PORTFOLIO, null, values);
        db.close();
    }

    public void addCoin(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_NAME, name);
        //inserting values
        db.insert(TABLE_FAVORITES, null, values);
        db.close();
    }


    public List<Coin> getPortfolio() {
        List<Coin> coins = new LinkedList<Coin>();

        //build the query

        String query = "SELECT * FROM " + TABLE_PORTFOLIO;

        //get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);


        //Go over each coin, build a coin and add it to the list:
        Coin coin = null;

        if(cursor.moveToFirst()){
            do{
                coin = new Coin();
                coin.setName(cursor.getString(1));
                //coin.setIcon(cursor.getString(2));
                coin.setQuantity(cursor.getString(2));
                coin.setDate(cursor.getString(3));
                //coin.setTime(cursor.getString(5));
                coin.setPrice(cursor.getString(4));

                //Add coin to coins
                coins.add(coin);

            } while(cursor.moveToNext());
        }
        Log.d("getPortfolio", coins.toString());

        //return Coins

        return coins;
    }

    public List<Coin> getFavorites(){
        List<Coin> coins = new LinkedList<Coin>();

        //Build the Query

        String query = "SELECT * FROM " + TABLE_FAVORITES;

        //get reference to writable DB

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        //Go over each coin, build coon and add it to the list:
        Coin coin = null;

        if(cursor.moveToFirst()){
           do{
               coin = new Coin();
               coin.setName(cursor.getString(1));

               coins.add(coin);
           }
           while(cursor.moveToNext());
        }
        Log.d("getFavorites", coins.toString());

        //return Coins

        return coins;
    }

    public void deleteCoinFromFavorite(String coinName) {

        // on below line we are creating
        // a variable to write our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are calling a method to delete our
        // course and we are comparing it with our course name.
        db.delete(TABLE_FAVORITES, "COLUMN_NAME_NAME=?", new String[]{coinName});
        db.close();
    }

    public void deleteCoinFromPortfolio(String coinName) {

        // on below line we are creating
        // a variable to write our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are calling a method to delete our
        // course and we are comparing it with our course name.
        db.delete(TABLE_PORTFOLIO, "COLUMN_NAME_NAME=?", new String[]{coinName});
        db.close();
    }

    public void deleteTablePortfolio(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_PORTFOLIO);
    }

    public void deleteTableFavorites(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_FAVORITES);
    }



}
