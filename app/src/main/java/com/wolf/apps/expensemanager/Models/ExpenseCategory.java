package com.wolf.apps.expensemanager.Models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

class ExpenseCategory {
    private int id;
    private String description;

    public ExpenseCategory(int id, String description){
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
        values.put("e_cat_description", this.description);
        db.insert("expense_category", null, values);
        db.close();
    }

    public void remove(SQLiteDatabase db){
        db.execSQL("DELETE FROM expense_category WHERE e_cat_id = " + this.id);
    }

    public void update(SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put("e_cat_description", this.description);
        db.update("expense_category", values, "e_cat_id = " + this.id, null);
        db.close();
    }

    public static ArrayList<ExpenseCategory> getAll(SQLiteDatabase db){
        ArrayList<ExpenseCategory>  categories = new ArrayList<ExpenseCategory>();
        Cursor cursor = db.rawQuery("SELECT * FROM expense_category", null);
        while(cursor.moveToNext()){
            categories.add(new ExpenseCategory(cursor.getInt(0), cursor.getString(1)));
        }
        db.close();
        return  categories;
    }

    public static ExpenseCategory getById(int id, SQLiteDatabase db){
        Cursor cursor = db.rawQuery("SELECT * FROM expense_category WHERE e_cat_id = " + id, null);
        if(cursor.moveToFirst()){
            return new ExpenseCategory(id, cursor.getString(1));
        }
        return null;
    }

    public static ExpenseCategory getByDescription(String description, SQLiteDatabase db){
        Cursor cursor = db.rawQuery("SELECT * FROM expense_category WHERE description = '" + description + "'", null);
        if(cursor.moveToFirst()){
            return new ExpenseCategory(cursor.getInt(0), description);
        }
        return null;
    }
}
