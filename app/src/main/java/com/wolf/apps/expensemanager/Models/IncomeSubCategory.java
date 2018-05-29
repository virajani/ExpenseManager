package com.wolf.apps.expensemanager.Models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

class IncomeSubCategory {
    private int id;
    private String description;
    private IncomeCategory incomeCategory;

    public IncomeSubCategory(int id, String description, IncomeCategory incomeCategory){
        this.id = id;
        this.description = description;
        this.incomeCategory = incomeCategory;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public IncomeCategory getIncomeCategory() {
        return incomeCategory;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void add(SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put("i_sub_cat_description", this.description);
        values.put("i_cat_id", this.incomeCategory.getId());
        db.insert("income_sub_category", null, values);
        db.close();
    }

    public void remove(SQLiteDatabase db){
        db.execSQL("DELETE FROM income_sub_category WHERE i_sub_cat_id = " + this.id);
    }

    public void update(SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put("i_sub_cat_description", this.description);
        db.update("income_sub_category", values, "i_sub_cat_id = " + this.id, null);
        db.close();
    }

    public static ArrayList<IncomeSubCategory> getAll(SQLiteDatabase db){
        ArrayList<IncomeSubCategory>  sub_categories = new ArrayList<IncomeSubCategory>();
        Cursor cursor = db.rawQuery("SELECT * FROM income_sub_category", null);
        while(cursor.moveToNext()){
            sub_categories.add(new IncomeSubCategory(cursor.getInt(0), cursor.getString(1), IncomeCategory.getById(cursor.getInt(2), db)));
        }
        db.close();
        return  sub_categories;
    }

    public static IncomeSubCategory getById(int id, SQLiteDatabase db){
        Cursor cursor = db.rawQuery("SELECT * FROM income_sub_category WHERE i_sub_cat_id = " + id, null);
        if(cursor.moveToFirst()){
            return new IncomeSubCategory(id, cursor.getString(1), IncomeCategory.getById(cursor.getInt(2), db));
        }
        return null;
    }

    public static IncomeSubCategory getByDescription(String description, SQLiteDatabase db){
        Cursor cursor = db.rawQuery("SELECT * FROM income_sub_category WHERE description = '" + description + "'", null);
        if(cursor.moveToFirst()){
            return new IncomeSubCategory(cursor.getInt(0), description, IncomeCategory.getById(cursor.getInt(2), db));
        }
        return null;
    }

    public static ArrayList<IncomeSubCategory> getByCategory(IncomeCategory category, SQLiteDatabase db){
        ArrayList<IncomeSubCategory>  sub_categories = new ArrayList<IncomeSubCategory>();
        Cursor cursor = db.rawQuery("SELECT * FROM income_sub_category WHERE i_cat_id = " + category.getId(), null);
        while(cursor.moveToNext()){
            sub_categories.add(new IncomeSubCategory(cursor.getInt(0), cursor.getString(1), category));
        }
        db.close();
        return  sub_categories;
    }
}
