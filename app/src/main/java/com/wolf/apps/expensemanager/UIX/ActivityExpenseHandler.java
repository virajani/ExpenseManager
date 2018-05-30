package com.wolf.apps.expensemanager.UIX;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.wolf.apps.expensemanager.Models.Account;
import com.wolf.apps.expensemanager.Models.ExpenseCategory;
import com.wolf.apps.expensemanager.Models.ExpenseDbManager;
import com.wolf.apps.expensemanager.Models.ExpenseDetails;
import com.wolf.apps.expensemanager.Models.ExpenseSubCategory;
import com.wolf.apps.expensemanager.Models.IncomeDetails;
import com.wolf.apps.expensemanager.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ActivityExpenseHandler extends AppCompatActivity implements View.OnClickListener{

    static ExpenseDetails current_expense_details;
    static Boolean mode;
    ExpenseDbManager db;
    TextView txt_date, txt_amount, txt_description;
    Spinner txt_category, txt_account, txt_repeat, txt_sub_category;
    Button btn_save;
    ImageButton btn_delete;
    ArrayAdapter<ExpenseCategory> category_adapter;
    ArrayAdapter<ExpenseSubCategory> sub_category_adapter;
    ArrayAdapter<Account> account_adapter;
    ArrayAdapter<String> repeat_adapter;
    Calendar today;

    public ActivityExpenseHandler() {
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
        category_adapter = new ExpenseCategoryArrayAdapter(this, ExpenseCategory.getAll(db.getReadableDatabase()));
        account_adapter = new AccountsArrayAdapter(this, Account.getAll(db.getReadableDatabase()));
        txt_category.setAdapter(category_adapter);
        txt_account.setAdapter(account_adapter);

        txt_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sub_category_adapter = new ExpenseSubCategoryArrayAdapter(ActivityExpenseHandler.this, ExpenseSubCategory.getByCategory((ExpenseCategory) txt_category.getAdapter().getItem(position), db.getReadableDatabase()));
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
            txt_date.setText(current_expense_details.getDate().get(Calendar.YEAR) + "/" + current_expense_details.getDate().get(Calendar.MONTH) + "/" + current_expense_details.getDate().get(Calendar.DATE) + " " + current_expense_details.getDate().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));
            txt_category.setSelection(category_adapter.getPosition(current_expense_details.getExpenseSubCategory().getExpenseCategory()));
            sub_category_adapter = new ExpenseSubCategoryArrayAdapter(this, ExpenseSubCategory.getByCategory(current_expense_details.getExpenseSubCategory().getExpenseCategory(), db.getReadableDatabase()));
            txt_sub_category.setAdapter(sub_category_adapter);
            txt_sub_category.setSelection(sub_category_adapter.getPosition(current_expense_details.getExpenseSubCategory()));
            txt_account.setSelection(account_adapter.getPosition(current_expense_details.getAccount()));
            txt_amount.setText(Float.toString(current_expense_details.getAmount()));
            txt_description.setText(current_expense_details.getDescription());
        }



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_TransationHandlerSave:
                if(!txt_amount.getText().toString().matches("")){
                    if(mode == false){
                        new ExpenseDetails(0, Float.parseFloat(txt_amount.getText().toString()), txt_description.getText().toString(), new Date(today.getTimeInMillis()), sub_category_adapter.getItem(txt_sub_category.getSelectedItemPosition()), account_adapter.getItem(txt_account.getSelectedItemPosition())).add(db.getWritableDatabase());
                        finish();
                        return;
                    }
                    current_expense_details.setAmount(Float.parseFloat(txt_amount.getText().toString()));
                    current_expense_details.setDescription(txt_description.getText().toString());
                    current_expense_details.update(db.getWritableDatabase());
                    finish();
                }
                return;
            case R.id.btn_TransactionHandlerDelete:
                if(mode == true){
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    removeCurrentTransaction();
                                    finish();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Delete this transaction? Are you sure?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }
                return;
            default:
                return;
        }
    }

    class ExpenseCategoryArrayAdapter extends ArrayAdapter<ExpenseCategory>{

        public ExpenseCategoryArrayAdapter(@NonNull Context context, @NonNull List<ExpenseCategory> objects) {
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

    class ExpenseSubCategoryArrayAdapter extends ArrayAdapter<ExpenseSubCategory>{

        public ExpenseSubCategoryArrayAdapter(@NonNull Context context, @NonNull List<ExpenseSubCategory> objects) {
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


    public static void enableUpdate(ExpenseDetails expenseDetails){
        current_expense_details = expenseDetails;
        mode = true;
    }

    private void removeCurrentTransaction(){
        if(current_expense_details != null){
            current_expense_details.remove(db.getWritableDatabase());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        current_expense_details = null;
        mode = null;
    }
}
