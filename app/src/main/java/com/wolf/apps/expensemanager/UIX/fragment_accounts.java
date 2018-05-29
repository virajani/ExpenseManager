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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    RadioGroup rbg_Type ;
    RadioButton rbtn_Cash, rbtn_Credit, rbtn_Debit, rbtn_All;
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

        rbg_Type = view.findViewById(R.id.rbg_AccountsType);
        rbtn_Cash = view.findViewById(R.id.rbtn_AccountsCash);
        rbtn_Credit = view.findViewById(R.id.rbtn_AcountsCredit);
        rbtn_Debit = view.findViewById(R.id.rbtn_AccountsDebit);
        rbtn_All = view.findViewById(R.id.rbtn_AccountsAll);
        AccountsButtonListener accountsButtonListener = new AccountsButtonListener();
        rbtn_All.setOnClickListener(accountsButtonListener);
        rbtn_Cash.setOnClickListener(accountsButtonListener);
        rbtn_Debit.setOnClickListener(accountsButtonListener);
        rbtn_Credit.setOnClickListener(accountsButtonListener);
        rbg_Type.check(R.id.rbtn_AccountsAll);

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(fragment_accounts.this.getContext(), AccountHandlerActivity.class));
            }
        });

        db = new ExpenseDbManager(getContext());
        list = (ListView) view.findViewById(R.id.list_accounts);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AccountHandlerActivity.enableUpdate((Account)list.getAdapter().getItem(position));
                startActivity(new Intent(fragment_accounts.this.getContext(), AccountHandlerActivity.class));
            }
        });
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

    @Override
    public void onResume() {
        super.onResume();

        switch(rbg_Type.getCheckedRadioButtonId()){
            case R.id.rbtn_AccountsAll:
                refreshSetAll();
                return;
            case R.id.rbtn_AccountsCash:
                refreshSetCash();
                return;
            case R.id.rbtn_AcountsCredit:
                refreshSetCredit();
                return;
            case R.id.rbtn_AccountsDebit:
                refreshSetDebit();
                return;
            default:
                return;
        }
    }

    public void refreshSetAll(){
        list_adapter.clear();
        list_adapter.addAll(Account.getAll(db.getReadableDatabase()));
        list_adapter.notifyDataSetChanged();
    }

    public void refreshSetCash(){
        list_adapter.clear();
        list_adapter.addAll(Account.getAllByType(Account.types.CASH, db.getReadableDatabase()));
        list_adapter.notifyDataSetChanged();
    }

    public void refreshSetCredit(){
        list_adapter.clear();
        list_adapter.addAll(Account.getAllByType(Account.types.CREDIT, db.getReadableDatabase()));
        list_adapter.notifyDataSetChanged();
    }

    public void refreshSetDebit(){
        list_adapter.clear();
        list_adapter.addAll(Account.getAllByType(Account.types.DEBIT, db.getReadableDatabase()));
        list_adapter.notifyDataSetChanged();
    }


    class AccountsButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.rbtn_AccountsAll:
                    refreshSetAll();
                    return;
                case R.id.rbtn_AccountsCash:
                    refreshSetCash();
                    return;
                case R.id.rbtn_AcountsCredit:
                    refreshSetCredit();
                    return;
                case R.id.rbtn_AccountsDebit:
                    refreshSetDebit();
                    return;
                default:
                    return;
            }
        }
    }


}
