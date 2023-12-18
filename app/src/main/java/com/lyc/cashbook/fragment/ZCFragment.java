package com.lyc.cashbook.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;

import com.lyc.cashbook.R;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lyc.cashbook.R;
import com.lyc.cashbook.base.BaseFragment;
import com.lyc.cashbook.bean.Info;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ZCFragment extends BaseFragment {


    private ImageView mIv_hf;
    private ImageView mIv_gw;
    private ImageView mIv_cf;
    private ImageView mIv_qt;
    private TextView mTv_hf;
    private TextView mTv_gw;
    private TextView mTv_cf;
    private TextView mTv_qt;
    private TextView mTv_value;
    private String name = "话费";
    private int res=R.mipmap.hf;
    private EditText mEt_remark;
    private TextView mTv_time;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_zc;
    }

    @Override
    protected void initView() {
        $(R.id.ll_hf).setOnClickListener(this);
        $(R.id.ll_gw).setOnClickListener(this);
        $(R.id.ll_cf).setOnClickListener(this);
        $(R.id.ll_qt).setOnClickListener(this);
        mIv_hf = $(R.id.iv_hf);
        mIv_gw = $(R.id.iv_gw);
        mIv_cf = $(R.id.iv_cf);
        mIv_qt = $(R.id.iv_qt);
        mTv_hf = $(R.id.tv_hf);
        mTv_gw = $(R.id.tv_gw);
        mTv_cf = $(R.id.tv_cf);
        mTv_qt = $(R.id.tv_qt);
        mTv_value = $(R.id.tv_value);
        mEt_remark = $(R.id.et_remark);
        mTv_time = $(R.id.tv_time);

        $(R.id.btn_1).setOnClickListener(this);
        $(R.id.btn_2).setOnClickListener(this);
        $(R.id.btn_3).setOnClickListener(this);
        $(R.id.btn_4).setOnClickListener(this);
        $(R.id.btn_5).setOnClickListener(this);
        $(R.id.btn_6).setOnClickListener(this);
        $(R.id.btn_7).setOnClickListener(this);
        $(R.id.btn_8).setOnClickListener(this);
        $(R.id.btn_9).setOnClickListener(this);
        $(R.id.btn_0).setOnClickListener(this);
        $(R.id.btn_dot).setOnClickListener(this);
        $(R.id.btn_delete).setOnClickListener(this);
        $(R.id.btn_sure).setOnClickListener(this);
        $(R.id.btn_reset).setOnClickListener(this);


        mTv_time.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_hf://点击话费
                name = "话费";
                res=R.mipmap.hf;
                initType(1);
                break;
            case R.id.ll_gw://点击购物
                name = "购物";
                res=R.mipmap.gw;
                initType(2);
                break;
            case R.id.ll_cf://点击吃饭
                name = "吃饭";
                res=R.mipmap.cf;
                initType(3);
                break;
            case R.id.ll_qt://点击其他
                name = "其他";
                res=R.mipmap.qt;
                initType(4);
                break;
            case R.id.btn_0:
                setValue("0");
                break;
            case R.id.btn_1:
                setValue("1");
                break;
            case R.id.btn_2:
                setValue("2");
                break;
            case R.id.btn_3:
                setValue("3");
                break;
            case R.id.btn_4:
                setValue("4");
                break;
            case R.id.btn_5:
                setValue("5");
                break;
            case R.id.btn_6:
                setValue("6");
                break;
            case R.id.btn_7:
                setValue("7");
                break;
            case R.id.btn_8:
                setValue("8");
                break;
            case R.id.btn_9:
                setValue("9");
                break;
            case R.id.btn_dot:
                setValue(".");
                break;
            case R.id.btn_delete://按了删除键，如果为0，没反应
                if (!mTv_value.getText().toString().equals("0")) {
                    //如果剩下一位数，变成0
                    if (mTv_value.getText().toString().length() == 1) {
                        mTv_value.setText("0");
                    } else {
                        //否则去掉最后一位
                        mTv_value.setText(mTv_value.getText().toString().subSequence(0, mTv_value.getText().toString().length() - 1));
                    }
                }
                break;
            case R.id.btn_reset:
                mTv_value.setText("0");//重置归0
                break;
            case R.id.btn_sure:
                if (mTv_value.getText().equals("0")) {
                    toast("请输入金额");
                } else {
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                    String userId = sharedPreferences.getString("userId","");
                    Info info = new Info();
                    info.setId(System.currentTimeMillis());//设置id
                    info.setType(2);//设置类型
                    info.setName(name);//设置类别
                    info.setRemark(mEt_remark.getText().toString());//设置备注信息
                    info.setTime(mTv_time.getText().toString());//设置时间
                    info.setMoney(mTv_value.getText().toString());//设置金额
                    info.setRes(res);//设置图片资源
                    info.setUserId(userId);
                    boolean save = info.save();//保存到数据库
                    if(save){
                        toast("记录成功");
                        getActivity().sendBroadcast(new Intent("refresh"));
                        getActivity().finish();
                    }else {
                        toast("记录失败");
                    }
                }
                break;

        }
    }

    public void setValue(String a) {
        //如果是0，点击了. 变成0.
        if (mTv_value.getText().toString().equals("0")) {
            if (a.equals(".")) {
                mTv_value.setText("0.");
            } else {//否则直接变成相应的数字
                mTv_value.setText(a + "");
            }
        } else {//如果不为0，则拼接数字
            mTv_value.setText(mTv_value.getText().toString() + a + "");
        }
    }


    private void initType(int type) {
        switch (type) {
            case 1://设置话费为高亮
                mIv_hf.setImageResource(R.mipmap.hf_click);
                mTv_hf.setTextColor(getResources().getColor(R.color.colorClick));
                mIv_gw.setImageResource(R.mipmap.gw);
                mTv_gw.setTextColor(getResources().getColor(R.color.colorNormal));
                mIv_cf.setImageResource(R.mipmap.cf);
                mTv_cf.setTextColor(getResources().getColor(R.color.colorNormal));
                mIv_qt.setImageResource(R.mipmap.qt);
                mTv_qt.setTextColor(getResources().getColor(R.color.colorNormal));
                break;
            case 2://设置购物为高亮
                mIv_hf.setImageResource(R.mipmap.hf);
                mTv_hf.setTextColor(getResources().getColor(R.color.colorNormal));
                mIv_gw.setImageResource(R.mipmap.gw_click);
                mTv_gw.setTextColor(getResources().getColor(R.color.colorClick));
                mIv_cf.setImageResource(R.mipmap.cf);
                mTv_cf.setTextColor(getResources().getColor(R.color.colorNormal));
                mIv_qt.setImageResource(R.mipmap.qt);
                mTv_qt.setTextColor(getResources().getColor(R.color.colorNormal));
                break;
            case 3://设置吃饭为高亮
                mIv_hf.setImageResource(R.mipmap.hf);
                mTv_hf.setTextColor(getResources().getColor(R.color.colorNormal));
                mIv_gw.setImageResource(R.mipmap.gw);
                mTv_gw.setTextColor(getResources().getColor(R.color.colorNormal));
                mIv_cf.setImageResource(R.mipmap.cf_click);
                mTv_cf.setTextColor(getResources().getColor(R.color.colorClick));
                mIv_qt.setImageResource(R.mipmap.qt);
                mTv_qt.setTextColor(getResources().getColor(R.color.colorNormal));
                break;
            case 4://设置其他为高亮
                mIv_hf.setImageResource(R.mipmap.hf);
                mTv_hf.setTextColor(getResources().getColor(R.color.colorNormal));
                mIv_gw.setImageResource(R.mipmap.gw);
                mTv_gw.setTextColor(getResources().getColor(R.color.colorNormal));
                mIv_cf.setImageResource(R.mipmap.cf);
                mTv_cf.setTextColor(getResources().getColor(R.color.colorNormal));
                mIv_qt.setImageResource(R.mipmap.qt_click);
                mTv_qt.setTextColor(getResources().getColor(R.color.colorClick));
                break;
        }
    }


}
