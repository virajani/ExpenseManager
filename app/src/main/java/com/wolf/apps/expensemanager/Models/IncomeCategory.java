package com.wolf.apps.expensemanager.Models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;

class IncomeCategory {
    private int id;
    private String description;

    public IncomeCategory(int id, String description){
        this.id = id;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void add(SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put("i_cat_description", this.description);
        db.insert("income_category", null, values);
        values.clear();
        values.put("i_sub_cat_description", "Unspecified");
        values.put("i_cat_id", this.id);
        db.insert("income_sub_category", null, values);
        db.close();
    }

    public void remove(SQLiteDatabase db){
        db.execSQL("DELETE FROM income_category WHERE i_cat_id = " + this.id);
        db.execSQL("DELETE FROM income_sub_category WHERE i_cat_id = " + this.id);
    }

    public void update(SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put("i_cat_description", this.description);
        db.update("income_category", values, "i_cat_id = " + this.id, null);
        db.close();
    }

    public static ArrayList<IncomeCategory> getAll(SQLiteDatabase db){
        ArrayList<IncomeCategory>  categories = new ArrayList<IncomeCategory>();
        Cursor cursor = db.rawQuery("SELECT * FROM income_category", null);
        while(cursor.moveToNext()){
            categories.add(new IncomeCategory(cursor.getInt(0), cursor.getString(1)));
        }
        db.close();
        return  categories;
    }

    public static IncomeCategory getById(int id, SQLiteDatabase db){
        Cursor cursor = db.rawQuery("SELECT * FROM income_category WHERE i_cat_id = " + id, null);
        if(cursor.moveToFirst()){
            return new IncomeCategory(id, cursor.getString(1));
        }
        return null;
    }

    public static IncomeCategory getByDescription(String description, SQLiteDatabase db){
        Cursor cursor = db.rawQuery("SELECT * FROM income_category WHERE description = '" + description + "'", null);
        if(cursor.moveToFirst()){
            return new IncomeCategory(cursor.getInt(0), description);
        }
        return null;
    }
}
