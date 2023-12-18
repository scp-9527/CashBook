package com.lyc.cashbook.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.lyc.cashbook.R;
import com.lyc.cashbook.base.BaseActivity;
import com.lyc.cashbook.bean.User;

public class RegisterActivity extends BaseActivity {

    private EditText mEt_account;
    private EditText mEt_psw;
    private EditText mEt_psw_again;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        mEt_account = findViewById(R.id.et_account);
        mEt_psw = findViewById(R.id.et_psw);
        mEt_psw_again = findViewById(R.id.et_psw_again);

        findViewById(R.id.back).setOnClickListener(this);
        findViewById(R.id.register).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back://返回登录页
                finish();
                break;
            case R.id.register:
                register();
                break;
        }
    }

    //注册
    private void register() {
        String account = mEt_account.getText().toString();
        String psw = mEt_psw.getText().toString();
        String psw_again = mEt_psw_again.getText().toString();
        if (TextUtils.isEmpty(account)) {
            toast("账号不能为空");
        } else if (TextUtils.isEmpty(psw) || TextUtils.isEmpty(psw_again)) {
            toast("密码不能为空");
        } else if (!psw.equals(psw_again)) {
            toast("两次密码输入不一致");
        } else {
            User user=new User();
            user.setId(System.currentTimeMillis());//设置id
            user.setUsername(account);//设置账号
            user.setPassword(psw);//设置密码
            user.setMonthBudget("0");//设置默认月预算
            user.setYearBudget("0");//设置默认年预算
            boolean save = user.save();//保存数据
            if(save){
               toast("注册成功");
               finish();
            }else {
                toast("注册失败");
            }
        }
    }
}
