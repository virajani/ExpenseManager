package com.wolf.apps.expensemanager.Models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TransactionDetails {
    protected int id;
    protected float amount;
    protected String description;
    protected Date date;
    protected Account account;

    public TransactionDetails(int id, float amount, String description, Date date, Account account) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.account = account;
    }

    public int getId() {
        return id;
    }

    public float getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public Calendar getDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public Account getAccount() {
        return account;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static ArrayList<TransactionDetails> sortByDates(ArrayList<TransactionDetails> list){
        int n = list.size();
        TransactionDetails temp;
        for(int i=0; i < n; i++){
            for(int j=1; j < (n-i); j++){
                if(list.get(j -1).date.before(list.get(j).date)){
                    //swap elements
                    temp = list.get(j-1);
                    list.set(j-1, list.get(j));
                    list.set(j, temp);
                }
            }
        }
        return list;
    }
}
