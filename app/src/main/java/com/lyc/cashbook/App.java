package com.lyc.cashbook;

import android.app.Application;


import com.lyc.cashbook.utils.ToastUtils;

import org.litepal.LitePal;

//使用LitePal数据库是为了简化数据的存储与维护过程
public class App extends Application {


    public static App sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        //初始化toast
        ToastUtils.init(this);
        //初始化litepal数据库
        LitePal.initialize(this);
    }
}
