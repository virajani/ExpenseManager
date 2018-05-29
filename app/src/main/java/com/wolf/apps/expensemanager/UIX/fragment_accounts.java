package com.wolf.apps.expensemanager.UIX;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.wolf.apps.expensemanager.Models.Account;
import com.wolf.apps.expensemanager.Models.ExpenseDbManager;
import com.wolf.apps.expensemanager.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_accounts extends Fragment {


    ListView list;
    ArrayAdapter<Account> list_adapter;
    ExpenseDbManager db;

    public fragment_accounts() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_accounts, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton fab_add = (FloatingActionButton) view.findViewById(R.id.fab_AddAccount);

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(fragment_accounts.this.getContext(), AccountHandlerActivity.class));
                refreshSetAll();
            }
        });

        db = new ExpenseDbManager(getContext());
        list = (ListView) view.findViewById(R.id.list_accounts);
        list_adapter = new AccountsArrayAdapter(getContext(), Account.getAll(db.getReadableDatabase()));
        list.setAdapter(list_adapter);

    }


    class AccountsArrayAdapter extends ArrayAdapter<Account>{

        public AccountsArrayAdapter(@NonNull Context context, @NonNull List<Account> objects) {
            super(context, 0, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            Account account = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.account_holder, parent, false);
            }

            TextView txt_AccountName = (TextView) convertView.findViewById(R.id.txt_AccountHolderName);
            TextView txt_AccountType = (TextView) convertView.findViewById(R.id.txt_AccountHolderType);
            TextView txt_AccountBalance = (TextView) convertView.findViewById(R.id.txt_AccountHolderBalance);

            txt_AccountName.setText(account.getAccount_name());
            txt_AccountType.setText(account.getType().name());
            txt_AccountBalance.setText(Double.toString(account.getAmount()));

            return convertView;
        }
    }

    public void refreshSetAll(){
        list_adapter = new AccountsArrayAdapter(getContext(), Account.getAll(db.getReadableDatabase()));
        list.setAdapter(list_adapter);
    }

    public void refreshSetCash(){
        list_adapter = new AccountsArrayAdapter(getContext(), Account.getAllByType(Account.types.CASH, db.getReadableDatabase()));
        list.setAdapter(list_adapter);
    }

    public void refreshSetCredit(){
        list_adapter = new AccountsArrayAdapter(getContext(), Account.getAllByType(Account.types.CREDIT, db.getReadableDatabase()));
        list.setAdapter(list_adapter);
    }

    public void refreshSetDebit(){
        list_adapter = new AccountsArrayAdapter(getContext(), Account.getAllByType(Account.types.DEBIT, db.getReadableDatabase()));
        list.setAdapter(list_adapter);
    }


}
