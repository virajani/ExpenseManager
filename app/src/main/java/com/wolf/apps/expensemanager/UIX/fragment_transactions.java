package com.wolf.apps.expensemanager.UIX;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.wolf.apps.expensemanager.R;

import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_transactions extends Fragment implements View.OnClickListener{

    TextView txt_Date;
    static Calendar current_date;
    Button btn_AddExpense, btn_AddIncome, btn_Stats;

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

        btn_AddIncome.setOnClickListener(this);
        btn_AddExpense.setOnClickListener(this);
        btn_Stats.setOnClickListener(this);

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
                    startActivity(new Intent(this.getContext(), ActivityExpenseHandler.class));
                    return;
                case R.id.btn_MainNewIncome:
                    startActivity(new Intent(this.getContext(), ActivityIncomeHandler.class));
                    return;
                case R.id.btn_MainStats:
                    return;
                case R.id.btn_mainFilter:
                    return;
                default:
                    return;
            }
    }
}
