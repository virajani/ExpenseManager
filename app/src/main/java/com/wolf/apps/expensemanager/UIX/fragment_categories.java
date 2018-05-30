package com.wolf.apps.expensemanager.UIX;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.wolf.apps.expensemanager.Models.ExpenseCategory;
import com.wolf.apps.expensemanager.Models.ExpenseDbManager;
import com.wolf.apps.expensemanager.Models.IncomeCategory;
import com.wolf.apps.expensemanager.R;

import java.util.List;

public class fragment_categories extends Fragment implements View.OnClickListener{

    ListView list_categories;
    ArrayAdapter<ExpenseCategory> expense_adapter;
    ArrayAdapter<IncomeCategory> income_adapter;
    Button btn_expense, btn_income;
    FloatingActionButton fab_add;
    ExpenseDbManager db;
    Boolean isExpense;

    public fragment_categories() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_categories, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = new ExpenseDbManager(this.getContext());
        btn_expense = view.findViewById(R.id.btn_CategoriesExpense);
        btn_income = view.findViewById(R.id.btn_CategoriesIncome);
        btn_income.setOnClickListener(this);
        btn_expense.setOnClickListener(this);
        fab_add = view.findViewById(R.id.fab_MainCategoriesAdd);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isExpense){
                    startActivity(new Intent(getContext(), ExpenseCategoryHandlerActivity.class));
                }else{
                    startActivity(new Intent(getContext(), IncomeCategoryHandlerActivity.class));
                }
            }
        });
        list_categories = view.findViewById(R.id.list_CategoriesMain);
        list_categories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(isExpense){
                    ExpenseCategoryHandlerActivity.enableUpdate((ExpenseCategory)list_categories.getAdapter().getItem(position));
                    startActivity(new Intent(getContext(), ExpenseCategoryHandlerActivity.class));
                }
                else{
                    IncomeCategoryHandlerActivity.enableUpdate((IncomeCategory)list_categories.getAdapter().getItem(position));
                    startActivity(new Intent(getContext(), IncomeCategoryHandlerActivity.class));
                }
            }
        });
        setModeExpense();
        arrangeOnMode();

    }

    public void setModeExpense(){
        isExpense = true;
    }

    public void setModeIncome(){
        isExpense = false;
    }

    private void arrangeOnMode(){
        if(isExpense){
            expense_adapter = new ExpenseArrayAdapter(this.getContext(), ExpenseCategory.getAll(db.getReadableDatabase()));
            expense_adapter.notifyDataSetChanged();
            list_categories.setAdapter(expense_adapter);
            btn_expense.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            btn_income.setBackgroundColor(android.R.drawable.btn_default);
        }
        else{
            income_adapter = new IncomeArrayAdapter(this.getContext(), IncomeCategory.getAll(db.getReadableDatabase()));
            income_adapter.notifyDataSetChanged();
            list_categories.setAdapter(income_adapter);
            btn_income.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            btn_expense.setBackgroundColor(android.R.drawable.btn_default);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_CategoriesExpense:
                setModeExpense();
                arrangeOnMode();
                return;
            case R.id.btn_CategoriesIncome:
                setModeIncome();
                arrangeOnMode();
                return;
            default:
                return;
        }
    }


    class ExpenseArrayAdapter extends ArrayAdapter<ExpenseCategory>{

        public ExpenseArrayAdapter(@NonNull Context context, @NonNull List<ExpenseCategory> objects) {
            super(context, 0, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ExpenseCategory expenseCategory = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.general_holder, parent, false);
            }

            TextView txt_name = convertView.findViewById(R.id.txt_GeneralHolderName);
            txt_name.setText(expenseCategory.getDescription());

            return convertView;
        }
    }

    class IncomeArrayAdapter extends ArrayAdapter<IncomeCategory>{

        public IncomeArrayAdapter(@NonNull Context context, @NonNull List<IncomeCategory> objects) {
            super(context, 0, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            IncomeCategory incomeCategory = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.general_holder, parent, false);
            }

            TextView txt_name = convertView.findViewById(R.id.txt_GeneralHolderName);
            txt_name.setText(incomeCategory.getDescription());

            return convertView;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        arrangeOnMode();
    }
}
