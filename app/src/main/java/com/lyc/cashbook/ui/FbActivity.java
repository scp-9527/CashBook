package com.lyc.cashbook.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.lyc.cashbook.R;
import com.lyc.cashbook.base.BaseActivity;
import com.lyc.cashbook.fragment.SRFragment;
import com.lyc.cashbook.fragment.ZCFragment;

import java.util.ArrayList;
import java.util.List;

public class FbActivity extends BaseActivity{

    private TabLayout mTab;
    private ViewPager mViewPager;
    private List<Fragment> mFragmentList=new ArrayList<>();
    String[] titles = {"支出", "收入"};

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_fb;
    }

    @Override
    protected void initView() {

        mTab = findViewById(R.id.tab);
        mViewPager = findViewById(R.id.viewPager);
        mTab.setupWithViewPager(mViewPager);//tab关联viewpager
        mFragmentList.add(new ZCFragment());//添加支出页面
        mFragmentList.add(new SRFragment());//添加收入页面
        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }
            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

}
