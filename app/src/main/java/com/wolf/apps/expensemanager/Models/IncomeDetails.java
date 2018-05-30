package com.wolf.apps.expensemanager.Models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class IncomeDetails extends TransactionDetails{

    private IncomeSubCategory incomeSubCategory;


    public IncomeDetails(int id, float amount, String description, Date date, IncomeSubCategory incomeSubCategory, Account account){
        super(id, amount, description, date, account);
        this.incomeSubCategory = incomeSubCategory;
    }

    public IncomeSubCategory getIncomeSubCategory() {
        return incomeSubCategory;
    }

    public Calendar getDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }


    public void add(SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put("i_details_description", this.description);
        values.put("i_details_amount", this.amount);
        values.put("date", date.getTime());
        values.put("ac_id", this.account.getAccount_id());
        values.put("i_sub_cat_id", this.incomeSubCategory.getId());
        db.insert("income_details", null, values);
        db.close();
    }

    public void remove(SQLiteDatabase db){
        db.execSQL("DELETE FROM income_details WHERE i_details_id = " + this.id);
    }

    public void update(SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put("i_details_amount", this.amount);
        values.put("i_details_description", this.description);
        db.update("income_details", values, "i_details_id = " + this.id, null);
        db.close();
    }

    public static ArrayList<IncomeDetails> getAll(SQLiteDatabase db){
        ArrayList<IncomeDetails>  income_details = new ArrayList<IncomeDetails>();
        Date dt = new Date();
        Cursor cursor = db.rawQuery("SELECT * FROM income_details", null);
        while(cursor.moveToNext()){
            dt.setTime(cursor.getLong(3));
            income_details.add(new IncomeDetails(cursor.getInt(0) , cursor.getFloat(1), cursor.getString(2), dt, IncomeSubCategory.getById(cursor.getInt(4),db), Account.getById(cursor.getInt(5), db)));
        }
        db.close();
        return  income_details;
    }

    public static IncomeDetails getById(int id, SQLiteDatabase db){
        Cursor cursor = db.rawQuery("SELECT * FROM income_details WHERE i_details_id = " + id, null);
        Date dt = new Date();
        if(cursor.moveToFirst()){
            dt.setTime(cursor.getLong(3));
            return new IncomeDetails(id , cursor.getFloat(1), cursor.getString(2), dt, IncomeSubCategory.getById(cursor.getInt(4),db), Account.getById(cursor.getInt(5), db));
        }
        return null;
    }

    public static IncomeDetails getByDescription(String description, SQLiteDatabase db){
        Cursor cursor = db.rawQuery("SELECT * FROM income_details WHERE i_details_description = '" + description + "'", null);
        Date dt = new Date();
        if(cursor.moveToFirst()){
            dt.setTime(cursor.getLong(3));
            return new IncomeDetails(cursor.getInt(0) , cursor.getFloat(1), cursor.getString(2), dt, IncomeSubCategory.getById(cursor.getInt(4),db), Account.getById(cursor.getInt(5), db));
        }
        return null;
    }

    public static ArrayList<IncomeDetails> getByIncomeSubCategory(IncomeSubCategory category, SQLiteDatabase db){
        ArrayList<IncomeDetails>  income_details = new ArrayList<IncomeDetails>();
        Date dt = new Date();
        Cursor cursor = db.rawQuery("SELECT * FROM income_details WHERE i_sub_cat_id = " + category.getId(), null);
        while(cursor.moveToNext()){
            dt.setTime(cursor.getLong(3));
            income_details.add(new IncomeDetails(cursor.getInt(0) , cursor.getFloat(1), cursor.getString(2), dt, IncomeSubCategory.getById(cursor.getInt(4),db), Account.getById(cursor.getInt(5), db)));
        }
        db.close();
        return  income_details;
    }

    public static ArrayList<IncomeDetails> getByAccountType(Account account, SQLiteDatabase db){
        ArrayList<IncomeDetails>  income_details = new ArrayList<IncomeDetails>();
        Date dt = new Date();
        Cursor cursor = db.rawQuery("SELECT * FROM income_details WHERE ac_id = " +  account.getAccount_id(), null);
        while(cursor.moveToNext()){
            dt.setTime(cursor.getLong(3));
            income_details.add(new IncomeDetails(cursor.getInt(0) , cursor.getFloat(1), cursor.getString(2), dt, IncomeSubCategory.getById(cursor.getInt(4),db), Account.getById(cursor.getInt(5), db)));
        }
        db.close();
        return  income_details;
    }

    public static void removeByIncomeSubCategory(IncomeSubCategory incomeSubCategory, SQLiteDatabase db){
        db.execSQL("DELETE FROM income_details WHERE i_sub_cat_id = " + incomeSubCategory.getId());
    }

}
