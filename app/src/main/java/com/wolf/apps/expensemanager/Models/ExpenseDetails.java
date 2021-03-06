package com.wolf.apps.expensemanager.Models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;

class ExpenseDetails {

    private int id;
    private float amount;
    private String description;
    private Date date;
    private Account account;
    private ExpenseSubCategory expenseSubCategory;

    public ExpenseDetails(int id, float amount, String description, Date date, ExpenseSubCategory expenseSubCategory, Account account){
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.account = account;
        this.expenseSubCategory = expenseSubCategory;
    }

    public int getId() {
        return id;
    }

    public float getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }


    public ExpenseSubCategory getExpenseSubCategory() {
        return expenseSubCategory;
    }

    public Calendar getDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Account getAccount() {
        return account;
    }


    public void add(SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put("e_details_description", this.description);
        values.put("e_details_amount", this.amount);
        values.put("e_sub_cat_id", this.expenseSubCategory.getId());
        values.put("ac_id", this.account.getAccount_id());
        values.put("date", date.getTime());
        db.insert("expense_details", null, values);
        db.close();
    }

    public void remove(SQLiteDatabase db){
        db.execSQL("DELETE FROM expense_details WHERE e_details_id = " + this.id);
    }

    public void update(SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put("e_details_amount", this.amount);
        values.put("e_details_description", this.description);
        db.update("expense_details", values, "e_details_id = " + this.id, null);
        db.close();
    }

    public static ArrayList<ExpenseDetails> getAll(SQLiteDatabase db){
        ArrayList<ExpenseDetails>  expense_details = new ArrayList<ExpenseDetails>();
        Date dt = new Date();
        Cursor cursor = db.rawQuery("SELECT * FROM expense_details", null);
        while(cursor.moveToNext()){
            dt.setTime(cursor.getLong(3));
            expense_details.add(new ExpenseDetails(cursor.getInt(0), cursor.getFloat(1), cursor.getString(2), dt, ExpenseSubCategory.getById(cursor.getInt(4),db), Account.getById(cursor.getInt(5), db)));
        }
        db.close();
        return  expense_details;
    }

    public static ExpenseDetails getById(int id, SQLiteDatabase db){
        Cursor cursor = db.rawQuery("SELECT * FROM expense_details WHERE e_details_id = " + id, null);
        Date dt = new Date();
        if(cursor.moveToFirst()){
            dt.setTime(cursor.getLong(3));
            return new ExpenseDetails(cursor.getInt(0), cursor.getFloat(1), cursor.getString(2), dt, ExpenseSubCategory.getById(cursor.getInt(4),db), Account.getById(cursor.getInt(5), db));
        }
        return null;
    }

    public static ExpenseDetails getByDescription(String description, SQLiteDatabase db){
        Cursor cursor = db.rawQuery("SELECT * FROM expense_details WHERE e_details_description = '" + description + "'", null);
        Date dt = new Date();
        if(cursor.moveToFirst()){
            dt.setTime(cursor.getLong(3));
            return new ExpenseDetails(cursor.getInt(0), cursor.getFloat(1), cursor.getString(2),dt, ExpenseSubCategory.getById(cursor.getInt(4),db), Account.getById(cursor.getInt(5), db));
        }
        return null;
    }

    public static ArrayList<ExpenseDetails> getByExpenseSubCategory(ExpenseSubCategory category, SQLiteDatabase db){
        ArrayList<ExpenseDetails>  expense_details = new ArrayList<ExpenseDetails>();
        Date dt = new Date();
        Cursor cursor = db.rawQuery("SELECT * FROM expense_details WHERE e_sub_cat_id = " + category.getId(), null);
        while(cursor.moveToNext()){
            dt.setTime(cursor.getLong(3));
            expense_details.add(new ExpenseDetails(cursor.getInt(0), cursor.getFloat(1), cursor.getString(2), dt, ExpenseSubCategory.getById(cursor.getInt(4),db), Account.getById(cursor.getInt(5), db)));
        }
        db.close();
        return  expense_details;
    }

    public static ArrayList<ExpenseDetails> getByAccountType(Account account, SQLiteDatabase db){
        ArrayList<ExpenseDetails>  expense_details = new ArrayList<ExpenseDetails>();
        Date dt = new Date();
        Cursor cursor = db.rawQuery("SELECT * FROM expense_details WHERE ac_id = " +  account.getAccount_id(), null);
        while(cursor.moveToNext()){
            dt.setTime(cursor.getLong(3));
            expense_details.add(new ExpenseDetails(cursor.getInt(0), cursor.getFloat(1), cursor.getString(2), dt, ExpenseSubCategory.getById(cursor.getInt(4),db), Account.getById(cursor.getInt(5), db)));
        }
        db.close();
        return  expense_details;
    }
}
