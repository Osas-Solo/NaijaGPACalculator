package com.ostech.naijagpacalculator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;

public class ResultActivity extends NavigationActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = ResultActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switchFragment(new ResultFragment());
    }   //  end of onCreate()

    @Override
    public void onBackPressed() {
        finish();
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, ResultActivity.class);

        return intent;
    }
}
