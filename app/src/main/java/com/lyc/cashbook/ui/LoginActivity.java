package com.lyc.cashbook.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.lyc.cashbook.R;
import com.lyc.cashbook.base.BaseActivity;
import com.lyc.cashbook.bean.User;

import org.litepal.LitePal;

import java.util.List;

public class LoginActivity extends BaseActivity {


    private EditText mEt_account;
    private EditText mEt_psw;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_login;
    }

    //控件初始化
    @Override
    protected void initView() {
        mEt_account = findViewById(R.id.et_account);
        mEt_psw = findViewById(R.id.et_psw);
        findViewById(R.id.register).setOnClickListener(this);
        findViewById(R.id.login).setOnClickListener(this);


        //在登录界面初始化的时候获取是否已经有登录状态了
        boolean isLogin = getSharedPreferences("user", MODE_PRIVATE).getBoolean("isLogin", false);
        //如果登录过了直接进入首页
        if(isLogin){
            startActivity(MainActivity.class);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register://跳转到注册页面
                startActivity(RegisterActivity.class);
                break;
            case R.id.login:
                login();
                break;

        }
    }

    //登录
    private void login() {
        String account = mEt_account.getText().toString();
        String psw = mEt_psw.getText().toString();
        if (TextUtils.isEmpty(account)) {
            toast("账号不能为空");
        } else if (TextUtils.isEmpty(psw)) {
            toast("密码不能为空");
        } else {
            //本地所有用户
            final List<User> all = LitePal.findAll(User.class);
            //查询本地所有用户
            for (int i = 0; i < all.size(); i++) {
                //如果有输入的账号的
                if (all.get(i).getUsername().equals(account)) {
                    //如果账号存在去匹配这条数据的密码
                    if (all.get(i).getPassword().equals(psw)) {
                        toast("登录成功");
                        //保存用户id和登录状态到本地，不用重复登录
                        getSharedPreferences("user", MODE_PRIVATE).edit()
                                .putString("userId", all.get(i).getId() + "")
                                .putBoolean("isLogin",true)
                                .commit();
                        startActivity(MainActivity.class);
                        finish();
                    }else {
                        toast("密码错误");
                    }
                }else {
                    toast("账号不存在");
                }
            }

        }
    }
}
