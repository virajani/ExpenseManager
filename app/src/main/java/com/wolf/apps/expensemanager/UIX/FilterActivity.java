package com.wolf.apps.expensemanager.UIX;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.wolf.apps.expensemanager.Models.Account;
import com.wolf.apps.expensemanager.Models.ExpenseDbManager;
import com.wolf.apps.expensemanager.R;

import java.util.ArrayList;
import java.util.List;

public class FilterActivity extends AppCompatActivity {

    ExpenseDbManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_filter);

        Spinner txt_time = (Spinner) findViewById(R.id.txt_Time);
        Spinner txt_type = (Spinner) findViewById(R.id.txt_Type);
        Spinner txt_account = (Spinner) findViewById(R.id.txt_Account);

        ArrayAdapter<String> time_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<String>(){
            {
                add("All");
                add("Yearly");
                add("Monthly");
                add("Daily");
            }
        });

        txt_time.setAdapter(time_adapter);

        ArrayAdapter<String> type_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<String>(){
            {
                add("All");
                add("Expense");
                add("Income");
            }
        });

        txt_type.setAdapter(type_adapter);

        db = new ExpenseDbManager(this);

        AccountsArrayAdapter accounts_adapter = new AccountsArrayAdapter(this, Account.getAll(db.getReadableDatabase()));
        txt_account.setAdapter(accounts_adapter);
    }


    class AccountsArrayAdapter extends ArrayAdapter<Account>{

        public AccountsArrayAdapter(@NonNull Context context, @NonNull List<Account> objects) {
            super(context, android.R.layout.simple_spinner_dropdown_item, android.R.id.text1, objects);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            TextView label = (TextView) super.getView(position, convertView, parent);
            label.setTextColor(Color.BLACK);
            label.setText(getItem(position).getAccount_name());
            return label;
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            TextView label = (TextView) super.getDropDownView(position, convertView, parent);
            label.setTextColor(Color.BLACK);
            label.setText(getItem(position).getAccount_name());

            return label;
        }
    }
}
