package com.wolf.apps.expensemanager.Models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by San on 5/29/2018.
 */

public class Account {
    private int account_id;
    private String account_name;
    private double amount;
    public enum types {CREDIT, DEBIT, CASH};
    private types type;

    public Account(int account_id, String account_name, types type, double amount) {
        this.account_id = account_id;
        this.account_name = account_name;
        this.type = type;
        this.amount = amount;
    }

    public int getAccount_id() {
        return account_id;
    }

    public String getAccount_name() {
        return account_name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public types getType() {
        return type;
    }



    public void add(SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put("ac_name", this.account_name);
        values.put("amount", this.amount);
        values.put("ac_type", this.type.ordinal());
        db.insert("transaction_account", null, values);
        db.close();
    }

    public void remove(SQLiteDatabase db){
        db.execSQL("DELETE FROM transaction_account WHERE ac_id = " + this.account_id);
    }

    public void update(SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put("ac_name", this.account_name);
        values.put("amount", this.amount);
        db.update("transaction_account", values, "ac_id = " + this.account_id, null);
        db.close();
    }

    public static ArrayList<Account> getAll(SQLiteDatabase db){
        ArrayList<Account> accounts = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM transaction_account", null);
        while(cursor.moveToNext()){
            accounts.add(new Account(cursor.getInt(0), cursor.getString(1), types.values()[cursor.getInt(2)], cursor.getDouble(3)));
        }
        db.close();
        return  accounts;
    }

    public static Account getById(int id, SQLiteDatabase db){
        Cursor cursor = db.rawQuery("SELECT * FROM transaction_account WHERE ac_id = " + id, null);
        if(cursor.moveToFirst()){
            return new Account(cursor.getInt(0), cursor.getString(1), types.values()[cursor.getInt(2)], cursor.getDouble(3));
        }
        return null;
    }

    public static Account getByName(String name, SQLiteDatabase db){
        Cursor cursor = db.rawQuery("SELECT * FROM transaction_account WHERE ac_name = '" + name + "'", null);
        if(cursor.moveToFirst()){
            return new Account(cursor.getInt(0), cursor.getString(1), types.values()[cursor.getInt(2)], cursor.getDouble(3));
        }
        return null;
    }

    public static ArrayList<Account> getAllByType(types type, SQLiteDatabase db){
        ArrayList<Account> accounts = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM transaction_account WHERE ac_type = " + type.ordinal(), null);
        while(cursor.moveToNext()){
            accounts.add(new Account(cursor.getInt(0), cursor.getString(1), types.values()[cursor.getInt(2)], cursor.getDouble(3)));
        }
        db.close();
        return  accounts;
    }

    public static int getLastID(SQLiteDatabase db)
    {
        int last_id = 0;
        Cursor cursor = db.rawQuery("SELECT * FROM transaction_account", null);
        while(cursor.moveToNext()){
            if(cursor.getInt(0) > last_id){
                last_id = cursor.getInt(0);
            }
        }
        db.close();

        return last_id + 1;
    }

}
