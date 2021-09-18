package com.ostech.naijagpacalculator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.navigation.NavigationView;
import com.ostech.naijagpacalculator.model.AcademicRecord;
import com.ostech.naijagpacalculator.model.Semester;

import java.util.ArrayList;

public class SemesterPagerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = SemesterPagerActivity.class.getCanonicalName();

    private DrawerLayout rootLayout;
    private ActionBarDrawerToggle drawerToggler;
    private NavigationView navigationView;

    public ViewPager2 semesterViewPager;

    private static ArrayList<Semester> semesterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semester_pager);

        rootLayout = findViewById(R.id.semester_pager_drawer_layout);
        drawerToggler = new ActionBarDrawerToggle(this, rootLayout, R.string.nav_open,
                R.string.nav_close);
        navigationView = findViewById(R.id.semester_pager_navigation);
        navigationView.setNavigationItemSelectedListener(this);
        rootLayout.addDrawerListener(drawerToggler);
        drawerToggler.syncState();

        semesterViewPager = findViewById(R.id.semester_view_pager);

        semesterList = AcademicRecord.getInstance().getSemesterList();

        semesterViewPager.setAdapter(new FragmentStateAdapter(this) {
            @Override
            public Fragment createFragment(int position) {
                return SemesterFragment.newInstance(position);
            }

            @Override
            public int getItemCount() {
                return semesterList.size();
            }
        });

        for (int i = 0; i < semesterList.size(); i++) {
            semesterViewPager.setCurrentItem(i);
            break;
        }
    }

    @Override
    public void onBackPressed() {
        if (semesterViewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            semesterViewPager.setCurrentItem(semesterViewPager.getCurrentItem() - 1);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(drawerToggler.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }   //  end of onOptionsItemSelected()

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.gpa_calculator_menu_item:
                returnHome();
                break;

            /*case R.id.help_menu_item:
                if (!(onScreenFragment instanceof HelpFragment)) {
                    switchFragment(new HelpFragment());
                }
                break;

            case R.id.about_menu_item:
                if (!(onScreenFragment instanceof AboutFragment)) {
                    switchFragment(new AboutFragment());
                }
                break;*/

            default:
                return super.onOptionsItemSelected(item);
        }

        rootLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    private void returnHome() {
        Intent intent = new Intent(this, NavigationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void navigateToPreviousSemester(int position) {
        if (position != 0) {
            semesterViewPager.setCurrentItem(position - 1);
        }
    }

    public void navigateToNextSemester(int position) {
        if (position != semesterList.size() - 1) {
            semesterViewPager.setCurrentItem(position + 1);
        }
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SemesterPagerActivity.class);

        return intent;
    }
}
