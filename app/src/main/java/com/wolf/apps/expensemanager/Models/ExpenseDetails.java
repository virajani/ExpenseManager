package com.wolf.apps.expensemanager.Models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;

class ExpenseDetails {
    public enum transaction_types{CASH,CREDIT,DEBIT};

    private int id;
    private float amount;
    private String description;
    private Date date;
    private transaction_types tr_type;
    private ExpenseSubCategory expenseSubCategory;

    public ExpenseDetails(int id, float amount, String description, transaction_types tr_type, Date date, ExpenseSubCategory expenseSubCategory){
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.tr_type = tr_type;
        this.date = date;
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

    public transaction_types getTr_type() {
        return tr_type;
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

    public void setTr_type(transaction_types tr_type) {
        this.tr_type = tr_type;
    }

    public void add(SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put("e_details_description", this.description);
        values.put("e_details_amount", this.amount);
        values.put("transaction_type", this.tr_type.ordinal());
        values.put("e_sub_cat_id", this.expenseSubCategory.getId());
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
        values.put("transaction_type", this.tr_type.ordinal());
        db.update("expense_details", values, "e_details_id = " + this.id, null);
        db.close();
    }

    public static ArrayList<ExpenseDetails> getAll(SQLiteDatabase db){
        ArrayList<ExpenseDetails>  expense_details = new ArrayList<ExpenseDetails>();
        Date dt = new Date();
        Cursor cursor = db.rawQuery("SELECT * FROM expense_details", null);
        while(cursor.moveToNext()){
            dt.setTime(cursor.getLong(4));
            expense_details.add(new ExpenseDetails(cursor.getInt(0), cursor.getFloat(1), cursor.getString(2), transaction_types.values()[cursor.getInt(3)], dt, ExpenseSubCategory.getById(cursor.getInt(5),db)));
        }
        db.close();
        return  expense_details;
    }

    public static ExpenseDetails getById(int id, SQLiteDatabase db){
        Cursor cursor = db.rawQuery("SELECT * FROM expense_details WHERE e_details_id = " + id, null);
        Date dt = new Date();
        if(cursor.moveToFirst()){
            dt.setTime(cursor.getLong(4));
            return new ExpenseDetails(id , cursor.getFloat(1), cursor.getString(2), transaction_types.values()[cursor.getInt(3)], dt,  ExpenseSubCategory.getById(cursor.getInt(5),db));
        }
        return null;
    }

    public static ExpenseDetails getByDescription(String description, SQLiteDatabase db){
        Cursor cursor = db.rawQuery("SELECT * FROM expense_details WHERE e_details_description = '" + description + "'", null);
        Date dt = new Date();
        if(cursor.moveToFirst()){
            dt.setTime(cursor.getLong(4));
            return new ExpenseDetails(cursor.getInt(0) , cursor.getFloat(1), description, transaction_types.values()[cursor.getInt(3)], dt, ExpenseSubCategory.getById(cursor.getInt(5),db));
        }
        return null;
    }

    public static ArrayList<ExpenseDetails> getByExpenseSubCategory(ExpenseSubCategory category, SQLiteDatabase db){
        ArrayList<ExpenseDetails>  expense_details = new ArrayList<ExpenseDetails>();
        Date dt = new Date();
        Cursor cursor = db.rawQuery("SELECT * FROM expense_details WHERE e_sub_cat_id = " + category.getId(), null);
        while(cursor.moveToNext()){
            dt.setTime(cursor.getLong(4));
            expense_details.add(new ExpenseDetails(cursor.getInt(0), cursor.getFloat(1), cursor.getString(2), transaction_types.values()[cursor.getInt(3)], dt, category));
        }
        db.close();
        return  expense_details;
    }

    public static ArrayList<ExpenseDetails> getByTransactionType(transaction_types type, SQLiteDatabase db){
        ArrayList<ExpenseDetails>  expense_details = new ArrayList<ExpenseDetails>();
        Date dt = new Date();
        Cursor cursor = db.rawQuery("SELECT * FROM expense_details WHERE transatcion_type = " +  type.ordinal(), null);
        while(cursor.moveToNext()){
            dt.setTime(cursor.getLong(4));
            expense_details.add(new ExpenseDetails(cursor.getInt(0), cursor.getFloat(1), cursor.getString(2), type, dt,  ExpenseSubCategory.getById(cursor.getInt(5),db)));
        }
        db.close();
        return  expense_details;
    }
}
