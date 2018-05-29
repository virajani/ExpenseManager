package com.wolf.apps.expensemanager.Models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ExpenseDbManager extends SQLiteOpenHelper {

    public ExpenseDbManager(Context context){
        super(context, "expenseDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String tbl_income_category = "CREATE TABLE income_category (i_cat_id INTEGER PRIMARY KEY AUTOINCREMENT, i_cat_description TEXT)";
        String tbl_income_sub_category = "CREATE TABLE income_sub_category( i_sub_cat_id INTEGER PRIMARY KEY AUTOINCREMENT, i_sub_cat_description TEXT, i_cat_id INTEGER, FOREIGN KEY(i_cat_id) REFERENCES income_category(i_cat_id))";
        String tbl_income_details = "CREATE TABLE income_details( i_details_id INTEGER PRIMARY KEY AUTOINCREMENT, i_details_amount NUMERIC, i_details_description TEXT, transaction_type int, date BIGINT, i_sub_cat_id INTEGER, FOREIGN KEY(i_sub_cat_id) REFERENCES income_sub_category(i_sub_cat_id))";

        String tbl_expense_category = "CREATE TABLE expense_category (e_cat_id INTEGER PRIMARY KEY AUTOINCREMENT, e_cat_description TEXT)";
        String tbl_expense_sub_category = "CREATE TABLE expense_sub_category( e_sub_cat_id INTEGER PRIMARY KEY AUTOINCREMENT, e_sub_cat_description TEXT, e_cat_id INTEGER, FOREIGN KEY(e_cat_id) REFERENCES expense_category(e_cat_id))";
        String tbl_expense_details = "CREATE TABLE expense_details( e_details_id INTEGER PRIMARY KEY AUTOINCREMENT, e_details_amount NUMERIC, e_details_description TEXT, transaction_type int, date BIGINT, e_sub_cat_id INTEGER, FOREIGN KEY(e_sub_cat_id) REFERENCES expense_sub_category(e_sub_cat_id))";

        db.execSQL(tbl_income_category);
        db.execSQL(tbl_income_sub_category);
        db.execSQL(tbl_income_details);
        db.execSQL(tbl_expense_category);
        db.execSQL(tbl_expense_sub_category);
        db.execSQL(tbl_expense_details);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists income_category");
        db.execSQL("drop table if exists income_sub_category");
        db.execSQL("drop table if exists income_details");

        db.execSQL("drop table if exists expense_category");
        db.execSQL("drop table if exists expense_sub_category");
        db.execSQL("drop table if exists expense_details");

        onCreate(db);
    }


}
