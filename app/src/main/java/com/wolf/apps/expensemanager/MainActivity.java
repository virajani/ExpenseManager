package com.wolf.apps.expensemanager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;

import com.wolf.apps.expensemanager.UIX.SettingsActivity;
import com.wolf.apps.expensemanager.UIX.fragment_accounts;
import com.wolf.apps.expensemanager.UIX.fragment_categories;
import com.wolf.apps.expensemanager.UIX.fragment_transactions;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener{

    fragment_transactions calling_frag_transactions;
    fragment_accounts calling_frag_accounts;
    fragment_categories calling_frag_categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {

        }
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_main));
        String title = "Expense Manager";
        SpannableString str = new SpannableString(title);
        str.setSpan(new ForegroundColorSpan(Color.WHITE), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(str);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs_main);
        tabLayout.addOnTabSelectedListener(this);

        calling_frag_transactions = new fragment_transactions();
        calling_frag_accounts = new fragment_accounts();
        calling_frag_categories = new fragment_categories();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frame_main, calling_frag_transactions);
        fragmentTransaction.add(R.id.frame_main, calling_frag_accounts);
        fragmentTransaction.add(R.id.frame_main, calling_frag_categories);

        int init_fragment = getIntent().getIntExtra("OpenFragment", 1);

        switch(init_fragment){
            case 1: fragmentTransaction.hide(calling_frag_accounts);
                    fragmentTransaction.hide(calling_frag_categories);
                    fragmentTransaction.commit();
                    tabLayout.getTabAt(0).select();
                    break;
            case 2: fragmentTransaction.hide(calling_frag_transactions);
                    fragmentTransaction.hide(calling_frag_accounts);
                    fragmentTransaction.commit();
                    tabLayout.getTabAt(1).select();
                    break;
            case 3: fragmentTransaction.hide(calling_frag_transactions);
                    fragmentTransaction.hide(calling_frag_categories);
                    fragmentTransaction.commit();
                    tabLayout.getTabAt(2).select();
                    break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_item_settings){
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch(tab.getPosition()){
            case 0:
                getSupportFragmentManager().beginTransaction().show(calling_frag_transactions).commit();
                getSupportFragmentManager().beginTransaction().hide(calling_frag_categories).commit();
                getSupportFragmentManager().beginTransaction().hide(calling_frag_accounts).commit();
                return;
            case 1:
                getSupportFragmentManager().beginTransaction().hide(calling_frag_transactions).commit();
                getSupportFragmentManager().beginTransaction().show(calling_frag_categories).commit();
                getSupportFragmentManager().beginTransaction().hide(calling_frag_accounts).commit();
                return;
            case 2:
                getSupportFragmentManager().beginTransaction().hide(calling_frag_transactions).commit();
                getSupportFragmentManager().beginTransaction().hide(calling_frag_categories).commit();
                getSupportFragmentManager().beginTransaction().show(calling_frag_accounts).commit();
                return;
            default:
                return;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
