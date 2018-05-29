package com.wolf.apps.expensemanager.UIX;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wolf.apps.expensemanager.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_accounts extends Fragment {


    public fragment_accounts() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_accounts, container, false);
    }

}
