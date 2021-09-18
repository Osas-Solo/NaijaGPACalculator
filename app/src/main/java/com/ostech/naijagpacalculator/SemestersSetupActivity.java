package com.ostech.naijagpacalculator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;

public class SemestersSetupActivity extends NavigationActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = SemestersSetupActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switchFragment(new SemestersSetupFragment());
    }   //  end of onCreate()

    @Override
    public void onBackPressed() {
        finish();
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SemestersSetupActivity.class);

        return intent;
    }
}
