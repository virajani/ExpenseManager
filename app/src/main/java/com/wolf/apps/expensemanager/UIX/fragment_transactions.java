package com.wolf.apps.expensemanager.UIX;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.wolf.apps.expensemanager.Models.ExpenseDbManager;
import com.wolf.apps.expensemanager.Models.ExpenseDetails;
import com.wolf.apps.expensemanager.Models.IncomeCategory;
import com.wolf.apps.expensemanager.Models.IncomeDetails;
import com.wolf.apps.expensemanager.Models.TransactionDetails;
import com.wolf.apps.expensemanager.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_transactions extends Fragment implements View.OnClickListener{

    ExpenseDbManager db;
    TextView txt_Date;
    static Calendar current_date;
    Button btn_AddExpense, btn_AddIncome, btn_Stats, btn_Filter;
    ListView list_transactions;
    ArrayAdapter<TransactionDetails> transactions_adapter;
    ArrayList<TransactionDetails> transactions_array;

    public fragment_transactions() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_transactions, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = new ExpenseDbManager(this.getContext());
        current_date = Calendar.getInstance();
        txt_Date = view.findViewById(R.id.txt_mainDate);
        txt_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(fragment_transactions.this.getContext(), CalendarActivity.class));
            }
        });

        btn_AddExpense = view.findViewById(R.id.btn_MainNewExpense);
        btn_AddIncome = view.findViewById(R.id.btn_MainNewIncome);
        btn_Stats = view.findViewById(R.id.btn_MainStats);
        btn_Filter = view.findViewById(R.id.btn_mainFilter);

        btn_AddIncome.setOnClickListener(this);
        btn_AddExpense.setOnClickListener(this);
        btn_Stats.setOnClickListener(this);
        btn_Filter.setOnClickListener(this);

        list_transactions = view.findViewById(R.id.list_TransactionsMain);
        arrangeList();

        list_transactions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TransactionDetails transactionDetails = (TransactionDetails) list_transactions.getAdapter().getItem(position);

                if(transactionDetails instanceof ExpenseDetails){
                    ActivityExpenseHandler.enableUpdate((ExpenseDetails) transactionDetails);
                    Intent i = new Intent(fragment_transactions.this.getContext(), ActivityExpenseHandler.class);
                    startActivityForResult(i, 1);
                }
                else{
                    ActivityIncomeHandler.enableUpdate((IncomeDetails) transactionDetails);
                    Intent i = new Intent(fragment_transactions.this.getContext(), ActivityIncomeHandler.class);
                    startActivityForResult(i, 1);
                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        txt_Date.setText("< " + current_date.get(Calendar.YEAR) + "/" + (current_date.get(Calendar.MONTH) + 1) + "/" + current_date.get(Calendar.DATE) + " " + current_date.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) +  " >");
    }



    public static void setCurrentDate(Calendar date){
        current_date = date;
    }

    @Override
    public void onClick(View v) {
            switch(v.getId()){
                case R.id.btn_MainNewExpense:
                    startActivityForResult(new Intent(this.getContext(), ActivityExpenseHandler.class), 1);
                    return;
                case R.id.btn_MainNewIncome:
                    startActivityForResult(new Intent(this.getContext(), ActivityIncomeHandler.class), 1);
                    return;
                case R.id.btn_MainStats:
                    return;
                case R.id.btn_mainFilter:
                    startActivityForResult(new Intent(this.getContext(), FilterActivity.class), 1);
                    return;
                default:
                    return;
            }
    }

    class TransactionArrayAdapter extends ArrayAdapter<TransactionDetails>{

        public TransactionArrayAdapter(@NonNull Context context, @NonNull List<TransactionDetails> objects) {
            super(context, 0, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            TransactionDetails transactionDetails = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.transaction_holder, parent, false);
            }

            TextView txt_date, txt_month_and_year, txt_day_of_week, txt_account, txt_amount, txt_category, txt_sub_category, txt_description;
            txt_date = (TextView)convertView.findViewById(R.id.txt_date);
            txt_month_and_year = (TextView)convertView.findViewById(R.id.txt_monthAndYear);
            txt_day_of_week = (TextView)convertView.findViewById(R.id.txt_dayOfWeek);
            txt_account = (TextView)convertView.findViewById(R.id.txt_account);
            txt_amount = (TextView)convertView.findViewById(R.id.txt_amount);
            txt_category = (TextView)convertView.findViewById(R.id.txt_category);
            txt_sub_category = (TextView)convertView.findViewById(R.id.txt_subCategory);
            txt_description = (TextView)convertView.findViewById(R.id.txt_description);

            txt_date.setText(Integer.toString(transactionDetails.getDate().get(Calendar.DATE)));
            txt_day_of_week.setText(transactionDetails.getDate().getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));
            txt_month_and_year.setText((transactionDetails.getDate().get(Calendar.MONTH) + 1) + "." + transactionDetails.getDate().get(Calendar.YEAR));
            txt_account.setText(transactionDetails.getAccount().getAccount_name());
            txt_amount.setText(Double.toString(transactionDetails.getAmount()));
            txt_description.setText(transactionDetails.getDescription());

            if(transactionDetails instanceof IncomeDetails){
                txt_category.setText(((IncomeDetails) transactionDetails).getIncomeSubCategory().getIncomeCategory().getDescription());
                txt_sub_category.setText(((IncomeDetails) transactionDetails).getIncomeSubCategory().getDescription());
                txt_amount.setTextColor(Color.GREEN);
            }
            else{
                txt_category.setText(((ExpenseDetails) transactionDetails).getExpenseSubCategory().getExpenseCategory().getDescription());
                txt_sub_category.setText(((ExpenseDetails) transactionDetails).getExpenseSubCategory().getDescription());
                txt_amount.setTextColor(Color.RED);
            }

            return convertView;
        }
    }

    private void arrangeList(){
        transactions_array = new ArrayList<>();
        transactions_array.addAll(ExpenseDetails.getAll(db.getReadableDatabase()));
        transactions_array.addAll(IncomeDetails.getAll(db.getReadableDatabase()));
        transactions_array = TransactionDetails.sortByDates(transactions_array);
        transactions_adapter = new TransactionArrayAdapter(this.getContext(), transactions_array);
        transactions_adapter.notifyDataSetChanged();
        list_transactions.setAdapter(transactions_adapter);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        arrangeList();
    }
}
