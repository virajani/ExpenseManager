package com.wolf.apps.expensemanager.UIX;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.wolf.apps.expensemanager.Models.Account;
import com.wolf.apps.expensemanager.Models.ExpenseCategory;
import com.wolf.apps.expensemanager.Models.ExpenseDbManager;
import com.wolf.apps.expensemanager.Models.ExpenseDetails;
import com.wolf.apps.expensemanager.Models.ExpenseSubCategory;
import com.wolf.apps.expensemanager.Models.IncomeCategory;
import com.wolf.apps.expensemanager.Models.IncomeDetails;
import com.wolf.apps.expensemanager.Models.IncomeSubCategory;
import com.wolf.apps.expensemanager.R;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ActivityIncomeHandler extends AppCompatActivity implements View.OnClickListener{

    static IncomeDetails current_income_details;
    static Boolean mode;
    ExpenseDbManager db;
    TextView txt_date, txt_amount, txt_description;
    Spinner txt_category, txt_account, txt_repeat, txt_sub_category;
    Button btn_save;
    ImageButton btn_delete;
    ArrayAdapter<IncomeCategory> category_adapter;
    ArrayAdapter<IncomeSubCategory> sub_category_adapter;
    ArrayAdapter<Account> account_adapter;
    ArrayAdapter<String> repeat_adapter;
    Calendar today;

    public ActivityIncomeHandler() {
        super();
        if(mode == null){
            mode = false;
        }
        db = new ExpenseDbManager(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_handler);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_TransactionHandler);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Transactions");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        today = Calendar.getInstance();

        txt_date = (TextView)findViewById(R.id.txt_TransactionHandlerDate);
        txt_amount = (TextView)findViewById(R.id.txt_TransactionHandlerAmount);
        txt_description = (TextView)findViewById(R.id.txt_TransactionHandlerDescription);
        txt_category = (Spinner)findViewById(R.id.txt_TransactionHandlerCategory);
        txt_account = (Spinner)findViewById(R.id.txt_TransactionHandlerAccount);
        txt_repeat = (Spinner)findViewById(R.id.txt_TransactionHandlerRepeat);
        txt_sub_category = (Spinner)findViewById(R.id.txt_TransactionHandlerSubCategory);
        btn_save = (Button)findViewById(R.id.btn_TransationHandlerSave);
        btn_delete = (ImageButton)findViewById(R.id.btn_TransactionHandlerDelete);
        btn_save.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        category_adapter = new IncomeCategoryArrayAdapter(this, IncomeCategory.getAll(db.getReadableDatabase()));
        account_adapter = new AccountsArrayAdapter(this, Account.getAll(db.getReadableDatabase()));
        txt_category.setAdapter(category_adapter);
        txt_account.setAdapter(account_adapter);

        txt_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sub_category_adapter = new IncomeSubCategoryArrayAdapter(ActivityIncomeHandler.this, IncomeSubCategory.getByCategory((IncomeCategory) txt_category.getAdapter().getItem(position), db.getReadableDatabase()));
                txt_sub_category.setAdapter(sub_category_adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(mode == false){
            txt_date.setText(today.get(Calendar.YEAR) + "/" + today.get(Calendar.MONTH) + "/" + today.get(Calendar.DATE) + " " + today.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));
        }
        else{
            txt_date.setText(current_income_details.getDate().get(Calendar.YEAR) + "/" + current_income_details.getDate().get(Calendar.MONTH) + "/" + current_income_details.getDate().get(Calendar.DATE) + " " + current_income_details.getDate().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));
            txt_category.setSelection(category_adapter.getPosition(current_income_details.getIncomeSubCategory().getIncomeCategory()));
            sub_category_adapter = new IncomeSubCategoryArrayAdapter(this, IncomeSubCategory.getByCategory(current_income_details.getIncomeSubCategory().getIncomeCategory(), db.getReadableDatabase()));
            txt_sub_category.setAdapter(sub_category_adapter);
            txt_sub_category.setSelection(sub_category_adapter.getPosition(current_income_details.getIncomeSubCategory()));
        }



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_TransationHandlerSave:
                return;
            case R.id.btn_TransactionHandlerDelete:
                return;
            default:
                return;
        }
    }

    class IncomeCategoryArrayAdapter extends ArrayAdapter<IncomeCategory>{

        public IncomeCategoryArrayAdapter(@NonNull Context context, @NonNull List<IncomeCategory> objects) {
            super(context, android.R.layout.simple_spinner_dropdown_item, android.R.id.text1, objects);
        }

        public View getView(int position, View convertView,ViewGroup parent) {
            TextView label = (TextView) super.getView(position, convertView, parent);
            label.setTextColor(Color.BLACK);
            label.setText(getItem(position).getDescription());
            return label;
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            TextView label = (TextView) super.getDropDownView(position, convertView, parent);
            label.setTextColor(Color.BLACK);
            label.setText(getItem(position).getDescription());

            return label;
        }
    }

    class AccountsArrayAdapter extends ArrayAdapter<Account>{

        public AccountsArrayAdapter(@NonNull Context context, @NonNull List<Account> objects) {
            super(context, android.R.layout.simple_spinner_dropdown_item, android.R.id.text1, objects);
        }

        public View getView(int position, View convertView,ViewGroup parent) {
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

    class IncomeSubCategoryArrayAdapter extends ArrayAdapter<IncomeSubCategory>{

        public IncomeSubCategoryArrayAdapter(@NonNull Context context, @NonNull List<IncomeSubCategory> objects) {
            super(context, android.R.layout.simple_spinner_dropdown_item, android.R.id.text1, objects);
        }

        public View getView(int position, View convertView,ViewGroup parent) {
            TextView label = (TextView) super.getView(position, convertView, parent);
            label.setTextColor(Color.BLACK);
            label.setText(getItem(position).getDescription());
            return label;
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            TextView label = (TextView) super.getDropDownView(position, convertView, parent);
            label.setTextColor(Color.BLACK);
            label.setText(getItem(position).getDescription());
            return label;
        }
    }


    public static void enableUpdate(IncomeDetails incomeDetails){
        current_income_details = incomeDetails;
        mode = true;
    }

    private void removeCurrentTransaction(){
        if(current_income_details != null){
            current_income_details.remove(db.getWritableDatabase());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        current_income_details = null;
        mode = null;
    }
}
