package com.wolf.apps.expensemanager.Models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

class ExpenseSubCategory {
    private int id;
    private String description;
    private ExpenseCategory expenseCategory;

    public ExpenseSubCategory(int id, String description, ExpenseCategory expenseCategory){
        this.id = id;
        this.description = description;
        this.expenseCategory = expenseCategory;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public ExpenseCategory getExpenseCategory() {
        return expenseCategory;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void add(SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put("e_sub_cat_description", this.description);
        values.put("e_cat_id", this.expenseCategory.getId());
        db.insert("expense_sub_category", null, values);
        db.close();
    }

    public void remove(SQLiteDatabase db){
        db.execSQL("DELETE FROM expense_sub_category WHERE e_sub_cat_id = " + this.id);
    }

    public void update(SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put("e_sub_cat_description", this.description);
        db.update("expense_sub_category", values, "e_sub_cat_id = " + this.id, null);
        db.close();
    }

    public static ArrayList<ExpenseSubCategory> getAll(SQLiteDatabase db){
        ArrayList<ExpenseSubCategory>  sub_categories = new ArrayList<ExpenseSubCategory>();
        Cursor cursor = db.rawQuery("SELECT * FROM expense_sub_category", null);
        while(cursor.moveToNext()){
            sub_categories.add(new ExpenseSubCategory(cursor.getInt(0), cursor.getString(1), ExpenseCategory.getById(cursor.getInt(2), db)));
        }
        db.close();
        return  sub_categories;
    }

    public static ExpenseSubCategory getById(int id, SQLiteDatabase db){
        Cursor cursor = db.rawQuery("SELECT * FROM expense_sub_category WHERE e_sub_cat_id = " + id, null);
        if(cursor.moveToFirst()){
            return new ExpenseSubCategory(id, cursor.getString(1), ExpenseCategory.getById(cursor.getInt(2), db));
        }
        return null;
    }

    public static ExpenseSubCategory getByDescription(String description, SQLiteDatabase db){
        Cursor cursor = db.rawQuery("SELECT * FROM expense_sub_category WHERE description = '" + description + "'", null);
        if(cursor.moveToFirst()){
            return new ExpenseSubCategory(cursor.getInt(0), description, ExpenseCategory.getById(cursor.getInt(2), db));
        }
        return null;
    }

    public static ArrayList<ExpenseSubCategory> getByCategory(ExpenseCategory category, SQLiteDatabase db){
        ArrayList<ExpenseSubCategory>  sub_categories = new ArrayList<ExpenseSubCategory>();
        Cursor cursor = db.rawQuery("SELECT * FROM expense_sub_category WHERE e_cat_id = " + category.getId(), null);
        while(cursor.moveToNext()){
            sub_categories.add(new ExpenseSubCategory(cursor.getInt(0), cursor.getString(1), category));
        }
        db.close();
        return  sub_categories;
    }
}
