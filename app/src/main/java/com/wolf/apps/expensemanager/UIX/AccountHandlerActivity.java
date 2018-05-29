package com.wolf.apps.expensemanager.UIX;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wolf.apps.expensemanager.MainActivity;
import com.wolf.apps.expensemanager.Models.Account;
import com.wolf.apps.expensemanager.Models.ExpenseDbManager;
import com.wolf.apps.expensemanager.R;

public class AccountHandlerActivity extends AppCompatActivity implements View.OnClickListener{

    ExpenseDbManager db;
    static Account current_account;
    static Boolean mode;
    TextView txt_Name;
    TextView txt_Type;
    TextView txt_Balance;
    TextView txt_ID;
    Button btn_Save;
    ImageButton btn_Increase;
    ImageButton btn_Decrease;
    ImageButton btn_Delete;

    public AccountHandlerActivity() {
        super();
        mode = false;
        db = new ExpenseDbManager(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_handler);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_AccountHandler);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Accounts");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txt_ID = (TextView)findViewById(R.id.txt_AccountHandlerID);
        txt_Name = (TextView)findViewById(R.id.txt_AccountHandlerName);
        txt_Type = (TextView)findViewById(R.id.txt_AccountHandlerType);
        txt_Balance = (TextView) findViewById(R.id.txt_AccountHandlerBalance);
        btn_Save = (Button)findViewById(R.id.btn_AccountHandlerSave);
        btn_Delete = (ImageButton)findViewById(R.id.btn_AccountHandlerDelete);
        btn_Increase = (ImageButton)findViewById(R.id.btn_IncreaseBalance);
        btn_Decrease = (ImageButton)findViewById(R.id.btn_DecreaseBalance);
        btn_Save.setOnClickListener(this);
        btn_Delete.setOnClickListener(this);
        btn_Increase.setOnClickListener(this);
        btn_Decrease.setOnClickListener(this);

        if(mode == false){
            this.txt_ID.setText(Integer.toString(Account.getLastID(db.getReadableDatabase())));
        }
        else{
            this.txt_ID.setText(current_account.getAccount_id());
            this.txt_Name.setText(current_account.getAccount_name());
            this.txt_Type.setText(current_account.getType().name());
            this.txt_Balance.setText(Double.toString(current_account.getAmount()));
        }
    }



    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent upIntent = new Intent(this, MainActivity.class);
                upIntent.putExtra("OpenFragment", 3);
                NavUtils.navigateUpTo(this, upIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {


        switch (v.getId()){
            case R.id.btn_AccountHandlerSave:
                if(mode == false) {
                    new Account(0, txt_Name.getText().toString(), Account.types.valueOf(txt_Type.getText().toString()), Double.parseDouble(txt_Balance.getText().toString())).add(db.getWritableDatabase());
                    finish();
                    return;
                }
                current_account.setAccount_name(txt_Name.getText().toString());
                current_account.setAmount(Double.parseDouble(txt_Balance.getText().toString()));
                current_account.update(db.getWritableDatabase());
                finish();
                return;

            case R.id.btn_IncreaseBalance:
            case R.id.btn_DecreaseBalance:
            case R.id.btn_AccountHandlerDelete:
                if(mode == true){
                    current_account.remove(db.getWritableDatabase());
                    finish();
                    return;
                }
            default:
                return;
        }
    }


    public static void enableUpdate(Account account){
        current_account = account;
        mode = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        current_account = null;
        mode = null;
    }
}
