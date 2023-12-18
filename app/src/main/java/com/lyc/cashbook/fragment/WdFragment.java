package com.lyc.cashbook.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.lyc.cashbook.R;
import com.lyc.cashbook.base.BaseFragment;
import com.lyc.cashbook.bean.Info;
import com.lyc.cashbook.bean.User;
import com.lyc.cashbook.ui.LoginActivity;

import org.litepal.LitePal;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class WdFragment extends BaseFragment {


    private TextView mTv_year_ys;
    private TextView mTv_year_ye;
    private TextView mTv_month_ys;
    private TextView mTv_month_ye;
    private TextView mTv_total;
    private TextView mTv_remind;

    private double totalMonty;
    private String mUserId;
    private boolean isChange;
    private View mBg;
    //接收到广播刷新
    private BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {    //上下文内容连接跳转
            if(intent.getAction().equals("bg")){
                isChange=!isChange;
                if(isChange){
                    mBg.setBackgroundResource(R.mipmap.b);  //改变
                }else {
                    mBg.setBackgroundResource(R.mipmap.a);  //不变
                }
            }else {
                initInfo(); //初始化
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_wd;
    }

    @Override
    protected void initView() {
        getActivity().registerReceiver(receiver,new IntentFilter("refresh"));
        getActivity().registerReceiver(receiver,new IntentFilter("bg"));  //显示刷新内容
        $(R.id.exit).setOnClickListener(this);
        $(R.id.ll_year).setOnClickListener(this);
        $(R.id.ll_month).setOnClickListener(this);
        mTv_year_ys = $(R.id.tv_year_ys);
        mTv_year_ye = $(R.id.tv_year_ye);
        mTv_month_ys = $(R.id.tv_month_ys);
        mTv_month_ye = $(R.id.tv_month_ye);
        mTv_total = $(R.id.tv_total);
        mTv_remind = $(R.id.tv_remind);
        mBg = $(R.id.bg);
        initInfo();
    }

    private void initInfo() {
        totalMonty=0;
        //获取明细总数据
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId","");
        //获取本地所有的明细
        List<Info> list = LitePal.where("userId=?",userId).find(Info.class);
        mTv_total.setText("共记账："+list.size()+"笔");
        //计算总支出
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getType()==2){
                totalMonty+=Double.valueOf(list.get(i).getMoney());
            }
        }

        //得到登录存在本地的用户id
        mUserId = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE).getString("userId", "");
        List<User> userList = LitePal.findAll(User.class);
        for (int i = 0; i < userList.size(); i++) {
            if(String.valueOf(userList.get(i).getId()).equals(mUserId)){//匹配到id 获取该用户的月和年预算
                String monthBudget = userList.get(i).getMonthBudget();
                String yearBudget = userList.get(i).getYearBudget();
                if(monthBudget.equals("0")){//如果没有设置月预算
                    mTv_month_ys.setText("点击设置");
                    mTv_month_ye.setText("暂未设置预算");
                }else{
                    mTv_month_ys.setText(monthBudget);
                    mTv_month_ye.setText(String.valueOf(Double.parseDouble(monthBudget)-totalMonty));
                    double yue = Double.parseDouble(monthBudget)-totalMonty;
                    if (yue<0){
                        mTv_remind.setText("超出预算"+Math.abs(yue));
                    }
                }
                if(yearBudget.equals("0")){//没有设置年预算
                    mTv_year_ys.setText("点击设置");
                    mTv_year_ye.setText("暂未设置预算");
                }else {
                    mTv_year_ys.setText(yearBudget);
                    mTv_year_ye.setText(String.valueOf(Double.parseDouble(yearBudget)-totalMonty));
                }
            }
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.exit://清空登录信息，跳转登录页
                getActivity().getSharedPreferences("user", Context.MODE_PRIVATE).edit().clear().commit();
                startActivity(LoginActivity.class);
                getActivity().finish();
                break;
            case R.id.ll_year://设置年预算
                final EditText editText=new EditText(getActivity());
                new AlertDialog.Builder(getActivity())
                        .setView(editText)
                        .setTitle("设置当年预算")
                        .setNeutralButton("取消",null)
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String string = editText.getText().toString();
                                if(TextUtils.isEmpty(string)){
                                    toast("请设置预算");
                                }else {//更新用户的年预算
                                    User user=new User();
                                    user.setYearBudget(string);
                                    user.update(Long.parseLong(mUserId));
                                    initInfo();
                                }
                            }
                        }).show();
                break;
            case R.id.ll_month://设置月预算
                final EditText editText1=new EditText(getActivity());
                new AlertDialog.Builder(getActivity())
                        .setView(editText1)
                        .setTitle("设置当月份预算")
                        .setNeutralButton("取消",null)
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String string = editText1.getText().toString();
                                if(TextUtils.isEmpty(string)){
                                    toast("请设置预算");
                                }else {//更新用户的月预算
                                    User user=new User();
                                    user.setMonthBudget(string);
                                    user.update(Long.parseLong(mUserId));
                                    initInfo();
                                }
                            }
                        }).show();
                break;
        }
    }
}
