package com.example.patientappointmentproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;

import com.example.patientappointmentproject.fragments.AdminLoginFragment;
import com.example.patientappointmentproject.fragments.DoctorLoginFragment;
import com.example.patientappointmentproject.fragments.PaitentLoginFragment;

public class LoginActivity extends FragmentActivity {

    Context context = LoginActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
    }


    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            switch(pos) {

                case 0: return PaitentLoginFragment.newInstance(context);
                case 1: return DoctorLoginFragment.newInstance(context);
                case 2: return AdminLoginFragment.newInstance(context);
                default: return PaitentLoginFragment.newInstance(context);
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }


}
