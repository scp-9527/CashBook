package com.lyc.cashbook.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.lyc.cashbook.R;
import com.lyc.cashbook.base.BaseActivity;
import com.lyc.cashbook.base.BaseMainActivity;
import com.lyc.cashbook.fragment.MxFragment;
import com.lyc.cashbook.fragment.TbFragment;
import com.lyc.cashbook.fragment.WdFragment;

public class MainActivity extends BaseMainActivity {


    private Fragment[] mFragments;
    private MxFragment mMxFragment;
    private TbFragment mTbFragment;
    private WdFragment mWdFragment;
    private int index;
    private int currentTabIndex;
    private LinearLayout[] mTabs;
    private MediaPlayer mediaPlayer;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        //初始化添加fragment
        mMxFragment = new MxFragment();
        mTbFragment = new TbFragment();
        mWdFragment = new WdFragment();
        mFragments = new Fragment[]{mMxFragment, mTbFragment, mWdFragment};

        //默认显示第一个fragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mMxFragment)
                .show(mMxFragment)
                .commit();

        //每一个底部做监听
        mTabs = new LinearLayout[mFragments.length];
        mTabs[0] = findViewById(R.id.mx);
        mTabs[1] = findViewById(R.id.tb);
        mTabs[2] = findViewById(R.id.wd);
        mTabs[0].setOnClickListener(this);
        mTabs[1].setOnClickListener(this);
        mTabs[2].setOnClickListener(this);


        //设置第一个为选中状态
        mTabs[0].setSelected(true);

        mediaPlayer = MediaPlayer.create(this, R.raw.a);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("关于作者")
                        .setMessage("小组成员：沈杨程、周佳彬、吴彦霖、黄思宜、盘华毅\n")
                        .setNegativeButton("确定",null)
                        .show();
                break;
            case R.id.bg:
                sendBroadcast(new Intent("bg"));
                break;
            case R.id.music:
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                }else {
                    mediaPlayer.start();
                }
                break;

        }
        return true;
    }


    @Override
    public void onClick(View v) {
        //点击改变下标
        switch (v.getId()) {
            case R.id.mx:
                index = 0;
                break;
            case R.id.tb:
                index = 1;
                break;
            case R.id.wd:
                index = 2;
                break;
        }
        setIndex();
    }

    public void setIndex() {
        //如果点击的是另外一个下标
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(mFragments[currentTabIndex]);//隐藏当前的页面
            if (!mFragments[index].isAdded()) {//如果新的页面没有显示过，则添加
                trx.add(R.id.fragment_container, mFragments[index]);
            }
            //显示当前选中的页面
            trx.show(mFragments[index]).commitAllowingStateLoss();
        }
        mTabs[currentTabIndex].setSelected(false);
        mTabs[index].setSelected(true);
        //把当前显示的下标赋值给currentTabIndex
        currentTabIndex = index;
    }
}
